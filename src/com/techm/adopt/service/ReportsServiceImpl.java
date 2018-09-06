package com.techm.adopt.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.ReportBean;
import com.techm.adopt.dao.ReportsDao;
import com.techm.adopt.dao.ReportsDaoImpl;

public class ReportsServiceImpl implements ReportsService {
	private ReportsDao reportsDao;

	public ReportsServiceImpl() {
		this.reportsDao = new ReportsDaoImpl();
	}

	@Override
	public JSONArray getReportsList(LoginBean lb) throws JSONException {
		List<ReportBean> list = reportsDao.getReportsList(lb);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			ReportBean rb = (ReportBean) list.get(i);
			JSONObject json = new JSONObject();
			json.put("reportname", rb.getReportName());
			jsonArray.put(json);
		}
		return jsonArray;
	}

	@Override
	public List<ReportBean> getReportsPage(LoginBean lb) {
		return reportsDao.getReportsPage(lb);
	}

}
