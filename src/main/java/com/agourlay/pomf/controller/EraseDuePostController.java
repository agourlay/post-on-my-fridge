package com.agourlay.pomf.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.tools.predicates.DuePost;
import com.agourlay.pomf.tools.transformer.ExtractPostId;
import com.google.common.collect.FluentIterable;

public class EraseDuePostController extends HttpServlet {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3523137355230182591L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		List<Long> postIdToDelete = FluentIterable
	       .from(Post.getAllPost())
	       .filter(new DuePost())
	       .transform(new ExtractPostId())
	       .toList();
		
		Post.remove(postIdToDelete);

		resp.setContentType("text/html");
		resp.setStatus(HttpServletResponse.SC_ACCEPTED);	
	}
}
