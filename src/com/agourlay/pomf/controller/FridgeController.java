package com.agourlay.pomf.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FridgeController extends HttpServlet{

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException {
		//TODO add some string validation there
		String fridgeId= req.getPathInfo().substring(1);
		req.setAttribute("fridgeId",fridgeId);
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/fridge.jsp");
		rd.forward(req, resp);
	}
}
