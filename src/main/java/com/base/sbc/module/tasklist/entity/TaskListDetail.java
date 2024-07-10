/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 任务列表详情实体对象
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_distribute_task_item")
@ApiModel("任务列表详情实体对象")
public class TaskListDetail extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 任务列表主表 ID（t_distribute_task）
     */
    @ApiModelProperty(value = "任务列表主表 ID（t_distribute_task）")
    private String taskListId;
    /**
     * 关联的数据 ID（款式打标下发）
     */
    @ApiModelProperty(value = "关联的数据 ID（款式打标下发）")
    @NotBlank(message = "关联的数据 ID 不能为空")
    private String dataId;
    /**
     * 错误信息（用来记录这个数据为什么下发失败）
     */
    @ApiModelProperty(value = "错误信息（用来记录这个数据为什么下发失败）")
    @NotBlank(message = "错误信息不能为空")
    private String errorInfo;
    /**
     * 同步结果（1-成功 2-失败）
     */
    @ApiModelProperty(value = "同步结果（1-成功 2-失败）")
    @NotNull(message = "同步结果不能为空")
    @Range(min = 1, max = 2, message = "同步结果仅支持「1-成功 2-失败」")
    private Integer syncResult;
    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String styleNo;
}
