package com.base.sbc.module.smp.dto;

import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailAuditStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonAnyGetter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ScmProductionDto extends ScmProductionSupportDto {

    /** 紧急度 */
    private String emergencyDegree;

    /** 供应商名称 */
    private String materielSupplierName;
    
    /** 下单品牌 */
    private String brand;
    
    /** 年份 */
    private String year;
    
    /** 季节 */
    private String season;
    
    /** 商品款号 */
    private String styleNo;
    
    /** 颜色 */
    private String color;
    
    /** 商品要求货期 */
    private String deliveryAt;
    
    /** 下单类型 */
    private String placeOrderType;
    
    /** 投产类型 */
    private String productType;
    
    /** 成衣备料单号 */
    private String readyMadePrepareBillNo;
    
    /** 备注 */
    private String remark;
    
    /** 币种 */
    private String currency;
    
    /** 制单人 */
    private String preparedByName;
    
    /** 制单人日期 */
    @JsonProperty("placeorderAt")
    private String placeOrderAt;
    
    /** 负责人 */
    private String chargerName;
    
    /** 参考单号 */
    private String referenceNo;
    
    /** 主料要求货期 */
    private String mainMaterialDeliveryAt;
    
    /** 辅料要求获取 */
    private String assistMaterialDeliveryAt;
    
    /** 行号 */
    private String bussinessRow;
    
    /** 销售分类 */
    private String saleTypeId;
    
    /** 预算号 */
    private String budgetNo;
    
    /** 生产模式 */
    private String productionModels;
    
    /** 集货单号 */
    private String shippingAreaNo;
    
    /** 是否投产合并 */
    private String facMerge;

    /** 工号 */
    private String loginName;

    /** 审核日期 */
    private String reviewerAt;

    /** 审核人 */
    private String reviewerName;

    @JsonAnyGetter
    public Map<String, String> getSingleSizeMap() {
        Map<String, String> map = new HashMap<>(7);
        if (isSingleChannel()) {
            for (Map.Entry<OrderBookChannelType, Map<String, String>> entry : getAllSizeMap().entrySet()) {
                map = entry.getValue();
                break;
            }
        }
        return map;
    }

    @JsonAnyGetter
    public Map<String, String> getOnlineSizeMap() {
        return getSizeMap(OrderBookChannelType.ONLINE);
    }

    @JsonAnyGetter
    public Map<String, String> getOfflineSizeMap() {
        return getSizeMap(OrderBookChannelType.OFFLINE);
    }
}
