package com.techm.adopt.service;

import java.sql.SQLException;
import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;
import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.ProjectDetailsBean;
import com.techm.adopt.dao.ProjectDao;
import com.techm.adopt.dao.ProjectDaoImpl;
import com.techm.adopt.util.Constants;

public class ProjectServiceImpl implements ProjectService {

	@Override
	public JSONArray getProjectList(LoginBean lb) throws JSONException {
		ProjectDao projectDao = new ProjectDaoImpl();
		List<ProjectDetailsBean> list = projectDao.getProjectList(lb);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			ProjectDetailsBean pdb = (ProjectDetailsBean) list.get(i);
			JSONObject json = new JSONObject();
			json.put("projectname", pdb.getProjectName());
			jsonArray.put(json);
		}
		return jsonArray;
	}
	
	public String getOutputFromUrl(String url) throws SQLException {
		String username = "shilpa";
		//String password = "12345";
		String password = "shilpa";
		Client client = Client.create();
		String auth = new String(Base64.encode(username + ":" + password));
		String output = null;
		try {
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource
					.header("Authorization", "Basic " + auth)
					.type("application/json").accept("application/json")
					.get(ClientResponse.class);
			int statusCode = response.getStatus();
			if (statusCode == 401) {
				throw new AuthenticationException(
						"Invalid Username or Password");
			}
			output = response.getEntity(String.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	
	public JSONObject getReleaseList() throws SQLException, JSONException {
		//String url = "http://172.19.82.13:8050/rest/agile/1.0/board/2/version";
		String url=Constants.VERSION_LIST;
		String output = getOutputFromUrl(url);
		JSONObject json = new JSONObject(output);
		JSONArray array = json.getJSONArray("values");
		JSONObject releaseIdNameJson = new JSONObject();
		for(int i = 0; i < array.length(); i++) {
			JSONObject jsonRelease = array.getJSONObject(i);
			releaseIdNameJson.put(jsonRelease.getString("name"), jsonRelease.get("id").toString());
		}
		return releaseIdNameJson;
	}
}
