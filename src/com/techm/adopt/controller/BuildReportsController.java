package com.techm.adopt.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import com.techm.adopt.service.BuildReportsService;
import com.techm.adopt.service.BuildReportsServiceImpl;

@WebServlet({ "/buildgraph", "/buildtable" })
public class BuildReportsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Logger LOGGER = Logger.getLogger(BuildReportsController.class);
	private BuildReportsService buildReportsService;

	public BuildReportsController() {
		this.buildReportsService = new BuildReportsServiceImpl();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String servletPath = request.getServletPath();
		HttpSession session = request.getSession();
		String projectname = session.getAttribute("currentproject").toString();

		if (servletPath.equals("/buildtable")) {
			try {
				JSONArray buildDetaisjsonArray = buildReportsService
						.getBuildDetails(projectname);
				response.setContentType("application/json");
				response.getWriter().write(buildDetaisjsonArray.toString());
			} catch (SQLException | JSONException e) {
				LOGGER.error("Error in Build Details report", e);
			}
		} else if (servletPath.equals("/buildgraph")) {
			try {
				JSONArray jsonArray = buildReportsService
						.getDailyBuildDetails(projectname);
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			} catch (SQLException | JSONException e) {
				LOGGER.error("Error in Daily Build Details report", e);
			}
		}
	}
}
