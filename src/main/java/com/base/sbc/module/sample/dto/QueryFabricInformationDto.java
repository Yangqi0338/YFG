package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("查询面料信息 QueryFabricInformationDto")
public class QueryFabricInformationDto extends Page {
    private String companyCode;

    /** 年份 */
    @ApiModelProperty(value = "年份")
    private String yearName;

    /** 季节 */
    @ApiModelProperty(value = "季节")
    private String seasonName;

    @ApiModelProperty(value = "品牌")
    private String brandName;

    /*厂家编号*/
    @ApiModelProperty(value = "供应商料号")
    private String supplierMaterialCode;

    /*厂家*/
    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "供应商编号")
    private String supplierCode;

    /** 厂家色号 */
    @ApiModelProperty(value = "供应商色号")
    private String supplierColor;

    @ApiModelProperty(value = "搜索")
    private String search;

    /** 是否新面料*/
    @ApiModelProperty(value = "是否新面料")
    private String isNewFabric;

    /** 数量（米） */
    private Integer quantity;

    /** 登记时间 */
    private String[] registerDate;

    private BigDecimal[] fabricPrice;

    /*成分*/
    private String ingredient;

    /*期货*/
    private Integer[] leadtime;

    /*胚布情况*/
    private String germinalCondition;

    /*面料是否可用*/
    private String fabricIsUsable;

    /*理化检测结果*/
    private String physicochemistryDetectionResult;

    /*洗涤检测结果*/
    private String  washDetectionResult;

    /*0查询我发起的 1我接受的*/
    private String originate;

    /*用户id*/
    private  String userId;

    private  String  fabricDetailedId;

    /*岗位id*/
    private List<String> jobIdList;

    @ApiModelProperty(value = "调样设计师")
    private  String   atactiformStylist;


}
