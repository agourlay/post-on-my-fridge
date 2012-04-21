package com.agourlay.pomf.controller;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agourlay.pomf.dao.Dao;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class RemovePostController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		String id = req.getParameter("id");
		Dao.INSTANCE.remove(Long.parseLong(id));
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.delete("fridgeContent");
		
		resp.setContentType("text/html");
		resp.setStatus(HttpServletResponse.SC_ACCEPTED);	
	}
}

