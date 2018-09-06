package com.techm.adopt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.techm.adopt.bean.BuildBean;
import com.techm.adopt.controller.BuildReportsController;
import com.techm.adopt.util.DBConnectionUtil;

public class BuildReportsDaoImpl implements BuildReportsDao {
	final static Logger LOGGER = Logger.getLogger(BuildReportsController.class);

	@Override
	public List<BuildBean> getBuildDetails(String projectname) {
		Connection connection = null;
		List<BuildBean> list = new ArrayList<BuildBean>();

		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "select * from build_details where project_name=? order by build_details_id desc limit 10";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, projectname);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				BuildBean buildBean = new BuildBean();
				buildBean.setBuildDisplayName(rs
						.getString("build_display_name"));
				buildBean.setStatus(rs.getInt("build_status"));
				buildBean.setStartDate(rs.getString("build_start_date"));
				buildBean.setDuration(rs.getString("build_duration"));
				buildBean.setBuildUrl(rs.getString("build_url"));
				list.add(buildBean);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("Error in build details report dao", e);
		} finally {
			DBConnectionUtil.closeConnection(connection);
		}
		return list;
	}

	@Override
	public List<BuildBean> getDailyBuildDetails(String projectname) {
		Connection connection = null;
		List<BuildBean> list = new ArrayList<BuildBean>();
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "select date(build_start_date) as build_date, sum(case when build_status=1 then 1 else 0 end) as passed_count, sum(case when build_status=0 then 1 else 0 end) as failed_count from build_details where project_name=? group by build_date order by build_details_id desc limit 10";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, projectname);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				BuildBean buildBean = new BuildBean();
				buildBean.setStartDate(rs.getString("build_date"));
				buildBean.setPassedCount(rs.getString("passed_count"));
				buildBean.setFailedcount(rs.getString("failed_count"));
				list.add(buildBean);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("Error in Daily build details report dao", e);
		} finally {
			DBConnectionUtil.closeConnection(connection);
		}
		return list;
	}
}
