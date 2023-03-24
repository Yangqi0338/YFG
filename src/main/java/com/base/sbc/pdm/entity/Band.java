/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述： 实体类
 * @address com.base.sbc.pdm.entity.Band
 * @author lile
 * @email lilemyemail@163.com
 * @date 创建时间：2023-3-17 18:08:52
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_band")
public class Band extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
	/**
	 * id集合
	 */
	@TableField(exist = false)
	private String[] ids;

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 年份 */
    private String particularYear;
    /** 季节 */
    private String season;
    /** 月份 */
    private String month;
    /** 波段名称 */
    private String bandName;
    /** 编码 */
    private String code;
    /** 排序 */
    private String sort;
    /** 是否启用(0启用，1删除) */
    private String status;
    /*******************************************getset方法区************************************/
    public String getParticularYear() {
		return particularYear;
	}
	public Band setParticularYearAnd(String particularYear) {
		this.particularYear = particularYear;
		return this;
	}
	public void setParticularYear(String particularYear) {
		this.particularYear = particularYear;
	}
    public String getSeason() {
		return season;
	}
	public Band setSeasonAnd(String season) {
		this.season = season;
		return this;
	}
	public void setSeason(String season) {
		this.season = season;
	}
    public String getMonth() {
		return month;
	}
	public Band setMonthAnd(String month) {
		this.month = month;
		return this;
	}
	public void setMonth(String month) {
		this.month = month;
	}
    public String getBandName() {
		return bandName;
	}
	public Band setBandNameAnd(String bandName) {
		this.bandName = bandName;
		return this;
	}
	public void setBandName(String bandName) {
		this.bandName = bandName;
	}
    public String getCode() {
		return code;
	}
	public Band setCodeAnd(String code) {
		this.code = code;
		return this;
	}
	public void setCode(String code) {
		this.code = code;
	}
    public String getSort() {
		return sort;
	}
	public Band setSortAnd(String sort) {
		this.sort = sort;
		return this;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
    public String getStatus() {
		return status;
	}
	public Band setStatusAnd(String status) {
		this.status = status;
		return this;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}

