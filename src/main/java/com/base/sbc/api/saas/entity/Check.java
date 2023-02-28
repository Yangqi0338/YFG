package com.base.sbc.api.saas.entity;

/**
 * @author
 */
public class Check {
	/***
	 * 检查数
	 */
	private int checkNum;
	 /**检查类型*/
	private String type;
	/**不良数*/
	private int badNum;
	/**部门名称*/
	private String deptName;
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public int getCheckNum() {
		return checkNum;
	}
	public void setCheckNum(int checkNum) {
		this.checkNum = checkNum;
	}
	public int getBadNum() {
		return badNum;
	}
	public void setBadNum(int badNum) {
		this.badNum = badNum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
