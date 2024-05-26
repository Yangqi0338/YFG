package com.base.sbc.module.smp.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.enums.business.PutInProductionType;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailAuditStatusEnum;
import com.base.sbc.config.enums.smp.StylePutIntoType;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class ScmProductionDto extends ScmProductionSupportDto {

    /** 订货本Id不能为空 */
    @NotBlank(message = "订货本Id不能为空")
    private String orderBookDetailId;

    /** 名称 */
    @JsonIgnore
    private String name;

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
    @NotNull(message = "商品要求货期不能为空")
    private Date deliveryAt;

    @JsonAnyGetter
    public Map<String, String> getSingleSizeMap() {
        return facMerge == YesOrNoEnum.YES ? new HashMap<>() : getSizeMap(null);
    }

    public Map<String, String> getSizeMap(OrderBookChannelType channelType) {
        Map<String, String> map = new HashMap<>(7);
        boolean isAll = channelType == null;
        for (Map.Entry<OrderBookChannelType, Map<String, String>> entry : getAllSizeMap().entrySet()) {
            OrderBookChannelType key = entry.getKey();
            Map<String, String> sizeMap = entry.getValue();
            if (isAll || channelType == key) {
                String fill = isAll ? "" : (channelType.ordinal() + 1) + "";
                sizeMap.forEach((sizeName,value)-> {
                    String realSizeName = sizeName + fill;
                    BigDecimal defaultValue = BigDecimal.ZERO;
                    BigDecimal num = NumberUtil.isNumber(value) ? new BigDecimal(value) : defaultValue;
                    String sum = map.getOrDefault(realSizeName, defaultValue.toString());
                    map.put(realSizeName, new BigDecimal(sum).add(num).toString());
                });
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
        return Opt.ofNullable(this.getDevtType()).orElse(PutInProductionType.CMT).getText();
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
    private YesOrNoEnum facMerge;

    public void setFacMerge(YesOrNoEnum facMerge) {
        this.facMerge = facMerge == null ? (isSingleChannel() ? YesOrNoEnum.NO : YesOrNoEnum.YES) : facMerge;
    }

    public String getFacMerge(){
        return facMerge.getName();
    }

    @JsonAnyGetter
    public Map<String, String> getOnlineSizeMap() {
        return facMerge == YesOrNoEnum.NO ? new HashMap<>() : getSizeMap(OrderBookChannelType.ONLINE);

    }

    @JsonAnyGetter
    public Map<String, String> getOfflineSizeMap() {
        return facMerge == YesOrNoEnum.NO ? new HashMap<>() : getSizeMap(OrderBookChannelType.OFFLINE);
    }

    /** 工号 */
    private String loginName;

    /** 生产编号 */
    private String orderNo;

    public static Map<Function<ScmProductionDto, String>, BiConsumer<ScmProductionDto, String>> findUserIdFuncList() {
        return MapUtil.ofEntries(
                MapUtil.entry(ScmProductionDto::getLoginName, ScmProductionDto::setLoginName),
                MapUtil.entry(ScmProductionDto::getChargerName, ScmProductionDto::setChargerName),
                MapUtil.entry(ScmProductionDto::getReviewerName, ScmProductionDto::setReviewerName),
                MapUtil.entry(ScmProductionDto::getPreparedByName, ScmProductionDto::setPreparedByName)
        );
    }

    public List<String> findUserIdList() {
        return ScmProductionDto.findUserIdFuncList().keySet().stream().map(it-> it.apply(this)).collect(Collectors.toList());
    }

    public void decorateUserId(List<UserCompany> userCompanyList) {
        if (CollUtil.isEmpty(userCompanyList)) return;
        ScmProductionDto.findUserIdFuncList().forEach((getFunc,setFunc)-> {
            String id = getFunc.apply(this);
            userCompanyList.stream().filter(it-> it.getId().equals(id)).findFirst().ifPresent(userCompany -> {
                setFunc.accept(this, userCompany.getUserId());
            });
        });
    }
}
