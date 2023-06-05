/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：流程配置-动作定义 Vo类
 * @address com.base.sbc.module.process.vo.ProcessAction
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-5 11:03:08
 * @version 1.0
 */
@Data

@ApiModel("流程配置-动作定义 ProcessAction")
public class ProcessActionVo  {

    private String id;
    /** 动作名称 */
    @ApiModelProperty(value = "动作名称"  )
    private String actionName;
    /** 动作编码 */
    @ApiModelProperty(value = "动作编码"  )
    private String actionCode;
    /** 动作说明 */
    @ApiModelProperty(value = "动作说明"  )
    private String actionDescription;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
}
