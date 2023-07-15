/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.module.smp.entity.SmpSizeQty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
/**
 * 类描述：资料包-物料清单-配码 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackBomSize
 * @email your email
 * @date 创建时间：2023-7-15 13:55:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_bom_size")
@ApiModel("资料包-物料清单-配码 PackBomSize")
public class PackBomSize extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;

    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    public SmpSizeQty toSmpSizeQty() {
        SmpSizeQty smpSizeQty = new SmpSizeQty();
        smpSizeQty.setItemQty(quantity);
        return smpSizeQty;
    }
    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    /**
     * 资料包类型:packDesign:设计资料包
     */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包")
    private String packType;
    /**
     * 版本id
     */
    @ApiModelProperty(value = "版本id")
    private String bomVersionId;
    /**
     * 物料编号
     */
    @ApiModelProperty(value = "物料编号")
    private String bomId;
    /**
     * 尺码
     */
    @ApiModelProperty(value = "尺码")
    private String size;
    /**
     * 尺码id
     */
    @ApiModelProperty(value = "尺码id")
    private String sizeId;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
    /**
     * 门幅/规格
     */
    @ApiModelProperty(value = "门幅/规格")
    private String width;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

