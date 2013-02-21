package com.agourlay.pomf.controller;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.agourlay.pomf.dao.Dao;
import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.FridgeMessage;
import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.service.ClientService;
import com.agourlay.pomf.service.RssService;
import com.agourlay.pomf.util.Constantes;

@Path("/fridge/{fridgeId}")
public class FridgeController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Fridge getFridge(@PathParam("fridgeId") String fridgeId) {
		return Dao.getFridgeById(fridgeId);
	}

	@GET
	@Path("/rss")
	@Produces(MediaType.APPLICATION_XML)
	public String getFridgeRssContent(@PathParam("fridgeId") String fridgeId) throws Exception {
		return RssService.getRssStream(Arrays.asList(Dao.getFridgeById(fridgeId)), fridgeId);
	}

	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getFridgeIds(@QueryParam("term") String term) {
		return Dao.searchFridgeNamesWithNameLike(term);
	}

	@DELETE
	@Path("post/{postId}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deletePost(@PathParam("fridgeId") String fridgeId, @PathParam("postId") String postId) {
		Dao.deletePost(Long.parseLong(postId));
		ClientService.notifyClientsFromFridge(fridgeId, new FridgeMessage(Constantes.COMMAND_REFRESH, null, null));
		return Response.ok("Post "+postId+" deleted").build();
	}

	@PUT
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response updatePost(Post post) {
		Dao.savePost(post);
		return Response.ok("Post "+post.getId()+" updated").build();
	}

	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response addPost(@PathParam("fridgeId") String fridgeId, Post post) {
		Dao.createFridgeIfNotExist(fridgeId);
		Dao.savePost(post);
		ClientService.notifyClientsFromFridge(fridgeId, new FridgeMessage(Constantes.COMMAND_REFRESH, null, null));
		return Response.ok("Post "+post.getId()+" created").build();
	}
}