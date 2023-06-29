package com.base.sbc.module.sample.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 类描述：样衣销售 vo
 * @address com.base.sbc.module.sample.vo.SampleVo
 */
@Data
@ApiModel("样衣销售 SampleSaleVo")
public class SampleSaleVo {
    /** id */
    @ApiModelProperty(value = "id")
    private String id;

    /** 编号 */
    @ApiModelProperty(value = "单号")
    private String code;

    /** 销售时间 */
    @ApiModelProperty(value = "销售时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date saleDate;

    /** 业务员 */
    @ApiModelProperty(value = "业务员")
    private String saleName;

    /** 客户 */
    @ApiModelProperty(value = "客户")
    private String custmerName;

    /** 总金额 */
    @ApiModelProperty(value = "总金额")
    private BigDecimal totalPrice;

    /** 总数量 */
    @ApiModelProperty(value = "总数量")
    private String totalCount;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "关联的明细")
    private List<SampleSaleItemVo> sampleItemList;
}
