package com.base.sbc.module.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 卞康
 * @date 2023/4/4 9:39:14
 */
@Data
public class PageDto implements Serializable {
    @NotNull
    @ApiModelProperty(value = "第几页", example = "1")
    private int pageNum;
    @NotNull
    @ApiModelProperty(value = "每页数量", example = "10")
    private int pageSize;
    @ApiModelProperty(value = "排序(单表)", example = "create_date desc")
    private String orderBy;
}
