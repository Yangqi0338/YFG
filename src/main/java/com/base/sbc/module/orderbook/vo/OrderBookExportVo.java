package com.base.sbc.module.orderbook.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/12/16 9:28:27
 * @mail 247967116@qq.com
 */
@Data
public class OrderBookExportVo {
    /**
     * 订货本名称
     */
    @Excel(name = "订货本名称")
    private String name;
    /**
     * 产品季节名称
     */
    @Excel(name = "产品季节名称")
    private String seasonName;

    @Excel(name = "状态:1待确认,2:已确认,3:已下单,4:已驳回", replace = {"待确认_1", "已确认_2", "3_已下单", "4_已驳回"})
    private String status;

    @Excel(name = "创建时间")
    private String createDate;

    @Excel(name = "工艺员")
    private String createName;
}
