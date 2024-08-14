package com.base.sbc.module.replay.dto;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.config.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

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
    private Integer productionCount = 0;

    /** 库存款 */
    @ApiModelProperty(value = "库存款")
    private Integer storageCount = 0;

    /**
     * 列表数据合计成单个bean
     */
    public ProductionSaleDTO decorateTotal(List<ProductionSaleDTO> list) {
        if (CollUtil.isNotEmpty(list)) {
            this.setProduction(CommonUtils.sumBigDecimal(list, ProductionSaleDTO::getProduction));
            this.setSale(CommonUtils.sumBigDecimal(list, ProductionSaleDTO::getSale));
            this.setProductionCount(list.stream().mapToInt(ProductionSaleDTO::getProductionCount).sum());
            this.setStorageCount(list.stream().mapToInt(ProductionSaleDTO::getStorageCount).sum());
        }
        this.setProductionUnit(BigDecimal.ONE);
        this.setSaleUnit(BigDecimal.ONE);
        return this;
    }

}