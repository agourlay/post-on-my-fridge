package com.agourlay.pomf.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agourlay.pomf.dao.Dao;
import com.agourlay.pomf.model.Post;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.Gson;

public class GetPostController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		resp.setContentType("application/json");
		resp.setStatus(HttpServletResponse.SC_ACCEPTED);
		PrintWriter out = resp.getWriter();
		
	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    String postCached = (String) syncCache.get("fridgeContent"); 
	    
	    if (postCached == null) {
	    	List<Post> posts = Dao.INSTANCE.getPosts();	
			Gson gson = new Gson();
			String jsonResult = gson.toJson(posts);
			syncCache.put("fridgeContent", jsonResult);
			out.print(jsonResult);
	    }else{
	    	out.print(postCached);
	    }
		out.flush();	
	}
}
