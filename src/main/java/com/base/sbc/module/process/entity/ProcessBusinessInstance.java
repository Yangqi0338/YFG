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
 * 类描述：流程配置-业务实例 实体类
 * @address com.base.sbc.module.process.entity.ProcessBusinessInstance
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-6 15:03:26
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_process_business_instance")
@ApiModel("流程配置-业务实例 ProcessBusinessInstance")
public class ProcessBusinessInstance extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 流程方案id */
    @ApiModelProperty(value = "流程方案id"  )
    private String processSchemeId;
    /** 当前状态 */
    @ApiModelProperty(value = "当前状态"  )
    private String atPresentStatusName;
    /** 当前节点 */
    @ApiModelProperty(value = "当前节点"  )
    private String atPresentNodeName;

    private String atPresentNodeId;
    /** 业务数据id */
    @ApiModelProperty(value = "业务数据id"  )
    private String businessDataId;
    /** 是否完成(0未完成，1已完成) */
    @ApiModelProperty(value = "是否完成(0未完成，1已完成)"  )
    private String isComplete;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
