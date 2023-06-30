/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：流程配置-动作定义 实体类
 * @address com.base.sbc.module.process.entity.ProcessAction
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-5 11:03:08
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_process_action")
@ApiModel("流程配置-动作定义 ProcessAction")
public class ProcessAction extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
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
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
