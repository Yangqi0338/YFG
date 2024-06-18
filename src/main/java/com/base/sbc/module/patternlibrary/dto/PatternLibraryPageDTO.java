package com.base.sbc.module.patternlibrary.dto;

import com.base.sbc.module.patternlibrary.entity.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 版型库-主表分页DTO
 *
 * @author xhte
 * @create 2024-03-22
 */
@Data
@ApiModel(value = "PatternLibraryPageDTO对象", description = "版型库-主表分页DTO")
public class PatternLibraryPageDTO extends Page implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版型编码
     */
    @ApiModelProperty("版型编码")
    private String code;

    /**
     * 大货款号
     */
    @ApiModelProperty("大货款号")
    private String styleNo;

    /**
     * 品牌名称 （多选逗号分隔）
     */
    @ApiModelProperty("品牌名称 （多选逗号分隔）")
    private String brandNames;

    /**
     * 品牌名称集合
     */
    @ApiModelProperty("品牌名称集合")
    private List<String> brandNameList;

    /**
     * 大类名称
     */
    @ApiModelProperty("大类名称")
    private String prodCategory1stName;

    /**
     * 品类名称
     */
    @ApiModelProperty("品类名称")
    private String prodCategoryName;

    /**
     * 中类名称
     */
    @ApiModelProperty("中类名称")
    private String prodCategory2ndName;

    /**
     * 小类名称
     */
    @ApiModelProperty("小类名称")
    private String prodCategory3rdName;

    /**
     * 廓形名称
     */
    @ApiModelProperty("廓形名称")
    private String silhouetteName;

    /**
     * 模板名称（多选逗号分隔）（t_pattern_library name）
     */
    @ApiModelProperty("模板名称（多选逗号分隔）")
    private String templateName;

    /**
     * 涉及部件名称 （多选逗号分隔）
     */
    @ApiModelProperty("涉及部件名称 （多选逗号分隔）")
    private String partsNames;

    /**
     * 涉及部件名称集合
     */
    @ApiModelProperty("涉及部件名称集合")
    private List<String> partsNameList;

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

    /**
     * 是否导出（0-否，1-是)
     */
    @ApiModelProperty("是否导出（0-否，1-是)")
    private Integer isExcel = 0;
}
