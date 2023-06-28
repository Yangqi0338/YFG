/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 类描述：流程配置-节点表 Vo类
 * @address com.base.sbc.module.process.vo.ProcessNode
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-2 20:15:14
 * @version 1.0
 */
@Data

@ApiModel("流程配置-节点表 ProcessNode")
public class ProcessNodeVo  {

    private String id;
    /** 流程方案Id */
    @ApiModelProperty(value = "流程方案Id"  )
    private String processSchemeId;
    /** 节点名称 */
    @ApiModelProperty(value = "节点名称"  )
    private String nodeName;
    /** 节点说明 */
    @ApiModelProperty(value = "节点说明"  )
    private String description;
    /** 表单id */
    @ApiModelProperty(value = "表单id"  )
    private String formId;
    /** 阶段id */
    @ApiModelProperty(value = "阶段id"  )
    private String stageId;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

    /*排序*/
    private Integer sort;
}
