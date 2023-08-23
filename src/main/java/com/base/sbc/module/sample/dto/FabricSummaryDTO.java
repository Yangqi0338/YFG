package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 面料汇总DTO层
 * @author lizan
 * @date 2023-07-13 17:47
 */
@Data
@ApiModel("面料汇总")
@AllArgsConstructor
@NoArgsConstructor
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

    @ApiModelProperty(value = "企业编码")
    private String companyCode;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    @ApiModelProperty(value = "面料成分")
    private String ingredient;

    @ApiModelProperty(value = "版师id")
    private String patternDesignId;

    @ApiModelProperty(value = "设计师id")
    private List<String> designerIds;

    public FabricSummaryDTO(String companyCode, String materialId) {
        this.companyCode = companyCode;
        this.materialId = materialId;
    }
}
