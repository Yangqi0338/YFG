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
 * 类描述：流程配置-节点状态修改字段 实体类
 * @address com.base.sbc.module.process.entity.ProcessNodeStatusUpdateManagement
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-16 14:00:07
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_process_node_status_update_management")
@ApiModel("流程配置-节点状态修改字段 ProcessNodeStatusUpdateManagement")
public class ProcessNodeStatusUpdateManagement extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 节点状态条件id */
    @ApiModelProperty(value = "节点状态条件id"  )
    private String nodeStatusConditionId;
    /** 字段id */
    @ApiModelProperty(value = "字段id"  )
    private String fieldManagementId;
    /** 类型(0文本填写,1获取当前用户,2获取当前时间,3前端填写) */
    @ApiModelProperty(value = "类型(0文本填写,1获取当前用户,2获取当前时间,3前端填写)"  )
    private String type;
    /** 更新文本 */
    @ApiModelProperty(value = "更新文本"  )
    private String updateText;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
