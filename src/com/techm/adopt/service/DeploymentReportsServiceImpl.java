package com.techm.adopt.service;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techm.adopt.bean.DeploymentBean;
import com.techm.adopt.dao.DeploymentReportsDao;
import com.techm.adopt.dao.DeploymentReportsDaoImpl;

public class DeploymentReportsServiceImpl implements DeploymentReportsService {

	DeploymentReportsDao deploymentReportsDao;

	public DeploymentReportsServiceImpl() {
		this.deploymentReportsDao = new DeploymentReportsDaoImpl();
	}

	@Override
	public JSONArray getDeploymentTable(String projectname)
			throws SQLException, JSONException {
		List<DeploymentBean> list = deploymentReportsDao
				.getDeploymentTable(projectname);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			DeploymentBean db = (DeploymentBean) list.get(i);
			JSONObject json = new JSONObject();
			json.put("builddisplayname", db.getBuildDisplayName());
			json.put("deployedenvironment", db.getDeployedEnvironment());
			if (db.getDeploymentStatus() == 1) {
				json.put("deploymentstatus", "SUCCESS");
			} else {
				json.put("deploymentstatus", "FAILURE");
			}
			json.put("deploymentstartdate", db.getDeploymentStartDate());
			json.put("deploymentduration", db.getDeploymentDuration());
			json.put("taskname", db.getTaskName());
			json.put("taskparentname", db.getTaskParent());
			json.put("taskurl", db.getTaskUrl());
			jsonArray.put(json);
		}
		return jsonArray;
	}

	@Override
	public JSONArray getDeploymentDailyDetails(String projectname)
			throws SQLException, JSONException {
		List<DeploymentBean> list = deploymentReportsDao
				.getDeploymentDailyDetails(projectname);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			DeploymentBean db = (DeploymentBean) list.get(i);
			JSONObject json = new JSONObject();
			json.put("dailycount", db.getDailyCount());
			json.put("date", db.getDeploymentStartDate());
			jsonArray.put(json);
		}
		return jsonArray;
	}

	@Override
	public JSONArray getDeploymentEnvDetails(String projectname)
			throws SQLException, JSONException {
		List<DeploymentBean> list = deploymentReportsDao
				.getDeploymentEnvDetails(projectname);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			DeploymentBean db = (DeploymentBean) list.get(i);
			JSONObject json = new JSONObject();
			json.put("devcount", db.getDevCount());
			json.put("qacount", db.getQaCount());
			json.put("prodcount", db.getProdCount());
			json.put("date", db.getDeploymentStartDate());
			jsonArray.put(json);
		}
		return jsonArray;
	}

}
