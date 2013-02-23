package com.agourlay.pomf.controller;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.agourlay.pomf.model.FridgeMessage;
import com.agourlay.pomf.service.MessageService;
import com.agourlay.pomf.service.ClientService;

@Path("api/channel")
public class ChannelController {

	@GET
	@Path("/{fridgeId}")
	@Produces("application/json")
	public String createChannel(@PathParam("fridgeId") String fridgeId) {
		String channelId = UUID.randomUUID().toString();
		return ClientService.addChannelToFridge(fridgeId, channelId);
	}

	@POST
	@Path("/disconnected")
	public void disconnectChannel(@FormParam("from") final String channelId) {

	}

	@POST
	@Path("/connected")
	public void connectedChannel() {

	}

	@POST
	@Path("/{fridgeId}/message")
	public void sendMessage(@FormParam("fridgeId") final String fridgeId, @FormParam("message") final String message,
			@FormParam("user") final String user) {
		MessageService.sendMessage(fridgeId, message, user);
	}

	@GET
	@Path("/{fridgeId}/message")
	@Produces("application/json")
	public List<FridgeMessage> retrieveCachedMessage(@PathParam("fridgeId") String fridgeId) {
		return MessageService.retrieveCachedMessage(fridgeId);
	}
	
}
