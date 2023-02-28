package com.base.sbc.api.saas.entity;

import com.base.sbc.config.utils.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author
 */
public class Page implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 默认第一页 */
	public static final int PAGE_NUM = 1;
	/** 默认分页数量 */
	public static final int PAGE_SIZE = 10;
	/** 企业id */
	private String companyCode;
	/** 第几页 */
	@NotNull
	private int pageNum;
	/** 每页数量 */
	@NotNull
	private int pageSize;
	private String search;
	
	public String getSearch() {
		return search;
	}
	
	public void setSearch(String search) {
		this.search = search;
	}
	
	/** 排序(单表):  create_date desc */
	private String order;
	/** 查询的sql： user_id = '123'  */
	private String sql;
	
	public int getPageNum() {
		return pageNum;
	}
	
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public String getOrder() {
		return order;
	}
	
	public void setOrder(String order) {
		this.order = order;
	}
	
	public String getSql() {
		return StringUtils.replaceHtmlCode(sql);
	}
	
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public String getCompanyCode() {
		return companyCode;
	}
	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
}
