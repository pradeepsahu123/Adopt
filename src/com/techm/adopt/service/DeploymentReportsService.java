package com.techm.adopt.service;

import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;

public interface DeploymentReportsService {
	public JSONArray getDeploymentTable(String projectname)
			throws SQLException, JSONException;

	public JSONArray getDeploymentDailyDetails(String projectname)
			throws SQLException, JSONException;

	public JSONArray getDeploymentEnvDetails(String projectname)
			throws SQLException, JSONException;
}
