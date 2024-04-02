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
 * @create 2024-04-02
 */
@Data
@ApiModel(value = "PatternLibraryTemplateDTO对象", description = "版型库-模板表DTO")
public class PatternLibraryTemplateDTO extends Page implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 启用状态（0-停用，1-启用)
     */
    @ApiModelProperty("启用状态（0-停用，1-启用)")
    private Integer enableFlag;

}
