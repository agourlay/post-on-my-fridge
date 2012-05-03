package com.agourlay.pomf.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.Post;
import com.google.gson.Gson;

@Path("/fridge/{fridgeId}")
public class FridgeResource {
	
	@GET
	@Produces("application/json")
	public String getFridgeContent(@PathParam ("fridgeId") String fridgeId)  {
	    List<Post> posts = Fridge.getPosts(fridgeId);	
		Gson gson = new Gson();
		return gson.toJson(posts);
	}
}	


