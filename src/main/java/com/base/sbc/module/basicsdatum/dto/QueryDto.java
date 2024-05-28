package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/*
* 用于基础资料查询dto类
*
* */
@Data
@ApiModel(" QueryDto")
public class QueryDto extends Page {
    @ApiModelProperty(value = "编码"  )
    private String coding;

    @ApiModelProperty(value = "测量点"  )
    private String measurement;
    /** POM类型 */
    @ApiModelProperty(value = "POM类型"  )
    private String pdmType;

    @ApiModelProperty(value = "POM类型名称"  )
    private String pdmTypeName;
    /** 号型类型 */
    @ApiModelProperty(value = "号型类型"  )
    private String modelType;
    /** 号型类型编 */
    @ApiModelProperty(value = "号型类型编"  )
    private String modelTypeCode;
    /*编码*/
    @ApiModelProperty(value = "编码"  )
    private String code;

    /*名称*/
    @ApiModelProperty(value = "名称"  )
    private String name;

    /*多个编码*/
    @ApiModelProperty(value = "多个编码"  )
    private String codes;
    /*品类*/
    @ApiModelProperty(value = "品类"  )
    private String categoryId;

    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建日期"  )
    private String[] createDate;

    /**  创建者名称 */
    @ApiModelProperty(value = "创建者名称"  )
    private String createName;
    @ApiModelProperty(value = "洗标 "  )
    private String careLabel;
    @ApiModelProperty(value = "描述 "  )
    private String description;
    @ApiModelProperty(value = "洗标类型 "  )
    private String washType;

    private String  dimensionType;

    private String rangeDifference;

    /*状态*/
    private String status;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
}
