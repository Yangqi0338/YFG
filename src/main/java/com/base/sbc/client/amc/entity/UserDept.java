/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.client.amc.entity;

import com.base.sbc.config.common.base.BaseDataEntity;

/**
 * 类描述：用户-部门 实体类
 *
 * @author youkehai
 * @version 1.0
 * @address com.base.sbc.company.entity.UserDept
 * @email 717407966@qq.com
 * @date 创建时间：2019-4-26 18:14:25
 */
public class UserDept extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    /**
     * 用户名称
     */
    private String name;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户账号
     */
    private String username;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 用户别名
     */
    private String aliasUserName;
    /**
     * 企业别名
     */
    private String aliasCompanyName;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 是否加入了团队(0 ,未加入)
     */
    private String isTeamId;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getIsTeamId() {
        return isTeamId;
    }

    public void setIsTeamId(String isTeamId) {
        this.isTeamId = isTeamId;
    }
    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /**
     * 员工档案ID
     */
    private String userCompanyId;
    /**
     * 员工档案ID
     */
    private String userId;
    /**
     * 部门ID
     */
    private String deptId;
    /**
     * 普通(0)'部门负责人(1)
     */
    private String userType;
    /**
     * 正常状态(0)
     */
    private String status;

    /*******************************************getset方法区************************************/
    public String getUserCompanyId() {
        return userCompanyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public void setUserCompanyId(String userCompanyId) {
        this.userCompanyId = userCompanyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
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
}

