/******************************************************************************
 * Copyright (C) 2018 celizi.com
 * All Rights Reserved.
 * 本软件为网址：celizi.com 开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.config.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.base.sbc.config.common.IdGen;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * @author 卞康
 * @date 2023/3/31 19:56:38
 */
@Data
public abstract class BaseDataEntity<T> extends BaseEntity {

    @TableField(exist = false)
    private static final long serialVersionUID = 7022181519896948997L;

    /** 更新者名称  */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;

    /**  更新者id */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateId;

    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    /**  创建者名称 */
    @TableField(fill = FieldFill.INSERT)
    private String createName;

    /** 创建者id */
    @TableField(fill = FieldFill.INSERT)
    private String createId;

    /**  删除标记（0：正常；1：删除；） */
    @Length(min = 1, max = 1)
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    protected String delFlag;
    /**  备注 */
    //@Length(min = 0, max = 500)
    //protected String remarks;



    //public BaseDataEntity() {
    //    super();
    //}
    //
    //public BaseDataEntity(String id) {
    //    super(id);
    //}


    //public String getCreateName() {
    //    return createName;
    //}
    //
    //public BaseDataEntity setCreateName(String createName) {
    //    this.createName = createName;
    //    return this;
    //}


    //public Date getCreateDate() {
    //    return createDate;
    //}
    //
    //public BaseDataEntity setCreateDate(Date createDate) {
    //    this.createDate = createDate;
    //    return this;
    //}


    //public String getCreateId() {
    //    return createId;
    //}
    //
    //public BaseDataEntity setCreateId(String createId) {
    //    this.createId = createId;
    //    return this;
    //}

    //public String getUpdateId() {
    //    return updateId;
    //}

    //public BaseDataEntity setUpdateId(String updateId) {
    //    this.updateId = updateId;
    //    return this;
    //}

    //public String getUpdateName() {
    //    return updateName;
    //}

    //public BaseDataEntity setUpdateName(String updateName) {
    //    this.updateName = updateName;
    //    return this;
    //}

    //public Date getUpdateDate() {
    //    return updateDate;
    //}

    //public BaseDataEntity setUpdateDate(Date updateDate) {
    //    this.updateDate = updateDate;
    //    return this;
    //}


    //public String getRemarks() {
    //    return remarks;
    //}
    //
    //public BaseDataEntity setRemarks(String remarks) {
    //    this.remarks = remarks;
    //    return this;
    //}


    //public String getDelFlag() {
    //    return delFlag;
    //}
    //
    //public BaseDataEntity setDelFlag(String delFlag) {
    //    this.delFlag = delFlag;
    //    return this;
    //}

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
        insertInit(companyUserInfo.get());
    }

    /**
     * 设置修改时间
     */
    @Override
    public void preUpdate() {
        updateInit();
    }

    /**
     * 将修改信息清空
     */
    public void updateClear() {
        this.updateDate = null;
        this.updateId = null;
        this.updateName = null;
    }

    /**
     * 设置修改时间,修改人,拿当前线程中的用户信息
     */
    public void updateInit() {
        updateInit(companyUserInfo.get());
    }

    /**
     * 设置修改时间,修改人创建人等
     */
    public void insertInit() {
        this.insertInit(companyUserInfo.get());
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
        this.delFlag=BaseGlobal.DEL_FLAG_NORMAL;
    }

    public <T extends BaseDataEntity> void copyFrom(T t){
        this.setUpdateDate(t.getUpdateDate());
        this.setUpdateId(t.getUpdateId());
        this.setUpdateName(t.getCreateName());
        this.setCreateDate(t.getCreateDate());
        this.setCreateId(t.getCreateId());
        this.setCreateName(t.getCreateName());
        this.setDelFlag(t.getDelFlag());
    }
}
