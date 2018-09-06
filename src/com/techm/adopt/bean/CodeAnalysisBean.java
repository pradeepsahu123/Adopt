package com.techm.adopt.bean;

public class CodeAnalysisBean {
	private String buildDisplayName;
	private String technicalDebt;
	private String unitTestcovergae;
	private String complexity;
	private String critical;
	private String blocker;
	private String major;
	private String minor;
	private String info;
	private String loc;

	public String getBuildDisplayName() {
		return buildDisplayName;
	}

	public void setBuildDisplayName(String buildDisplayName) {
		this.buildDisplayName = buildDisplayName;
	}

	public String getTechnicalDebt() {
		return technicalDebt;
	}

	public void setTechnicalDebt(String technicalDebt) {
		this.technicalDebt = technicalDebt;
	}

	public String getUnitTestcovergae() {
		return unitTestcovergae;
	}

	public void setUnitTestcovergae(String unitTestcovergae) {
		this.unitTestcovergae = unitTestcovergae;
	}

	public String getComplexity() {
		return complexity;
	}

	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}

	public String getCritical() {
		return critical;
	}

	public void setCritical(String critical) {
		this.critical = critical;
	}

	public String getBlocker() {
		return blocker;
	}

	public void setBlocker(String blocker) {
		this.blocker = blocker;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getMinor() {
		return minor;
	}

	public void setMinor(String minor) {
		this.minor = minor;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

}
