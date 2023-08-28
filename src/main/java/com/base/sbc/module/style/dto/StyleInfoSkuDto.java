/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.common.base.GroupQuery;
import com.base.sbc.config.common.base.GroupUpdate;
import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 类描述：款式设计SKU表 DTO类
 * @address com.base.sbc.module.style.dto.StyleInfoSkuDto
 * @author LiZan
 * @email 2682766618@qq.com
 * @date 创建时间：2023-8-24 15:21:34
 * @version 1.0
 */
@Data
@ApiModel("款式设计SKU表 StyleInfoSkuDto")
public class StyleInfoSkuDto  extends Page {

	private static final long serialVersionUID = 1L;
    /** id */
    @ApiModelProperty(value = "id"  )
    @NotNull(message = "id不能为空" , groups = {GroupUpdate.class})
    private String id;
    /** 主数据id */
    @ApiModelProperty(value = "主数据id"  )
    @NotNull(message = "主数据id不能为空" , groups = {GroupUpdate.class, GroupQuery.class})
    private String foreignId;
    /** 资料包类型:packDesign:设计资料包 */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包"  )
    private String packType;
    /** SKU编码 */
    @ApiModelProperty(value = "SKU编码"  )
    private String skuCode;
    /** 颜色名称 */
    @ApiModelProperty(value = "颜色名称"  )
    private String colorName;
    /** 颜色编码 */
    @ApiModelProperty(value = "颜色编码"  )
    private String colorCode;
    /** 尺码名称 */
    @ApiModelProperty(value = "尺码名称"  )
    private String sizeName;
    /** 尺码编码 */
    @ApiModelProperty(value = "尺码编码"  )
    private String sizeCode;
    /** 成本价格 */
    @ApiModelProperty(value = "成本价格"  )
    private BigDecimal costPrice;
    /** 吊牌价格 */
    @ApiModelProperty(value = "吊牌价格"  )
    private BigDecimal tagPrice;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
}
