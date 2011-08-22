package com.agourlay.pomf.controller;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agourlay.pomf.dao.Dao;

@SuppressWarnings("serial")
public class AddPostController extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		System.out.println("Creating new Post ");

		String content = checkNull(req.getParameter("content"));
		String author = checkNull(req.getParameter("author"));
		Dao.INSTANCE.add(author, content);

		resp.sendRedirect("/TheFridge.jsp");
	}

	private String checkNull(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}
}