package com.agourlay.pomf.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FridgeController extends HttpServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 779325282149978767L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/fridge.jsp");
		rd.forward(req, resp);
	}
}
