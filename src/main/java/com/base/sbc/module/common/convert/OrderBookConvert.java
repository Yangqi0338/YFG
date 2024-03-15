package com.base.sbc.module.common.convert;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.base.sbc.config.enums.business.StylePutIntoType;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.module.orderbook.vo.OrderBookSimilarStyleVo;
import com.base.sbc.module.orderbook.vo.StyleSaleIntoDto;
import jdk.jfr.Name;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code 描述：吊牌模块通用MapStruct转化}
 * @author KC
 * @since 2024/1/9
 * @CopyRight @ 广州尚捷科技有限公司
 */
@Mapper(uses = {BaseConvert.class}, imports = {BaseSpringConvert.class}, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderBookConvert {
    OrderBookConvert INSTANCE = Mappers.getMapper(OrderBookConvert.class);

    @Mappings({
            @Mapping(target = "bulkStyleNo", source = "PROD_CODE"),
            @Mapping(target = "correctValue", source = "SALE_TYPE"),
            @Mapping(target = "type", source = "ORDER_TYPE"),
            @Mapping(target = "channel", source = "CHANNEL_TYPE"),
            @Mapping(target = "sum", source = "SUM"),
    })
    StyleSaleIntoDto copy2StyleSaleInto(Map<String, Object> map);
    List<StyleSaleIntoDto> copyList2StyleSaleInto(List<Map<String, Object>> source);
    OrderBookSimilarStyleVo copy2SimilarStyleVo(StyleSaleIntoDto source);

    List<OrderBookSimilarStyleVo> copyList2SimilarStyleVo(List<Map<String, Object>> source);

    default boolean saleTypeToBool(String saleType) {
        return "是".equals(saleType);
    }

    default StylePutIntoType orderTypeToEnum(String saleType) {
        return Arrays.stream(StylePutIntoType.values()).filter(it-> it.getText().equals(saleType)).findFirst().orElse(null);
    }

    default OrderBookChannelType channelToEnum(String channel) {
        return Arrays.stream(OrderBookChannelType.values()).filter(it-> it.getText().equals(channel)).findFirst().orElse(null);
    }

    default Map<String,Double> map2SizeMap(Object source) {
        Map<String, Double> map = new HashMap<>();
        if (ObjectUtil.isNull(source)) return map;
        ((Map<String, Object>)source).forEach((key,value)-> {
            String valueStr = value.toString();
            if (NumberUtil.isDouble(valueStr)) {
                map.put(key,Double.valueOf(valueStr));
            }
        });
        return map;
    }

    default String obj2Str(Object obj) {
        return (String) obj;
    }



}
