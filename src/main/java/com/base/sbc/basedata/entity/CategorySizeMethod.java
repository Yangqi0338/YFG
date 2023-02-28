/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.basedata.entity;

import com.base.sbc.config.common.base.BaseDataEntity;

import java.util.List;

/** 
 * 类描述：品类尺寸量法 实体类
 * @address com.base.sbc.basedata.entity.CategorySizeMethod
 * @author youkehai
 * @email 717407966@qq.com
 * @date 创建时间：2021-5-6 16:34:43
 * @version 1.0  
 */
public class CategorySizeMethod extends BaseDataEntity<String> implements Cloneable{

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
	private List<String> sizeList;
	private List<String> standardList;

	public List<String> getSizeList() {
		return sizeList;
	}

	public void setSizeList(List<String> sizeList) {
		this.sizeList = sizeList;
	}

	public List<String> getStandardList() {
		return standardList;
	}

	public void setStandardList(List<String> standardList) {
		this.standardList = standardList;
	}

	/**********************************实体存放的其他字段区 【other_end】******************************************/
	
    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 品类名称（对应CCM的品类名称） */
    private String categoryName;
    /** 部位名称 */
    private String partName;
    /** 量法 */
    private String method;
    /** 尺寸(对应CCM中的品类的尺寸) */
    private String size;
    /** 公差 */
    private String tolerance;
    /** 标准值 */
    private String standard;
    /** 说明 */
    private String tips;
    /** 图片 */
    private String image;
    /** 码差 */
    private String codeError;
    /** 基码 */
    private String baseSize;
    /*******************************************getset方法区************************************/
    public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
    public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
    public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
    public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
    public String getTolerance() {
		return tolerance;
	}
	public void setTolerance(String tolerance) {
		this.tolerance = tolerance;
	}
    public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
    public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
    public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
    public String getCodeError() {
		return codeError;
	}
	public void setCodeError(String codeError) {
		this.codeError = codeError;
	}
    public String getBaseSize() {
		return baseSize;
	}
	public void setBaseSize(String baseSize) {
		this.baseSize = baseSize;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}

