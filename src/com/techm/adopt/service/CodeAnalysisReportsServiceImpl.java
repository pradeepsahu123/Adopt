package com.techm.adopt.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techm.adopt.bean.CodeAnalysisBean;
import com.techm.adopt.dao.CodeAnalysisReportsDao;
import com.techm.adopt.dao.CodeAnalysisReportsDaoImpl;
import com.techm.adopt.util.Constants;

public class CodeAnalysisReportsServiceImpl implements
		CodeAnalysisReportsService {

	CodeAnalysisReportsDao codeAnalysisReportsDao;

	public CodeAnalysisReportsServiceImpl() {
		this.codeAnalysisReportsDao = new CodeAnalysisReportsDaoImpl();
	}

	@Override
	public JSONArray getCodeAnalysisDetails(String projectname)
			throws SQLException, JSONException {
		List<CodeAnalysisBean> list = codeAnalysisReportsDao
				.getCodeAnalysisDetails(projectname);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			CodeAnalysisBean codeAnalysisBean = (CodeAnalysisBean) list.get(i);
			JSONObject json = new JSONObject();
			json.put("builddisplayname", codeAnalysisBean.getBuildDisplayName());
			json.put("loc", codeAnalysisBean.getLoc());
			json.put("technicaldebt", codeAnalysisBean.getTechnicalDebt());
			json.put("complexity", codeAnalysisBean.getComplexity());
			json.put("unit_test_coverage",
					codeAnalysisBean.getUnitTestcovergae());
			jsonArray.put(json);
		}
		return jsonArray;
	}

	@Override
	public JSONArray getCodeAnalysisIssues(String projectname)
			throws SQLException, JSONException {
		List<CodeAnalysisBean> list = codeAnalysisReportsDao
				.getCodeAnalysisIssues(projectname);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			CodeAnalysisBean codeAnalysisBean = (CodeAnalysisBean) list.get(i);
			JSONObject json = new JSONObject();
			json.put("buildnumber", codeAnalysisBean.getBuildDisplayName());
			json.put("blocker", codeAnalysisBean.getBlocker());
			json.put("critical", codeAnalysisBean.getCritical());
			json.put("major", codeAnalysisBean.getMajor());
			json.put("minor", codeAnalysisBean.getMinor());
			json.put("info", codeAnalysisBean.getInfo());
			jsonArray.put(json);
		}
		return jsonArray;
	}

	
	
	public JSONObject getCodeQualityPro(String projectname)
			throws SQLException, JSONException {
		//String projectname = "LISA BANK";
		String projectStatus = null;
		int value = 0;
		JSONObject result = new JSONObject();
		List<CodeAnalysisBean> list = codeAnalysisReportsDao
				.getCodeAnalysisIssues(projectname);
		CodeAnalysisBean bean = list.get(0);
		String criticalIssues = bean.getCritical();
		String blockerlIssues = bean.getBlocker();
		String majorIssues = bean.getMajor();
		float cri = Integer.parseInt(criticalIssues);
		float val1 = 0;
		float block = Integer.parseInt(blockerlIssues);
		float val2 = 0;
		float major = Integer.parseInt(majorIssues);
		float val3 = 0;
		if(cri>0){
			val1 = (50/cri)*10;
		}else{val1=50;}
		if(block>0){
			val2 = (25/block)*10;
		}else{val2=25;}
		if(major>0){
			val3 = (25/major)*10;
		}
		value = (int) (val1+val2+val3);
		if(value>Constants.GREEN_VALUE){
			projectStatus = "green";
		}else if(value>Constants.AMBER_VALUE){
			projectStatus = "amber";
		}else if(value>Constants.RED_VALUE){
			projectStatus = "red";
		}
		result.put("projectStatus", projectStatus);
		result.put("completion%", value);
		return result;
	}

	@Override
	public JSONObject getCodeQualityProject() throws SQLException, JSONException {
		ProjectReportsServiceImpl report = new ProjectReportsServiceImpl();
		List list= report.getProjectName();
		JSONObject result = null;
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		for(int i=0;i<list.size();i++){
			String projName = list.get(0).toString();
			JSONObject json = getCodeQualityPro(projName);
			map.put(projName, json);
		}
		result = new JSONObject(map);
		return result;
	}

}
