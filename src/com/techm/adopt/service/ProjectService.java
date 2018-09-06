package com.techm.adopt.service;

import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techm.adopt.bean.LoginBean;

public interface ProjectService {
	public JSONArray getProjectList(LoginBean lb) throws JSONException;
	public JSONObject getReleaseList() throws SQLException, JSONException;
}
