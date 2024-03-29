/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Supplier2;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.hangtag.entity.HangTagIngredient;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * 类描述：吊牌表 实体类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.entity.HangTag
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MoreLanguageHangTagVO extends HangTagVO {

    private final Map<String, MoreLanguageCodeMapping<?>> baseCodeMapping = MapUtil.ofEntries(
            MapUtil.entry("DP02", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getSaftyTypeCode, MoreLanguageHangTagVO::getSaftyType)),

            MapUtil.entry("DP03", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getExecuteStandardCode, MoreLanguageHangTagVO::getExecuteStandard)),
            MapUtil.entry("DP04", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getBulkStyleNo, MoreLanguageHangTagVO::getBulkStyleNo)),
            MapUtil.entry("DP05", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getProductCode, MoreLanguageHangTagVO::getProductName)),

            MapUtil.entry("DP06", new MoreLanguageCodeMapping<>(ModelType::getUniqueCode, ModelType::getName).setListFunc(MoreLanguageHangTagVO::getSizeList)),

            MapUtil.entry("DP07", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getColorCode, MoreLanguageHangTagVO::getColor)),

            MapUtil.entry("DP09", new MoreLanguageCodeMapping<>(HangTagIngredient::getIngredientCode, HangTagIngredient::getIngredientName).setListFunc(MoreLanguageHangTagVO::getIngredientList)),
            MapUtil.entry("DP10", new MoreLanguageCodeMapping<>(HangTagIngredient::getIngredientDescriptionCode, HangTagIngredient::getIngredientDescription).setListFunc(MoreLanguageHangTagVO::getIngredientList)),
            MapUtil.entry("DP11", new MoreLanguageCodeMapping<>(HangTagIngredient::getTypeCode, HangTagIngredient::getType).setListFunc(MoreLanguageHangTagVO::getIngredientList)),
            MapUtil.entry("DP13", new MoreLanguageCodeMapping<>(HangTagIngredient::getIngredientSecondCode, HangTagIngredient::getIngredientSecondName).setListFunc(MoreLanguageHangTagVO::getIngredientList)),

            MapUtil.entry("DP12", new MoreLanguageCodeMapping<>(ModelType::getUniqueCode, ModelType::getName).setListFunc(MoreLanguageHangTagVO::getSizeList).setSearchStandardColumnCode("DP06")),
            MapUtil.entry("DP16", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getBulkStyleNo, MoreLanguageHangTagVO::getBulkStyleNo)),

            MapUtil.entry("XM01", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getWarmTipsCode, MoreLanguageHangTagVO::getWarmTips)),
            MapUtil.entry("XM02", new MoreLanguageCodeMapping<>(HangTagIngredient::getIngredientCode, HangTagIngredient::getIngredientName).setListFunc(MoreLanguageHangTagVO::getIngredientList)),
            MapUtil.entry("XM03", new MoreLanguageCodeMapping<>(HangTagIngredient::getIngredientDescriptionCode, HangTagIngredient::getIngredientDescription).setListFunc(MoreLanguageHangTagVO::getIngredientList)),
            MapUtil.entry("XM04", new MoreLanguageCodeMapping<>(HangTagIngredient::getTypeCode, HangTagIngredient::getType).setListFunc(MoreLanguageHangTagVO::getIngredientList)),
            MapUtil.entry("XM06", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getWashingLabelName, MoreLanguageHangTagVO::getWashingLabelName)),
            MapUtil.entry("XM07", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getStorageDemand, MoreLanguageHangTagVO::getStorageDemandName)),
            MapUtil.entry("XM08", new MoreLanguageCodeMapping<>(ModelType::getUniqueCode, ModelType::getName).setListFunc(MoreLanguageHangTagVO::getSizeList)),
            MapUtil.entry("XM09", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getColorCode, MoreLanguageHangTagVO::getColor)),
            MapUtil.entry("XM10", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getBulkStyleNo, MoreLanguageHangTagVO::getBulkStyleNo)),
            MapUtil.entry("XM11", new MoreLanguageCodeMapping<>(HangTagIngredient::getIngredientSecondCode, HangTagIngredient::getIngredientSecondName).setListFunc(MoreLanguageHangTagVO::getIngredientList))
    );

    @Data
    @AllArgsConstructor
    public static class HangTagMoreLanguageGroup {
        private String standColumnCode;
        private String standColumnName;
        private Function<MoreLanguageHangTagVO, String> content;

        private String separator = MoreLanguageProperties.multiSeparator;

        public HangTagMoreLanguageGroup(Function<MoreLanguageHangTagVO, String> content) {
            this.content = content;
        }

        public HangTagMoreLanguageGroup(String standColumnCode, String standColumnName, Function<MoreLanguageHangTagVO, String> content) {
            this.standColumnCode = standColumnCode;
            this.content = content;
            this.standColumnName = standColumnName;
        }
    }

    private List<HangTagIngredient> ingredientList;

    private List<ModelType> sizeList;

    public void setSizeList(List<BasicsdatumModelType> modelTypeList) {
        // 分装新的modelType以适配业务
        this.sizeList = modelTypeList.stream().flatMap(it-> {
            // 切割名字和编码
            String[] sizeArray = it.getSize().split(COMMA);
            List<ModelType> modelTypes = new ArrayList<>();
            String[] sizeCodeArray = it.getSizeCode().split(COMMA);
            for (int i = 0, splitLength = sizeCodeArray.length; i < splitLength; i++) {
                String sizeCode = sizeCodeArray[i];
                modelTypes.add(new ModelType(it.getCode(), sizeCode, sizeArray[i]));
            }
            return modelTypes.stream();
        }).collect(Collectors.toList());
    }

    public List<MoreLanguageHangTagVO> getMySelfList(){
        return CollUtil.toList(this);
    }


    @Getter
    public class MoreLanguageCodeMapping<K> extends Pair<Function<K, String>, Function<K, String>> {
        private Function<MoreLanguageHangTagVO, List<K>> listFunc = (moreLanguageHangTagVO)-> (List<K>) moreLanguageHangTagVO.getMySelfList();
        private String searchStandardColumnCode;

        public MoreLanguageCodeMapping<K> setListFunc(Function<MoreLanguageHangTagVO, List<K>> listFunc) {
            this.listFunc = listFunc;
            return this;
        }

        public MoreLanguageCodeMapping<K> setSearchStandardColumnCode(String searchStandardColumnCode) {
            this.searchStandardColumnCode = searchStandardColumnCode;
            return this;
        }

        public MoreLanguageCodeMapping(Function<K, String> key, Function<K, String> value) {
            super(key, value);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ModelType {
        private String modelTypeCode;

        private String code;

        private String name;

        public String getUniqueCode(){
            return this.getCode() + "-" + this.getModelTypeCode();
        }
    }
}

