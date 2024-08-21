package com.base.sbc.config.constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.exception.OtherException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@code 描述：生成工艺单属性}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/29
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("business")
public class BusinessProperties {

    public static String tablePrefix = "t_";
    public static Integer orderBookProductionInThreadLimit = 2;
    public static String topCategory = "A01,A02,A03";
    public static String bottomCategory = "A04";
    public static Map<String, List<String>> defectiveCheck = MapUtil.ofEntries(MapUtil.entry("packagingFormCode", CollUtil.newArrayList("-9")));

    public static Map<String, String> topCategoryStructure = MapUtil.ofEntries(MapUtil.entry("围度数据", "pattern"), MapUtil.entry("长度数据", "length"));
    public static Map<String, String> bottomCategoryStructure = MapUtil.ofEntries(MapUtil.entry("围度数据", "pattern2"), MapUtil.entry("长度数据", "length2"));

    public void setTablePrefix(String tablePrefix) {
        BusinessProperties.tablePrefix = tablePrefix;
    }

    public void setOrderBookProductionInThreadLimit(Integer orderBookProductionInThreadLimit) {
        BusinessProperties.orderBookProductionInThreadLimit = orderBookProductionInThreadLimit;
    }

    public void setDefectiveCheck(Map<String, List<String>> defectiveCheck) {
        BusinessProperties.defectiveCheck = defectiveCheck;
    }

    /* ----------------------------额外处理方法---------------------------- */
    public static String getStructureCode(String category, String structureKey) {
        if (topCategory.contains(category)) return topCategoryStructure.getOrDefault(structureKey, "");
        if (bottomCategory.contains(category)) return bottomCategoryStructure.getOrDefault(structureKey, "");
        throw new OtherException("仅允许处理上下装");
    }

    public void setTopCategory(String topCategory) {
        BusinessProperties.topCategory = topCategory;
    }

    public void setBottomCategory(String bottomCategory) {
        BusinessProperties.bottomCategory = bottomCategory;
    }

    public void setTopCategoryStructure(Map<String, String> topCategoryStructure) {
        BusinessProperties.topCategoryStructure = topCategoryStructure;
    }

    public void setBottomCategoryStructure(Map<String, String> bottomCategoryStructure) {
        BusinessProperties.bottomCategoryStructure = bottomCategoryStructure;
    }

    public static boolean needCheckByDefective(String styleNo, String key) {
        if (StrUtil.isBlank(styleNo)) return true;
        return defectiveCheck.getOrDefault(key, new ArrayList<>()).stream().anyMatch(styleNo::endsWith);
    }

}
