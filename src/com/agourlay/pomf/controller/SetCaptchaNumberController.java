package com.agourlay.pomf.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SetCaptchaNumberController extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private int min = 0;
	private int max = 4;
	private Random rand = new Random();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		int captchaNumber = rand.nextInt(max - min + 1) + min;
		req.getSession().setAttribute("captchaNumber", captchaNumber);
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.println(captchaNumber);
		}
}
