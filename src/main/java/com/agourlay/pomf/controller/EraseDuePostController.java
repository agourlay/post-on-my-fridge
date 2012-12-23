package com.agourlay.pomf.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import com.agourlay.pomf.model.Post;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class EraseDuePostController extends HttpServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3523137355230182591L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// waiting for filter on date to be implemented on delete in Objectify
		List<Long> postIdToDelete = FluentIterable.from(Post.getAllPost()).filter(new Predicate<Post>() {
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

		Post.remove(postIdToDelete);

		resp.setContentType("text/html");
		resp.setStatus(HttpServletResponse.SC_ACCEPTED);
	}
}
