package com.agourlay.pomf.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.agourlay.pomf.model.ChatMessage;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class ClientRepository {

	private static final Map<String, List<String>> clientRepo = new ConcurrentHashMap<String,  List<String>>();
	private static ChannelService channelService = ChannelServiceFactory.getChannelService();
	
	public static synchronized String addChannelToFridge(String fridgeId,String channelId){
		String token = channelService.createChannel(channelId);
		if (clientRepo.get(fridgeId) == null){
			clientRepo.put(fridgeId, new ArrayList<String>());
		}
		clientRepo.get(fridgeId).add(channelId);
		return token;
	}
	
	public static synchronized void removeChannelFromFridge(String channelId){
		for (Entry<String, List<String>> entry : clientRepo.entrySet())	{
			if (entry.getValue().remove(channelId)){
				break;
			}
		}
	}
	
	public static synchronized void notifyAllClientFromFridge(String fridgeId, String command,String message,String user){
		if (clientRepo.get(fridgeId) != null){
			for (String channelId : clientRepo.get(fridgeId)){
				ChatMessage chatMessage = new ChatMessage(command, user, message);
				ObjectMapper mapper = new ObjectMapper();
				try {
					String chatMessageJson = mapper.writeValueAsString(chatMessage);
					channelService.sendMessage(new ChannelMessage(channelId,chatMessageJson));
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
