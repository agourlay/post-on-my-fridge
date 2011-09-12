package com.agourlay.pomf.controller;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agourlay.pomf.dao.Dao;
import com.agourlay.pomf.model.Post;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

public class GetPositionPostController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Long id = Long.parseLong(req.getParameter("id"));
		Post post = Dao.INSTANCE.getPostById(id);
		resp.setContentType("text/x-json");
		
		if (post != null){
			JSONObject jsonPosition = new JSONObject();
			try {
				jsonPosition.append("left", post.getPositionX());
				jsonPosition.append("top", post.getPositionY());
				jsonPosition.write(resp.getWriter());
			} catch (JSONException e1) {
				e1.printStackTrace();
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);	
			}
		}
		resp.setStatus(HttpServletResponse.SC_ACCEPTED);	
		
	}

}
