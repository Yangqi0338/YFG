/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.entity;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/** 
 * 类描述： 实体类
 * @address com.base.sbc.pdm.entity.Planning
 * @author lile
 * @email lilemyemail@163.com
 * @date 创建时间：2023-3-17 11:24:43
 * @version 1.0  
 */
public class Planning extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
	
	
	/**********************************实体存放的其他字段区 【other_end】******************************************/
	
    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 企划名称 */
    private String planningName;
    /** 年份 */
    private String particularYear;
    /** 季节 */
    private String season;
    /** 月份 */
    private String month;
    /** 品牌名称 */
    private String brand;
    /** 性别 */
    private String gender;
    /** 款式数量 */
    private Integer styleNum;
    /** 配色数量 */
    private Integer colorNum;
    /** 波段名称 */
    private String waveBand;
    /*******************************************getset方法区************************************/
    public String getPlanningName() {
		return planningName;
	}
	public Planning setPlanningNameAnd(String planningName) {
		this.planningName = planningName;
		return this;
	}
	public void setPlanningName(String planningName) {
		this.planningName = planningName;
	}
    public String getParticularYear() {
		return particularYear;
	}
	public Planning setParticularYearAnd(String particularYear) {
		this.particularYear = particularYear;
		return this;
	}
	public void setParticularYear(String particularYear) {
		this.particularYear = particularYear;
	}
    public String getSeason() {
		return season;
	}
	public Planning setSeasonAnd(String season) {
		this.season = season;
		return this;
	}
	public void setSeason(String season) {
		this.season = season;
	}
    public String getMonth() {
		return month;
	}
	public Planning setMonthAnd(String month) {
		this.month = month;
		return this;
	}
	public void setMonth(String month) {
		this.month = month;
	}
    public String getBrand() {
		return brand;
	}
	public Planning setBrandAnd(String brand) {
		this.brand = brand;
		return this;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
    public String getGender() {
		return gender;
	}
	public Planning setGenderAnd(String gender) {
		this.gender = gender;
		return this;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
    public Integer getStyleNum() {
		return styleNum;
	}
	public Planning setStyleNumAnd(Integer styleNum) {
		this.styleNum = styleNum;
		return this;
	}
	public void setStyleNum(Integer styleNum) {
		this.styleNum = styleNum;
	}
    public Integer getColorNum() {
		return colorNum;
	}
	public Planning setColorNumAnd(Integer colorNum) {
		this.colorNum = colorNum;
		return this;
	}
	public void setColorNum(Integer colorNum) {
		this.colorNum = colorNum;
	}
    public String getWaveBand() {
		return waveBand;
	}
	public Planning setWaveBandAnd(String waveBand) {
		this.waveBand = waveBand;
		return this;
	}
	public void setWaveBand(String waveBand) {
		this.waveBand = waveBand;
	}
}