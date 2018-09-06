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
import org.json.JSONObject;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.service.ProjectService;
import com.techm.adopt.service.ProjectServiceImpl;

/**
 * Servlet implementation class ProjectController
 */
@WebServlet({"/projectlist", "/releaselist"})
public class ProjectController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProjectService projectService;
	final static Logger LOGGER = Logger.getLogger(ProjectController.class);

	public ProjectController() {
		this.projectService = new ProjectServiceImpl();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		if(servletPath.equals("/projectlist")) {
			JSONArray projectlistjsonArray;
			try {
				HttpSession session = request.getSession(false);
				LoginBean lb = (LoginBean) session.getAttribute("username");
				projectlistjsonArray = projectService.getProjectList(lb);
				response.setContentType("application/json");
				response.getWriter().write(projectlistjsonArray.toString());
			} catch (JSONException e) {
				LOGGER.error("Error in fetching projectlist", e);
			}
		} else if(servletPath.equals("/releaselist")) {
			JSONObject json = null;
			HttpSession session = request.getSession(false);
			try {
				json = projectService.getReleaseList();
				response.setContentType("application/json");
				response.getWriter().write(json.toString());
				session.setAttribute("releaseNameId", json);
				//System.out.println("dd "+ request.getAttribute("releaseNameId"));
			} catch (JSONException | SQLException e) {
				LOGGER.error("Error in fetching releae list", e);
			}
		}
		
	}
}
