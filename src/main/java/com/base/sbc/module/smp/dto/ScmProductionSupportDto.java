package com.base.sbc.module.smp.dto;

import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonAnyGetter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@JsonIgnoreType
public class ScmProductionSupportDto {
    
    /** 渠道名称 */
    private String channelTypeName;
    
    /** 尺码组合 */
    private Map<OrderBookChannelType, Map<String, String>> allSizeMap;

    /** 渠道类型 */
    public List<OrderBookChannelType> getChannelTypeList(){
        return Arrays.stream(OrderBookChannelType.values()).filter(it-> channelTypeName.contains(it.getText())).collect(Collectors.toList());
    }
    
    /** 是否单渠道 */
    public boolean isSingleChannel(){
        return getChannelTypeList().size() <= 1;
    }

    @JsonAnyGetter
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
