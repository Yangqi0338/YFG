/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
/**
 * 类描述：流程配置-节点记录 实体类
 * @address com.base.sbc.module.process.entity.ProcessNodeRecord
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-6 15:03:28
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_process_node_record")
@ApiModel("流程配置-节点记录 ProcessNodeRecord")
public class ProcessNodeRecord extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 业务实例id */
    @ApiModelProperty(value = "业务实例id"  )
    private String businessInstanceId;
    /** 节点名称 */
    @ApiModelProperty(value = "节点名称"  )
    private String nodeName;
    /** 节点id */
    private String nodeId;
    /*阶段id*/
    private String stageId;
    /** 开始时间 */
    @ApiModelProperty(value = "开始时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;
    /** 结束时间 */
    @ApiModelProperty(value = "结束时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
    /** 当前状态 */
    @ApiModelProperty(value = "当前状态"  )
    private String atPresentStatus;
    /** 是否完成(0未完成，1已完成) */
    @ApiModelProperty(value = "是否完成(0未完成，1已完成)"  )
    private String isComplete;

    private Integer sort;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
