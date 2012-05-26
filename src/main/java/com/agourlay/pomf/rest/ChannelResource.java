package com.agourlay.pomf.rest;

import java.util.UUID;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.agourlay.pomf.service.ClientRepository;
import com.google.gson.Gson;

@Path("/channel")
public class ChannelResource {

	@GET
	@Path("/{fridgeId}")
	@Produces("application/json")
	public String createChannel(@PathParam("fridgeId") String fridgeId) {
		String channelId = UUID.randomUUID().toString();
		String token = ClientRepository.addChannelToFridge(fridgeId, channelId);
		Gson gson = new Gson();
		return gson.toJson(token);
	}
	
	@POST
	@Path("/disconnected")
	public void disconnectChannel(@FormParam("from") final String channelId) {
		ClientRepository.removeChannelFromFridge(channelId);
	}
	
	@POST
	@Path("/connected")
	public void connectedChannel() {

	}

}
