package com.agourlay.pomf.service;

import java.io.IOException;
import java.util.List;

import com.agourlay.pomf.model.FridgeMessage;
import com.agourlay.pomf.util.Constantes;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class ClientService {

	private static ChannelService channelService = ChannelServiceFactory.getChannelService();

	public static synchronized String addChannelToFridge(String fridgeId, String channelId) {
		String key = Constantes.CACHE_CHANNEL_KEY + fridgeId;
		String token = channelService.createChannel(channelId);
		CacheService.addToList(key, channelId);
		return token;
	}

	public static synchronized void notifyClientsFromFridge(String fridgeId, FridgeMessage fridgeMessage) {
		String key = Constantes.CACHE_CHANNEL_KEY + fridgeId;
		List<String> channels = CacheService.getList(key);
		if (!channels.isEmpty()) {
			for (String channelId : channels) {
				ObjectMapper mapper = new ObjectMapper();
				try {
					String channelMessageAsJson = mapper.writeValueAsString(fridgeMessage);
					channelService.sendMessage(new ChannelMessage(channelId, channelMessageAsJson));
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
