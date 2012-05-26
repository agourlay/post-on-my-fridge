package com.agourlay.pomf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gson.Gson;

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
	
	public static synchronized void notifyAllClientFromFridge(String fridgeId){
		if (clientRepo.get(fridgeId) != null){
			for (String channelId : clientRepo.get(fridgeId)){
				Gson gson = new Gson();
				String jsonMessage = gson.toJson("#FRIDGE-UPATE#");
				channelService.sendMessage(new ChannelMessage(channelId,jsonMessage));
			}
		}
	}
	
}
