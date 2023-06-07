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
 * 类描述：新增修改流程配置-节点条件公式 dto类
 * @address com.base.sbc.module.process.dto.ProcessNodeConditionFormula
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-7 15:47:31
 * @version 1.0
 */
@Data
@ApiModel("流程配置-节点条件公式 ProcessNodeConditionFormula")
public class AddRevampProcessNodeConditionFormulaDto  {

    private String id;

    /** 节点信息Id */
    @ApiModelProperty(value = "节点信息Id"  )
    private String nodeId;
    /** 节点状态条件id */
    @ApiModelProperty(value = "节点状态条件id"  )
    private String nodeStatusConditionId;
    /** 节点条件公式 */
    @ApiModelProperty(value = "节点条件公式"  )
    private String nodeConditionFormula;
    /** 节点条件 不满足时的提示 */
    @ApiModelProperty(value = "节点条件 不满足时的提示"  )
    private String reminder;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
}
