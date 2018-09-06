package com.techm.adopt.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.techm.adopt.bean.LinkedApplicationBean;
import com.techm.adopt.bean.ProjectDetailsBean;
import com.techm.adopt.bean.RoleBean;
import com.techm.adopt.bean.UserBean;
import com.techm.adopt.dao.ProjectAdminDao;
import com.techm.adopt.dao.ProjectAdminDaoImpl;

public class ProjectAdminServiceImpl implements ProjectAdminService {

	@Override
	public void insertApplication(LinkedApplicationBean userData,ProjectDetailsBean pdb,RoleBean rb) throws SQLException {
		// TODO Auto-generated method stub

		ProjectAdminDao pad=new ProjectAdminDaoImpl();
		pad.insertApplication(userData,pdb,rb);
	}

	@Override
	public List<LinkedApplicationBean> getLinkedApps() {
		// TODO Auto-generated method stub
		List<LinkedApplicationBean> lapps=new ArrayList<LinkedApplicationBean>();
		ProjectAdminDao pad=new ProjectAdminDaoImpl();
		lapps=pad.getLinkedApps();
		
		return lapps;
	}

	@Override
	public void mapUserRoles(UserBean user, RoleBean role, ProjectDetailsBean project)
			throws SQLException {
		// TODO Auto-generated method stub
		ProjectAdminDao pad=new ProjectAdminDaoImpl();
		pad.mapUserRoles(user, role, project);
		
	}

	@Override
	public List<Map<String, String>> getUserRoles(List<ProjectDetailsBean> projects) {
		// TODO Auto-generated method stub
		List<Map<String, String>> roleusers=new ArrayList<Map<String,String>>();
		
		ProjectAdminDao pad=new ProjectAdminDaoImpl();
		roleusers=pad.getUserRoles(projects);
		
		return roleusers;		
	}

	@Override
	public void updateApplication(LinkedApplicationBean lapp)
			throws SQLException {
		// TODO Auto-generated method stub
		
		ProjectAdminDao pad=new ProjectAdminDaoImpl();
		pad.updateApplication(lapp);
		
	}

	@Override
	public List<LinkedApplicationBean> getLinkedAppsFromRoles(UserBean user,
			RoleBean role) throws SQLException {
		// TODO Auto-generated method stub
		List<LinkedApplicationBean> lapps=new ArrayList<LinkedApplicationBean>();
		ProjectAdminDao pad=new ProjectAdminDaoImpl();
		lapps=pad.getLinkedAppsFromRoles(user, role);
		
		return lapps;
	}

	@Override
	public List<LinkedApplicationBean> getLinkedAppsForProject(
			List<ProjectDetailsBean> projects) {
		// TODO Auto-generated method stub
		List<LinkedApplicationBean> lapps=new ArrayList<LinkedApplicationBean>();
		ProjectAdminDao pad=new ProjectAdminDaoImpl();
		lapps=pad.getLinkedAppsForProject(projects);
		
		return lapps;
	}

}
