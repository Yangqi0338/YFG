/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.dto;

import com.base.sbc.config.common.base.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 类描述：资料包-款式设计详情颜色表 实体类
 * @address com.base.sbc.module.style.entity.StyleInfoColor
 * @author LiZan
 * @email 2682766618@qq.com
 * @date 创建时间：2023-8-24 15:21:29
 * @version 1.0
 */
@Data
@ApiModel("资料包-款式设计详情颜色表 StyleInfoColorDto")
public class StyleInfoColorDto extends Page {

	private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id"  )
    @NotNull(message = "id不能为空" , groups = {GroupUpdate.class})
    private String id;
    /** 主数据id */
    @ApiModelProperty(value = "主数据id"  )
    @NotNull(message = "主数据id不能为空" , groups = {GroupUpdate.class , GroupInsert.class, GroupDelete.class})
    private String foreignId;
    /** 资料包类型:packDesign:设计资料包 */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包"  )
    @NotNull(message = "资料包类型不能为空" , groups = {GroupInsert.class})
    private String packType;
    /** 颜色编码 */
    @ApiModelProperty(value = "颜色编码"  )
    @NotNull(message = "颜色编码不能为空" , groups = {GroupInsert.class})
    private String colorCode;
    /** 颜色名称 */
    @ApiModelProperty(value = "颜色名称"  )
    @NotNull(message = "颜色名称不能为空" , groups = {GroupInsert.class})
    private String colorName;
    /** 颜色色号 */
    @ApiModelProperty(value = "颜色色号"  )
    private String colorNumber;
    /** 图片名称 */
    @ApiModelProperty(value = "图片名称"  )
    private String imagesName;
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
    /** 颜色codes */
    @ApiModelProperty(value = "颜色codes"  )
    @NotNull(message = "颜色编码不能为空" , groups = {GroupDelete.class})
    private String codes;
    /** 款式id */
    @ApiModelProperty(value = "款式id"  )
    @NotNull(message = "颜色编码不能为空" , groups = {GroupDelete.class})
    private String styleId;
    /** 公司编码 */
    @ApiModelProperty(value = "公司编码"  )
    private String companyCode;




}
