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
 * @address com.base.sbc.pdm.entity.Material
 * @author lile
 * @email lilemyemail@163.com
 * @date 创建时间：2023-3-22 13:11:09
 * @version 1.0  
 */
public class Material extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
	
	
	/**********************************实体存放的其他字段区 【other_end】******************************************/
	
    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 图片地址 */
    private String pictureUrl;
    /** 图片格式 */
    private String pictureFormat;
    /** 图片大小 */
    private String pictureSize;
    /** 文件名 */
    private String fileName;
    /** 所属素材库 */
    private String materialLibrary;
    /*******************************************getset方法区************************************/
    public String getPictureUrl() {
		return pictureUrl;
	}
	public Material setPictureUrlAnd(String pictureUrl) {
		this.pictureUrl = pictureUrl;
		return this;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
    public String getPictureFormat() {
		return pictureFormat;
	}
	public Material setPictureFormatAnd(String pictureFormat) {
		this.pictureFormat = pictureFormat;
		return this;
	}
	public void setPictureFormat(String pictureFormat) {
		this.pictureFormat = pictureFormat;
	}
    public String getPictureSize() {
		return pictureSize;
	}
	public Material setPictureSizeAnd(String pictureSize) {
		this.pictureSize = pictureSize;
		return this;
	}
	public void setPictureSize(String pictureSize) {
		this.pictureSize = pictureSize;
	}
    public String getFileName() {
		return fileName;
	}
	public Material setFileNameAnd(String fileName) {
		this.fileName = fileName;
		return this;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
    public String getMaterialLibrary() {
		return materialLibrary;
	}
	public Material setMaterialLibraryAnd(String materialLibrary) {
		this.materialLibrary = materialLibrary;
		return this;
	}
	public void setMaterialLibrary(String materialLibrary) {
		this.materialLibrary = materialLibrary;
	}
}