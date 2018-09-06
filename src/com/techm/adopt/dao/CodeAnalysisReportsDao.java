package com.techm.adopt.dao;

import java.sql.SQLException;
import java.util.List;

import com.techm.adopt.bean.CodeAnalysisBean;

public interface CodeAnalysisReportsDao {
	public List<CodeAnalysisBean> getCodeAnalysisDetails(String projectname)
			throws SQLException;

	public List<CodeAnalysisBean> getCodeAnalysisIssues(String projectname)
			throws SQLException;
}
