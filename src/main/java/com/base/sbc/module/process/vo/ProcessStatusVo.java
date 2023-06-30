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
 * 类描述：流程配置-状态定义 Vo类
 * @address com.base.sbc.module.process.vo.ProcessStatus
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-2 20:15:16
 * @version 1.0
 */
@Data

@ApiModel("流程配置-状态定义 ProcessStatus")
public class ProcessStatusVo  {

    private String id;
    /** 状态名称 */
    @ApiModelProperty(value = "状态名称"  )
    private String statusName;
    /** 状态说明 */
    @ApiModelProperty(value = "状态说明"  )
    private String statusDescription;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
}
