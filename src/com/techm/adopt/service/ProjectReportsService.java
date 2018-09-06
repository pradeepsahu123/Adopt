package com.techm.adopt.service;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techm.adopt.bean.ProjectDetailsBean;

public interface ProjectReportsService {
	public JSONArray getSprintDetails(String projectname) throws JSONException,
			UnsupportedEncodingException, SQLException;

	public JSONArray getTestTrend(String projectname) throws JSONException,
			UnsupportedEncodingException, SQLException;

	public List<ProjectDetailsBean> getProjectDetails() throws JSONException,
			UnsupportedEncodingException, SQLException;

	public ProjectDetailsBean getStoryDetails(String projectname)
			throws UnsupportedEncodingException, JSONException, SQLException;

	public String getOutputFromUrl(String url) throws SQLException;

	public JSONArray getSprintBurnDown(String projectname)
			throws SQLException, JSONException, ParseException,
			UnsupportedEncodingException;
	
	public JSONObject releaseBurndown(String releaseId)
			throws SQLException, JSONException, ParseException,
			UnsupportedEncodingException;
	public JSONObject getSprintsIssuesDetails(String projectname) throws JSONException,
			UnsupportedEncodingException, SQLException;

	public JSONArray getReleaseVersions(String projectname)throws SQLException, JSONException;

	public JSONObject  getReleasesIds(String projectName) throws SQLException,
			JSONException;

	public JSONObject getAllProjects() throws JSONException, SQLException;

	public JSONObject getAllDetailsofProject() throws SQLException, JSONException, UnsupportedEncodingException;

	public JSONObject getOpenIssuesofProject() throws SQLException, JSONException;

}
