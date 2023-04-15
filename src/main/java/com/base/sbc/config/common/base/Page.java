package com.base.sbc.config.common.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

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


    @NotNull
    @ApiModelProperty(value = "第几页", example = "1")
    private int pageNum;
    @NotNull
    @ApiModelProperty(value = "每页数量", example = "10")
    private int pageSize;
    @ApiModelProperty(value = "排序(单表)", example = "create_date")
    private String orderBy = "create_date";
    @ApiModelProperty(value = "关键字搜索", example = "")
    private String search;
    @ApiModelProperty(value = "状态", example = "")
    private String status;

    public String getSql() {
        return null;
    }

}
