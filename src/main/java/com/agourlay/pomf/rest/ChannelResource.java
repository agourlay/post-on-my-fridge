package com.agourlay.pomf.rest;

import java.util.UUID;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.agourlay.pomf.service.ClientRepository;
import com.agourlay.pomf.tools.Constantes;

@Path("/channel")
public class ChannelResource {

	@GET
	@Path("/{fridgeId}")
	@Produces("application/json")
	public String createChannel(@PathParam("fridgeId") String fridgeId) {
		String channelId = UUID.randomUUID().toString();
		return ClientRepository.addChannelToFridge(fridgeId, channelId);
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
		ClientRepository.notifyAllClientFromFridge(fridgeId, Constantes.COMMAND_MESSAGE, message, user);
	}

}
