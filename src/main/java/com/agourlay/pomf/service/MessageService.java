package com.agourlay.pomf.service;

import java.util.Comparator;
import java.util.List;

import com.agourlay.pomf.model.FridgeMessage;
import com.agourlay.pomf.util.Constantes;
import com.google.common.collect.FluentIterable;

public class MessageService {

	public static void sendMessage(String fridgeId, String message, String user) {
		String key = Constantes.CACHE_CHAT_KEY + fridgeId;
		FridgeMessage fridgeMessage = new FridgeMessage(Constantes.COMMAND_MESSAGE, user, message);
		CacheService.addToList(key, fridgeMessage);
		ClientService.notifyClientsFromFridge(fridgeId, fridgeMessage);
	}

	public static List<FridgeMessage> retrieveCachedMessage(String fridgeId) {
		String key = Constantes.CACHE_CHAT_KEY + fridgeId;
		List<FridgeMessage> cached = CacheService.getList(key);
		List<FridgeMessage> fridgeMessages = FluentIterable//
				.from(cached)//
				.toSortedList(new Comparator<FridgeMessage>() {
					@Override
					public int compare(FridgeMessage o1, FridgeMessage o2) {
						return o1.getTimestamp().compareTo(o2.getTimestamp());
					}
				});
		return fridgeMessages;
	}

}
