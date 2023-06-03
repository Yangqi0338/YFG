/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.nodestatus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
/**
 * 类描述：节点状态记录 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.nodestatus.entity.NodeStatus
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-3 9:31:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_node_status")
@ApiModel("节点状态记录 NodeStatus")
public class NodeStatus extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 数据Id */
    @ApiModelProperty(value = "数据Id"  )
    private String dataId;
    /** 节点 */
    @ApiModelProperty(value = "节点"  )
    private String node;
    /** 状态 */
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
    /**
     * 节点开始标志(0否，1是)
     */
    @ApiModelProperty(value = "节点开始标志(0否，1是)")
    private String startFlg;
    /**
     * 节点结束标志(0否，1是)
     */
    @ApiModelProperty(value = "节点结束标志(0否，1是)")
    private String endFlg;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

