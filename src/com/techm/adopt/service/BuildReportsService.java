package com.techm.adopt.service;

import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;

public interface BuildReportsService {
	public JSONArray getBuildDetails(String projectname) throws SQLException,
			JSONException;

	public JSONArray getDailyBuildDetails(String projectname)
			throws SQLException, JSONException;
}
