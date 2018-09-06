package com.techm.adopt.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.service.UserService;
import com.techm.adopt.service.UserServiceImpl;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter("/authentication")
public class AuthenticationFilter implements Filter {
	final static Logger LOGGER = Logger.getLogger(AuthenticationFilter.class);
	private UserService userService;

	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		LoginBean lb = new LoginBean();
		lb.setUsername(username);
		lb.setPassword(password);

		userService = new UserServiceImpl();
		try {
			if (userService.checkUser(lb)) {
				HttpSession session = request.getSession();
				response.setHeader("Cache-Control",
						"no-cache, no-store, must-revalidate");
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
				session.setAttribute("username", lb);
				//response.sendRedirect("userhome");
				RequestDispatcher rd = request.getRequestDispatcher("beforeuserhome");
				rd.forward(request, response);
				//response.sendRedirect("beforeuserhome");
				return;
			} else {
				response.sendRedirect("invalidlogin");
				return;
			}
		} catch (Exception e) {
			LOGGER.error("Invalid user login", e);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
