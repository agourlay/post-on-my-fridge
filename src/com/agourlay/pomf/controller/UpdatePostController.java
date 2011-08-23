package com.agourlay.pomf.controller;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agourlay.pomf.dao.Dao;

public class UpdatePostController extends HttpServlet {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
			Long id = Long.parseLong(req.getParameter("id"));
			String posXString = req.getParameter("positionX");
			String posYString = req.getParameter("positionY");
			
			Integer positionX = Integer.parseInt(posXString);
			Integer positionY = Integer.parseInt(posYString);
			
			Dao.INSTANCE.updatePosition(id, positionX, positionY);
			
			resp.setContentType("text/html");
			resp.setStatus(HttpServletResponse.SC_FOUND);	
			}
	}

