/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryItem;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
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
public class ReplayRatingPatternDTO extends ReplayRatingSaveDTO {

    /**
     * 款式 ID（t_style：id）
     */
    @ApiModelProperty("款式 ID")
    private String styleColorId;

    /**
     * 版型库id
     */
    @ApiModelProperty("版型库id")
    private String patternLibraryId;

    /**
     * 套版款号
     */
    @ApiModelProperty("套版款号")
    private String code;

    /**
     * 款式编码
     */
    @ApiModelProperty("款式编码")
    private String designNo;

    /**
     * 款式 ID（t_style：id）
     */
    @ApiModelProperty("款式 ID")
    private String styleId;

    /**
     * 热销大货款（多选逗号分隔，从当前款下获取）
     */
    @ApiModelProperty("热销大货款（多选逗号分隔，从当前款下获取）")
    private String styleNos;

    /**
     * 图片 ID
     */
    @ApiModelProperty("图片 ID")
    private String picId;

    /**
     * 图片 URL
     */
    @ApiModelProperty("图片 URL")
    private String picUrl;

    /**
     * 大类 code
     */
    @ApiModelProperty("大类 code")
    private String prodCategory1st;

    /**
     * 大类名称
     */
    @ApiModelProperty("大类名称")
    @JsonIgnore
    private String prodCategory1stName;

    /**
     * 品类 code
     */
    @ApiModelProperty("品类 code")
    private String prodCategory;

    /**
     * 品类名称
     */
    @ApiModelProperty("品类名称")
    @JsonIgnore
    private String prodCategoryName;

    /**
     * 中类 code
     */
    @ApiModelProperty("中类 code")
    private String prodCategory2nd;

    /**
     * 中类名称
     */
    @ApiModelProperty("中类名称")
    @JsonIgnore
    private String prodCategory2ndName;

    /**
     * 小类 code
     */
    @ApiModelProperty("小类 code")
    private String prodCategory3rd;

    /**
     * 小类名称
     */
    @ApiModelProperty("小类名称")
    @JsonIgnore
    private String prodCategory3rdName;
    /**
     * 廓形 code
     */
    @ApiModelProperty("廓形 code")
    private String silhouetteCode;

    ;
    /**
     * 廓形名称
     */
    @ApiModelProperty("廓形名称")
    private String silhouetteName;
    /**
     * 模板 code（t_pattern_library code）
     */
    @ApiModelProperty("模板 code")
    private String templateCode;
    /**
     * 模板名称（t_pattern_library name）
     */
    @ApiModelProperty("模板名称")
    private String templateName;
    /**
     * 部件库-所属版型库
     */
    @ApiModelProperty("部件库-所属版型库")
    private PatternLibraryTemplate patternLibraryTemplate;
    /**
     * 部件库-子表数据集合
     */
    @ApiModelProperty("部件库-子表数据集合")
    private List<PatternLibraryItem> patternLibraryItemList;
    /**
     * 部件库-子表围度数据
     */
    @ApiModelProperty("部件库-子表围度数据")
    private Map<String, String> patternLibraryItemPattern;
    /**
     * 部件库-子表长度数据
     */
    @ApiModelProperty("部件库-子表长度数据")
    private Map<String, String> patternLibraryItemLength;
    /** 使用款 */
    @ApiModelProperty(value = "使用款")
    private BigDecimal useCount;
    /** 投产款 */
    @ApiModelProperty(value = "投产款")
    private BigDecimal productionCount;
    /** 投产款 */
    @ApiModelProperty(value = "投产款")
    @JsonIgnore
    private ReplayConfigDetailDTO saleSeason;

    /**
     * 所属品类 大类/品类/中类/小类
     */
    public String getCategoryName() {
        return StrJoiner.of("/").setNullMode(StrJoiner.NullMode.IGNORE)
                .append(prodCategory1stName)
                .append(prodCategoryName)
                .append(prodCategory2ndName)
                .append(prodCategory3rdName)
                .toString();
    }

    /**
     * 可否改版
     */
    @ApiModelProperty("可否改版")
    public String getPatternType() {
        return Opt.ofNullable(patternLibraryTemplate).map(PatternLibraryTemplate::getPatternType).orElse("");
    }

    /**
     * 部件库-子表围度数据
     */
    @ApiModelProperty("部件库-子表围度数据")
    public Map<String, String> getPatternLibraryItemPattern() {
        if (MapUtil.isEmpty(patternLibraryItemPattern)) {
            patternLibraryItemPattern = decoratePatternLibrary(1);
        }
        return patternLibraryItemPattern;
    }

    /**
     * 部件库-子表长度数据
     */
    @ApiModelProperty("部件库-子表长度数据")
    public Map<String, String> getPatternLibraryItemLength() {
        if (MapUtil.isEmpty(patternLibraryItemLength)) {
            patternLibraryItemLength = decoratePatternLibrary(2);
        }
        return patternLibraryItemLength;
    }

    private Map<String, String> decoratePatternLibrary(Integer type) {
        return CollUtil.removeWithAddIf(patternLibraryItemList, (it) -> it.getType().equals(type)).stream()
                .collect(CommonUtils.toMap(PatternLibraryItem::getName, PatternLibraryItem::getStructureValue));
    }

    /** 版型成功率 */
    @ApiModelProperty(value = "版型成功率")
    public String getPatternSuccessRate() {
        return BigDecimalUtil.dividePercentage(productionCount, useCount) + "%";
    }

    public List<String> getSaleSeasonList() {
        if (saleSeason == null) return new ArrayList<>();
        return saleSeason.getTitleList();
    }

    @Override
    public ReplayRatingType getType() {
        return ReplayRatingType.PATTERN;
    }

    @Override
    public String getForeignId() {
        return getStyleColorId();
    }

    @Override
    public String getCode() {
        return getDesignNo();
    }


}
