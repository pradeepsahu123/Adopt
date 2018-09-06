package com.techm.adopt.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.techm.adopt.bean.UserBean;
import com.techm.adopt.dao.UserMangementDao;
import com.techm.adopt.service.UserMangementServiveImpl;
import com.techm.adopt.util.DBConnectionUtil;

/**
 * Servlet implementation class UserMangementController
 */
@WebServlet({"/UserMangementController", "/edituser" ,"/updateuser","/updaterole","/addrole","/deleteuser","/usertable","/roletable","/projectstableforpa","/projectadminconfig"})
public class UserMangementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final  Logger LOGGER = Logger.getLogger(UserMangementController.class);
       
   

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		if(servletPath.equals("/UserMangementController")) {
			try {
				String firstname = request.getParameter("firstname").trim();
				String lastname = request.getParameter("lastname").trim();
				String username = request.getParameter("username").trim();
				String email = request.getParameter("email").trim();
				String password =request.getParameter("password").trim();
				
				if(firstname!= null && firstname.length()>0 && lastname != null && lastname.length()>0 && username != null && username.length()>0 && email != null && email.length()>0&& password != null && password.length()>0){
					UserBean ub = new UserBean();
					ub.setFirstname(firstname);
					ub.setLaastname(lastname);
					ub.setUsername(username);
					ub.setEmail(email);
					ub.setPassword(password);
					new UserMangementServiveImpl().cryptWithMD5(ub);
					
					RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
					rd.forward(request, response);
				}else {
					LOGGER.error("username,password,email ,role can not be null or empty "+"username="+username +"password="+password +"firstname="+firstname +"email="+email +"lastname="+lastname);
				}
			} catch (Exception e) {
				LOGGER.error("Error in User Mangement Controller", e);
			}
		}
		else if (servletPath.equals("/updateuser")) {
			try {
				String firstname = request.getParameter("editfirstname").trim();
				String lastname = request.getParameter("editlastname").trim();
				String username = request.getParameter("editusername").trim();
				String email = request.getParameter("editemail").trim();
				String userID = request.getParameter("edituserid").trim();
				System.out.println("editfirstname="+firstname+" lastname="+lastname+" username="+ username+" email="+email+" userID="+userID);
				
				if(firstname!= null && firstname.length()>0 && lastname != null && lastname.length()>0 && username != null && username.length()>0 && email != null && email.length()>0){
					UserBean ub = new UserBean();
					ub.setFirstname(firstname);
					ub.setLaastname(lastname);
					ub.setUsername(username);
					ub.setEmail(email);
					ub.setId(userID);
					
					new UserMangementDao().updateUserDetails(ub);
					
					RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
					rd.forward(request, response);
				}else {
					LOGGER.error("username,password,email ,role can not be null or empty "+"username="+username +"firstname="+firstname +"email="+email +"lastname="+lastname);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		else if (servletPath.equals("/updaterole")) {
			try {
				String roleid=request.getParameter("editroleid").trim();
				String roletitle=request.getParameter("editroletitle").trim();
				String roledesc=request.getParameter("editroledesc").trim();
				
				if(roletitle!=null && roletitle.length()>0 && roledesc!=null && roledesc.length()>0){
					RoleBean rb=new RoleBean();
					rb.setId(roleid);
					rb.setTitle(roletitle);
					rb.setDesc(roledesc);
					
					new UserMangementDao().updateRoleDetails(rb);
					
					RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
					rd.forward(request, response);
				}
				else {
					LOGGER.error("roletitle,roledesc can not be null or empty "+"roletitle="+roletitle +"roledesc="+roledesc);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		else if (servletPath.equals("/addrole")) {
			try {
				String roleName = request.getParameter("roleName").trim();
				String description = request.getParameter("description").trim();
				
				System.out.println("roleName="+roleName+" description="+description);
				
				if(roleName!= null && roleName.length()>0 && description != null && description.length()>0){
					RoleBean ub = new RoleBean();
					ub.setTitle(roleName);
					ub.setDesc(description);
					
					new UserMangementDao().insertIntoRoleTable(ub);
					
					RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
					rd.forward(request, response);
				}
				
				
				else {
					LOGGER.error("roleName,description can not be null or empty "+"roleName="+roleName +"description="+description );
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if (servletPath.equals("/deleteuser")) {
			try {
				String userId = request.getParameter("deleteuserid").trim();
				
				UserBean ub=new UserBean();
				ub.setId(userId);
				
				new UserMangementDao().deleteUserDetails(ub);
				
				RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
				rd.forward(request, response);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if(servletPath.equals("/projectadminconfig")){
			try{
				String user=request.getParameter("uservalue");
				int project=Integer.parseInt(request.getParameter("projectvalue"));
				
				UserBean ub=new UserBean();
				ub.setId(user);
				
				ProjectDetailsBean pdb=new ProjectDetailsBean();
				pdb.setProjectId(project);
				
				new UserMangementDao().mapProjectAdmin(ub, pdb);
				
				RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				
			}
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String servletPath = request.getServletPath();
		 if(servletPath.equals("/edituser")) {
			    JSONArray array = new JSONArray();
				
				ResultSet resultset=null;
				Connection connection=null;
				Statement statement=null;
				try {
					connection = DBConnectionUtil.getJNDIConnection();
					statement = connection.createStatement();
					resultset = statement.executeQuery("SELECT * from users") ;
					
					if(resultset != null){
						
						 while(resultset.next()){
							 JSONObject object = new JSONObject();
							 object.put("first_name", resultset.getString("first_name"));
							 object.put("last_name", resultset.getString("last_name"));
							 object.put("user_name", resultset.getString("user_name"));
							 object.put("user_email", resultset.getString("user_email"));
							 object.put("id", resultset.getString("id"));
			                
							 if(resultset.getString("user_status").equals("1")){
								 object.put("user_status", "Active"); 
							 }else {
								 object.put("user_status", "Deactive");
							}
							 array.put(object);
			             }  
						 
						response.setContentType("application/json");
						response.getWriter().write(array.toString());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				finally{
					try {
						if(connection != null && !connection.isClosed() && statement != null && !statement.isClosed() && resultset != null && !resultset.isClosed()){
							resultset.close();
							statement.close();
							connection.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		 
		 else if(servletPath.equals("/usertable")){
				List<UserBean> users= new ArrayList<UserBean>();
				UserMangementDao umd=new UserMangementDao();
				users=umd.getUserDetails();
				
				JSONArray jsonArray = new JSONArray();
				try {
				for (int i = 0; i < users.size(); i++) {
					UserBean user=users.get(i);
					JSONObject json = new JSONObject();
						json.put("firstname", user.getFirstname());
						json.put("username", user.getUsername());
						json.put("email", user.getEmail());
						json.put("lastname", user.getLaastname());
						json.put("id", user.getId());
						jsonArray.put(json);
					}
						response.setContentType("application/json");
						response.getWriter().write(jsonArray.toString());	
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						LOGGER.error("Error in Building User Table", e);
					}
					
				}
		 
		 else if(servletPath.equals("/roletable")){
			 List<RoleBean> roles=new ArrayList<RoleBean>();
			 UserMangementDao umd=new UserMangementDao();
			 roles=umd.getRoleDetails();
			 JSONArray jsonArray = new JSONArray();
			 
			 try{
				 for(int i=0; i<roles.size(); i++){
					 RoleBean role=roles.get(i);
					 JSONObject json = new JSONObject();
					 json.put("roleid", role.getId());
					 json.put("rtitle", role.getTitle());
					 json.put("rdesc", role.getDesc());
					 jsonArray.put(json);
				 }
				 response.setContentType("application/json");
					response.getWriter().write(jsonArray.toString());	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					LOGGER.error("Error in Building Role Table", e);
				}
			 
		 }
		 
		 else if(servletPath.equals("/projectstableforpa")){
			 HttpSession session = request.getSession(false);
			 LoginBean lb = (LoginBean) session.getAttribute("username");
			 
			 List<ProjectDetailsBean> projects=new ArrayList<ProjectDetailsBean>();
			 UserMangementDao umd=new UserMangementDao();
			 JSONArray jsonArray = new JSONArray();
			 try {
				projects=umd.getProjects(lb);
				
				for(int i=0; i<projects.size(); i++){
					ProjectDetailsBean project=projects.get(i);
					JSONObject json = new JSONObject();
					json.put("projid", project.getProjectId());
					json.put("projname", project.getProjectName());
					jsonArray.put(json);
				}
				 response.setContentType("application/json");
					response.getWriter().write(jsonArray.toString());
			} catch (SQLException | JSONException e) {
				// TODO Auto-generated catch block
				LOGGER.error("Error in Building Projects Table", e);
			}
		 }
		 
	}

}
