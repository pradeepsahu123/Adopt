package com.techm.adopt.service;

import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface CodeAnalysisReportsService {
	public JSONArray getCodeAnalysisDetails(String projectname)
			throws SQLException, JSONException;

	public JSONArray getCodeAnalysisIssues(String projectname)
			throws SQLException, JSONException;

	public JSONObject getCodeQualityProject()
			throws SQLException, JSONException;
}
