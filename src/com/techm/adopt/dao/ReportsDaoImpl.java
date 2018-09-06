package com.techm.adopt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.ReportBean;
import com.techm.adopt.util.DBConnectionUtil;

public class ReportsDaoImpl implements ReportsDao{
	final static Logger LOGGER = Logger.getLogger(ReportsDaoImpl.class);
	
	@Override
	public List<ReportBean> getReportsList(LoginBean lb) {
		Connection connection = null;
		List<ReportBean> list = new ArrayList<ReportBean>();
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "select r.report_description from role_report rr join report_master r on r.report_id=rr.report_id where role_id=?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, 1007);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ReportBean reportBean = new ReportBean();
				reportBean.setReportName(rs
						.getString("report_description"));
				list.add(reportBean);
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
	public List<ReportBean> getReportsPage(LoginBean lb) {
		Connection connection = null;
		List<ReportBean> list = new ArrayList<ReportBean>();
		try {
			connection = DBConnectionUtil.getJNDIConnection();
			String query = "select * from report_page";
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ReportBean reportBean = new ReportBean();
				reportBean.setReportPageName(rs
						.getString("name"));
				reportBean.setPageid(rs
						.getString("report_page_name"));
				list.add(reportBean);
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

}
