package com.agourlay.pomf.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.*;

import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.tools.Utils;

@Path("/post")
public class PostResource {
	
	@DELETE
	@Path("/{postId}")
	public void deletePost(@PathParam ("postId") String postId)  {
		Post.remove(Long.parseLong(postId));
	}
	
	@PUT
	@Path("/{postId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void updatePost(@PathParam ("postId") String postId,
			@FormParam("positionX") String positionX,
			@FormParam("positionY") String positionY) {
		
		Long id = Long.parseLong(postId);

		Double posX = Double.parseDouble(positionX);
		Double posY = Double.parseDouble(positionY);
		
		Post.updatePosition(id, posX, posY);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void addPost(@Context HttpServletRequest req,
			@FormParam("fridgeId") final String fridgeId,
			@FormParam("captcha") String captcha,
			@FormParam("positionX") String positionX,@FormParam("positionY") String positionY,
			@FormParam("author") String author,@FormParam("content") String content,
			@FormParam("color") String color, @FormParam("dueDate") String dueDate) {
		
		int captchaNumberInSession = 0;
		int captchaNumberSubmitted = 0;
		
		try {
			captchaNumberInSession = (Integer) req.getSession().getAttribute("captchaNumber");
			captchaNumberSubmitted = Integer.parseInt(captcha);
		} catch (Exception e) {
			throw new WebApplicationException(
			        Response
			          .status(Status.BAD_REQUEST)
			          .entity("Captcha invalid")
			          .build()
			      );
		}
		
		if (captchaNumberInSession == captchaNumberSubmitted){
			Double posX = Double.parseDouble(positionX);
			Double posY = Double.parseDouble(positionY);
			Post.add(fridgeId,author, content,posX,posY,color,Utils.stringToDate(dueDate, "MM/dd/yyyy"));	
		}
	}

}
