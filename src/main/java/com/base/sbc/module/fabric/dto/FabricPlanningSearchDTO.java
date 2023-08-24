package com.base.sbc.module.fabric.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 面料企划查询
 *
 * @Author xhj
 * @Date 2023/6/26 17:53
 */
@Data
@ApiModel(value = "面料企划查询")
public class FabricPlanningSearchDTO extends Page {
    @ApiModelProperty("年份")
    public String year;
    @ApiModelProperty("季节")
    public String season;
    @ApiModelProperty("品牌")
    public String brand;
    @ApiModelProperty("审核状态：1.未提交、2.审核中、3.审核通过、4.审核失败")
    public String approveStatus;


    private String companyCode;

}
