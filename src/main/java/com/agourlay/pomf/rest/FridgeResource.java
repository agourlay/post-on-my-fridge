package com.agourlay.pomf.rest;

import java.io.ByteArrayOutputStream;
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

import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.service.ClientRepository;
import com.agourlay.pomf.tools.Constantes;
import com.agourlay.pomf.tools.rss.Feed;
import com.agourlay.pomf.tools.rss.RSSFeedWriter;
import com.agourlay.pomf.tools.rss.RssUtils;

@Path("/fridge/{fridgeId}")
public class FridgeResource {
	
    @GET
    @Produces("application/json")
    public Fridge getFridge(@PathParam ("fridgeId") String fridgeId)  {
    	return Fridge.getFridgeById(fridgeId);
     }
    
    @GET
    @Path("/rss")
    @Produces("application/xml")
    public String getFridgeRssContent(@PathParam ("fridgeId") String fridgeId) throws Exception  {
    	Feed rssFeeder = RssUtils.createRssFeed(fridgeId);
    	rssFeeder.getMessages().addAll(RssUtils.getRssEntry(Arrays.asList(Fridge.getFridgeById(fridgeId))));
    	RSSFeedWriter writer =  new RSSFeedWriter(rssFeeder, new ByteArrayOutputStream());
    	return writer.write().toString();
    }
    
    @GET
    @Path("/search")
    @Produces("application/json")
    public List<String> getFridgeIds(@QueryParam("term") String term)  {
    	return Fridge.searchFridgeNamesWithNameLike(term);
    }
    
	@DELETE
	@Path("post/{postId}")
	public void deletePost(@PathParam ("fridgeId") String fridgeId,@PathParam ("postId") String postId)  {
		ClientRepository.notifyAllClientFromFridge(fridgeId,Constantes.COMMAND_REFRESH,null,null);
		Post.remove(Long.parseLong(postId));
	}
	
	@PUT
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void updatePost(Post post) {
		Post.savePost(post);
	}
	
	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void addPost(@PathParam ("fridgeId") String fridgeId,Post post) {
		Fridge.createFridgeIfNotExist(fridgeId);
		Post.savePost(post);
        ClientRepository.notifyAllClientFromFridge(fridgeId,Constantes.COMMAND_REFRESH,null,null);
	}
    
}