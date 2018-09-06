package com.techm.adopt.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.ReportBean;

public interface ReportsService {
	public JSONArray getReportsList(LoginBean lb) throws JSONException;

	public List<ReportBean> getReportsPage(LoginBean lb);
}
