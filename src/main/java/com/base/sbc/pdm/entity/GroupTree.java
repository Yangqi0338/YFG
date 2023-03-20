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
 * 类描述：分组树表 实体类
 * @address com.base.sbc.pdm.entity.GroupTree
 * @author lile
 * @email lilemyemail@163.com
 * @date 创建时间：2023-3-18 10:04:22
 * @version 1.0  
 */
public class GroupTree extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
	
	
	/**********************************实体存放的其他字段区 【other_end】******************************************/
	
    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 组名 */
    private String groupName;
    /** 名称 */
    private String name;
    /** 编码 */
    private String code;
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
    public String getGroupName() {
		return groupName;
	}
	public GroupTree setGroupNameAnd(String groupName) {
		this.groupName = groupName;
		return this;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
    public String getName() {
		return name;
	}
	public GroupTree setNameAnd(String name) {
		this.name = name;
		return this;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String getCode() {
		return code;
	}
	public GroupTree setCodeAnd(String code) {
		this.code = code;
		return this;
	}
	public void setCode(String code) {
		this.code = code;
	}
    public Integer getSort() {
		return sort;
	}
	public GroupTree setSortAnd(Integer sort) {
		this.sort = sort;
		return this;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
    public String getParentId() {
		return parentId;
	}
	public GroupTree setParentIdAnd(String parentId) {
		this.parentId = parentId;
		return this;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    public String getParentIds() {
		return parentIds;
	}
	public GroupTree setParentIdsAnd(String parentIds) {
		this.parentIds = parentIds;
		return this;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
    public Integer getLevel() {
		return level;
	}
	public GroupTree setLevelAnd(Integer level) {
		this.level = level;
		return this;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
    public String getIsLeaf() {
		return isLeaf;
	}
	public GroupTree setIsLeafAnd(String isLeaf) {
		this.isLeaf = isLeaf;
		return this;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
    public String getStatus() {
		return status;
	}
	public GroupTree setStatusAnd(String status) {
		this.status = status;
		return this;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}