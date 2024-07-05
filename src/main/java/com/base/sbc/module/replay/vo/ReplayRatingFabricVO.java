/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.vo;

import com.base.sbc.config.enums.UnitConverterEnum;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import com.base.sbc.config.utils.BigDecimalUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述：基础资料-复盘评分Vo 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.vo.ReplayRatingVo
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("基础资料-复盘评分 ReplayRatingVo")
public class ReplayRatingFabricVO extends ReplayRatingVO {

    /** 物料id */
    @ApiModelProperty(value = "物料清单id")
    private String bomId;

    /** 物料版本id */
    @ApiModelProperty(value = "物料版本id")
    private String bomVersionId;

    /** 物料id */
    @ApiModelProperty(value = "物料id")
    private String materialId;

    /** 面料编号 */
    @ApiModelProperty(value = "面料编号")
    private String materialCode;

    /** 面料成分 */
    @ApiModelProperty(value = "面料成分")
    private String ingredient;

    /** 面料规格code */
    @ApiModelProperty(value = "面料规格code")
    private String translateCode;

    /** 面料规格 */
    @ApiModelProperty(value = "面料规格")
    private String translate;

    /** 颜色 */
    @ApiModelProperty(value = "颜色")
    private String color;

    /** 颜色code */
    @ApiModelProperty(value = "颜色code")
    private String colorCode;

    /** 单位用量 */
    @ApiModelProperty(value = "单位用量")
    private BigDecimal bulkUnitUse;

    /** 供应商id */
    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    /** 供应商 */
    @ApiModelProperty(value = "供应商")
    private String supplierName;

    /** 单位 */
    @ApiModelProperty(value = "单位")
    private String unitCode;

    /** 资料包id */
    @ApiModelProperty(value = "资料包id")
    private String packInfoId;

    /* ----------------------------代码获取---------------------------- */

    /** 面料自主研发 */
    @ApiModelProperty(value = "面料自主研发")
    private YesOrNoEnum fabricOwnDevelopFlag;

    /** 剩余备料 */
    @ApiModelProperty(value = "剩余备料")
    private BigDecimal remainingMaterial;

    /** 投产量 */
    @ApiModelProperty(value = "投产量")
    private BigDecimal production;

    /** 使用米数 */
    @ApiModelProperty(value = "使用米数")
    private UnitConverterEnum unitConverter = UnitConverterEnum.METER;

    /** 使用米数 */
    @ApiModelProperty(value = "使用米数")
    public BigDecimal getRealProduction() {
        return unitConverter.calculate(BigDecimalUtil.mul(production, bulkUnitUse));
    }

    @Override
    public ReplayRatingType getType() {
        return ReplayRatingType.FABRIC;
    }


}
