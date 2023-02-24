/******************************************************************************
 * Copyright (C) 2018 celizi.com 
 * All Rights Reserved.
 * 本软件为网址：celizi.com 开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.config.common.base;

import java.util.Date;

import com.base.sbc.config.aspect.GetCurUserInfoAspect;
import org.hibernate.validator.constraints.Length;

import com.base.sbc.config.common.IdGen;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 类描述：
 *
 * @author shenzhixiong
 * @version 1.0
 * @address com.celizi.base.common.base.DataEntity
 * @email 731139982@qq.com
 * @date 创建时间：2017年4月8日 上午9:27:06
 */
public abstract class BaseDataEntity<T> extends BaseEntity {
    /**
     *
     */
    private static final long serialVersionUID = 7022181519896948997L;
    /** 更新者  */
    protected String updateName;
    /**  更新者 */
    protected String updateId;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createDate;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateDate;
    /**  创建者 */
    protected String createName;
    /**  备注 */
    @Length(min = 0, max = 500)
    protected String remarks;
    /**  删除标记（0：正常；1：删除；） */
    @Length(min = 1, max = 1)
    protected String delFlag;
    /** 创建者 */
    protected String createId;

    public BaseDataEntity() {
        super();
    }

    public BaseDataEntity(String id) {
        super(id);
    }


    public String getCreateName() {
        return createName;
    }

    public BaseDataEntity setCreateName(String createName) {
        this.createName = createName;
        return this;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public BaseDataEntity setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }


    public String getCreateId() {
        return createId;
    }

    public BaseDataEntity setCreateId(String createId) {
        this.createId = createId;
        return this;
    }

    public String getUpdateId() {
        return updateId;
    }

    public BaseDataEntity setUpdateId(String updateId) {
        this.updateId = updateId;
        return this;
    }

    public String getUpdateName() {
        return updateName;
    }

    public BaseDataEntity setUpdateName(String updateName) {
        this.updateName = updateName;
        return this;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public BaseDataEntity setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
        return this;
    }


    public String getRemarks() {
        return remarks;
    }

    public BaseDataEntity setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }


    public String getDelFlag() {
        return delFlag;
    }

    public BaseDataEntity setDelFlag(String delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    /**
     * 新增 实体前手动调用
     * 设置ID为UUID
     */
    @Override
    public void preInsert() {
        preInsert(IdGen.getId().toString());
    }


    /**
     * 新增  主键为传入主键
     *
     * @param id 主键ID
     */
    public void preInsert(String id) {
        //设置主键
        setId(id);
        insertInit(GetCurUserInfoAspect.companyUserInfo.get());
    }

    /**
     * 设置修改时间
     */
    @Override
    public void preUpdate() {
        updateInit();
    }

    /**
     * 设置修改时间,修改人,拿当前线程中的用户信息
     */
    public void updateInit() {
        updateInit(GetCurUserInfoAspect.companyUserInfo.get());
    }

    /**
     * 设置修改时间,修改人创建人等
     */
    public void insertInit() {
        this.insertInit(GetCurUserInfoAspect.companyUserInfo.get());
    }

    /**
     * 设置修改时间,修改人
     */
    public void updateInit(UserCompany userCompany) {
        this.updateDate=new Date();
        this.updateId = userCompany.getUserId();
        this.updateName = userCompany.getAliasUserName();
    }

    /**
     * 设置修改时间,修改人创建人等
     */
    public void insertInit(UserCompany userCompany) {
        this.updateInit(userCompany);
        this.createDate = new Date();
        this.createId = userCompany.getUserId();
        this.createName = userCompany.getAliasUserName();
    }

}
