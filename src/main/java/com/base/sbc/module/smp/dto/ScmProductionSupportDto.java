package com.base.sbc.module.smp.dto;

import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.enums.smp.StylePutIntoType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ScmProductionSupportDto {

    /** 下单类型 */
    @NotBlank(message = "下单类型不能为空")
    @JsonIgnore
    private StylePutIntoType placeOrderTypeCode;

    /** 投产类型 */
    @JsonIgnore
    @NotBlank(message = "投产类型不能为空")
    private ProductionType devtType;
    
    /** 渠道名称 */
    @JsonIgnore
    private String channelName;
    
    /** 尺码组合 */
    @JsonIgnore
    private Map<OrderBookChannelType, Map<String, String>> allSizeMap = new HashMap<>();

    /** 渠道类型 */
    @JsonIgnore
    public List<OrderBookChannelType> getChannelTypeList(){
        return Arrays.stream(OrderBookChannelType.values()).filter(it-> channelName.contains(it.getText())).collect(Collectors.toList());
    }
    
    /** 是否单渠道 */
    @JsonIgnore
    public boolean isSingleChannel(){
        return getChannelTypeList().size() <= 1;
    }

    public Map<String, String> getSizeMap(OrderBookChannelType channelType) {
        Map<String, String> map = new HashMap<>(7);
        if (!isSingleChannel()) {
            Map<String, String> sizeMap = getAllSizeMap().getOrDefault(channelType, new HashMap<>());
            sizeMap.forEach((key,value)-> {
                map.put(key + (channelType.ordinal() + 1), value);
            });
        }
        return map;
    }
}
