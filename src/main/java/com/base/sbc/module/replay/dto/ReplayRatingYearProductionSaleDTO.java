/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("基础资料-复盘评分计算-详情 ReplayRatingYearCalculateDTO")
public class ReplayRatingYearProductionSaleDTO extends ReplayRatingProductionSaleDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关键key")
    @JsonIgnore
    private Object key;

    /** 子计算集 */
    @ApiModelProperty(value = "子计算集")
    @JsonIgnore
    private List<ReplayRatingYearProductionSaleDTO> childrenList = new ArrayList<>();

    public Map<Object, ReplayRatingYearProductionSaleDTO> findCalculateMap() {
        if (CollUtil.isEmpty(childrenList)) return new HashMap<>();
        return childrenList.stream().collect(CommonUtils.toMap(ReplayRatingYearProductionSaleDTO::getKey));
    }

    public void calculate() {
        if (CollUtil.isEmpty(childrenList)) return;
        childrenList.forEach(children -> {
            children.calculate();
            this.setProduction(BigDecimalUtil.add(children.getProduction(), this.getProduction()));
            this.setSale(BigDecimalUtil.add(children.getSale(),this.getSale()));
            if (this.getProductionUnit() == null) this.setProductionUnit(children.getProductionUnit());
            if (this.getSaleUnit() == null) this.setSaleUnit(children.getSaleUnit());
        });
    }

}
