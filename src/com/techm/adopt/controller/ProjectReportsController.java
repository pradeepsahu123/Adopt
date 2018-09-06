package com.techm.adopt.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techm.adopt.service.ProjectReportsService;
import com.techm.adopt.service.ProjectReportsServiceImpl;
import com.techm.adopt.service.ProjectService;
import com.techm.adopt.service.ProjectServiceImpl;

/**
 * Servlet implementation class ProjectReportsController
 */
@WebServlet({ "/sprintreport", "/testtrend", "/projectburndown", "/releaseburndown", "/sprintIssues", "/releaseVersions" ,"/sprintIssuesprojects" ,"/allProjectsState" ,"/allProjSprintDetails" ,"/openIssues"})
public class ProjectReportsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Logger LOGGER = Logger
			.getLogger(ProjectReportsController.class);
	private ProjectReportsService projectReportsService;
	private ProjectService projectService;
	
	public ProjectReportsController() {
		this.projectReportsService = new ProjectReportsServiceImpl();
		this.projectService = new ProjectServiceImpl();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String servletPath = request.getServletPath();
		String projectname;
		HttpSession session = request.getSession(false);

		if (request.getParameter("projectnamejsp") != null) {
			projectname = request.getParameter("projectnamejsp");
		} else {
			projectname = session.getAttribute("currentproject").toString();
		}

		if (servletPath.equals("/sprintreport")) {
			try {

				JSONArray jsonArray = projectReportsService
						.getSprintDetails(projectname);
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			} catch (JSONException | SQLException e) {
				LOGGER.error("Error in Sprint report", e);
			}
		} else if (servletPath.equals("/testtrend")) {
			try {
				JSONArray jsonArray = projectReportsService
						.getTestTrend(projectname);
				response.setContentType("application/json");
				//response.getWriter().write(jsonArray.toString());
			} catch (JSONException | SQLException e) {
				LOGGER.error("Error in Test trend report", e);
			}
		} else if (servletPath.equals("/projectburndown")) {
			try {
				JSONArray jsonArray = projectReportsService
						.getSprintBurnDown(projectname);
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			} catch (JSONException | SQLException | ParseException e) {
				LOGGER.error("Error in Projectburndown report", e);
			}
		} else if (servletPath.equals("/releaseburndown")) {
			try {
				String releaseName = request.getParameter("releasename");
				String releaseID = request.getParameter("releaseId");
				JSONObject json = projectService.getReleaseList();
				//String releaseId = json.getString(releaseName);
				//String releaseId = "10001";
				JSONObject jsonObject = projectReportsService
						.releaseBurndown(releaseID);
				response.setContentType("application/json");
				response.getWriter().write(jsonObject.toString());
			} catch (JSONException | SQLException | ParseException e) {
				LOGGER.error("Error in Release burndown report", e);
			}
		}//swapna start
		else if (servletPath.equals("/sprintIssues")) {
			try {
				JSONObject jsonObject = projectReportsService
						.getSprintsIssuesDetails(projectname);
				response.setContentType("application/json");
				response.getWriter().write(jsonObject.toString());
			} catch (JSONException | SQLException e) {
				LOGGER.error("Error in Sprint Issues report", e);
			}
		}else if (servletPath.equals("/sprintIssuesprojects")) {
			try {
				String projectName = request.getParameter("projectName");
				JSONArray jsonArray = projectReportsService
						.getReleaseVersions(projectName);
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			} catch (JSONException | SQLException e) {
				LOGGER.error("Error in Release versions report", e);
			}
		}else if (servletPath.equals("/releaseVersions")) {
			try {
				
				JSONArray jsonArray = projectReportsService
						.getReleaseVersions(projectname);
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			} catch (JSONException | SQLException e) {
				LOGGER.error("Error in Release versions report", e);
			}}
			else if (servletPath.equals("/releaseIds")) {
				try {
					
					JSONObject jsonObject = projectReportsService
							.getReleasesIds(projectname);
					response.setContentType("application/json");
					response.getWriter().write(jsonObject.toString());
				} catch (JSONException | SQLException e) {
					LOGGER.error("Error in Release versions report", e);
				}
		}else if (servletPath.equals("/allProjectsState")) {
			try {
				JSONObject jsonObject = projectReportsService
						.getAllProjects();
				System.out.println(jsonObject);
				response.setContentType("application/json");
				response.getWriter().write(jsonObject.toString());
			} catch (JSONException | SQLException e) {
				LOGGER.error("Error in Sprint Issues report", e);
			}
		}else if (servletPath.equals("/allProjSprintDetails")) {
			try {
				JSONObject jsonObject = projectReportsService
						.getAllDetailsofProject();
				System.out.println(jsonObject);
				response.setContentType("application/json");
				response.getWriter().write(jsonObject.toString());
			} catch (JSONException | SQLException e) {
				LOGGER.error("Error in Sprint Issues report", e);
			}
		}else if (servletPath.equals("/openIssues")) {
			try {
				JSONObject jsonObject = projectReportsService
						.getOpenIssuesofProject();
				System.out.println(jsonObject);
				response.setContentType("application/json");
				response.getWriter().write(jsonObject.toString());
			} catch (JSONException | SQLException e) {
				LOGGER.error("Error in Sprint Issues report", e);
			}
		}
		//swapna end
	}
}
