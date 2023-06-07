/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：流程配置-节点条件公式 实体类
 * @address com.base.sbc.module.process.entity.ProcessNodeConditionFormula
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-7 15:47:31
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_process_node_condition_formula")
@ApiModel("流程配置-节点条件公式 ProcessNodeConditionFormula")
public class ProcessNodeConditionFormula extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
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
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
