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

import com.techm.adopt.service.CodeAnalysisReportsService;
import com.techm.adopt.service.CodeAnalysisReportsServiceImpl;

/**
 * Servlet implementation class CodeAnalysisReportsController
 */
@WebServlet({ "/codeanalysistdgrapgh", "/codeanalysistable",
		"/codeanalysisissuegraph" ,"/codeQualityProject"})
public class CodeAnalysisReportsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Logger LOGGER = Logger
			.getLogger(CodeAnalysisReportsController.class);
	private CodeAnalysisReportsService codeAnalysisReportsService;

	public CodeAnalysisReportsController() {
		this.codeAnalysisReportsService = new CodeAnalysisReportsServiceImpl();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String servletPath = request.getServletPath();
		HttpSession session = request.getSession();
		String projectname = session.getAttribute("currentproject").toString();

		if (servletPath.equals("/codeanalysistable")) {
			try {
				JSONArray jsonArray = codeAnalysisReportsService
						.getCodeAnalysisDetails(projectname);
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			} catch (SQLException | JSONException e) {
				LOGGER.error("Error in Code Analysis Details report", e);
			}

		} else if (servletPath.equals("/codeanalysisissuegraph")) {
			try {
				JSONArray jsonArray = codeAnalysisReportsService
						.getCodeAnalysisIssues(projectname);
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			} catch (SQLException | JSONException e) {
				LOGGER.error("Error in Code Analysis Issues report", e);
			}
		} else if (servletPath.equals("/codeQualityProject")) {
			try {
				JSONObject jsonArray = codeAnalysisReportsService
						.getCodeQualityProject();
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			} catch (SQLException | JSONException e) {
				LOGGER.error("Error in Code Analysis Issues report", e);
			}
		}
	}
}
