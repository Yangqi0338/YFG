package com.base.sbc.module.patternlibrary.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 列表筛选条件VO
 *
 * @author XHTE
 * @create 2024-04-03
 */
@Data
@ApiModel(value = "FilterCriteriaVO对象", description = "筛选条件VO")
public class FilterCriteriaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版型编码
     */
    @ApiModelProperty("版型编码")
    private String code;

    /**
     * 品牌 code
     */
    @ApiModelProperty("品牌 code")
    private String brand;

    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String brandName;

    /**
     * 大类 code
     */
    @ApiModelProperty("大类 code")
    private String prodCategory1st;

    /**
     * 大类名称
     */
    @ApiModelProperty("大类名称")
    private String prodCategory1stName;

    /**
     * 品类 code
     */
    @ApiModelProperty("品类 code")
    private String prodCategory;

    /**
     * 品类名称
     */
    @ApiModelProperty("品类名称")
    private String prodCategoryName;

    /**
     * 中类 code
     */
    @ApiModelProperty("中类 code")
    private String prodCategory2nd;

    /**
     * 中类名称
     */
    @ApiModelProperty("中类名称")
    private String prodCategory2ndName;

    /**
     * 小类 code
     */
    @ApiModelProperty("小类 code")
    private String prodCategory3rd;

    /**
     * 小类名称
     */
    @ApiModelProperty("小类名称")
    private String prodCategory3rdName;

    /**
     * 廓形 code
     */
    @ApiModelProperty("廓形 code")
    private String silhouetteCode;

    /**
     * 廓形名称
     */
    @ApiModelProperty("廓形名称")
    private String silhouetteName;

    /**
     * 模板 code（t_pattern_library code）
     */
    @ApiModelProperty("模板 code")
    private String templateCode;

    /**
     * 模板名称（t_pattern_library name）
     */
    @ApiModelProperty("模板名称")
    private String templateName;


    /**
     * 涉及部件 code
     */
    @ApiModelProperty("涉及部件 code")
    private String partsCode;
    /**
     * 涉及部件名称
     */
    @ApiModelProperty("涉及部件名称")
    private String partsName;

    /**
     * 状态（1-待补齐 2-待提交 3-待审核 4-已审核 5-已驳回）
     */
    @ExcelProperty(value = "审核状态", index = 11)
    @ApiModelProperty("状态（1-待补齐 2-待提交 3-待审核 4-已审核 5-已驳回）")
    private Integer status;

    /**
     * 启用状态（0-停用，1-启用)
     */
    @ExcelProperty(value = "启用状态", index = 12)
    @ApiModelProperty("启用状态（0-停用，1-启用)")
    private Integer enableFlag;

}
