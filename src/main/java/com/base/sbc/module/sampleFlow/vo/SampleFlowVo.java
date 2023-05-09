/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sampleFlow.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：样衣流程返回类
 * @address com.base.sbc.module.sampleFlow.vo.SampleFlowVo
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-9 10:43:28
 * @version 1.0
 */
@Data
public class SampleFlowVo {

    private String id;
    /** 样衣Id */
    @ApiModelProperty(value = "样衣Id"  )
    private String sampleId;
    /** 流程名称 */
    @ApiModelProperty(value = "流程名称"  )
    private String flowName;
    /** 流程开始时间 */
    @ApiModelProperty(value = "流程开始时间"  )
    private Date flowStartDate;
    /** 流程结束时间 */
    @ApiModelProperty(value = "流程结束时间"  )
    private Date flowEndDate;
    /** 审核人id */
    @ApiModelProperty(value = "审核人id"  )
    private String auditUserId;
    /** 审核人名称 */
    @ApiModelProperty(value = "审核人名称"  )
    private String auditUserName;
    /** 流程状态(0未完成,1完成) */
    @ApiModelProperty(value = "流程状态(0未完成,1完成)"  )
    private String flowStatus;
    /** 节点顺序 */
    @ApiModelProperty(value = "节点顺序"  )
    private Integer flowOrder;

}
