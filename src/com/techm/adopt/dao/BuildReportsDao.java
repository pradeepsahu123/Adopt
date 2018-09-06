package com.techm.adopt.dao;

import java.sql.SQLException;
import java.util.List;

import com.techm.adopt.bean.BuildBean;

public interface BuildReportsDao {
	public List<BuildBean> getBuildDetails(String projectname)
			throws SQLException;

	public List<BuildBean> getDailyBuildDetails(String projectname)
			throws SQLException;
}
