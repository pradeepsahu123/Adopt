package com.techm.adopt.bean;

public class ProjectDetailsBean {
	private int projectId;
	private String projectName;
	private String projectStartDate;
	private String projectEndDate;
	private String projectStoryCount;
	private String closedEstimate;
	private String totalEstimate;
	private String sprintName;
	private String sprintStartDate;
	private String sprintEndDate;
	private String sprintStroyClosed;
	private String sprintStroyTotal;
	
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public String getSprintStartDate() {
		return sprintStartDate;
	}

	public void setSprintStartDate(String sprintStartDate) {
		this.sprintStartDate = sprintStartDate;
	}

	public String getSprintEndDate() {
		return sprintEndDate;
	}

	public void setSprintEndDate(String sprintEndDate) {
		this.sprintEndDate = sprintEndDate;
	}

	public String getSprintStroyClosed() {
		return sprintStroyClosed;
	}

	public void setSprintStroyClosed(String sprintStroyClosed) {
		this.sprintStroyClosed = sprintStroyClosed;
	}

	public String getSprintStroyTotal() {
		return sprintStroyTotal;
	}

	public void setSprintStroyTotal(String sprintStroyTotal) {
		this.sprintStroyTotal = sprintStroyTotal;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectStartDate() {
		return projectStartDate;
	}

	public void setProjectStartDate(String projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public String getProjectEndDate() {
		return projectEndDate;
	}

	public void setProjectEndDate(String projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public String getProjectStoryCount() {
		return projectStoryCount;
	}

	public void setProjectStoryCount(String projectStoryCount) {
		this.projectStoryCount = projectStoryCount;
	}

	public String getClosedEstimate() {
		return closedEstimate;
	}

	public void setClosedEstimate(String closedEstimate) {
		this.closedEstimate = closedEstimate;
	}

	public String getTotalEstimate() {
		return totalEstimate;
	}

	public void setTotalEstimate(String totalEstimate) {
		this.totalEstimate = totalEstimate;
	}
}
