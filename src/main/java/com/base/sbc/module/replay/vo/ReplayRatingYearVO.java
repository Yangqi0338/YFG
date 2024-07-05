/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.module.replay.dto.ReplayRatingYearProductionSaleDTO;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel("基础资料-复盘评分年份VO ReplayRatingYearVO")
public class ReplayRatingYearVO {

    private String id;

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

    /** 款图 */
    @ApiModelProperty(value = "款图")
    private String stylePic;

    /** 产品季id */
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    /** 产品季名称 */
    @ApiModelProperty(value = "产品季名称")
    @JsonIgnore
    private String planningSeasonName;

    /** 波段code */
    @ApiModelProperty(value = "波段code")
    private String bandCode;

    /** 波段名称 */
    @ApiModelProperty(value = "波段名称")
    @JsonIgnore
    private String bandName;

    /** 款式id */
    @ApiModelProperty(value = "款式id")
    private String styleId;

    /** 大类code */
    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;

    /** 大类name */
    @ApiModelProperty(value = "大类name")
    @JsonIgnore
    private String prodCategory1stName;

    /** 品类code */
    @ApiModelProperty(value = "品类code")
    private String prodCategory;

    /** 品类name */
    @ApiModelProperty(value = "品类name")
    @JsonIgnore
    private String prodCategoryName;

    /** 中类code */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;

    /** 中类name */
    @ApiModelProperty(value = "中类name")
    @JsonIgnore
    private String prodCategory2ndName;

    /** 小类code */
    @ApiModelProperty(value = "小类code")
    private String prodCategory3rd;

    /** 小类code */
    @ApiModelProperty(value = "小类code")
    @JsonIgnore
    private String prodCategory3rdName;

    /** 廓形code */
    @ApiModelProperty(value = "廓形code")
    private String silhouetteCode;

    /** 廓形 */
    @ApiModelProperty(value = "廓形")
    @JsonIgnore
    private String silhouetteName;
    /**
     * 搭配名称
     */
    @ApiModelProperty(value = "搭配编码")
    private String collocationCode;
    /**
     * 搭配编码
     */
    @ApiModelProperty(value = "搭配名称")
    private String collocationName;
    /** 年份投产汇总数据 */
    @ApiModelProperty(value = "年份投产汇总数据")
    @JsonIgnore
    private ReplayRatingYearProductionSaleDTO replayRatingYearProductionSaleDTO;
    /** 销售季标签 */
    @ApiModelProperty(value = "销售季标签")
    private YesOrNoEnum seasonFlag;
    /** 年份数据 */
    @ApiModelProperty(value = "年份数据")
    private List<String> yearList = new ArrayList<>();

    public String getCategoryName() {
        return categoryNameJoiner().toString();
    }

    protected StrJoiner categoryNameJoiner() {
        return StrJoiner.of("\n").setNullMode(StrJoiner.NullMode.IGNORE).append(planningSeasonName).append(bandName).append(
                StrJoiner.of("/").append(prodCategory1stName).append(prodCategoryName).append(prodCategory2ndName).append(prodCategory3rdName)
        ).append(silhouetteName);
    }

    @JsonAnyGetter
    public Map<String, Object> decorateWebMap() {
        return seasonFlag == YesOrNoEnum.NO
                ? handlerYearProductionSaleChildren(replayRatingYearProductionSaleDTO)
                : handlerSeasonProductionSaleChildren(handlerSeasonProductionSale(replayRatingYearProductionSaleDTO));
    }

    private ReplayRatingYearProductionSaleDTO handlerSeasonProductionSale(ReplayRatingYearProductionSaleDTO productionSaleDTO) {
        ReplayRatingYearProductionSaleDTO yearProductionSaleDTO = BeanUtil.copyProperties(productionSaleDTO, ReplayRatingYearProductionSaleDTO.class);

        List<ReplayRatingYearProductionSaleDTO> childrenList = new ArrayList<>();
        yearProductionSaleDTO.setChildrenList(childrenList);
        productionSaleDTO.getChildrenList().stream()
                .flatMap(it -> it.getChildrenList().stream())
                .collect(Collectors.groupingBy(ReplayRatingYearProductionSaleDTO::getKey))
                .forEach((seasonKey, sameKeyList) -> {
                    ReplayRatingYearProductionSaleDTO seasonProductionSaleDTO = new ReplayRatingYearProductionSaleDTO();
                    childrenList.add(seasonProductionSaleDTO);
                    seasonProductionSaleDTO.setChildrenList(sameKeyList);
                    seasonProductionSaleDTO.setKey(seasonKey);
                    seasonProductionSaleDTO.calculate();
                    seasonProductionSaleDTO.setChildrenList(null);
                });

        productionSaleDTO.getChildrenList().add(yearProductionSaleDTO);
        return productionSaleDTO;
    }

    private Map<String, Object> handlerSeasonProductionSaleChildren(ReplayRatingYearProductionSaleDTO productionSaleDTO) {
        Map<String, Object> map = new HashMap<>();
        Map<Object, ReplayRatingYearProductionSaleDTO> calculateMap = productionSaleDTO.findCalculateMap();
        calculateMap.put(productionSaleDTO.getKey(), productionSaleDTO);
        calculateMap.forEach((key, value) -> {
            Map<String, Object> childrenMap = handlerSeasonProductionSaleChildren(value);
            if (MapUtil.isEmpty(childrenMap)) {
                childrenMap = value.findMap();
            }
            childrenMap.forEach((childKey, childValue) -> {
                map.put(key + StrUtil.upperFirst(childKey), childValue);
            });
        });

        return map;
    }

    private Map<String, Object> handlerYearProductionSaleChildren(ReplayRatingYearProductionSaleDTO productionSaleDTO) {
        Map<String, Object> map = new HashMap<>();
        Map<Object, ReplayRatingYearProductionSaleDTO> calculateMap = productionSaleDTO.findCalculateMap();
        calculateMap.put(productionSaleDTO.getKey(), productionSaleDTO);
        calculateMap.forEach((key, value) -> {
            value.findMap().forEach((childKey, childValue) -> {
                map.put(key + StrUtil.upperFirst(childKey), childValue);
            });
        });
        return map;
    }

}
