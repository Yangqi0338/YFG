/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.client.ccm.entity;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 类描述：基础-字典依赖 实体类
 * @address com.base.sbc.companyconfig.basic.entity.BasicDictDepend
 * @author 孟繁江
 * @email xx@qq.com
 * @date 创建时间：2023-8-3 11:04:25
 * @version 1.0
 */
public class BasicDictDepend extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 编码 */
    private String code;
    /** 字典类型 */
    private String dictType;
    /** 字典类型名称 */
    private String dictTypeName;
    /** 字典编码 */
    private String dictCode;
    /** 字典名称 */
    private String dictName;
    /** 依赖状态0字典，1结构管理 */
    private String dependStatus;
    /** 依赖字典类型或结构管理类目 */
    private String dependDictType;
    /** 依赖字典类型名称 */
    private String dependDictTypeName;
    /** 依赖名称 */
    private String dependName;
    /** 依赖编码 */
    private String dependCode;
    /*******************************************getset方法区************************************/
    public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    public String getDictType() {
		return dictType;
	}
	public void setDictType(String dictType) {
		this.dictType = dictType;
	}
    public String getDictTypeName() {
		return dictTypeName;
	}
	public void setDictTypeName(String dictTypeName) {
		this.dictTypeName = dictTypeName;
	}
    public String getDictCode() {
		return dictCode;
	}
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
    public String getDictName() {
		return dictName;
	}
	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
    public String getDependStatus() {
		return dependStatus;
	}
	public void setDependStatus(String dependStatus) {
		this.dependStatus = dependStatus;
	}
    public String getDependDictType() {
		return dependDictType;
	}
	public void setDependDictType(String dependDictType) {
		this.dependDictType = dependDictType;
	}
    public String getDependDictTypeName() {
		return dependDictTypeName;
	}
	public void setDependDictTypeName(String dependDictTypeName) {
		this.dependDictTypeName = dependDictTypeName;
	}
    public String getDependName() {
		return dependName;
	}
	public void setDependName(String dependName) {
		this.dependName = dependName;
	}
    public String getDependCode() {
		return dependCode;
	}
	public void setDependCode(String dependCode) {
		this.dependCode = dependCode;
	}
}

