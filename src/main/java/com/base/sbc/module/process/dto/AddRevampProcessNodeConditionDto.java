/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：新增修改流程配置-节点条件 dto类
 * @address com.base.sbc.module.process.dto.ProcessNodeCondition
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-2 20:15:15
 * @version 1.0
 */
@Data
@ApiModel("流程配置-节点条件 ProcessNodeCondition")
public class AddRevampProcessNodeConditionDto  {

    private String id;

    /** 节点信息Id */
    @ApiModelProperty(value = "节点信息Id"  )
    private String nodeId;
    /** 节点条件 */
    @ApiModelProperty(value = "节点条件"  )
    private String nodeCondition;
    /** 原状态 */
    @ApiModelProperty(value = "原状态"  )
    private String originalStatus;
    /** 目标状态 */
    @ApiModelProperty(value = "目标状态"  )
    private String targetStatus;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
}
