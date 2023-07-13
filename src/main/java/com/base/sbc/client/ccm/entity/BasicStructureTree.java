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
 * 类描述：结构管理树表 实体类
 * @address com.base.sbc.companyconfig.basic.entity.BasicStructureTree
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-28 9:25:13
 * @version 1.0
 */
public class BasicStructureTree extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

	private List<BasicStructureTree> children;

	public List<BasicStructureTree> getChildren() {
		return children;
	}

	public void setChildren(List<BasicStructureTree> children) {
		this.children = children;
	}
	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 结构分类id */
    private String structureId;
    /** 名称 */
    private String name;
    /** 编码 */
    private String value;
    /** 顺序 */
    private Integer sort;
    /** 父类ID */
    private String parentId;
    /** 父类ID集合(逗号隔开) */
    private String parentIds;
    /** 第几级树(0为第一级) */
    private Integer level;
    /** 是否子节点(0表示是，1表示不是） */
    private String isLeaf;
    /** 状态启用(0),停用(1) */
    private String status;
    /*******************************************getset方法区************************************/
    public String getStructureId() {
		return structureId;
	}
	public void setStructureId(String structureId) {
		this.structureId = structureId;
	}
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
    public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
    public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
    public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
    public String getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
    public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}

