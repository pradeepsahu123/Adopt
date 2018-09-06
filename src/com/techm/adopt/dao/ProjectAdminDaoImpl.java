package com.techm.adopt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.techm.adopt.bean.LinkedApplicationBean;
import com.techm.adopt.bean.ProjectDetailsBean;
import com.techm.adopt.bean.RoleBean;
import com.techm.adopt.bean.UserBean;
import com.techm.adopt.util.DBConnectionUtil;

public class ProjectAdminDaoImpl implements ProjectAdminDao {
	private final  Logger LOGGER = Logger.getLogger(ProjectAdminDaoImpl .class);

	@Override
	public void insertApplication(LinkedApplicationBean userData,ProjectDetailsBean pdb,RoleBean rb) throws SQLException {
		// TODO Auto-generated method stub
		Connection connection = null;
		  PreparedStatement pst= null;
		  ResultSet rs= null;
		  int feature_id=0;
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			if(connection != null) {
			String query1 ="insert into feature_master(feature_url,feature_display_name,feature_name,feature_status,create_at,project_name) values(?,?,?,1,now(),(select project_id from project_details where project_details_id=?))";				
				pst = connection.prepareStatement(query1); 
				pst.setString(1, userData.getUrl());
				pst.setString(2, userData.getTabName());
				pst.setString(3, userData.getDescription());
				pst.setInt(4, pdb.getProjectId());
				pst.executeUpdate();
				pst.close();
				
			String query2="select feature_id from feature_master where feature_url=? and feature_display_name=? and feature_name=? and project_name=(select project_id from project_details where project_details_id=?)";
				pst = connection.prepareStatement(query2); 
				pst.setString(1, userData.getUrl());
				pst.setString(2, userData.getTabName());
				pst.setString(3, userData.getDescription());
				pst.setInt(4, pdb.getProjectId());
				rs=pst.executeQuery();
				while(rs.next()){
					feature_id=rs.getInt(1);
				}
				pst.close();
				
			String query3="insert into feature_role(role_id,feature_id) values(?,?)";
				pst= connection.prepareStatement(query3);
				pst.setString(1,rb.getId());
				pst.setInt(2,feature_id);
				pst.executeUpdate();
				
			}else {
				LOGGER.error("DB connection null");
			}
			
		} catch (Exception e) {
			LOGGER.error("Exception while feature master insert", e);
		}finally{
			if(connection != null && !connection.isClosed() && pst != null && !pst.isClosed() && rs != null && !rs.isClosed()){
				pst.close();
				rs.close();
				connection.close();
			}
		}

	}

	@Override
	public List<LinkedApplicationBean> getLinkedApps() {
		// TODO Auto-generated method stub
		
		List<LinkedApplicationBean> lapps=new ArrayList<LinkedApplicationBean>();
		Connection con=null;
		try {
			con=DBConnectionUtil.getJNDIConnection();
			Statement st=con.createStatement();
			
			String sql="select feature_id,feature_display_name,feature_name,feature_url from feature_master";
			ResultSet rs=st.executeQuery(sql);
			LinkedApplicationBean lapp=null;
			
			while(rs.next()){
				lapp=new LinkedApplicationBean();
				lapp.setId(rs.getInt(1));
				lapp.setTabName(rs.getString(2));
				lapp.setDescription(rs.getString(3));
				lapp.setUrl(rs.getString(4));
				
				lapps.add(lapp);
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error in fetching feature details from table dao", e);
		}
		
		finally{
			DBConnectionUtil.closeConnection(con);
		}
		
		
		return lapps;
	}

	@Override
	public void mapUserRoles(UserBean user, RoleBean role, ProjectDetailsBean project) throws SQLException {
		// TODO Auto-generated method stub
		Connection connection = null;
		  PreparedStatement pst= null;
		  ResultSet rs= null;
		  try {
			  connection = DBConnectionUtil.getJNDIConnection();
				if(connection != null) {
					String sql="insert into role_users (user_id,role_id,project_id) values(?,?,?)";
					pst = connection.prepareStatement(sql); 
					pst.setString(1, user.getId());
					pst.setString(2, role.getId());
					pst.setInt(3, project.getProjectId());
					pst.executeUpdate();
				}
				else {
					LOGGER.error("DB connection null");
				}
		  }
		  catch (Exception e) {
				LOGGER.error("Exception while map user role", e);
			}
		  finally{
				if(connection != null && !connection.isClosed() && pst != null && !pst.isClosed() && rs != null && !rs.isClosed()){
					pst.close();
					rs.close();
					connection.close();
				}
		  }
	}

	@Override
	public List<Map<String, String>> getUserRoles(List<ProjectDetailsBean> projects) {
		// TODO Auto-generated method stub
		Connection con=null;
		List<Map<String, String>> roleusers=null;
		try {
			con=DBConnectionUtil.getJNDIConnection();
			Statement st=con.createStatement();
			roleusers=new ArrayList<Map<String,String>>();
			for(int i=0; i<projects.size(); i++){
				String sql="select first_name,user_email,user_name,GROUP_CONCAT(DISTINCT title SEPARATOR ',') titles,project from roleuser_map_view where project='"+projects.get(i).getProjectName()+"' group by first_name,user_email,user_name,project";
				ResultSet rs=st.executeQuery(sql);

				Map<String,String> roleuser=null;
				while(rs.next()){
					roleuser=new HashMap<String, String>();
					roleuser.put("first_name", rs.getString(1));
					roleuser.put("user_email", rs.getString(2));
					roleuser.put("user_name", rs.getString(3));
					roleuser.put("titles", rs.getString(4));
					roleuser.put("project", rs.getString(5));
				
					roleusers.add(roleuser);
				}
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error in fetching user-role details from table dao", e);
		}
		
		finally{
			DBConnectionUtil.closeConnection(con);
		}
		
		return roleusers;
	}

	@Override
	public void updateApplication(LinkedApplicationBean lapp) throws SQLException {
		// TODO Auto-generated method stub
		Connection connection = null;
		  PreparedStatement pst= null;
		  ResultSet rs= null;
		  try {
			  connection = DBConnectionUtil.getJNDIConnection();
				String query = "SELECT * from feature_master";
				System.out.println(query);
				PreparedStatement ps = connection.prepareStatement(query);
				rs = ps.executeQuery();
				if(connection != null && rs != null) {
					String sql="update feature_master set feature_display_name=?, feature_name=?, feature_url=?, modified_at=now(), project_name=? where feature_id=?";
					
					pst=connection.prepareStatement(sql);
					pst.setString(1, lapp.getTabName());
					pst.setString(2, lapp.getDescription());
					pst.setString(3, lapp.getUrl());
					pst.setString(4, lapp.getProject());
					pst.setInt(5, lapp.getId());
					pst.executeUpdate();					
				}
				else {
					LOGGER.error("DB connection null");
				}
		  }
		  catch (Exception e) {
				LOGGER.error("Exception while feature data updating", e);
			}finally{
				if(connection != null && !connection.isClosed() && pst != null && !pst.isClosed() && rs != null && !rs.isClosed()){
					pst.close();
					rs.close();
					connection.close();
				}
			}
		  
	}

	@Override
	public List<LinkedApplicationBean> getLinkedAppsFromRoles(UserBean user,
			RoleBean role) throws SQLException {
		// TODO Auto-generated method stub
		Connection connection=null;
		PreparedStatement pst= null;
		  ResultSet rs= null;
		  List<LinkedApplicationBean> lappsList=null;
		  LinkedApplicationBean lapp=null;
		try{
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "SELECT * from role_user";
			System.out.println(query);
			PreparedStatement ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if(connection != null && rs != null) {
				ResultSet rs2=null;
				String sql="select feature_ids from role_user where user_id=(select id from users where user_email=?) and role_id=(select id from role where title=?)";
				pst=connection.prepareStatement(sql);
				pst.setString(1, user.getEmail());
				pst.setString(2, role.getTitle());
				rs2=pst.executeQuery();
				
				while(rs2.next()){
					String[] splitted=rs2.getString(1).split(",");
					ResultSet rs1=null;
					lappsList= new ArrayList<LinkedApplicationBean>();
					for(int i=0; i<splitted.length; i++){
						String sql1="select feature_id,feature_display_name from feature_master where feature_id=?";
						pst=connection.prepareStatement(sql1);
						pst.setString(1, splitted[i]);
						rs1=pst.executeQuery();
						
						while(rs1.next()){
							lapp=new LinkedApplicationBean();
							lapp.setId(rs1.getInt(1));
							lapp.setTabName(rs1.getString(2));
							
							lappsList.add(lapp);
						}
					}
				}
			}
			else {
				LOGGER.error("DB connection null");
			}
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error in fetching user-role details from table dao", e);
		}
		
		finally{
			if(connection != null && !connection.isClosed() && pst != null && !pst.isClosed() && rs != null && !rs.isClosed()){
				pst.close();
				rs.close();
				connection.close();
			}
		}
		
		return lappsList;
	}

	@Override
	public List<LinkedApplicationBean> getLinkedAppsForProject(
			List<ProjectDetailsBean> projects) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pst= null;
		ResultSet rs= null;
		List<LinkedApplicationBean> lapps=null;
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			lapps=new ArrayList<LinkedApplicationBean>();
			for(int i=0; i<projects.size(); i++){
				String query="select feature_id,feature_display_name,feature_name,feature_url,project_name from feature_master where project_name=?";
				pst=connection.prepareStatement(query);
				pst.setString(1, projects.get(i).getProjectName());
				rs=pst.executeQuery();
				LinkedApplicationBean lapp=null;
				
				while(rs.next()){
					lapp=new LinkedApplicationBean();
					lapp.setId(rs.getInt(1));
					lapp.setTabName(rs.getString(2));
					lapp.setDescription(rs.getString(3));
					lapp.setUrl(rs.getString(4));
					lapp.setProject(rs.getString(5));
					
					lapps.add(lapp);
				}
				
				pst.close();
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error in fetching linked applications details for Project dao", e);
		}
		
		finally{
			DBConnectionUtil.closeConnection(connection);
		}
		
		return lapps;
	}

	
}
