package com.techm.adopt.dao;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;

import com.techm.adopt.bean.DeploymentBean;

public interface DeploymentReportsDao {
	public List<DeploymentBean> getDeploymentTable(String projectname)
			throws SQLException;

	public List<DeploymentBean> getDeploymentDailyDetails(String projectname)
			throws SQLException, JSONException;

	public List<DeploymentBean> getDeploymentEnvDetails(String projectname)
			throws SQLException, JSONException;
}
