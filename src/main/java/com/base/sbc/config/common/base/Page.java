package com.base.sbc.config.common.base;

import com.base.sbc.config.utils.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youkehai
 * @data 创建时间:2021/4/2
 */
public class Page implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 默认第一页
     */
    public static final int PAGE_NUM = 1;
    /**
     * 默认分页数量
     */
    public static final int PAGE_SIZE = 10;

    /**
     * 企业id
     */
    private String companyCode;

    /**
     * 第几页
     */
    @NotNull
    private int pageNum;
    /**
     * 每页数量
     */
    @NotNull
    private int pageSize;

    /**
     * 排序(单表):  create_date desc
     */
    private String order;
    /**
     * 查询的sql： user_id = '123'
     */
    private String sql;
    /***/
    private String search;
    /**
     * 分组id(plm)
     */
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

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

    public Page(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Page(int pageNum, int pageSize, String order, String sql) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.order = order;
        this.sql = sql;
    }

    public Page() {
    }
}
