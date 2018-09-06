package com.techm.adopt.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.techm.adopt.bean.LinkedApplicationBean;
import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.ProjectDetailsBean;
import com.techm.adopt.bean.RoleBean;
import com.techm.adopt.bean.UserBean;
import com.techm.adopt.dao.UserMangementDao;
import com.techm.adopt.service.ProjectAdminService;
import com.techm.adopt.service.ProjectAdminServiceImpl;

/**
 * Servlet implementation class ProjectAdminController
 */
@WebServlet({"/roleusertable", "/linkedappstable","/mapuserrole","/addfeature","/updatefeature","/getfeaturesfromroles","/projectadmin","/linkedappsforproject"})
public class ProjectAdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final  Logger LOGGER = Logger.getLogger(ProjectAdminController.class);
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String servletPath = request.getServletPath();
		
		if(servletPath.equals("/roleusertable")){
			 HttpSession session = request.getSession(false);
			 LoginBean lb = (LoginBean) session.getAttribute("username");
			 
			 List<Map<String, String>> roleusers=new ArrayList<Map<String,String>>();
			 ProjectAdminService pas=new ProjectAdminServiceImpl();
			 List<ProjectDetailsBean> projects=new ArrayList<ProjectDetailsBean>();
			 UserMangementDao umd=new UserMangementDao();
			 try {
				 projects=umd.getProjects(lb);
				 
				 roleusers=pas.getUserRoles(projects);
				 JSONArray jsonArray = new JSONArray();
				 Map<String, String> roleuser=new HashMap<String, String>();
			
				 for (int i = 0; i < roleusers.size(); i++) {
					roleuser=roleusers.get(i);
					JSONObject json = new JSONObject();
						json.put("fname", roleuser.get("first_name"));
						json.put("uemail", roleuser.get("user_email"));
						json.put("uname", roleuser.get("user_name"));
						json.put("titles", roleuser.get("titles"));
						json.put("proj", roleuser.get("project"));
						jsonArray.put(json);
				}
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				LOGGER.error("Error in Fetching RoleUser Table", e);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				LOGGER.error("Error in Fetching RoleUser Table", e);
			}
			
		}
		
		
		else if(servletPath.equals("/linkedappstable")){
			List<LinkedApplicationBean> lapps=new ArrayList<LinkedApplicationBean>();
			ProjectAdminService pas=new ProjectAdminServiceImpl();
			lapps=pas.getLinkedApps();
			JSONArray jsonArray = new JSONArray();
			try {
				for (int i = 0; i < lapps.size(); i++) {
					LinkedApplicationBean lapp=lapps.get(i);
					JSONObject json = new JSONObject();
						json.put("fid", lapp.getId());
						json.put("featurename", lapp.getTabName());
						json.put("fdesc", lapp.getDescription());
						json.put("furl", lapp.getUrl());
						jsonArray.put(json);
				}
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				LOGGER.error("Error in Fetching Feature Table", e);
			}
			
		}
		
		else if(servletPath.equals("/linkedappsforproject")){
			HttpSession session = request.getSession(false);
			 LoginBean lb = (LoginBean) session.getAttribute("username");
			 
			List<ProjectDetailsBean> projects=new ArrayList<ProjectDetailsBean>();
			List<LinkedApplicationBean> lapps=new ArrayList<LinkedApplicationBean>();
			UserMangementDao umd=new UserMangementDao();
			 try {
				 projects=umd.getProjects(lb);
				 
				 ProjectAdminService pas=new ProjectAdminServiceImpl();
				 lapps=pas.getLinkedAppsForProject(projects);
				 JSONArray jsonArray = new JSONArray();
				 for (int i = 0; i < lapps.size(); i++) {
					 LinkedApplicationBean lapp=lapps.get(i);
						JSONObject json = new JSONObject();
							json.put("fid", lapp.getId());
							json.put("featurename", lapp.getTabName());
							json.put("fdesc", lapp.getDescription());
							json.put("furl", lapp.getUrl());
							json.put("fproject", lapp.getProject());
							jsonArray.put(json);
				 }
				 response.setContentType("application/json");
					response.getWriter().write(jsonArray.toString());
			 }
			 catch (JSONException e) {
					// TODO Auto-generated catch block
					LOGGER.error("Error in Fetching Feature Table For Project", e);
				} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		
		else if(servletPath.equals("/getfeaturesfromroles")){
			String title=request.getParameter("title");
			String email=request.getParameter("email");
				UserBean user=new UserBean();
					user.setEmail(email);
				RoleBean role=new RoleBean();
					role.setTitle(title);
			List<LinkedApplicationBean> lapps=new ArrayList<LinkedApplicationBean>();
			ProjectAdminService pas=new ProjectAdminServiceImpl();
			JSONArray jsonArray = new JSONArray();
			try {
				lapps=pas.getLinkedAppsFromRoles(user, role);
				for (int i = 0; i < lapps.size(); i++) {
					LinkedApplicationBean lapp=lapps.get(i);
					JSONObject json = new JSONObject();
					json.put("appid", lapp.getId());
					json.put("appname", lapp.getTabName());
					jsonArray.put(json);
				}
				response.setContentType("application/json");
				response.getWriter().write(jsonArray.toString());
							
			} catch (SQLException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		else if(servletPath.equals("/projectadmin")){
			RequestDispatcher rd = request.getRequestDispatcher("projectadmin.jsp");
			rd.forward(request, response);
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String servletPath = request.getServletPath();
		
		if(servletPath.equals("/mapuserrole")){
			try {
				String user=request.getParameter("uservalue").trim();
				String role=request.getParameter("rolevalue").trim();
				Integer projectId=Integer.parseInt(request.getParameter("projectvalue"));
				
				if(user!=null && user.length()>0 && role!=null && role.length()>0 && projectId!=null){
					UserBean ub=new UserBean();
						ub.setId(user);
					RoleBean rb=new RoleBean();
						rb.setId(role);
					ProjectDetailsBean pb=new ProjectDetailsBean();
						pb.setProjectId(projectId);
						
					ProjectAdminService pas=new ProjectAdminServiceImpl();
					pas.mapUserRoles(ub, rb, pb);
						System.out.println("Project Mapping");
					RequestDispatcher rd = request.getRequestDispatcher("/projectadmin");
					rd.forward(request, response);
					
				}
				else {
					LOGGER.error("rolename and username cannot be null");
				}
			}
			catch (Exception e) {
				LOGGER.error("Error in mapuserrole", e);
				RequestDispatcher rd = request.getRequestDispatcher("/projectadmin");
				rd.forward(request, response);
			}
		
		}
		
		
		else if(servletPath.equals("/addfeature")){
			try {
				String url = request.getParameter("inputURL").trim();
				String tabname = request.getParameter("inputTabname").trim();
				String description = request.getParameter("inputDescription").trim();
				String role=request.getParameter("rolevalue").trim();
				int project=Integer.parseInt(request.getParameter("projectvalue"));
				
				if(url!= null && url.length()>0 && tabname != null && tabname.length()>0 && description != null && description.length()>0 ){
					LinkedApplicationBean lab=new LinkedApplicationBean();
					lab.setUrl(url);
					lab.setTabName(tabname);
					lab.setDescription(description);
					
					ProjectDetailsBean pdb=new ProjectDetailsBean();
					pdb.setProjectId(project);
					
					RoleBean rb=new RoleBean();
					rb.setId(role);
					
					ProjectAdminService pas=new ProjectAdminServiceImpl();
					pas.insertApplication(lab,pdb,rb);
					
					RequestDispatcher rd = request.getRequestDispatcher("projectadmin.jsp");
					rd.forward(request, response);
				}
				else {
					LOGGER.error("url, tabname and description cannot be null");
				}
			}
			catch (Exception e) {
				LOGGER.error("Error in addfeature", e);
			}
		}
		
		
		else if(servletPath.equals("/updatefeature")){
			try {
				String fid=request.getParameter("editfeatureid");
				int id=Integer.parseInt(fid);
				String fname=request.getParameter("editfeaturename");
				String furl=request.getParameter("editfeatureurl");
				String fdesc=request.getParameter("editfeaturedesc");
				String fproject=request.getParameter("editfeatureproject");
				
				if(fname!=null && fname.length()>0 && furl!=null && furl.length()>0 && fdesc!=null && fdesc.length()>0){
					LinkedApplicationBean lab=new LinkedApplicationBean();
						lab.setId(id);
						lab.setTabName(fname);
						lab.setDescription(fdesc);
						lab.setUrl(furl);
						lab.setProject(fproject);
						ProjectAdminService pas=new ProjectAdminServiceImpl();
						pas.updateApplication(lab);
						
						RequestDispatcher rd = request.getRequestDispatcher("projectadmin.jsp");
						rd.forward(request, response);
				}
				else {
					LOGGER.error("feature name, description and url cannot be null");
				}
				
				
			}
			catch (Exception e) {
				LOGGER.error("Error in updatefeature", e);
			}
		}
		
		
		else if(servletPath.equals("/projectadmin")){
			RequestDispatcher rd = request.getRequestDispatcher("projectadmin.jsp");
			rd.forward(request, response);
		}
		
		
	}

}
