package com.base.sbc.config.constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import com.base.sbc.config.enums.business.RFIDType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
@ConfigurationProperties("rfid")
public class RFIDProperties {

    public static String materialName = "RFID";

    public static List<RFIDFilter> filterList = CollUtil.list(false,new RFIDFilter());
    public static Map<String, RFIDType> categoryRfidMapping = MapUtil.ofEntries(
            MapUtil.entry("FBF",RFIDType.WASHING),
            MapUtil.entry("FBE", RFIDType.HANG_TAG)
    );

    public void setMaterialName(String materialName) {
        RFIDProperties.materialName = materialName;
    }

    public void setCategoryRfidMapping(Map<String, RFIDType> categoryRfidMapping) {
        RFIDProperties.categoryRfidMapping = categoryRfidMapping;
    }

    public void setFilterList(List<RFIDFilter> filterList) {
        RFIDProperties.filterList = filterList;
    }

    @Data
    public static class RFIDFilter{
        private String categoryCode = "1,6,7,8,9,0,2,3,4,5";
        private Integer year = 2024;
        private String brand = "MM";

        public boolean check(String year, String brandName, String categoryCode){
            return (NumberUtil.isNumber(year) && Integer.parseInt(year) >= this.year)
                    &&
                    this.categoryCode.contains(categoryCode)
                    &&
                    this.brand.equals(brandName)
                    ;
        }
    }
}
