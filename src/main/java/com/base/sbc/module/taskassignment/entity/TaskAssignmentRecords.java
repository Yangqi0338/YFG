/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：任务分配-触发记录 实体类
 *
 * @author XHTE
 * @create 2024/6/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_task_assignment_records")
@ApiModel("任务分配-触发记录 TaskAssignmentRecords")
public class TaskAssignmentRecords extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;

    /**
     * 任务分配 ID
     */
    @ApiModelProperty("任务分配 ID")
    private String taskAssignmentId;
    /**
     * 设计款编码
     */
    @ApiModelProperty(value = "设计款编码")
    private String designNo;
    /**
     * 样板号
     */
    @ApiModelProperty(value = "样板号")
    private String patternNo;
    /**
     * 打版类型
     */
    @ApiModelProperty(value = "打版类型")
    private String sampleType;
    /**
     * 打版类型名称
     */
    @ApiModelProperty(value = "打版类型名称")
    private String sampleTypeName;
    /**
     * 触发菜单（产品季总览，技术中心看板）
     */
    @ApiModelProperty(value = "触发菜单（产品季总览，技术中心看板）")
    private String triggerMenu;
    /**
     * 成功状态（0-失败 1-成功）
     */
    @ApiModelProperty(value = "成功状态（0-失败 1-成功）")
    private String successFlag;
    /**
     * 触发结果
     */
    @ApiModelProperty(value = "触发结果")
    private String triggerResult;
}
