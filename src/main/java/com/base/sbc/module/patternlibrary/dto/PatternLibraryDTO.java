package com.base.sbc.module.patternlibrary.dto;

import com.base.sbc.module.patternlibrary.entity.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 版型库-主表DTO
 *
 * @author xhte
 * @create 2024-03-22
 */
@Data
@ApiModel(value = "PatternLibraryDTO对象", description = "版型库-主表DTO")
public class PatternLibraryDTO extends Page implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版型编码
     */
    @ApiModelProperty("版型编码")
    private String code;

    /**
     * 品牌（多选逗号分隔）
     */
    @ApiModelProperty("品牌（多选逗号分隔）")
    private String brand;

    /**
     * 大类 code
     */
    @ApiModelProperty("大类 code")
    private String prodCategory1st;

    /**
     * 品类 code
     */
    @ApiModelProperty("品类 code")
    private String prodCategory;

    /**
     * 中类 code
     */
    @ApiModelProperty("中类 code")
    private String prodCategory2nd;

    /**
     * 廓形 code
     */
    @ApiModelProperty("廓形 code")
    private String silhouetteCode;

    /**
     * 模板 code（t_pattern_library code）（多选逗号分隔）
     */
    @ApiModelProperty("模板 code（多选逗号分隔）")
    private String templateCode;

    /**
     * 涉及部件 code（多选逗号分隔）
     */
    @ApiModelProperty("涉及部件 code（多选逗号分隔）")
    private String partsCode;

    /**
     * 状态（1-待补齐 2-待提交 3-待审核 4-已审核 5-已驳回）
     */
    @ApiModelProperty("状态（1-待补齐 2-待提交 3-待审核 4-已审核 5-已驳回）")
    private Integer status;

    /**
     * 启用状态（0-停用，1-启用)
     */
    @ApiModelProperty("启用状态（0-停用，1-启用)")
    private Integer enableFlag;
}
