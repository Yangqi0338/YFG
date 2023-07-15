package com.base.sbc.module.pack.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 核价管理选择制版列表
 */
@Data
@ApiModel("核价管理选择制版列表")
public class PricingSelectListVO {

    /**
     * 制版单id
     */
    @ApiModelProperty("制版单id")
    private String id;
    /**
     * 设计款号
     */
    @ApiModelProperty("设计款号")
    private String designNo;
    /**
     * 大货款号
     */
    @ApiModelProperty("大货款号")
    private String styleNo;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createDate;
    /**
     * 品类
     */
    @ApiModelProperty("品类")
    private String categoryName;
    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private String confirmStatus;
}
