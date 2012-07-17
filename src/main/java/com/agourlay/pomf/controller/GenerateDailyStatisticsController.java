package com.agourlay.pomf.controller;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agourlay.pomf.model.Stat;

public class GenerateDailyStatisticsController extends HttpServlet {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2466731097637252150L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Stat.generateDailyStat();
		resp.setStatus(HttpServletResponse.SC_ACCEPTED);	
	}
}

