package com.base.sbc.module.orderbook.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author 卞康
 * @date 2024-01-15 14:35:28
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_order_book_follow_up")
public class OrderBookFollowUp extends BaseDataEntity<String> {

    @ApiModelProperty(value = "订货本名称")
    private String orderBookName;

    @ApiModelProperty(value = "订货本id")
    private String orderBookId;

    @ApiModelProperty(value = "产品季名称")
    private String seasonName;

    @ApiModelProperty(value = "产品季id")
    private String seasonId;

    @ApiModelProperty(value = "品牌")
    private String brandName;

    @ApiModelProperty(value = "品牌编码")
    private String brandCode;

    @ApiModelProperty(value = "投产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date productionDate;

    @ApiModelProperty(value = "工艺员")
    private String technicianName;

    @ApiModelProperty(value = "工艺员id")
    private String technicianId;

    @ApiModelProperty(value = "款数")
    private String styleNumber;


    @ApiModelProperty(value = "关联订货本id集合")
    private String orderBookDetailIds;


    @ApiModelProperty(value = "关联订货本大货款号集合")
    private String bulkStyleNos;


    @ApiModelProperty(value = "状态(1:待确认，2:已下单，3:已驳回)")
    private String status;
}
