package com.techm.adopt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.ProjectDetailsBean;
import com.techm.adopt.util.DBConnectionUtil;

public class ProjectDaoImpl implements ProjectDao {
	final static Logger LOGGER = Logger.getLogger(ProjectDaoImpl.class);

	@Override
	public List<ProjectDetailsBean> getProjectList(LoginBean lb) {
		Connection connection = null;
		List<ProjectDetailsBean> list = new ArrayList<ProjectDetailsBean>();
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			//String query = "select project_id from project_details";
			//String query = "select pd.project_details_id,pd.project_id from users u inner join project_users pu on u.id=pu.user_id inner join project_details pd on pd.project_details_id=pu.project_id where u.id=?";
			String query="select pd.project_details_id,pd.project_id from users u inner join role_users ru on u.id=ru.user_id inner join project_details pd on pd.project_details_id=ru.project_id inner join role on role.id=ru.role_id where u.id=? and role.id=?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, lb.getUserId());
			ps.setString(2, lb.getRoleId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ProjectDetailsBean pdb = new ProjectDetailsBean();
				pdb.setProjectName(rs.getString("project_id"));
				list.add(pdb);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("Error in Deployment summary report dao", e);
		} finally {
			DBConnectionUtil.closeConnection(connection);
		}
		return list;
	}

	@Override
	public void insertAllProjects2DB(List list) {
		Connection connection = null;
		connection = DBConnectionUtil.getJNDIConnection();
		PreparedStatement pst= null;
for(int i=0;i<list.size();i++){
			
			String query="insert into project_details(project_id) values(?)";
			System.out.println("enter into dao");
			try {
				pst = connection.prepareStatement(query);
				pst.setString(1, (String)list.get(i));
				pst.executeUpdate();
				System.out.println("sucess*************");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}

}
