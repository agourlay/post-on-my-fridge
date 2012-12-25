package com.agourlay.pomf.controler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.view.Viewable;

@Path("/")
public class IndexControler {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable index(@Context HttpServletRequest request) {
		return new Viewable("/index.jsp", null);
	}

}
