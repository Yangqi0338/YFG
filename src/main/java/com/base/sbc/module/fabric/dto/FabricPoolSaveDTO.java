/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 面料池保存
 */
@Data
@ApiModel("面料池保存")
public class FabricPoolSaveDTO {

    private String id;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;

    /**
     * 面料池名称
     */
    @ApiModelProperty(value = "面料池名称")
    @NotBlank(message = "面料池名称不可为空")
    private String fabricPoolName;
    /**
     * 品牌编码
     */
    @ApiModelProperty(value = "品牌编码")
    @NotBlank(message = "品牌不可为空")
    private String brandCode;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")

    private String year;
    /**
     * 年份编码
     */
    @ApiModelProperty(value = "年份编码")
    @NotBlank(message = "年份不可为空")
    private String yearCode;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 季节编码
     */
    @ApiModelProperty(value = "季节编码")
    @NotBlank(message = "季节不可为空")
    private String seasonCode;
    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    private String category;
    /**
     * 品类编码
     */
    @ApiModelProperty(value = "品类编码")
    private String categoryCode;
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 类型编码
     */
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
    /**
     * 审核状态：1.未提交、2.审核中、3.审核通过、4.审核失败
     */
    @ApiModelProperty(value = "审核状态：1.未提交、2.审核中、3.审核通过、4.审核失败")
    private String approveStatus;

    /**
     * 面料企划id
     */
    @ApiModelProperty(value = "面料企划id")
    private String fabricPlanningId;

    /**
     * 面料池明细
     */
    @ApiModelProperty(value = "面料池明细")
    private List<FabricPoolItemSaveDTO> fabricPoolItemSaves;
}
