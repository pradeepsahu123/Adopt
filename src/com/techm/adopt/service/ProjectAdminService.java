package com.techm.adopt.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.techm.adopt.bean.LinkedApplicationBean;
import com.techm.adopt.bean.ProjectDetailsBean;
import com.techm.adopt.bean.RoleBean;
import com.techm.adopt.bean.UserBean;

public interface ProjectAdminService {

	public void insertApplication(LinkedApplicationBean userData,ProjectDetailsBean pdb,RoleBean rb) throws SQLException;
	public List<LinkedApplicationBean> getLinkedApps();
	public void mapUserRoles(UserBean user,RoleBean role,ProjectDetailsBean project) throws SQLException;
	public List<Map<String, String>> getUserRoles(List<ProjectDetailsBean> projects);
	public void updateApplication(LinkedApplicationBean lapp) throws SQLException;
	public List<LinkedApplicationBean> getLinkedAppsFromRoles(UserBean user,RoleBean role) throws SQLException;
	public List<LinkedApplicationBean> getLinkedAppsForProject(List<ProjectDetailsBean> projects);
}
