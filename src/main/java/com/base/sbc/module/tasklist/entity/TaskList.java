/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.Date;

 /**
 * 任务列表实体对象
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_task_list")
@ApiModel("任务列表实体对象")
public class TaskList extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 任务编号（单据编号）
     */
    @ApiModelProperty(value = "任务编号（单据编号）")
    private String taskCode;
     /**
      * 任务类型（1-款式打标下发）
      */
     @ApiModelProperty(value = "任务类型（1-款式打标下发）")
     private Integer taskType;
     /**
      * 任务状态（1-待办 2-已办）
      */
     @ApiModelProperty(value = "任务状态（1-待办 2-已办）")
     private Integer taskStatus;
    /**
     * 任务名称（「任务类型：单据编号」）
     */
    @ApiModelProperty(value = "任务名称（任务类型：单据编号）")
    private String taskName;
    /**
     * 任务内容（「单据编号：同步结果」同步结果举例：总共下发xx条，成功xx条，失败xx条）
     */
    @ApiModelProperty(value = "任务内容（「单据编号：同步结果」同步结果举例：总共下发xx条，成功xx条，失败xx条）")
    private String taskContent;
    /**
     * 发起人 ID
     */
    @ApiModelProperty(value = "发起人 ID")
    private String initiateUserId;
    /**
     * 发起人名称
     */
    @ApiModelProperty(value = "发起人名称")
    private String initiateUserName;
    /**
     * 接收人 ID
     */
    @ApiModelProperty(value = "接收人 ID")
    private String receiveUserId;
    /**
     * 接收人名称
     */
    @ApiModelProperty(value = "接收人名称")
    private String receiveUserName;

    /**
     * 接收时间
     */
    @ApiModelProperty(value = "接收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveDate;
}
