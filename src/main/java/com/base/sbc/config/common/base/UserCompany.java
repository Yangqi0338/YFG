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
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
@Data
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
//	private String jobName;
	/** 拼音*/
//    private String shortPinyin;
    /**
     * 手机号码
     */
//    private String phone;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 部门ID
     */
//    private String deptId;

	private List<String> virtualDeptIds;

    private String httpLogId;
    private String mainThreadId;
//    private List<String> threadIdList;

//    private List<Job> jobList;
//    private List<CompanyPost> postList;

//    private List<Dept> deptList;

	/** 用户ID */
	private String userId;
	/** 微信公众号openId */
//	private String wxOpenId;
	/** 员工(0) 管理(1)  */
//	private String userType;
	/** 正常状态(0),待公司审核(1),离职(3) */
//	private String status;
	/** 用户别名 */
	private String aliasUserName;
	/** 企业别名 */
//	private String aliasCompanyName;
	/** 手机号码 */
//	private String userPhone;
	/** 用户工号 */
//	private String workId;
	/** 默认(0表示是默认企业)(1表示不是) */
//	private String isDefault;
	/** 用户头像 */
	private String aliasUserAvatar;
	/** 员工编号 */
	private String userCode;
	/*是否离职*/
	private String isDimission;


}

