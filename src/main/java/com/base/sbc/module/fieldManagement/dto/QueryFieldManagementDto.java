package com.base.sbc.module.fieldManagement.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
     private String  targetId;
}
