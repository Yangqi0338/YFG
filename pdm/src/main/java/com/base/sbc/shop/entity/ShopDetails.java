/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.shop.entity;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/** 
 * 类描述：商品详情表 实体类
 * @address com.base.sbc.shop.entity.ShopDetails
 * @author youkehai
 * @email 717407966@qq.com
 * @date 创建时间：2021-8-12 11:21:39
 * @version 1.0  
 */
public class ShopDetails extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
	
	
	/**********************************实体存放的其他字段区 【other_end】******************************************/
	
    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 商品编号 */
    private String shopCode;
    /** 产品主表ID */
    private String promId;
    /** 商品详情图片,拼接 */
    private String shopImgs;
    /** 商品颜色(多个颜色逗号分割) */
    private String colors;
    /** 商品尺码(多个尺码逗号分割) */
    private String sizes;
    /** 商品风格 */
    private String manner;
    /** 商品面料 */
    private String lining;
    /** 商品辅料 */
    private String accessories;
    /** 商品品牌 */
    private String brand;
    /** 年度 */
    private String year;
    /** 季节 */
    private String season;
    /** 波度 */
    private String waviness;
    /** 设计师 */
    private String designer;
    /** 性别 */
    private String sex;
    /** 商品类型名称 */
    private String typeName;
    /** 生产周期 */
    private BigDecimal cycle;
    /** 起批量  */
    private BigDecimal quantity;
    /** 商品介绍信息 */
    private String remaks;
    /** 排序 */
    private String sort;
    /** 状态 */
    private String status;
    /** 设计师或来源名称 */
    private String designerName;
    /*******************************************getset方法区************************************/
    public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
    public String getPromId() {
		return promId;
	}
	public void setPromId(String promId) {
		this.promId = promId;
	}
    public String getShopImgs() {
		return shopImgs;
	}
	public void setShopImgs(String shopImgs) {
		this.shopImgs = shopImgs;
	}
    public String getColors() {
		return colors;
	}
	public void setColors(String colors) {
		this.colors = colors;
	}
    public String getSizes() {
		return sizes;
	}
	public void setSizes(String sizes) {
		this.sizes = sizes;
	}
    public String getManner() {
		return manner;
	}
	public void setManner(String manner) {
		this.manner = manner;
	}
    public String getLining() {
		return lining;
	}
	public void setLining(String lining) {
		this.lining = lining;
	}
    public String getAccessories() {
		return accessories;
	}
	public void setAccessories(String accessories) {
		this.accessories = accessories;
	}
    public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
    public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
    public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
    public String getWaviness() {
		return waviness;
	}
	public void setWaviness(String waviness) {
		this.waviness = waviness;
	}
    public String getDesigner() {
		return designer;
	}
	public void setDesigner(String designer) {
		this.designer = designer;
	}
    public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
    public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
    public BigDecimal getCycle() {
		return cycle;
	}
	public void setCycle(BigDecimal cycle) {
		this.cycle = cycle;
	}
    public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
    public String getRemaks() {
		return remaks;
	}
	public void setRemaks(String remaks) {
		this.remaks = remaks;
	}
    public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
    public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    public String getDesignerName() {
		return designerName;
	}
	public void setDesignerName(String designerName) {
		this.designerName = designerName;
	}
}