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

    private static Object findIngredient;
    private final Map<String, MoreLanguageCodeMapping<?>> baseCodeMapping = MapUtil.ofEntries(
            MapUtil.entry("DP02", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getSaftyTypeCode)),

            MapUtil.entry("DP03", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getExecuteStandardCode)),
            MapUtil.entry("DP04", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getBulkStyleNo)),
            MapUtil.entry("DP05", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getProductCode)),

            MapUtil.entry("DP06", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getSizeList, ModelType::getUniqueCode)),

            MapUtil.entry("DP07", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getColorCode)),
            MapUtil.entry("DP09", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getIngredientList, HangTagIngredient::getIngredientCode)),
            MapUtil.entry("DP10", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getIngredientList, HangTagIngredient::getIngredientDescriptionCode)),
            MapUtil.entry("DP11", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getIngredientList, HangTagIngredient::getTypeCode)),
            MapUtil.entry("DP13", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getIngredientList, HangTagIngredient::getIngredientSecondCode)),

            MapUtil.entry("DP12", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getSizeList, ModelType::getUniqueCode)),

            MapUtil.entry("XM01", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getWarmTipsCode)),
            MapUtil.entry("XM06", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getWashingLabelName)),
            MapUtil.entry("XM07", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getStorageDemand))
    );

    private final Map<String, HangTagMoreLanguageGroup> baseCodeGroupMapping = MapUtil.ofEntries(
            MapUtil.entry("DP11", new HangTagMoreLanguageGroup("成分信息", MoreLanguageHangTagVO::getIngredient, 2)),
            MapUtil.entry("DP13", new HangTagMoreLanguageGroup("成分信息", MoreLanguageHangTagVO::getIngredient, 4)),
            MapUtil.entry("DP09", new HangTagMoreLanguageGroup("成分信息", MoreLanguageHangTagVO::getIngredient, 1)),
            MapUtil.entry("DP10", new HangTagMoreLanguageGroup("成分信息", MoreLanguageHangTagVO::getIngredient, 3)),
            MapUtil.entry("DP06", new HangTagMoreLanguageGroup("DP06", null, 1)),
            MapUtil.entry("DP12", new HangTagMoreLanguageGroup("DP12", MoreLanguageHangTagVO::getDownContent, 1))
    );

    @Data
    @AllArgsConstructor
    public static class HangTagMoreLanguageGroup {
        private String groupName;
        private Function<MoreLanguageHangTagVO, String> content;
        private Integer index;

        private String separator = "\n";

        public HangTagMoreLanguageGroup(String groupName, Function<MoreLanguageHangTagVO, String> content, Integer index) {
            this.groupName = groupName;
            this.content = content;
            this.index = index;
        }
    }

    private List<HangTagIngredient> ingredientList;

    private List<ModelType> sizeList;

    public void setSizeList(List<BasicsdatumModelType> modelTypeList) {
        this.sizeList = modelTypeList.stream().flatMap(it-> {
            String[] sizeArray = it.getSize().split(",");
            List<ModelType> modelTypes = new ArrayList<>();
            String[] sizeCodeArray = it.getSizeCode().split(",");
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

    public class MoreLanguageCodeMapping<K> extends Pair<Function<MoreLanguageHangTagVO, List<K>>, Function<K, String>> {

        public MoreLanguageCodeMapping(Function<K, String> value) {
            super(moreLanguageHangTagVO -> (List<K>) moreLanguageHangTagVO.getMySelfList(), value);
        }
        public MoreLanguageCodeMapping(Function<MoreLanguageHangTagVO, List<K>> key, Function<K, String> value) {
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

