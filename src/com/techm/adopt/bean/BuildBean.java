package com.techm.adopt.bean;

public class BuildBean {
	private String startDate;
	private String passedCount;
	private String failedcount;
	private String averageCount;
	private int status;
	private String duration;
	private String buildDisplayName;

	public String getBuildDisplayName() {
		return buildDisplayName;
	}

	public void setBuildDisplayName(String buildDisplayName) {
		this.buildDisplayName = buildDisplayName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	private String buildUrl;

	public String getBuildUrl() {
		return buildUrl;
	}

	public void setBuildUrl(String buildUrl) {
		this.buildUrl = buildUrl;
	}

	public String getPassedCount() {
		return passedCount;
	}

	public void setPassedCount(String passedCount) {
		this.passedCount = passedCount;
	}

	public String getFailedcount() {
		return failedcount;
	}

	public void setFailedcount(String failedcount) {
		this.failedcount = failedcount;
	}

	public String getAverageCount() {
		return averageCount;
	}

	public void setAverageCount(String averageCount) {
		this.averageCount = averageCount;
	}

}
