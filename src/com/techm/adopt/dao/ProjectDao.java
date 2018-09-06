package com.techm.adopt.dao;

import java.util.List;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.ProjectDetailsBean;

public interface ProjectDao {
	public List<ProjectDetailsBean> getProjectList(LoginBean lb);
	public void insertAllProjects2DB(List list);
}
