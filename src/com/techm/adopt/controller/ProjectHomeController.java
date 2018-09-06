package com.techm.adopt.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.ReportBean;
import com.techm.adopt.service.ReportsService;
import com.techm.adopt.service.ReportsServiceImpl;
import com.techm.adopt.service.UserService;
import com.techm.adopt.service.UserServiceImpl;

/**
 * Servlet implementation class ProjectHomeController
 */
@WebServlet({ "/projecthome", "/buildpromotion", "/application" })
public class ProjectHomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Logger LOGGER = Logger.getLogger(ProjectHomeController.class);
	private UserService userService;
	private ReportsService reportsService;
	public ProjectHomeController() {
		this.userService = new UserServiceImpl();
		this.reportsService = new ReportsServiceImpl();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		if (null != session) {
			processRequest(request, response, session);
		}
	}
	
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		LoginBean lb = (LoginBean) session.getAttribute("username");
		if (servletPath.equals("/projecthome")) {
			List<ReportBean> reportpagelist = reportsService.getReportsPage(lb);
			RequestDispatcher rd = request
					.getRequestDispatcher("projecthome.jsp");
			request.setAttribute("reportpage", reportpagelist);
			rd.forward(request, response);
		} else if (servletPath.equals("/buildpromotion")) {
			RequestDispatcher rd = request
					.getRequestDispatcher("buildpromotion.jsp");
			rd.forward(request, response);
		} else if (servletPath.equals("/application")) {
			String projectname = session.getAttribute("currentproject")
					.toString();
			String pagename = request.getParameter("pagename");
			try {
				List<LoginBean> loginbeanlist = userService.getTabsList(lb,
						projectname);
				session.setAttribute("tabslist", loginbeanlist);

				List<LoginBean> iframepage = userService.getPage(lb,
						projectname, pagename);
				for(int i=0; i<iframepage.size(); i++){
				System.out.println(iframepage.get(i).getTabUrl());
				}
				RequestDispatcher rd = request
						.getRequestDispatcher("dashboard.jsp");
				request.setAttribute("iframeurl", iframepage);
				rd.forward(request, response);
			} catch (SQLException e) {
				LOGGER.error("Error in loading '/application' servlet ", e);
			}
		}
	}
}
