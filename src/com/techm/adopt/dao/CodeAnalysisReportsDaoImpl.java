package com.techm.adopt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.techm.adopt.bean.CodeAnalysisBean;
import com.techm.adopt.controller.BuildReportsController;
import com.techm.adopt.util.DBConnectionUtil;

public class CodeAnalysisReportsDaoImpl implements CodeAnalysisReportsDao {
	final static Logger LOGGER = Logger.getLogger(BuildReportsController.class);

	@Override
	public List<CodeAnalysisBean> getCodeAnalysisDetails(String projectname) {
		Connection connection = null;
		List<CodeAnalysisBean> list = new ArrayList<CodeAnalysisBean>();
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "select * from code_analysis_details where project_name=? order by code_analysis_details_id desc limit 10";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, projectname);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				CodeAnalysisBean codeAnalysisBean = new CodeAnalysisBean();
				codeAnalysisBean.setBuildDisplayName(rs
						.getString("build_display_name"));
				codeAnalysisBean.setTechnicalDebt(rs.getString(9));
				codeAnalysisBean.setUnitTestcovergae(rs
						.getString("unit_test_coverage"));
				codeAnalysisBean.setComplexity(rs.getString("complexity"));
				codeAnalysisBean.setLoc(rs.getString("loc"));
				list.add(codeAnalysisBean);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("Error in code analysis details report dao", e);
		} finally {
			DBConnectionUtil.closeConnection(connection);
		}
		return list;
	}

	@Override
	public List<CodeAnalysisBean> getCodeAnalysisIssues(String projectname)
			throws SQLException {
		Connection connection = null;
		List<CodeAnalysisBean> list = new ArrayList<CodeAnalysisBean>();
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "select build_display_name, critical, blocker, major, minor, info from code_analysis_details where project_name=? order by code_analysis_details_id desc limit 10";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, projectname);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				CodeAnalysisBean codeAnalysisBean = new CodeAnalysisBean();
				codeAnalysisBean.setBuildDisplayName(rs
						.getString("build_display_name"));
				codeAnalysisBean.setCritical(rs.getString("critical"));
				codeAnalysisBean.setBlocker(rs.getString("blocker"));
				codeAnalysisBean.setMajor(rs.getString("major"));
				codeAnalysisBean.setMinor(rs.getString("minor"));
				codeAnalysisBean.setInfo(rs.getString("info"));
				list.add(codeAnalysisBean);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("Error in code analysis issues report dao", e);
		} finally {
			DBConnectionUtil.closeConnection(connection);
		}
		return list;
	}
}
