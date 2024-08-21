package com.base.sbc.module.replay.dto;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductionSaleDTO extends ReplayRatingProductionSaleDTO {

    /** 大货款号 */
    @ApiModelProperty(value = "大货款号")
    @JsonIgnore
    private String bulkStyleNo;

    /** 尺码 */
    @ApiModelProperty(value = "尺码")
    private String sizeName;

    /** 投产次数 */
    @ApiModelProperty(value = "投产次数")
    private BigDecimal productionCount = BigDecimal.ZERO;

    /** 库存款 */
    @ApiModelProperty(value = "库存款")
    private BigDecimal storageCount = BigDecimal.ZERO;

    /**
     * 列表数据合计成单个bean
     */
    public ProductionSaleDTO decorateTotal(List<ProductionSaleDTO> list) {
        if (CollUtil.isNotEmpty(list)) {
            this.setProduction(CommonUtils.sumBigDecimal(list, ProductionSaleDTO::getProduction));
            this.setSale(CommonUtils.sumBigDecimal(list, ProductionSaleDTO::getSale));
            this.setProductionCount(CommonUtils.sumBigDecimal(list, ProductionSaleDTO::getProductionCount));
            this.setStorageCount(CommonUtils.sumBigDecimal(list, ProductionSaleDTO::getStorageCount));
        }
        this.setProductionUnit(BigDecimal.ONE);
        this.setSaleUnit(BigDecimal.ONE);
        return this;
    }

    public Comparator<ProductionSaleDTO> findOrderComparator() {
        // https://blog.csdn.net/m0_59084856/article/details/138452605
        Function<ProductionSaleDTO, BigDecimal> func;
        if (productionCount != null) {
            func = ProductionSaleDTO::getProductionCount;
        } else if (storageCount != null) {
            func = ProductionSaleDTO::getStorageCount;
        } else if (this.getProduction() != null) {
            func = ProductionSaleDTO::getProduction;
        } else if (this.getSale() != null) {
            func = ProductionSaleDTO::getSale;
        } else if (this.productionSaleRate != null) {
            func = ProductionSaleDTO::getProductionSaleRate;
        } else {
            return null;
        }
        Comparator<ProductionSaleDTO> comparing = Comparator.comparing(func);
        if (BigDecimalUtil.biggerThenZero(func.apply(this))) {
            comparing = comparing.reversed();
        }
        return comparing;
    }

}