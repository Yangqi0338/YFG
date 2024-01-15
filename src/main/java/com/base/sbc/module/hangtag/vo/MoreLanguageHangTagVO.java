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
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.hangtag.entity.HangTagIngredient;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

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
    private final Map<String, MoreLanguageCodeMapping<?, String>> baseCodeMapping = MapUtil.ofEntries(
            MapUtil.entry("DP02", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getSaftyTypeCode, MoreLanguageHangTagVO::getSaftyType)),

            MapUtil.entry("DP03", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getExecuteStandardCode, MoreLanguageHangTagVO::getExecuteStandard)),
            MapUtil.entry("DP04", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getBulkStyleNo, MoreLanguageHangTagVO::getBulkStyleNo)),
            MapUtil.entry("DP05", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getProductCode, MoreLanguageHangTagVO::getProductName)),

            MapUtil.entry("DP06", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getColorCode, MoreLanguageHangTagVO::getColor)),

            MapUtil.entry("DP07", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getColorCode, MoreLanguageHangTagVO::getColor)),
//            MapUtil.entry("DP09", new MoreLanguageCodeMapping<>(HangTagIngredient::getIngredientCode, HangTagIngredient::getIngredientName)),
//            MapUtil.entry("DP10", new MoreLanguageCodeMapping<>(HangTagIngredient::getIngredientDescriptionCode, HangTagIngredient::getIngredientDescription)),
//            MapUtil.entry("DP11", new MoreLanguageCodeMapping<>(HangTagIngredient::getTypeCode, HangTagIngredient::getType)),

            MapUtil.entry("DP12", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getDownContent, MoreLanguageHangTagVO::getDownContent)),

//            MapUtil.entry("DP13", new MoreLanguageCodeMapping<>(HangTagIngredient::getIngredientSecondCode, HangTagIngredient::getIngredientSecondName)),
            MapUtil.entry("XM01", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getWarmTipsCode, MoreLanguageHangTagVO::getWarmTips)),
            MapUtil.entry("XM06", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getWashingLabelName, MoreLanguageHangTagVO::getWashingLabelName)),
            MapUtil.entry("XM07", new MoreLanguageCodeMapping<>(MoreLanguageHangTagVO::getStorageDemand, MoreLanguageHangTagVO::getStorageDemandName))
    );

    private final Map<String, HangTagMoreLanguageGroup> baseCodeGroupMapping = MapUtil.ofEntries(
            MapUtil.entry("DP11", new HangTagMoreLanguageGroup("成分信息", 2, MoreLanguageHangTagVO::getIngredientList)),
            MapUtil.entry("DP13", new HangTagMoreLanguageGroup("成分信息", 3, MoreLanguageHangTagVO::getIngredientList)),
            MapUtil.entry("DP09", new HangTagMoreLanguageGroup("成分信息", 1, MoreLanguageHangTagVO::getIngredientList)),
            MapUtil.entry("DP10", new HangTagMoreLanguageGroup("成分信息", 3, MoreLanguageHangTagVO::getIngredientList)),
            MapUtil.entry("DP06", new HangTagMoreLanguageGroup("DP06", 1, MoreLanguageHangTagVO::getSizeList)),
            MapUtil.entry("DP12", new HangTagMoreLanguageGroup("DP12", 1,  MoreLanguageHangTagVO::getDownContentList))
    );

    @Data
    @AllArgsConstructor
    public static class HangTagMoreLanguageGroup {
        private String groupName;
        private String content = "";
        private Integer index;
        private Function<MoreLanguageHangTagVO, List<?>> list;

        private String separator = "\n";

        public HangTagMoreLanguageGroup(String groupName, Integer index, Function<MoreLanguageHangTagVO, List<?>> list) {
            this.groupName = groupName;
            this.index = index;
            this.list = list;
        }

    }

    private List<HangTagIngredient> ingredientList;
    private List<BasicsdatumModelType> sizeList;
    private List<HangTagIngredient> downContentList;
    public List<MoreLanguageHangTagVO> getMySelfList(){
        return CollUtil.toList(this);
    }

    public class MoreLanguageCodeMapping<K,V> extends Pair<Function<K, V>, Function<K, V>> {

        public MoreLanguageCodeMapping(Function<K, V> key, Function<K, V> value) {
            super(key, value);
        }
    }
}

