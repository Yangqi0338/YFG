package com.base.sbc.module.orderbook.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/12/5 13:30:55
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_order_book")
public class OrderBook extends BaseDataEntity<String> {
    /**
     * 订货本名称
     */
    @ApiModelProperty(value = "订货本名称")
    private String name;
    /**
     * 产品季节id
     */
    @ApiModelProperty(value = "产品季节id")
    private String seasonId;
    /**
     * 产品季节名称
     */
    @ApiModelProperty(value = "产品季节名称")
    private String seasonName;

    @ApiModelProperty(value = "状态:1待确认,2:已确认,3:已下单,4:已驳回")
    private String status;

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private String serialNumber;
}
