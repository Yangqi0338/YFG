package com.base.sbc.config.common.base;

import cn.hutool.core.lang.Opt;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Youkehai
 * @data 创建时间:2021/4/2
 */

@Data
@ApiModel("分页组件")
public class Page implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int PAGE_NUM = 1;
    public static final int PAGE_SIZE = 10;


    @ApiModelProperty(value = "第几页", example = "1")
    private Integer pageNum;
    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize;
    @ApiModelProperty(value = "排序(单表)", example = "create_date desc")
    private String orderBy;
    @ApiModelProperty(value = "关键字搜索", example = "")
    private String search;
    @ApiModelProperty(value = "状态", example = "")
    private String status;

    public String getSql() {
        return null;
    }

    public Boolean isAsc() {
        return Opt.ofBlankAble(orderBy).map(o -> o.toUpperCase().contains("ASC")).orElse(false);
    }

    public String getOrderByColumn() {
        return Opt.ofBlankAble(orderBy).map(o -> o.toUpperCase().replace("ASC", "").replace("DESC", "")).orElse(null);
    }

    public Integer getPageSize() {
        if (pageSize == null) {
            pageSize = 0;
        }
        return pageSize;
    }

    public Integer getPageNum() {
        if (pageNum == null) {
            pageNum = 0;
        }
        return pageNum;
    }

    public Boolean getPageSizeZero() {
        if (getPageSize() == 0) {
            return true;
        }
        return false;
    }

    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<?> toMPPage(){
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(this.pageNum, this.pageSize);
    }
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<Map<String,Object>> toMPPageMap(){
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(this.pageNum, this.pageSize);
    }

    public <E> com.github.pagehelper.Page<E> startPage(){
        return PageHelper.startPage(this.pageNum, this.pageSize);
    }

}
