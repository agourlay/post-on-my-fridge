package com.agourlay.pomf.rest;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.model.Stat;
import com.agourlay.pomf.tools.rss.Feed;
import com.agourlay.pomf.tools.rss.RSSFeedWriter;
import com.agourlay.pomf.tools.rss.RssUtils;
import com.google.gson.Gson;

@Path("/admin")
public class AdminResource {

	@GET
    @Path("fridges/rss")
	@Produces("application/xml")
	public String getFridgesRssContent() throws Exception  {
	    Feed rssFeeder = RssUtils.createRssFeed("admin");
		rssFeeder.getMessages().addAll(RssUtils.getRssEntry(Post.getAllPost()));
		RSSFeedWriter writer =  new RSSFeedWriter(rssFeeder, new ByteArrayOutputStream());
		return writer.write().toString();
	}
	
	@GET
    @Path("fridges/stats")
	@Produces("application/json")
	public String getFridgesStats()  {
		Gson gson = new Gson();
		return gson.toJson(Stat.getAllStats());
	}
	
}
