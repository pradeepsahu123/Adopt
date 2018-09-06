package com.techm.adopt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.RoleBean;
import com.techm.adopt.util.DBConnectionUtil;

public class UserDaoImpl implements UserDao {

	final static Logger LOGGER = Logger.getLogger(UserDaoImpl.class);

	@Override
	public boolean isValidUser(LoginBean lb) {
		Connection connection = null;
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			//String query = "SELECT user_name, password, display_name,user_role_id, last_login FROM portal_users WHERE user_name=? AND password=? AND user_status = 1";
			String query ="Select user_name, password, first_name,id from users where user_name=? and password=? and user_status=1";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, lb.getUsername());
			ps.setString(2, lb.getPassword());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("user_name").equals(lb.getUsername())
						&& rs.getString("password").equals(lb.getPassword())) {
					lb.setUserDisplayName(rs.getString("first_name"));
					lb.setUserId(rs.getString("id"));
					//lb.setUserDisplayName(rs.getString("display_name"));
					//lb.setRoleId(rs.getInt("user_role_id"));
					return true;
				}
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("Error in validating user", e);
		} finally {
			DBConnectionUtil.closeConnection(connection);
		}
		return false;
	}

	@Override
	public List<LoginBean> getTabsList(LoginBean lb, String projectname) {
		Connection connection = null;
		List<LoginBean> list = new ArrayList<LoginBean>();
		LoginBean loginbeandefault = new LoginBean();
		loginbeandefault.setTabDisplayName("Project Home");
		loginbeandefault.setTabUrl("/Adopt/projecthome");
		loginbeandefault.setTabOrder(1);
		loginbeandefault.setTabName("projecthome");
		list.add(loginbeandefault);

		try {
			connection = DBConnectionUtil.getJNDIConnection();
			//String query = "select fm.feature_name, fm.feature_display_name, fm.feature_order, fm.feature_url FROM role_feature rf INNER JOIN feature_master fm ON rf.feature_id = fm.feature_id INNER JOIN role_master rm ON rf.role_id = rm.role_id WHERE role_feature_status = 1 AND fm.feature_status=1 AND rf.role_id=? AND fm.project_name=? order by feature_order";
			String query="select fm.feature_name, fm.feature_display_name, fm.feature_order, fm.feature_url FROM feature_role fr INNER JOIN feature_master fm ON fr.feature_id = fm.feature_id INNER JOIN role r ON fr.role_id = r.id WHERE r.id=? AND fm.project_name=? order by feature_order";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, lb.getRoleId());
			ps.setString(2, projectname);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				LoginBean loginBean = new LoginBean();
				loginBean.setTabDisplayName(rs
						.getString("feature_display_name"));
				loginBean.setTabUrl(rs.getString("feature_url"));
				loginBean.setTabOrder(rs.getInt("feature_order"));
				loginBean.setTabName(rs.getString("feature_name"));
				list.add(loginBean);
				System.out.println(rs.getString("feature_url"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("Error while getting tabslist ", e);
		} finally {
			DBConnectionUtil.closeConnection(connection);
		}
		return list;
	}

	@Override
	public Map<String, String> getPlanningUser() {
		Connection connection = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "select pu.user_name, pu.password, fm.feature_url FROM role_feature rf INNER JOIN feature_master fm ON rf.feature_id = fm.feature_id INNER JOIN role_master rm ON rf.role_id = rm.role_id INNER join portal_users pu on pu.user_role_id=rm.role_id WHERE role_feature_status = 1 AND fm.feature_status=1 AND rf.role_id=1007 AND fm.feature_display_name='planning'";
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				map.put("username", rs.getString("user_name"));
				map.put("password", rs.getString("password"));
				map.put("url", rs.getString("feature_url"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("Error while fetching planning tool user", e);
		} finally {
			DBConnectionUtil.closeConnection(connection);
		}
		return map;
	}

	@Override
	public List<LoginBean> getPage(LoginBean lb, String projectname,
			String pagename) {
		Connection connection = null;
		List<LoginBean> list = new ArrayList<LoginBean>();
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			//String query = "select fm.feature_name, fm.feature_display_name, fm.feature_order, fm.feature_url FROM role_feature rf INNER JOIN feature_master fm ON rf.feature_id = fm.feature_id INNER JOIN role_master rm ON rf.role_id = rm.role_id WHERE role_feature_status = 1 AND fm.feature_status=1 AND rf.role_id=? AND fm.project_name=? AND fm.feature_name=? order by feature_order";
			String query = "select fm.feature_name, fm.feature_display_name, fm.feature_order, fm.feature_url FROM feature_role rf INNER JOIN feature_master fm ON rf.feature_id = fm.feature_id INNER JOIN role rm ON rf.role_id = rm.id WHERE fm.feature_status=1 AND rf.role_id=? AND fm.project_name=? AND fm.feature_name=? order by feature_order";
			PreparedStatement ps = connection.prepareStatement(query);
			//ps.setInt(1, 1007);
			ps.setString(1, lb.getRoleId());
			ps.setString(2, projectname);
			ps.setString(3, pagename);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				LoginBean loginBean = new LoginBean();
				loginBean.setTabDisplayName(rs
						.getString("feature_display_name"));
				loginBean.setTabUrl(rs.getString("feature_url"));
				loginBean.setTabOrder(rs.getInt("feature_order"));
				loginBean.setTabName(rs.getString("feature_name"));
				list.add(loginBean);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("Error in get page", e);
		} finally {
			DBConnectionUtil.closeConnection(connection);
		}
		return list;
	}

	@Override
	public List<RoleBean> getRolesForUser(LoginBean lb) {
		// TODO Auto-generated method stub
		Connection connection = null;
		List<RoleBean> list = new ArrayList<RoleBean>();
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query="select distinct role.id,role.title from users inner join role_users on users.id=role_users.user_id inner join role on role.id=role_users.role_id where users.id=?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, lb.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				RoleBean role=new RoleBean();
				role.setId(rs.getString(1));
				role.setTitle(rs.getString(2));
				list.add(role);
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e) {
			LOGGER.error("Error in getting roles for user", e);
		} finally {
			DBConnectionUtil.closeConnection(connection);
		}
		return list;
	}

}
