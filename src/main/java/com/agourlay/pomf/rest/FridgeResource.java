package com.agourlay.pomf.rest;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.tools.rss.Feed;
import com.agourlay.pomf.tools.rss.RSSFeedWriter;
import com.agourlay.pomf.tools.rss.RssUtils;

@Path("/fridge/{fridgeId}")
public class FridgeResource {
	
    @GET
    @Produces("application/json")
    public List<Post> getFridgeContent(@PathParam ("fridgeId") String fridgeId)  {
    	List<Post> posts = Fridge.getPosts(fridgeId);	
    	return posts;
     }
    
    @GET
    @Path("/rss")
    @Produces("application/xml")
    public String getFridgeRssContent(@PathParam ("fridgeId") String fridgeId) throws Exception  {
    	Feed rssFeeder = RssUtils.createRssFeed(fridgeId);
    	rssFeeder.getMessages().addAll(RssUtils.getRssEntry(Fridge.getPosts(fridgeId)));
    	RSSFeedWriter writer =  new RSSFeedWriter(rssFeeder, new ByteArrayOutputStream());
    	return writer.write().toString();
    }
    
    @GET
    @Path("/search")
    @Produces("application/json")
    public List<String> getFridgeIds(@QueryParam("term") String term)  {
    	return Fridge.searchFridgeNamesWithNameLike(term);
    }
}