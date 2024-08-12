/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.enums.UnitConverterEnum;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 类描述：基础资料-复盘评分-详情Vo 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.vo.ReplayRatingDetailVo
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */

@Data
@ApiModel("基础资料-复盘评分计算-详情 ReplayRatingYearCalculateDTO")
public class ReplayRatingProductionSaleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 投产 */
    @ApiModelProperty(value = "投产")
    private BigDecimal production = BigDecimal.ZERO;

    /** 单位 */
    @ApiModelProperty(value = "投产单位")
    private BigDecimal productionUnit;

    /** 投产单位转化 */
    @ApiModelProperty(value = "投产单位转化")
    @JsonIgnore
    private UnitConverterEnum productionUnitConvert;

    /** 销售 */
    @ApiModelProperty(value = "销售")
    private BigDecimal sale = BigDecimal.ZERO;

    /** 销售单位 */
    @ApiModelProperty(value = "销售单位")
    private BigDecimal saleUnit;

    /** 销售单位转化 */
    @ApiModelProperty(value = "销售单位转化")
    @JsonIgnore
    private UnitConverterEnum saleUnitConvert;

    /** 销量 */
    @ApiModelProperty(value = "销量")
    public BigDecimal getRealProduction() {
        BigDecimal decimal = BigDecimalUtil.mul(production, productionUnit);
        if (productionUnitConvert != null)
            decimal = productionUnitConvert.calculate(decimal);
        return decimal;
    }

    /** 销量 */
    @ApiModelProperty(value = "销量")
    public BigDecimal getRealSale() {
        BigDecimal decimal = BigDecimalUtil.mul(sale, saleUnit);
        if (saleUnitConvert != null)
            decimal = saleUnitConvert.calculate(decimal);
        return decimal;
    }

    /** 产销比 */
    @ApiModelProperty(value = "产销比")
    public String getProductionSaleRate() {
        return BigDecimalUtil.dividePercentage(getRealSale(), getRealProduction()) + "%";
    }

    public Map<String, Object> findMap() {
        Map<String, Object> map = BeanUtil.beanToMap(this, false, true);
        map.put("realProduction", getRealProduction());
        map.put("realSale", getRealSale());
        map.put("productionSaleRate", getProductionSaleRate());
        return map;
    }

}
