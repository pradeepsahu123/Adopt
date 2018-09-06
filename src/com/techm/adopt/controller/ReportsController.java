package com.techm.adopt.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.service.ReportsService;
import com.techm.adopt.service.ReportsServiceImpl;

/**
 * Servlet implementation class ReportsController
 */
@WebServlet({ "/buildreports", "/codequalityreports", "/deploymentreports",
		"/testingreports", "/projectsummary" })
public class ReportsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Logger LOGGER = Logger.getLogger(ReportsController.class);
	private ReportsService reportsService;

	public ReportsController() {
		this.reportsService = new ReportsServiceImpl();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (null != session) {
			try {
				processRequest(request, response, session);
			} catch (SQLException e) {
				LOGGER.error("Error in report page redirection", e);
			}
		} else {
			response.setHeader("Cache-Control",
					"no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			RequestDispatcher rd = request.getRequestDispatcher("invalid.jsp");
			rd.forward(request, response);
		}
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException, SQLException {
		String servletPath = request.getServletPath();
		LoginBean lb = (LoginBean) session.getAttribute("username");
		if (servletPath.equals("/buildreports")) {
			redirectReportPage(request, response, lb, "buildreports.jsp");
		} else if (servletPath.equals("/codequalityreports")) {
			redirectReportPage(request, response, lb, "codequalityreports.jsp");
		} else if (servletPath.equals("/deploymentreports")) {
			redirectReportPage(request, response, lb, "deploymentreports.jsp");
		} else if (servletPath.equals("/testingreports")) {
			redirectReportPage(request, response, lb, "testreports.jsp");
		} else if (servletPath.equals("/projectsummary")) {
			redirectReportPage(request, response, lb, "projectsummary.jsp");
		}
	}

	protected void redirectReportPage(HttpServletRequest request,
			HttpServletResponse response, LoginBean lb, String pagename)
			throws ServletException, IOException {
		JSONArray jsonArray = null;
		try {
			jsonArray = reportsService.getReportsList(lb);
			RequestDispatcher rd = request.getRequestDispatcher(pagename);
			request.setAttribute("reportlist", jsonArray);
			rd.forward(request, response);
		} catch (JSONException e) {
			LOGGER.error("Error in report page redirection", e);
		}
	}
}
