package com.base.sbc.module.common.convert;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.enums.smp.StylePutIntoType;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.module.orderbook.entity.StyleSaleIntoCalculateResultType;
import com.base.sbc.module.orderbook.entity.StyleSaleIntoResultType;
import com.base.sbc.module.orderbook.vo.OrderBookSimilarStyleChannelVo;
import com.base.sbc.module.orderbook.vo.OrderBookSimilarStyleSizeMapVo;
import com.base.sbc.module.orderbook.vo.OrderBookSimilarStyleVo;
import com.base.sbc.module.orderbook.vo.StyleSaleIntoDto;
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
            @Mapping(target = "brand", source = "BRAND_NAME"),
            @Mapping(target = "sum", source = "SUM"),
            @Mapping(target = "resultType", source = "RESULTTYPE"),
    })
    StyleSaleIntoDto copy2StyleSaleInto(Map<String, Object> map);
    List<StyleSaleIntoDto> copyList2StyleSaleInto(List<Map<String, Object>> source);
    OrderBookSimilarStyleVo copy2SimilarStyleVo(StyleSaleIntoDto source);

    List<OrderBookSimilarStyleVo> copyList2SimilarStyleVo(List<Map<String, Object>> source);
    Map<StyleSaleIntoCalculateResultType, OrderBookSimilarStyleChannelVo> copyResultTypeMyself(Map<StyleSaleIntoCalculateResultType, OrderBookSimilarStyleChannelVo> source);

    OrderBookSimilarStyleChannelVo copyMyself(OrderBookSimilarStyleChannelVo source);
    Map<OrderBookChannelType, OrderBookSimilarStyleSizeMapVo> copyChannelMyself(Map<OrderBookChannelType, OrderBookSimilarStyleSizeMapVo> source);
    OrderBookSimilarStyleSizeMapVo copyMyself(OrderBookSimilarStyleSizeMapVo source);
    Map<String,Double> copyMyself(Map<String,Double> source);

    default Boolean saleTypeToBool(String saleType) {
        if (StrUtil.isBlank(saleType)) return null;
        return "是".equals(saleType);
    }

    default StylePutIntoType orderTypeToEnum(String saleType) {
        return Arrays.stream(StylePutIntoType.values()).filter(it-> it.getText().equals(saleType)).findFirst().orElse(null);
    }

    default OrderBookChannelType channelToEnum(String channel) {
        return Arrays.stream(OrderBookChannelType.values()).filter(it-> it.getText().equals(channel)).findFirst().orElse(null);
    }

    default StyleSaleIntoResultType resultTypeToEnum(String resultType) {
        return Arrays.stream(StyleSaleIntoResultType.values()).filter(it-> it.getText().equals(resultType)).findFirst().orElse(null);
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
