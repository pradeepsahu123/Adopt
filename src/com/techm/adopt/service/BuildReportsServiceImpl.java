package com.techm.adopt.service;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techm.adopt.bean.BuildBean;
import com.techm.adopt.dao.BuildReportsDao;
import com.techm.adopt.dao.BuildReportsDaoImpl;

public class BuildReportsServiceImpl implements BuildReportsService {

	BuildReportsDao buildReportsDao;

	public BuildReportsServiceImpl() {
		this.buildReportsDao = new BuildReportsDaoImpl();
	}

	@Override
	public JSONArray getBuildDetails(String projectname) throws SQLException,
			JSONException {
		List<BuildBean> list = buildReportsDao.getBuildDetails(projectname);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			BuildBean buildBean = (BuildBean) list.get(i);
			JSONObject json = new JSONObject();
			json.put("builddisplayname", buildBean.getBuildDisplayName());
			if (buildBean.getStatus() == 1) {
				json.put("status", "SUCCESS");
			} else {
				json.put("status", "FAILURE");
			}
			json.put("startdate", buildBean.getStartDate());
			json.put("duration", buildBean.getDuration());
			json.put("url", buildBean.getBuildUrl());
			jsonArray.put(json);
		}
		return jsonArray;
	}

	@Override
	public JSONArray getDailyBuildDetails(String projectname)
			throws SQLException, JSONException {
		List<BuildBean> list = buildReportsDao
				.getDailyBuildDetails(projectname);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			BuildBean buildBean = (BuildBean) list.get(i);
			JSONObject json = new JSONObject();
			json.put("builddate", buildBean.getStartDate());
			json.put("passedcount", buildBean.getPassedCount());
			json.put("failedcount", buildBean.getFailedcount());
			jsonArray.put(json);
		}
		return jsonArray;
	}

}
