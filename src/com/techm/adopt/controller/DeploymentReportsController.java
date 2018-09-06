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

import com.techm.adopt.service.DeploymentReportsService;
import com.techm.adopt.service.DeploymentReportsServiceImpl;

/**
 * Servlet implementation class DeploymentReportsController
 */
@WebServlet({ "/deploymentdailygraph", "/deploymentenvgraph",
		"/deploymenttable" })
public class DeploymentReportsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Logger LOGGER = Logger
			.getLogger(CodeAnalysisReportsController.class);
	private DeploymentReportsService deploymentReportsService;

	public DeploymentReportsController() {
		this.deploymentReportsService = new DeploymentReportsServiceImpl();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		HttpSession session = request.getSession();
		String projectname = session.getAttribute("currentproject").toString();
		if (servletPath.equals("/deploymenttable")) {
			try {
				JSONArray jsonArray = deploymentReportsService
						.getDeploymentTable(projectname);
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			} catch (SQLException | JSONException e) {

				e.printStackTrace();
			}
		} else if (servletPath.equals("/deploymentdailygraph")) {
			try {
				JSONArray jsonArray = deploymentReportsService
						.getDeploymentDailyDetails(projectname);
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			} catch (SQLException | JSONException e) {
				LOGGER.error("Error in Deployment Daily Details report", e);
			}
		} else if (servletPath.equals("/deploymentenvgraph")) {
			try {
				JSONArray jsonArray = deploymentReportsService
						.getDeploymentEnvDetails(projectname);
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			} catch (SQLException | JSONException e) {
				LOGGER.error("Error in Deployment Env Details report", e);
			}
		}
	}

}
