package com.base.sbc.module.common.convert;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.enums.smp.StylePutIntoType;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.module.orderbook.entity.StyleSaleIntoCalculateResultType;
import com.base.sbc.module.orderbook.entity.StyleSaleIntoResultType;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.base.sbc.module.orderbook.vo.OrderBookSimilarStyleChannelVo;
import com.base.sbc.module.orderbook.vo.OrderBookSimilarStyleSizeMapVo;
import com.base.sbc.module.orderbook.vo.OrderBookSimilarStyleVo;
import com.base.sbc.module.orderbook.vo.StyleSaleIntoDto;
import com.base.sbc.module.smp.dto.ScmProductionDto;
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
            @Mapping(target = "bulkStyleNo", source = "PROD_CODE", qualifiedByName = "obj2Str"),
            @Mapping(target = "correctValue", source = "SALE_TYPE", qualifiedByName = "obj2Str"),
            @Mapping(target = "type", source = "ORDER_TYPE", qualifiedByName = "obj2Str"),
            @Mapping(target = "channel", source = "CHANNEL_TYPE", qualifiedByName = "obj2Str"),
            @Mapping(target = "brand", source = "BRAND_NAME", qualifiedByName = "obj2Str"),
            @Mapping(target = "sum", source = "SUM", qualifiedByName = "obj2Str"),
            @Mapping(target = "resultType", source = "RESULTTYPE", qualifiedByName = "obj2Str"),
    })
    StyleSaleIntoDto copy2StyleSaleInto(Map<String, Object> map);
    List<StyleSaleIntoDto> copyList2StyleSaleInto(List<Map<String, Object>> source);
    OrderBookSimilarStyleVo copy2SimilarStyleVo(StyleSaleIntoDto source);

    List<OrderBookSimilarStyleVo> copyList2SimilarStyleVo(List<Map<String, Object>> source);
    Map<StyleSaleIntoCalculateResultType, OrderBookSimilarStyleChannelVo> copyResultTypeMyself(Map<StyleSaleIntoCalculateResultType, OrderBookSimilarStyleChannelVo> source);

    OrderBookSimilarStyleChannelVo copyMyself(OrderBookSimilarStyleChannelVo source);
    Map<OrderBookChannelType, OrderBookSimilarStyleSizeMapVo> copyChannelMyself(Map<OrderBookChannelType, OrderBookSimilarStyleSizeMapVo> source);
    OrderBookSimilarStyleSizeMapVo copyMyself(OrderBookSimilarStyleSizeMapVo source);
    @Mappings({
            @Mapping(target = "orderBookDetailId", source = "id"),
            @Mapping(target = "emergencyDegree", source = "productionUrgencyName"),
            @Mapping(target = "materielSupplierName", source = "fobClothingFactoryName"),
            @Mapping(target = "year", source = "yearName"),
            @Mapping(target = "season", source = "seasonName"),
            @Mapping(target = "styleNo", source = "bulkStyleNo"),
            @Mapping(target = "color", source = "colorCode"),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "placeOrderAt",source = "orderDate"),
            @Mapping(target = "preparedByName", source = "orderPerson"),
            @Mapping(target = "reviewerName", source = "updateId"),
            @Mapping(target = "reviewerAt", source = "updateDate"),
            @Mapping(target = "placeOrderTypeCode", source = "placeOrderType"),
    })
    ScmProductionDto copy2ProductionDto(OrderBookDetailVo source);

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

    @Named("obj2Str")
    default String obj2Str(Object obj) {
        return (String) obj;
    }

}
