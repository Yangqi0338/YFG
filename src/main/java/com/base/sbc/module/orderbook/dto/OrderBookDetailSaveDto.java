package com.base.sbc.module.orderbook.dto;

import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.enums.business.orderBook.OrderBookDepartmentEnum;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/12/5 9:52:49
 * @mail 247967116@qq.com
 */
@Data
public class OrderBookDetailSaveDto extends OrderBookDetail {
    private List<String> styleColorIds;
    /**
     * 类型(1:分配设计师,2:分配商企)
     */
    private String type;
    /**
     * 吊牌价
     */
    @ApiModelProperty(value = "吊牌价")
    private BigDecimal tagPrice;

    /**
     * FOB成衣厂家编码
     */
    @ApiModelProperty(value = "FOB成衣厂家编码")
    private String fobClothingFactoryCode;
    /**
     * FOB成衣厂家名称
     */
    @ApiModelProperty(value = "FOB成衣厂家名称")
    private String fobClothingFactoryName;
    /**
     * FOB成衣厂家名称
     */
    @ApiModelProperty(value = "FOB成衣厂家名称")
    private String fobSupplier;
    /**
     * 颜色编码
     */
    @ApiModelProperty(value = "颜色编码")
    private String  colorCode;
    /**
     * 颜色名称
     */
    @ApiModelProperty(value = "颜色名称")
    private String  colorName;

    private String stylePricingId;

    private String packInfoId;

    private String productStyle;

    private String productStyleName;

    private String planningSeasonId;

    @NotEmpty(message = "id未传入",groups = {AssignPersonnel.class})
    private String ids;

    private String bandName;

    private String bandCode;

    private String totalCommissioningSize;

    private OrderBookChannelType channelType;

    /**
     * 部门
     * {@link OrderBookDepartmentEnum}
     */
    @ApiModelProperty(value = "部门：1 设计部，2 线下商企，3 线上商企"  )
    @NotEmpty(message = "分配部门不能为空")
    private String department;


    @ApiModelProperty(value = "OFFLINE-线下，ONLINE-线上"  )
    private String modifyType;

    //渠道
    private String channel;

    public interface AssignPersonnel{}
}
