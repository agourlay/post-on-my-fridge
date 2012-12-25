package com.agourlay.pomf.controler;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.agourlay.pomf.dao.Dao;
import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.FridgeMessage;
import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.service.ClientService;
import com.agourlay.pomf.service.RssService;
import com.agourlay.pomf.util.Constantes;
import com.sun.jersey.api.view.Viewable;

@Path("/fridge/{fridgeId}")
public class FridgeControler {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable index(@Context HttpServletRequest request) {
		return new Viewable("/fridge.jsp", null);
	}

	@GET
	@Produces("application/json")
	public Fridge getFridge(@PathParam("fridgeId") String fridgeId) {
		return Dao.getFridgeById(fridgeId);
	}

	@GET
	@Path("/rss")
	@Produces("application/xml")
	public String getFridgeRssContent(@PathParam("fridgeId") String fridgeId) throws Exception {
		return RssService.getRssStream(Arrays.asList(Dao.getFridgeById(fridgeId)), fridgeId);
	}

	@GET
	@Path("/search")
	@Produces("application/json")
	public List<String> getFridgeIds(@QueryParam("term") String term) {
		return Dao.searchFridgeNamesWithNameLike(term);
	}

	@DELETE
	@Path("post/{postId}")
	public void deletePost(@PathParam("fridgeId") String fridgeId, @PathParam("postId") String postId) {
		ClientService.notifyClientsFromFridge(fridgeId, new FridgeMessage(Constantes.COMMAND_REFRESH, null, null));
		Dao.deletePost(Long.parseLong(postId));
	}

	@PUT
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void updatePost(Post post) {
		Dao.savePost(post);
	}

	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void addPost(@PathParam("fridgeId") String fridgeId, Post post) {
		Dao.createFridgeIfNotExist(fridgeId);
		Dao.savePost(post);
		ClientService.notifyClientsFromFridge(fridgeId, new FridgeMessage(Constantes.COMMAND_REFRESH, null, null));
	}

}