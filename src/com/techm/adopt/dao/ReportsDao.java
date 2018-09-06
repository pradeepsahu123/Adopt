package com.techm.adopt.dao;

import java.util.List;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.ReportBean;

public interface ReportsDao {
	public List<ReportBean> getReportsList(LoginBean lb);

	public List<ReportBean> getReportsPage(LoginBean lb);
}
