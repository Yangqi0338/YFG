/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.config.common.base;

import com.base.sbc.client.amc.entity.CompanyPost;
import com.base.sbc.client.amc.entity.Dept;
import com.base.sbc.client.amc.entity.Job;
import com.base.sbc.module.httplog.entity.HttpLog;

import java.util.List;

/**
 * 类描述：用户-企业 实体类
 *
 * @author youkehai
 * @version 1.0
 * @address com.base.sbc.company.entity.UserCompany
 * @email 717407966@qq.com
 * @date 创建时间：2020-12-28 10:59:05
 */
public class UserCompany extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
	/** 用户名称      */
	private String name;
	/** 用户账号      */
	private String username;
	/** 企业名称      */
	private String companyName;
	private String deptName;
	private String jobName;
	/** 拼音*/
    private String shortPinyin;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 部门ID
     */
    private String deptId;

	private String virtualDeptIds;

    private HttpLog httpLog;

    private List<Job> jobList;
    private List<CompanyPost> postList;

    private List<Dept> deptList;

    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    public List<CompanyPost> getPostList() {
        return postList;
    }

    public void setPostList(List<CompanyPost> postList) {
        this.postList = postList;
    }

    public List<Dept> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<Dept> deptList) {
        this.deptList = deptList;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getJobName() {
        return jobName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getShortPinyin() {
		return shortPinyin;
	}

	public void setShortPinyin(String shortPinyin) {
		this.shortPinyin = shortPinyin;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
	/** 用户ID */
	private String userId;
	/** 微信公众号openId */
	private String wxOpenId;
	/** 员工(0) 管理(1)  */
	private String userType;
	/** 正常状态(0),待公司审核(1),离职(3) */
	private String status;
	/** 用户别名 */
	private String aliasUserName;
	/** 企业别名 */
	private String aliasCompanyName;
	/** 手机号码 */
	private String userPhone;
	/** 用户工号 */
	private String workId;
	/** 默认(0表示是默认企业)(1表示不是) */
	private String isDefault;
	/** 用户头像 */
	private String aliasUserAvatar;
	/** 员工编号 */
	private String userCode;
	/*是否离职*/
	private String isDimission;
    /*******************************************getset方法区************************************/
    public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
    public String getWxOpenId() {
		return wxOpenId;
	}
	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}
    public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
    public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    public String getAliasUserName() {
		return aliasUserName;
	}
	public void setAliasUserName(String aliasUserName) {
		this.aliasUserName = aliasUserName;
	}
    public String getAliasCompanyName() {
		return aliasCompanyName;
	}
	public void setAliasCompanyName(String aliasCompanyName) {
		this.aliasCompanyName = aliasCompanyName;
	}
    public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
    public String getWorkId() {
		return workId;
	}
	public void setWorkId(String workId) {
		this.workId = workId;
	}
    public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
    public String getAliasUserAvatar() {
		return aliasUserAvatar;
	}
	public void setAliasUserAvatar(String aliasUserAvatar) {
		this.aliasUserAvatar = aliasUserAvatar;
	}
    public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public HttpLog getHttpLog() {
		return httpLog;
	}

	public void setHttpLog(HttpLog httpLog) {
		this.httpLog = httpLog;
	}

	public String getIsDimission() {
		return isDimission;
	}

	public void setIsDimission(String isDimission) {
		this.isDimission = isDimission;
	}

	public String getVirtualDeptIds() {
		return virtualDeptIds;
	}

	public void setVirtualDeptIds(String virtualDeptIds) {
		this.virtualDeptIds = virtualDeptIds;
	}
}

