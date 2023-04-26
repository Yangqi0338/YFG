package com.base.sbc.module.fabricInformation.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("查询面料信息 QueryFabricInformationDto")
public class QueryFabricInformationDto extends Page {
    private String companyCode;

    /** 年份 */
    private String year;

    /** 季节 */
    private String season;

    /*厂家编号*/
    private String manufacturerNumber;

    /*厂家*/
    private String manufacturer;

    /** 厂家色号 */
    private String manufacturerColour;

    /** 是否新面料（0是 1否 */
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

    /*岗位id*/
    private List<String> jobIdList;

}
