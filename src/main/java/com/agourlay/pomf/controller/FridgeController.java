package com.agourlay.pomf.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agourlay.pomf.model.Fridge;
import com.google.common.base.Strings;

public class FridgeController extends HttpServlet{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 779325282149978767L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException {
		//TODO add some string validation there
		String fridgeId = req.getPathInfo();
		validFridgeIdOrGoHome(req, resp, fridgeId);
		fridgeId = fridgeId.substring(1);
		validFridgeIdOrGoHome(req, resp, fridgeId);
		String fridgeName = fridgeId;
		
		Fridge fridge = Fridge.getFridgeById(fridgeId);
		if(fridge != null){
			fridgeName = fridge.getName();
		}
		
		req.setAttribute("fridgeId",fridgeName);
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/fridge.jsp");
		rd.forward(req, resp);
	}

	private void validFridgeIdOrGoHome(HttpServletRequest req, HttpServletResponse resp,
			String fridgeId) throws ServletException, IOException {
			if(Strings.isNullOrEmpty(fridgeId)){
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/");
				rd.forward(req, resp);
			}
	}
}
