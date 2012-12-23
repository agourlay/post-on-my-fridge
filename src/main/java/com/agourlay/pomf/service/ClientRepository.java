package com.agourlay.pomf.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.agourlay.pomf.model.ChatMessage;
import com.agourlay.pomf.util.Constantes;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.SetPolicy;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class ClientRepository {

	private static ChannelService channelService = ChannelServiceFactory.getChannelService();
	private static MemcacheService cache = MemcacheServiceFactory.getMemcacheService();

	@SuppressWarnings("unchecked")
	public static synchronized String addChannelToFridge(String fridgeId, String channelId) {
		String key = Constantes.CACHE_CHANNEL_KEY + fridgeId;
		String token = channelService.createChannel(channelId);
		List<String> addChannel = new ArrayList<String>();
		addChannel.add(channelId);
		if (cache.get(key) != null) {
			addChannel.addAll((Collection<? extends String>) cache.get(key));
		}
		cache.put(key, addChannel, Expiration.byDeltaSeconds(1440), SetPolicy.SET_ALWAYS);
		return token;
	}

	@SuppressWarnings("unchecked")
	public static synchronized void notifyAllClientFromFridge(String fridgeId, String command, String message, String user) {
		String key = Constantes.CACHE_CHANNEL_KEY + fridgeId;
		List<String> channels = (List<String>) cache.get(key);
		if (channels != null && !channels.isEmpty()) {
			for (String channelId : channels) {
				ChatMessage chatMessage = new ChatMessage(command, user, message);
				ObjectMapper mapper = new ObjectMapper();
				try {
					String chatMessageJson = mapper.writeValueAsString(chatMessage);
					channelService.sendMessage(new ChannelMessage(channelId, chatMessageJson));
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
