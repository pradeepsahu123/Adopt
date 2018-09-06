package com.techm.adopt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.techm.adopt.bean.DeploymentBean;
import com.techm.adopt.util.DBConnectionUtil;

public class DeploymentReportsDaoImpl implements DeploymentReportsDao {
	final static Logger LOGGER = Logger
			.getLogger(DeploymentReportsDaoImpl.class);

	@Override
	public List<DeploymentBean> getDeploymentTable(String projectname) {
		Connection connection = null;
		List<DeploymentBean> list = new ArrayList<DeploymentBean>();
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "select d.build_display_name, d.deployment_status, d.deployment_start_date, d.deployment_duration, d.deployed_environment, t.task_name, t.task_parent_name, t.task_url from deployment_details d join build_details b on b.build_display_name=d.build_display_name join task_details t on t.build_id=b.build_details_id where d.project_name=? order by d.deployment_details_id desc limit 10";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, projectname);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DeploymentBean db = new DeploymentBean();
				db.setBuildDisplayName(rs.getString(1));
				db.setDeploymentStatus(rs.getInt(2));
				db.setDeploymentStartDate(rs.getString(3));
				db.setDeploymentDuration(rs.getString(4));
				db.setDeployedEnvironment(rs.getString(5));
				db.setTaskName(rs.getString(6));
				db.setTaskParent(rs.getString(7));
				db.setTaskUrl(rs.getString(8));
				list.add(db);
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
	public List<DeploymentBean> getDeploymentDailyDetails(String projectname)
			throws JSONException {
		Connection connection = null;
		List<DeploymentBean> list = new ArrayList<DeploymentBean>();
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "select count(*) as dailycount, date(deployment_start_date) as deployment_date from deployment_details where project_name=? group by deployment_date order by deployment_date desc;";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, projectname);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DeploymentBean db = new DeploymentBean();
				db.setDailyCount(rs.getString("dailycount"));
				db.setDeploymentStartDate(rs.getString("deployment_date"));
				list.add(db);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("Error in daily deployment details report dao", e);
		} finally {
			DBConnectionUtil.closeConnection(connection);
		}
		return list;
	}

	@Override
	public List<DeploymentBean> getDeploymentEnvDetails(String projectname)
			throws JSONException {
		Connection connection = null;
		List<DeploymentBean> list = new ArrayList<DeploymentBean>();
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "select sum(case when deployed_environment='DEV' then 1 else 0 end) as dev, sum(case when deployed_environment='QA' then 1 else 0 end) as qa, sum(case when deployed_environment='PROD' then 1 else 0 end) as prod, date(deployment_start_date) as deployment_date from deployment_details where project_name=? group by deployment_date order by deployment_date desc;";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, projectname);
			ResultSet rsenv = pstmt.executeQuery();
			while (rsenv.next()) {
				DeploymentBean db = new DeploymentBean();
				db.setDevCount(rsenv.getString("dev"));
				db.setQaCount(rsenv.getString("qa"));
				db.setProdCount(rsenv.getString("prod"));
				db.setDeploymentStartDate(rsenv.getString("deployment_date"));
				list.add(db);
			}
			rsenv.close();
			pstmt.close();
		} catch (SQLException e) {
			LOGGER.error("Error in deployment env report dao", e);
		} finally {
			DBConnectionUtil.closeConnection(connection);
		}
		return list;
	}
}
