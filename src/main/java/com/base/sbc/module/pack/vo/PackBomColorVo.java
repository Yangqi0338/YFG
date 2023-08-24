/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：资料包-物料清单-配色 实体类
 * @address com.base.sbc.module.pack.entity.PackBomColor
 * @author LiZan
 * @email 2682766618@qq.com
 * @date 创建时间：2023-8-23 9:44:43
 * @version 1.0
 */
@Data
@ApiModel("资料包-物料清单-配色 PackBomColorVO")
public class PackBomColorVo {

	private static final long serialVersionUID = 1L;
    /** 主数据id */
    @ApiModelProperty(value = "主数据id"  )
    private String foreignId;
    /** 资料包类型:packDesign:设计资料包 */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包"  )
    private String packType;
    /** 版本id */
    @ApiModelProperty(value = "版本id"  )
    private String bomVersionId;
    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
    private String bomId;
    /** 颜色名称 */
    @ApiModelProperty(value = "颜色名称"  )
    private String colorName;
    /** 颜色编码 */
    @ApiModelProperty(value = "颜色编码"  )
    private String colorCode;
    /** 物料颜色 */
    @ApiModelProperty(value = "物料颜色"  )
    private String materialColorName;
    /** 物料颜色Code */
    @ApiModelProperty(value = "物料颜色Code"  )
    private String materialColorCode;
}

