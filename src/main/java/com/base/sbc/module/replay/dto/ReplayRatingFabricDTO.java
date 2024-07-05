/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import com.base.sbc.config.enums.UnitConverterEnum;
import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import com.base.sbc.config.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
public class ReplayRatingFabricDTO extends ReplayRatingSaveDTO {

    /** 物料id */
    @ApiModelProperty(value = "物料id")
    private String materialId;

    /** 面料编号 */
    @ApiModelProperty(value = "面料编号")
    private String materialCode;

    /** 图片 */
    @ApiModelProperty(value = "图片")
    private String imageUrl;

    /** 面料成分 */
    @ApiModelProperty(value = "面料成分")
    private String ingredient;

    /** 面料规格code */
    @ApiModelProperty(value = "面料规格code")
    private String translateCode;

    /** 面料规格 */
    @ApiModelProperty(value = "面料规格")
    private String translate;

    /** 供应商id */
    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    /** 供应商 */
    @ApiModelProperty(value = "供应商")
    private String supplierName;

    /** 单位 */
    @ApiModelProperty(value = "单位")
    private String unitCode;

    /* ----------------------------代码获取---------------------------- */

    /** 颜色 */
    @ApiModelProperty(value = "颜色")
    private String color;

    /** 颜色code */
    @ApiModelProperty(value = "颜色code")
    private String colorCode;

    /** 月份数据 */
    @ApiModelProperty(value = "月份数据")
    @JsonIgnore
    private Map<ProductionType, List<FabricMonthDataDto>> monthData;

    /** 年份数组 */
    @ApiModelProperty(value = "年份数组")
    @JsonIgnore
    private int[] yearArray;
    /** cmt单位 */
    @ApiModelProperty(value = "cmt单位")
    private UnitConverterEnum cmtUnit;
    /** fob单位 */
    @ApiModelProperty(value = "fob单位")
    private UnitConverterEnum fobUnit;

    public List<String> getYears() {
        return Arrays.stream(yearArray).mapToObj(it -> it + "年").collect(Collectors.toList());
    }

    @Override
    public Map<Object, Object> decorateWebMap() {
        Map<Object, Object> map = super.decorateWebMap();
        Map<ProductionType, List<List<BigDecimal>>> result = new HashMap<>();
        this.monthData.forEach(((productionType, monthDataList) -> {
            ProductionType crudeProductionType = (productionType == ProductionType.CMT ? productionType : ProductionType.FOB);
            List<List<BigDecimal>> list = result.getOrDefault(crudeProductionType, new ArrayList<>());
            AtomicInteger yearIndexAtomic = new AtomicInteger();
            monthDataList.stream().sorted(Comparator.comparing(FabricMonthDataDto::getStartMonth)).collect(CommonUtils.groupingBy(it -> it.getStartMonth().getYear())).forEach((year, sameYearList) -> {
                int yearIndex = yearIndexAtomic.getAndIncrement();
                if (list.size() <= yearIndex) {
                    list.add(new ArrayList<>());
                }
                List<BigDecimal> decimalList = list.get(yearIndex);
                sameYearList.forEach(data -> {
                    decimalList.add(findUnit(crudeProductionType).calculate(data.getProduction()));
                });
            });
            result.put(crudeProductionType, list);
        }));
        map.putAll(result);
        return map;
    }

    @Override
    public ReplayRatingType getType() {
        return ReplayRatingType.FABRIC;
    }

    public UnitConverterEnum findUnit(ProductionType productionType) {
        return productionType == ProductionType.CMT ? cmtUnit : fobUnit;
    }

    @Override
    public String getForeignId() {
        return getMaterialId();
    }

    @Override
    public String getCode() {
        return getMaterialCode();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FabricMonthDataDto {
        /** 开始时间 */
        @ApiModelProperty(value = "开始时间")
        private YearMonth startMonth;

        /** 结束时间 */
        @ApiModelProperty(value = "结束时间")
        private YearMonth endMonth;

        /** 投产量 */
        @ApiModelProperty(value = "投产量")
        private BigDecimal production;
    }

}
