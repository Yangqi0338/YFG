package com.base.sbc.module.patternlibrary.dto;

import com.base.sbc.module.patternlibrary.entity.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 版型库-模板表DTO
 *
 * @author xhte
 * @create 2024-03-22
 */
@Data
@ApiModel(value = "PatternLibraryTemplateDTO对象", description = "版型库-模板表DTO")
public class PatternLibraryTemplatePageDTO extends Page implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板编码/模板名称
     */
    @ApiModelProperty("模板编码/模板名称")
    private String search;

    /**
     * 启用状态（0-停用，1-启用)
     */
    @ApiModelProperty("启用状态（0-停用，1-启用)")
    private Integer enableFlag;

}
