package com.techm.adopt.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutController
 */
@WebServlet({ "/logout", "/invalidlogin" })
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		if (servletPath.equals("/logout")) {
			HttpSession session = request.getSession(false);
			if (null != session) {
				response.setHeader("Cache-Control",
						"no-cache, no-store, must-revalidate");
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
				session.invalidate();
				RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
				rd.forward(request, response);
			}
		} else if (servletPath.equals("/invalidlogin")) {
			RequestDispatcher rd = request
					.getRequestDispatcher("invalidlogin.jsp");
			rd.forward(request, response);
		}
	}
}
