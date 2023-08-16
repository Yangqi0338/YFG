/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：主题企划素材 实体类
 */
@Data
@ApiModel("主题企划素材保存")
public class ThemePlanningMaterialSaveDTO {
    private String id;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 状态(0正常,1停用)
     */
    @ApiModelProperty(value = "状态(0正常,1停用)")
    private String status;
    /**
     * 主题企划id
     */
    @ApiModelProperty(value = "主题企划id")
    private String themePlanningId;
    /**
     * 素材图片
     */
    @ApiModelProperty(value = "素材图片")
    private String materialImage;
    /**
     * 素材分类编码
     */
    @ApiModelProperty(value = "素材分类编码")
    private String materialClassifId;
    /**
     * 素材分类
     */
    @ApiModelProperty(value = "素材分类")
    private String materialClassif;
    /**
     * 素材名称
     */
    @ApiModelProperty(value = "素材名称")
    private String materialName;
    /**
     * 素材id
     */
    @ApiModelProperty(value = "素材id")
    private String materialId;
    /**
     * 标签（多个逗号分割）
     */
    @ApiModelProperty(value = "标签（多个逗号分割）")
    private String tag;
    /**
     * 标签编码（多个逗号分割）
     */
    @ApiModelProperty(value = "标签编码（多个逗号分割）")
    private String tagCode;
    /**
     * 标签颜色（多个逗号分割）
     */
    @ApiModelProperty(value = "标签颜色（多个逗号分割）")
    private String tagColor;
}

