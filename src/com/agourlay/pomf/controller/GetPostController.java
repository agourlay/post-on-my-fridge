package com.agourlay.pomf.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agourlay.pomf.dao.Dao;
import com.agourlay.pomf.model.Post;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

public class GetPostController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("text/x-json");
		List<Post> posts = Dao.INSTANCE.getPosts();
		
		JSONObject jsonPositions = new JSONObject();
		
		try {
			for (Post post : posts) {
				JSONObject jsonPosition = new JSONObject();
				jsonPosition.put("id", post.getId());
				jsonPosition.put("left", post.getPositionX());
				jsonPosition.put("top", post.getPositionY());
				jsonPosition.put("content", post.getContent());
				jsonPosition.put("date", post.getFormatedDate());
				jsonPosition.put("author", post.getAuthor());
				
				//quick dirty fix
				if (posts.size() == 1){
					JSONObject[] array = new JSONObject[] {jsonPosition};
					jsonPositions.accumulate("postPosition", array);
				}else{
					jsonPositions.accumulate("postPosition", jsonPosition);
				}
				
			}
			jsonPositions.write(resp.getWriter());
		} catch (JSONException e1) {
			e1.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		resp.setStatus(HttpServletResponse.SC_ACCEPTED);

	}

}
