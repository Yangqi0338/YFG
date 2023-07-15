package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 面料汇总DTO层
 * @author lizan
 * @date 2023-07-13 17:47
 */
@Data
@ApiModel("面料汇总")
public class FabricSummaryDTO  extends Page {

    @ApiModelProperty(value = "搜索")
    private String search;

    @ApiModelProperty(value = "审核状态列表")
    private List<String> auditStatusList;

    @ApiModelProperty(value = "产品季列表")
    private List<String> productSeasonList;

    @ApiModelProperty(value = "月份列表")
    private List<String> monthList;

    @ApiModelProperty(value = "波段列表")
    private List<String> bandList;

    @ApiModelProperty(value = "波段")
    private String bandCode;

    @ApiModelProperty(value = "月份")
    private String month;


}
