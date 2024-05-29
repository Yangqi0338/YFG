package com.base.sbc.module.smp.dto;

import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.enums.business.PutInProductionType;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.enums.smp.StylePutIntoType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ScmProductionSupportDto {

    /** 下单类型 */
    @JsonIgnore
    @NotNull(message = "下单类型不能为空")
    private StylePutIntoType placeOrderTypeCode;

    /** 投产类型 */
    @JsonIgnore
    @NotNull(message = "投产类型不能为空")
    private PutInProductionType devtType;
    
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
}
