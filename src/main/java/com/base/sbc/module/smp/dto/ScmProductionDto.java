package com.base.sbc.module.smp.dto;

import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailAuditStatusEnum;
import com.base.sbc.config.enums.smp.StylePutIntoType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ScmProductionDto extends ScmProductionSupportDto {

    /** 订货本Id不能为空 */
    @NotBlank(message = "订货本Id不能为空")
    private String orderBookDetailId;

    /** 紧急度 */
    @NotBlank(message = "紧急度不能为空")
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotBlank(message = "商品要求货期不能为空")
    private Date deliveryAt;

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

    /** 下单类型 */
    public String getPlaceOrderType(){
        return this.getPlaceOrderTypeCode().getText();
    };
    
    /** 投产类型 */
    public String getProductType(){
        ProductionType devtType = this.getDevtType();
        return devtType == ProductionType.CMT ? devtType.getText() : ProductionType.FOB.getText();
    };

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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date placeOrderAt;

    /** 审核人 */
    private String reviewerName;

    /** 审核日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date reviewerAt;
    
    /** 负责人 */
    private String chargerName;
    
    /** 参考单号 */
    private String referenceNo;
    
    /** 主料要求货期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date mainMaterialDeliveryAt;
    
    /** 辅料要求获取 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date assistMaterialDeliveryAt;
    
    /** 行号 */
    private String bussinessRow;
    
    /** 销售分类 */
    @NotBlank(message = "销售分类不能为空")
    private String saleTypeId;
    
    /** 预算号 */
    @NotBlank(message = "预算号不能为空")
    private String budgetNo;
    
    /** 生产模式 */
    private String productionModels;
    
    /** 集货单号 */
    private String shippingAreaNo;
    
    /** 是否投产合并 */
    public String getFacMerge(){
        return isSingleChannel() ? YesOrNoEnum.NO.getName() : YesOrNoEnum.YES.getName();
    };

    @JsonAnyGetter
    public Map<String, String> getOnlineSizeMap() {
        return getSizeMap(OrderBookChannelType.ONLINE);
    }

    @JsonAnyGetter
    public Map<String, String> getOfflineSizeMap() {
        return getSizeMap(OrderBookChannelType.OFFLINE);
    }

    /** 工号 */
    private String loginName;
}
