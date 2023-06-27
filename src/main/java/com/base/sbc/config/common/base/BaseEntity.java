/******************************************************************************
 * Copyright (C) 2018 celizi.com
 * All Rights Reserved.
 * 本软件为网址：celizi.com 开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.config.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Map;

/**
 * 类描述：实体基类
 * @address com.celizi.base.common.base.BaseEntity
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2017年4月8日 上午9:26:55
 * @version 1.0
 */
public abstract class BaseEntity implements Serializable{
	private static final long serialVersionUID = -1274762284724633163L;
	/** 删除标记（0：正常；1：删除；2：审核；）*/
	public static final String DEL_FLAG_NORMAL = "0";
	/** 删除标记（0：正常；1：删除；2：审核；）*/
	public static final String DEL_FLAG_DELETE = "1";
	/** 删除标记（0：正常；1：删除；2：审核；）*/
	public static final String DEL_FLAG_AUDIT = "2";

	/** 实体主键 */
	@TableId(type = IdType.ASSIGN_ID)
	protected String id;

	/** 公司编码 */
	@TableField(fill = FieldFill.INSERT)
	protected String companyCode;

	/** 自定义SQL（SQL标识，SQL内容） */
	@TableField(exist = false)
	protected Map<String, String> sqlMap;


	public BaseEntity() {

	}

	public BaseEntity(String id) {
		this();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}


	/**
	 * 插入之前执行方法，子类实现
	 */
	public abstract void preInsert();

	/**
	 * 更新之前执行方法，子类实现
	 */
	public abstract void preUpdate();


	/**
	 * 全局变量对象
	 */
	@JsonIgnore
	public BaseGlobal getGlobal() {
		return BaseGlobal.getInstance();
	}


    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        BaseEntity that = (BaseEntity) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

   @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
