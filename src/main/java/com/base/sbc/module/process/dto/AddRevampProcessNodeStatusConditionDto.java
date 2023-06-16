/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：新增修改流程配置-节点状态条件 dto类
 * @address com.base.sbc.module.process.dto.ProcessNodeStatusCondition
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-5 17:10:23
 * @version 1.0
 */
@Data
@ApiModel("流程配置-节点状态条件 ProcessNodeStatusCondition")
public class AddRevampProcessNodeStatusConditionDto  {

    private String id;

    /** 节点信息Id */
    @ApiModelProperty(value = "节点信息Id"  )
    private String nodeId;
    /** 原状态 */
    @ApiModelProperty(value = "原状态"  )
    private String originalStatus;
    /** 目标状态 */
    @ApiModelProperty(value = "目标状态"  )
    private String targetStatus;
    /** 节点条件 */
    @ApiModelProperty(value = "节点条件"  )
    private String nodeCondition;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /*修改的字段*/
    private String updateField;
    /**/
    private String ruleUserId;

    @ApiModelProperty(value = "节点条件 不满足时的提示"  )
    private String reminder;
    /** 节点条件公式 */
    @ApiModelProperty(value = "节点条件公式"  )
    private String nodeConditionFormula;

    /*节点动作*/
    private List<AddRevampProcessNodeActionDto> list;

    /*修改字段*/
    private List<AddRevampProcessNodeStatusUpdateManagementDto> updateManagementDtoList;

}
