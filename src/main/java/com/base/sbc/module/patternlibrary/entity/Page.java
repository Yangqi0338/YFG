package com.base.sbc.module.patternlibrary.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页实体类
 *
 * @author xhte
 * @create 2024-03-22
 */
@Data
@ApiModel(value = "分页实体类", description = "分页实体类")
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 第几页
     */
    @ApiModelProperty(value = "第几页", example = "1")
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize = 10;

}
