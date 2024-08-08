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
import com.base.sbc.config.annotation.ExtendField;
import com.base.sbc.config.common.IdGen;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * @author 卞康
 * @date 2023/3/31 19:56:38
 */
@Data
public class BaseDataNewEntity extends BaseEntity {

    @TableField(exist = false)
    private static final long serialVersionUID = 7022181519896948997L;
    /** 删除标记（0：正常；id：删除；） */
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    protected String delFlag;
    /** 更新者名称 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExtendField
    private String updateName;
    /** 更新者id */
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
    /** 创建者名称 */
    @TableField(fill = FieldFill.INSERT)
    @ExtendField
    private String createName;
    /** 创建者id */
    @TableField(fill = FieldFill.INSERT)
    private String createId;

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
        this.updateDate = new Date();
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
        this.delFlag = BaseGlobal.DEL_FLAG_NORMAL;
    }
}
