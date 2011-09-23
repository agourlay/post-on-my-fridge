package com.agourlay.pomf.controller;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agourlay.pomf.dao.Dao;
import com.agourlay.pomf.tools.Validation;

@SuppressWarnings("serial")
public class AddPostController extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		resp.setContentType("text/html");
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);	
				
		int captchaNumberInSession = 0;
		int captchaNumberSubmitted = 0;
		
		try {
			captchaNumberInSession = (Integer) req.getSession().getAttribute("captchaNumber");
			captchaNumberSubmitted = Integer.parseInt(req.getParameter("captcha"));
		} catch (Exception e) {
			return;
		}
		
		if (captchaNumberInSession == captchaNumberSubmitted){
			String content = Validation.checkNull(req.getParameter("content"));
			String author = Validation.checkNull(req.getParameter("author"));
			Double positionX = Double.parseDouble(req.getParameter("positionX"));
			Double positionY = Double.parseDouble(req.getParameter("positionY"));
			String color = Validation.checkNull(req.getParameter("color"));
			
			Dao.INSTANCE.add(author, content,positionX,positionY,color);
			resp.setStatus(HttpServletResponse.SC_ACCEPTED);	
		}

	}

}