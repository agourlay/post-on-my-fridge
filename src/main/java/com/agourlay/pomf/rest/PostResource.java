package com.agourlay.pomf.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.Post;

@Path("/post")
public class PostResource {
	
	@DELETE
	@Path("/{postId}")
	public void deletePost(@PathParam ("postId") String postId)  {
		Post.remove(Long.parseLong(postId));
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void updatePost(Post post) {
		Post.savePost(post);	
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void addPost(Post post) {
		Fridge.createFridgeIfNotExist(post.getFridgeId());
		Post.savePost(post);	
	}
}
