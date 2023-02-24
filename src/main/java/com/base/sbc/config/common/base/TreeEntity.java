/******************************************************************************
 * Copyright (C) 2018 celizi.com 
 * All Rights Reserved.
 * 本软件为网址：celizi.com 开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.config.common.base;

/**
 * 类描述：
 * 
 * @address com.celizi.base.common.base.TreeEntity
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2017年4月8日 上午9:27:18
 * @version 1.0
 */
public class TreeEntity<T> extends BaseDataEntity<T> {
	/**  */
	private static final long serialVersionUID = 6747752812576826890L;
	
	/** 上级ID */
	protected String parentId;
	/** 父类名称 */
	protected String parentName;
	/** 所有上级Id集合 */
	protected String parentIds;
	/** 排序 */
	protected Long sort;
	/** 层级 */
	protected Integer level;
	/** 是否子节点 */
	protected Boolean isLeaf;
	/** 用于jqgrid 树形结构是否默认展开  */
	protected Boolean isOpen;
	/** 用于jqgrid 树形结构 异步加载树 当前行是否一次性加载  */
	protected Boolean loaded;
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public Boolean getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}
	public Boolean getLoaded() {
		return loaded;
	}
	public void setLoaded(Boolean loaded) {
		this.loaded = loaded;
	}
}
