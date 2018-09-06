package com.techm.adopt.bean;

public class DeploymentBean {

	private String buildDisplayName;
	private String deploymentStartDate;
	private String deploymentDuration;
	private String deployedEnvironment;
	private int deploymentStatus;
	private String dailyCount;
	private String devCount;
	private String qaCount;
	private String prodCount;

	public String getDevCount() {
		return devCount;
	}

	public void setDevCount(String devCount) {
		this.devCount = devCount;
	}

	public String getQaCount() {
		return qaCount;
	}

	public void setQaCount(String qaCount) {
		this.qaCount = qaCount;
	}

	public String getProdCount() {
		return prodCount;
	}

	public void setProdCount(String prodCount) {
		this.prodCount = prodCount;
	}

	public String getDailyCount() {
		return dailyCount;
	}

	public void setDailyCount(String dailyCount) {
		this.dailyCount = dailyCount;
	}

	private String taskName;
	private String taskParent;
	private String taskUrl;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskParent() {
		return taskParent;
	}

	public void setTaskParent(String taskParent) {
		this.taskParent = taskParent;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public String getBuildDisplayName() {
		return buildDisplayName;
	}

	public void setBuildDisplayName(String buildDisplayName) {
		this.buildDisplayName = buildDisplayName;
	}

	public String getDeploymentStartDate() {
		return deploymentStartDate;
	}

	public void setDeploymentStartDate(String deploymentStartDate) {
		this.deploymentStartDate = deploymentStartDate;
	}

	public String getDeploymentDuration() {
		return deploymentDuration;
	}

	public void setDeploymentDuration(String deploymentDuration) {
		this.deploymentDuration = deploymentDuration;
	}

	public String getDeployedEnvironment() {
		return deployedEnvironment;
	}

	public void setDeployedEnvironment(String deployedEnvironment) {
		this.deployedEnvironment = deployedEnvironment;
	}

	public int getDeploymentStatus() {
		return deploymentStatus;
	}

	public void setDeploymentStatus(int deploymentStatus) {
		this.deploymentStatus = deploymentStatus;
	}
}
