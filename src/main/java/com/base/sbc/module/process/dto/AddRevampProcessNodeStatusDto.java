/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：新增修改流程配置-节点状态 dto类
 * @address com.base.sbc.module.process.dto.ProcessNodeStatus
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-2 20:15:15
 * @version 1.0
 */
@Data
@ApiModel("流程配置-节点状态 ProcessNodeStatus")
public class AddRevampProcessNodeStatusDto  {

    private String id;

    /** 节点Id */
    @ApiModelProperty(value = "节点Id"  )
    private String nodeId;
    /** 状态id(对应状态表) */
    @ApiModelProperty(value = "状态id(对应状态表)"  )
    private String statusId;
    /** 状态名称 */
    @ApiModelProperty(value = "状态名称"  )
    private String statusName;
    /** 状态说明 */
    @ApiModelProperty(value = "状态说明"  )
    private String statusDescription;
    /** 开始状态(0未选中1选中) */
    @ApiModelProperty(value = "开始状态(0未选中1选中)"  )
    private String startStatus;
    /** 结束状态(0未选中1选中) */
    @ApiModelProperty(value = "结束状态(0未选中1选中)"  )
    private String endStatus;
}
