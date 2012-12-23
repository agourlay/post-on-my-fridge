package com.agourlay.pomf.rest;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.Stat;
import com.agourlay.pomf.tools.rss.Feed;
import com.agourlay.pomf.tools.rss.RSSFeedWriter;
import com.agourlay.pomf.tools.rss.RssUtils;

@Path("/admin")
public class AdminResource {

	@GET
	@Path("fridges/rss")
	@Produces("application/xml")
	public String getFridgesRssContent() throws Exception {
		Feed rssFeeder = RssUtils.createRssFeed("admin");
		rssFeeder.getMessages().addAll(RssUtils.getRssEntry(Fridge.getAllFridge()));
		RSSFeedWriter writer = new RSSFeedWriter(rssFeeder, new ByteArrayOutputStream());
		return writer.write().toString();
	}

	@GET
	@Path("fridges/stats")
	@Produces("application/json")
	public List<Stat> getFridgesStats() {
		return Stat.getAllStats();
	}

}
