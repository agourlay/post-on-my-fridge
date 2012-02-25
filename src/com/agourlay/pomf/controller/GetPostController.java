package com.agourlay.pomf.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agourlay.pomf.dao.Dao;
import com.agourlay.pomf.model.Post;
import com.google.gson.Gson;

public class GetPostController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		resp.setContentType("application/json");
		List<Post> posts = Dao.INSTANCE.getPosts();	
		Gson gson = new Gson();
		String jsonResult = gson.toJson(posts);
		PrintWriter out = resp.getWriter();
		out.print(jsonResult);
		out.flush();
		resp.setStatus(HttpServletResponse.SC_ACCEPTED);
	}

}
