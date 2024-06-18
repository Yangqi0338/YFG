/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import com.base.sbc.config.enums.business.replay.ReplayRatingLevelEnum;
import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    /** 销售等级 */
    @ApiModelProperty(value = "销售等级")
    private List<SaleLevelDTO> saleLevelList;
    /** 销售等级周 */
    @ApiModelProperty(value = "销售等级周")
    private List<String> saleLevelWeekends;
    /** 生产销售 */
    @ApiModelProperty(value = "生产销售")
    private List<ProductionSaleDTO> productionSaleList;

    @Override
    public ReplayRatingType getType() {
        return ReplayRatingType.STYLE;
    }

    @Data
    public static class SaleLevelDTO {

        /** 数据类型 */
        @ApiModelProperty(value = "数据类型")
        private String type;

        /** 周数据 */
        @ApiModelProperty(value = "周数据")
        private Map<String, String> weekendDataMap;
        /** 企划等级 */
        @ApiModelProperty(value = "企划等级")
        private ReplayRatingLevelEnum planningLevel;
        /** 季节等级 | 合计 */
        @ApiModelProperty(value = "季节等级 | 合计")
        private String seasonLevel;

        @JsonAnyGetter
        public Map<String, String> getWeekendDataMap() {
            return weekendDataMap;
        }

    }

    @Data
    public static class ProductionSaleDTO {

        /** 尺码Code */
        @ApiModelProperty(value = "尺码Code")
        private String sizeCode;

        /** 尺码 */
        @ApiModelProperty(value = "尺码")
        private String sizeName;

        /** 投产数 */
        @ApiModelProperty(value = "投产数")
        private BigDecimal production;

        /** 投产数 */
        @ApiModelProperty(value = "投产数")
        private String productionCount;

        /** 投产数 */
        @ApiModelProperty(value = "投产数")
        private BigDecimal sale;
        /** 库存款 */
        @ApiModelProperty(value = "库存款")
        private BigDecimal storageCount;

        /** 产销比 */
        @ApiModelProperty(value = "产销比")
        public BigDecimal getProductionSaleRate() {
            return BigDecimalUtil.div(production, sale);
        }

    }

}
