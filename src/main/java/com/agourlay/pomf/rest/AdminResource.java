package com.agourlay.pomf.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.joda.time.DateTime;

import com.agourlay.pomf.dao.Dao;
import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.model.Stat;
import com.agourlay.pomf.service.RssService;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

@Path("/admin")
public class AdminResource {

	@GET
	@Path("fridges/rss")
	@Produces("application/xml")
	public String getFridgesRssContent() throws Exception {
		return RssService.getRssStream(Dao.getAllFridge(),"admin");
	}

	@GET
	@Path("fridges/stats")
	@Produces("application/json")
	public List<Stat> getFridgesStats() {
		return Stat.getAllStats();
	}

	@GET
	@Path("generate-stat")
	@Produces("application/json")
	public String generateFridgesStats() {
		Stat.generateDailyStat();
		return "Stats generated";
	}

	@GET
	@Path("clean")
	@Produces("application/json")
	public String eraseDuePost() {
		// waiting for filter on date to be implemented on delete in Objectify
		List<Long> postIdToDelete = FluentIterable//
				.from(Dao.getAllPost())//
				.filter(new Predicate<Post>() {
					@Override
					public boolean apply(Post post) {
						return post.getDueDate() != null && post.getDueDate().isBefore(new DateTime()) ? true : false;
					}
				}).transform(new Function<Post, Long>() {
					@Override
					public Long apply(Post post) {
						return post.getId();
					}
				}).toList();

		Dao.deletePosts(postIdToDelete);
		return "Due posts deleted";
	}

}
