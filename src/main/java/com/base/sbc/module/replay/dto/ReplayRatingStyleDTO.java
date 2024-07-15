/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrJoiner;
import com.base.sbc.config.enums.business.replay.ReplayRatingLevelType;
import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import com.base.sbc.config.enums.business.smp.SluggishSaleWeekendsType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ReplayRatingStyleDTO extends ReplayRatingSaveDTO {
    /** 品牌 */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /** 品牌名 */
    @ApiModelProperty(value = "品牌名")
    private String brandName;
    /** 配色Id */
    @ApiModelProperty(value = "配色Id")
    private String styleColorId;
    /** 款号 */
    @ApiModelProperty(value = "款号")
    private String designNo;
    /** 大货款号 */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;
    /** 大货款图 */
    @ApiModelProperty(value = "大货款图")
    private String styleColorPic;
    /** 款式id */
    @ApiModelProperty(value = "款式id")
    private String styleId;
    /** 款式定位 */
    @ApiModelProperty(value = "款式定位")
    private String positioning;
    /** 款式定位名称 */
    @ApiModelProperty(value = "款式定位名称")
    private String positioningName;
    /** 款式风格 */
    @ApiModelProperty(value = "款式风格")
    private String styleFlavour;
    /** 款式风格名称 */
    @ApiModelProperty(value = "款式风格名称")
    private String styleFlavourName;
    /** 版型库Id */
    @ApiModelProperty(value = "版型库Id")
    private String registeringId;
    /** 版型库编码 */
    @ApiModelProperty(value = "版型库编码")
    private String registeringNo;
    /**
     * 廓形编码
     */
    @ApiModelProperty(value = "廓形编码")
    private String silhouetteCode;
    /**
     * 廓形
     */
    @ApiModelProperty(value = "廓形")
    private String silhouetteName;
    /**
     * 面料成分
     */
    @ApiModelProperty(value = "面料成分")
    private String fabricComposition;
    /**
     * 色系编码
     */
    @ApiModelProperty(value = "色系编码")
    private String colorSystemCode;
    /**
     * 色系
     */
    @ApiModelProperty(value = "色系")
    private String colorSystem;
    /**
     * 跳转版型大货款id
     */
    @ApiModelProperty(value = "跳转版型大货款id")
    private String gotoPatternId;
    /** 投产信息 */
    @ApiModelProperty(value = "投产信息")
    private List<ProductionInfoDTO> productionInfoList;
    /** 销售等级 */
    @ApiModelProperty(value = "销售等级")
    private List<SaleLevelDTO> saleLevelList;
    /** 销售等级周 */
    @ApiModelProperty(value = "销售等级周")
    private List<String> saleLevelWeekends;
    /** 生产销售 */
    @ApiModelProperty(value = "生产销售")
    private List<ProductionSaleDTO> productionSaleList;

    /**
     * 款式基础信息
     */
    @ApiModelProperty("款式基础信息")
    public String getStyleBasicInfo() {
        return StrJoiner.of("/")
                .setEmptyResult("未设置").setNullMode(StrJoiner.NullMode.IGNORE)
                .append(silhouetteName).append(fabricComposition).append(colorSystem)
                .toString();
    }

    /**
     * 款式基础信息
     */
    @ApiModelProperty("款式基础信息")
    public List<String> getSaleLevelYearList() {
        if (CollUtil.isEmpty(saleLevelList)) return new ArrayList<>();
        return saleLevelList.stream().map(SaleLevelDTO::getYear).distinct().collect(Collectors.toList());
    }

    @Override
    public ReplayRatingType getType() {
        return ReplayRatingType.STYLE;
    }

    @Override
    public String getCode() {
        return getBulkStyleNo();
    }

    @Override
    public String getForeignId() {
        return getStyleColorId();
    }

    @Data
    public static class SaleLevelDTO {

        /** 数据类型 */
        @ApiModelProperty(value = "数据类型")
        private ReplayRatingLevelType type;

        /** 年份 */
        @ApiModelProperty(value = "年份")
        private String year;

        /** 周数据 */
        @ApiModelProperty(value = "周数据")
        private Map<SluggishSaleWeekendsType, Object> weekendDataMap = new HashMap<>();


        @JsonAnyGetter
        public Map<SluggishSaleWeekendsType, Object> getWeekendDataMap() {
            return weekendDataMap;
        }

    }

    @Data
    public static class ProductionInfoDTO {

        /** 投产日期 */
        @ApiModelProperty(value = "投产日期")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private LocalDate date;

        /** 投产单号 */
        @ApiModelProperty(value = "投产单号")
        private String orderNo;

        /** 投产件数 */
        @ApiModelProperty(value = "投产件数")
        private BigDecimal production;

        /** 生产商列表 */
        @ApiModelProperty(value = "生产商列表")
        @JsonIgnore
        private List<SupplierInfo> supplierInfoList;

        /** 入库件数 */
        @ApiModelProperty(value = "入库件数")
        private BigDecimal storageCount;

        /** 生产商 */
        @ApiModelProperty(value = "生产商")
        public String getSupplierName() {
            return supplierInfoList.stream().map(SupplierInfo::getName).collect(Collectors.joining("/"));
        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SupplierInfo {

        /** 生产商id */
        @ApiModelProperty(value = "生产商id")
        private String id;

        /** 生产商name */
        @ApiModelProperty(value = "生产商name")
        private String name;

        @Override
        public String toString() {
            return "SupplierInfo{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
