package com.techm.adopt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.ProjectDetailsBean;
import com.techm.adopt.bean.RoleBean;
import com.techm.adopt.bean.UserBean;
import com.techm.adopt.util.DBConnectionUtil;

public class UserMangementDao {

private final  Logger LOGGER = Logger.getLogger(UserMangementDao.class);
	
	
	public void insertUserDetails( UserBean userData) throws SQLException {
		  Connection connection = null;
		  PreparedStatement pst= null;
		  ResultSet rs= null;
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "SELECT * from users";
			System.out.println(query);
			PreparedStatement ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if(connection != null && rs != null) {
			
			String query1 ="insert into users(id, first_name, last_name, user_email, user_name, password, user_status, date_created, created_by_id) select concat('user', max(convert(substring(id, 5), SIGNED INTEGER))+1), ?, ?, ?, ?, ?, 1, now(), 'user1001' from users";
				
				pst = connection.prepareStatement(query1); 
				pst.setString(1, userData.getFirstname());
				pst.setString(2, userData.getLaastname());
				pst.setString(3, userData.getEmail());
				pst.setString(4, userData.getUsername());
				pst.setString(5, userData.getPassword());
				pst.executeUpdate();
				
			}else {
				LOGGER.error("DB connection null");
			}
			
		} catch (Exception e) {
			LOGGER.error("Exception while user data insert", e);
		}finally{
			if(connection != null && !connection.isClosed() && pst != null && !pst.isClosed() && rs != null && !rs.isClosed()){
				pst.close();
				rs.close();
				connection.close();
			}
		}
	}
	
	public void updateUserDetails( UserBean userData) throws SQLException {
		  Connection connection = null;
		  PreparedStatement pst= null;
		  ResultSet rs= null;
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "SELECT * from users";
			System.out.println(query);
			PreparedStatement ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if(connection != null && rs != null) {
			
			String query1 ="UPDATE users set first_name=?, last_name=?, user_email=?, user_name=?, date_modified=now() where id=?";
				
				pst = connection.prepareStatement(query1); 
				pst.setString(1, userData.getFirstname());
				pst.setString(2, userData.getLaastname());
				pst.setString(3, userData.getEmail());
				pst.setString(4, userData.getUsername());
				pst.setString(5, userData.getId());
				pst.executeUpdate();
			}else {
				LOGGER.error("DB connection null");
			}
			
		} catch (Exception e) {
			LOGGER.error("Exception while user data updating", e);
		}finally{
			if(connection != null && !connection.isClosed() && pst != null && !pst.isClosed() && rs != null && !rs.isClosed()){
				pst.close();
				rs.close();
				connection.close();
			}
		}
	}
	
	public void deleteUserDetails(UserBean userData) throws SQLException{
		Connection connection = null;
		  PreparedStatement pst= null;
		  ResultSet rs= null;
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "SELECT * from users";
			System.out.println(query);
			PreparedStatement ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if(connection != null && rs != null) {
				String query2="delete from users where id=?";
				
				pst = connection.prepareStatement(query2);
				pst.setString(1, userData.getId());
				pst.executeUpdate();
				
			}
			else {
				LOGGER.error("DB connection null");
			}
		}
		catch (Exception e) {
			LOGGER.error("Exception while user data updating", e);
		}
		finally{
			if(connection != null && !connection.isClosed() && pst != null && !pst.isClosed() && rs != null && !rs.isClosed()){
				pst.close();
				rs.close();
				connection.close();
			}
		}
	}
	
	
	public void updateRoleDetails(RoleBean roleData) throws SQLException{
		Connection connection = null;
		  PreparedStatement pst= null;
		  ResultSet rs= null;
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "SELECT * from role";
			System.out.println(query);
			PreparedStatement ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if(connection != null && rs != null) {
				String query2="update role set title=?, description=?, date_modified=now() where id=?";
				
				pst = connection.prepareStatement(query2);
				pst.setString(1, roleData.getTitle());
				pst.setString(2, roleData.getDesc());
				pst.setString(3, roleData.getId());
				pst.executeUpdate();
			}
			else {
				LOGGER.error("DB connection null");
			}
		}
		catch (Exception e) {
			LOGGER.error("Exception while updating a role", e);
		}
		finally{
			if(connection != null && !connection.isClosed() && pst != null && !pst.isClosed() && rs != null && !rs.isClosed()){
				pst.close();
				rs.close();
				connection.close();
			}
		}
	}
	
	
	public void insertIntoRoleTable( RoleBean userData) throws SQLException {
		  Connection connection = null;
		  PreparedStatement pst= null;
		  ResultSet rs= null;
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "SELECT * from role";
			System.out.println(query);
			PreparedStatement ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if(connection != null && rs != null) {
			
			String query1 ="insert into role(id, description, title,status,date_created) select concat('role', max(convert(substring(id, 5), SIGNED INTEGER))+1), ?, ?,1,now() from role";
				
				pst = connection.prepareStatement(query1); 
				pst.setString(1, userData.getDesc());
				pst.setString(2, userData.getTitle());
				
				pst.executeUpdate();
			}else {
				LOGGER.error("DB connection null");
			}
			
		} catch (Exception e) {
			LOGGER.error("Exception while role configuration insert", e);
		}finally{
			if(connection != null && !connection.isClosed() && pst != null && !pst.isClosed() && rs != null && !rs.isClosed()){
				pst.close();
				rs.close();
				connection.close();
			}
		}
	}
	
	public List<UserBean> getUserDetails(){
		
		List<UserBean> users=new ArrayList<UserBean>();
		Connection con=null;
		try {
		con=DBConnectionUtil.getJNDIConnection();
			Statement st=con.createStatement();
			
			String sql="select first_name,last_name,user_name,user_email,id from users";
			ResultSet rs=st.executeQuery(sql);
			UserBean user=null;
			while(rs.next()){
				user=new UserBean();
				user.setFirstname(rs.getString(1));
				user.setLaastname(rs.getString(2));
				user.setUsername(rs.getString(3));
				user.setEmail(rs.getString(4));
				user.setId(rs.getString(5));
				
				users.add(user);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error in fetching user details from table dao", e);
		}
		
		finally{
			DBConnectionUtil.closeConnection(con);
		}
		
		return users;
		
	}
	
	public List<RoleBean> getRoleDetails(){
		
		List<RoleBean> roles=new ArrayList<RoleBean>();
		Connection con=null;
		try {
		con=DBConnectionUtil.getJNDIConnection();
			Statement st=con.createStatement();
			
			String sql="select id,title,description from role";
			ResultSet rs=st.executeQuery(sql);
			RoleBean role=null;
			
			while(rs.next()){
				role=new RoleBean();
				role.setId(rs.getString(1));
				role.setTitle(rs.getString(2));
				role.setDesc(rs.getString(3));
				
				roles.add(role);
			}
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error in fetching role details from table dao", e);
		}
		
		finally{
			DBConnectionUtil.closeConnection(con);
		}
		
		return roles;
		
	}
	
	public List<ProjectDetailsBean> getProjects(LoginBean lb) throws SQLException{
		Connection connection = null;
		PreparedStatement pst= null;
		ResultSet rs= null;
		List<ProjectDetailsBean> projects=null;
		
		try{
			connection = DBConnectionUtil.getJNDIConnection();
			String query="select pd.project_details_id,pd.project_id from project_details pd inner join role_users ru on ru.project_id=pd.project_details_id inner join users on users.id=ru.user_id where users.id=? and ru.role_id=?";
			pst = connection.prepareStatement(query);
			pst.setString(1, lb.getUserId());
			pst.setString(2, lb.getRoleId());
			
			rs=pst.executeQuery();
			projects =new ArrayList<ProjectDetailsBean>();
			while(rs.next()){
				ProjectDetailsBean project=new ProjectDetailsBean();
				project.setProjectId(rs.getInt(1));
				project.setProjectName(rs.getString(2));
				
				projects.add(project);
			}
		}
		catch(SQLException e){
			LOGGER.error("Error in fetching project details from table dao", e);
		}
		finally{
			if(connection != null && !connection.isClosed() && pst != null && !pst.isClosed() && rs != null && !rs.isClosed()){
				pst.close();
				rs.close();
				connection.close();
			}
		}
		  
		  return projects;
	}
	
	public void mapProjectAdmin(UserBean ub, ProjectDetailsBean pdb) throws SQLException{
		Connection connection = null;
		PreparedStatement pst= null;
		try{
			connection = DBConnectionUtil.getJNDIConnection();
			String query="insert into role_users(user_id,role_id,project_id) values(?,(select id from role where title=?),?)";
			pst = connection.prepareStatement(query);
			pst.setString(1, ub.getId());
			pst.setString(2, "Project Admin");
			pst.setInt(3, pdb.getProjectId());
			pst.executeUpdate();
		}
		catch (Exception e) {
			LOGGER.error("Exception while mapping project Admin", e);
		}finally{
			if(connection != null && !connection.isClosed() && pst != null && !pst.isClosed()){
				pst.close();
				connection.close();
			}
		}
	}
	
}
