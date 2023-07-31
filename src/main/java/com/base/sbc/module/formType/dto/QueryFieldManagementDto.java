package com.base.sbc.module.formType.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("查询等 QueryFieldManagementDto")
public class QueryFieldManagementDto  extends Page {

    /** 表单类型主键 */
    @ApiModelProperty(value = "表单类型主键"  )
    private String formTypeId;

    private String companyCode;

    private Long sequence;

    private String id;

    private String groupName;

    /*原先*/
    private String currentId;

    /*目标*/
    private String targetId;
    @ApiModelProperty(value = "字段id")
    List<String> ids;


    @ApiModelProperty(value = "表单编码")
    private String formTypeCode;
    @ApiModelProperty(value = "季节编码")
    private String season;

    @ApiModelProperty(value = "品类编码")
    private String categoryId;
}
