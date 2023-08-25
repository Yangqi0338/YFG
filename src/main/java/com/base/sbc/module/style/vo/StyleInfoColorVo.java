/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：款式设计详情颜色表 实体类
 * @address com.base.sbc.module.style.entity.StyleInfoColor
 * @author LiZan
 * @email 2682766618@qq.com
 * @date 创建时间：2023-8-24 15:21:29
 * @version 1.0
 */
@Data
@ApiModel("款式设计详情颜色表 StyleInfoColorVo")
public class StyleInfoColorVo {

	private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id"  )
    private String id;
    /** 主数据id */
    @ApiModelProperty(value = "主数据id"  )
    private String foreignId;
    /** 资料包类型:packDesign:设计资料包 */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包"  )
    private String packType;
    /** 颜色编码 */
    @ApiModelProperty(value = "颜色编码"  )
    private String colorCode;
    /** 颜色名称 */
    @ApiModelProperty(value = "颜色名称"  )
    private String colorName;
    /** 颜色色号 */
    @ApiModelProperty(value = "颜色色号"  )
    private String colorNumber;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String images;
    /** SKC成本价 */
    @ApiModelProperty(value = "SKC成本价"  )
    private BigDecimal skcCostPrice;
    /** 吊牌价 */
    @ApiModelProperty(value = "吊牌价"  )
    private BigDecimal tagPrice;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
}
