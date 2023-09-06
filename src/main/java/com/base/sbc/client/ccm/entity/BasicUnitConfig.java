/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.client.ccm.entity;

import com.base.sbc.config.common.base.BaseDataEntity;

import java.math.BigDecimal;

/** 
 * 类描述：单位换算配置表 实体类
 * @address com.base.sbc.companyconfig.basic.entity.BasicUnitConfig
 * @author youkehai
 * @email 717407966@qq.com
 * @date 创建时间：2021-3-10 17:51:37
 * @version 1.0  
 */
public class BasicUnitConfig extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
	
	
	/**********************************实体存放的其他字段区 【other_end】******************************************/
	
    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 类型 */
    private String type;
    /** 分组 */
    private String groupName;
    /** 单位key */
    private String unitKey;
    /** 单位名称 */
    private String unitName;
    /** 转换率 */
    private BigDecimal turnoverRate;
    /** 是否默认 */
    private String isDefault;
    /** 排序（升序） */
    private BigDecimal sort;
    /** 启用(0)停用 */
    private String status;
    /*******************************************getset方法区************************************/
    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
    public String getUnitKey() {
		return unitKey;
	}
	public void setUnitKey(String unitKey) {
		this.unitKey = unitKey;
	}
    public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
    public BigDecimal getTurnoverRate() {
		return turnoverRate;
	}
	public void setTurnoverRate(BigDecimal turnoverRate) {
		this.turnoverRate = turnoverRate;
	}
    public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
    public BigDecimal getSort() {
		return sort;
	}
	public void setSort(BigDecimal sort) {
		this.sort = sort;
	}
    public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}