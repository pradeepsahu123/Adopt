package com.techm.adopt.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class SessionFilter
 */

@WebFilter({ "/userhome", "/application", "/admin", "*.jsp" })
public class SessionFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String servletPath = request.getServletPath();
		if (servletPath.equals("/home.jsp")) {
			response.sendRedirect("home");
			return;
		} 
		else if (servletPath.equals("/application")) {
			String pagename = request.getParameter("pagename");
			if (null == pagename) {
				response.sendRedirect("invalidlogin");
				return;
			}
		}
		HttpSession session = request.getSession(false);
		boolean loggedIn = session != null
				&& session.getAttribute("username") != null
				&& !servletPath.contains("jsp");
 		if (loggedIn) {
			response.setHeader("Cache-Control",
					"no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			chain.doFilter(request, response);
		} else {
			response.sendRedirect("invalidlogin");
			return;
		}
	}

	public void init(FilterConfig config) throws ServletException {

	}
}
