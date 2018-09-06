package com.techm.adopt.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONObject;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.ProjectDetailsBean;
import com.techm.adopt.bean.RoleBean;
import com.techm.adopt.dao.ProjectDao;
import com.techm.adopt.dao.ProjectDaoImpl;
import com.techm.adopt.service.UserService;
import com.techm.adopt.service.UserServiceImpl;

/**
 * Servlet implementation class Logincontroller
 */
@WebServlet({ "/userhome","/beforeuserhome","/usersrolelist","/roleproj","/afteruserhome","/userhome2" })
public class Logincontroller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	final static Logger LOGGER = Logger.getLogger(Logincontroller.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
			
		if(servletPath.equals("/usersrolelist")){
			HttpSession session = request.getSession(false);
			LoginBean lb = (LoginBean) session.getAttribute("username");
			
			List<RoleBean> list=new ArrayList<RoleBean>();
			UserService us=new UserServiceImpl();
			list=us.getRolesForUser(lb);
			try{
				JSONArray jsonArray = new JSONArray();
				RoleBean role=new RoleBean();
				for(int i=0; i<list.size(); i++){
					role=list.get(i);
					JSONObject json = new JSONObject();
					json.put("roleid", role.getId());
					json.put("roletitle", role.getTitle());
					jsonArray.put(json);
				}
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				LOGGER.error("Error in Fetching Roles for User Table", e);
			}
			
		}
		
		else if(servletPath.equals("/userhome")){
			
			RequestDispatcher rd = request.getRequestDispatcher("userhome.jsp");
			rd.forward(request, response);
		}

		else if(servletPath.equals("/userhome2")){
			
			RequestDispatcher rd = request.getRequestDispatcher("userhome2.jsp");
			rd.forward(request, response);
		}
		else if(servletPath.equals("/roleproj")){
			
			RequestDispatcher rd = request.getRequestDispatcher("roleproj.jsp");
			rd.forward(request, response);
		}
		
	}
	
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath(); 
		
		if(servletPath.equals("/beforeuserhome")){
			//HttpSession session = request.getSession(false);
			//LoginBean lb = (LoginBean) session.getAttribute("username");
			
			RequestDispatcher rd = request.getRequestDispatcher("roleproj.jsp");
			rd.forward(request, response);
		}
		
		else if(servletPath.equals("/userhome")){
			try {
				//session.setAttribute("currentproject", "BNSF TSM");
				String role=request.getParameter("usersrole");
				HttpSession session = request.getSession(false);
				LoginBean lb = (LoginBean) session.getAttribute("username");
				lb.setRoleId(role);
				List<ProjectDetailsBean> projects=new ArrayList<ProjectDetailsBean>();
				ProjectDao pd=new ProjectDaoImpl();
				projects=pd.getProjectList(lb);
		
				session.setAttribute("currentproject", projects.get(0).getProjectName());
				
				RequestDispatcher rd = request.getRequestDispatcher("userhome.jsp");
				rd.forward(request, response);

			} catch (Exception e) {
				LOGGER.error("Error in login controller", e);
			}
		}
		
		else if(servletPath.equals("/afteruserhome")){
			HttpSession session = request.getSession(false);
			//LoginBean lb = (LoginBean) session.getAttribute("username");
			String project=request.getParameter("project");
			session.setAttribute("currentproject", project);
			
			RequestDispatcher rd = request.getRequestDispatcher("userhome.jsp");
			rd.forward(request, response);
		}
	}
}
