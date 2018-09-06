package com.techm.adopt.dao;

import java.util.List;
import java.util.Map;

import com.techm.adopt.bean.LoginBean;
import com.techm.adopt.bean.RoleBean;

public interface UserDao {
	public boolean isValidUser(LoginBean lb);

	public List<LoginBean> getTabsList(LoginBean lb, String projectname);

	public Map<String, String> getPlanningUser();

	public List<LoginBean> getPage(LoginBean lb, String projectname,
			String pagename);
	
	public List<RoleBean> getRolesForUser(LoginBean lb);
}
