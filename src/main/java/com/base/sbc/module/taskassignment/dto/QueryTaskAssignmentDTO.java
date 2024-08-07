/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.dto.QueryFieldDto;
import com.base.sbc.module.taskassignment.entity.TaskAssignment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * 类描述：任务分配列表分页入参
 *
 * @author XHTE
 * @create 2024/6/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("任务分配列表分页入参")
@Validated
public class QueryTaskAssignmentDTO  extends QueryFieldDto {

    private static final long serialVersionUID = 1L;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌 code")
    private String brand;

    /**
     * 虚拟部门 ID
     */
    @ApiModelProperty(value = "虚拟部门 ID")
    private String virtualDeptId;

    /**
     * 大类 code
     */
    @ApiModelProperty(value = "大类 code")
    private String prodCategory1st;

    /**
     * 品类 code
     */
    @ApiModelProperty(value = "品类 code")
    private String prodCategory;

    /**
     * 中类 code
     */
    @ApiModelProperty(value = "中类 code")
    private String prodCategory2nd;

    /**
     * 小类 code
     */
    @ApiModelProperty(value = "小类 code")
    private String prodCategory3rd;

    /**
     * 用户 ID（取值范围是属于这个虚拟部门下面的）
     */
    @ApiModelProperty(value = "用户 ID（取值范围是属于这个虚拟部门下面的）")
    private String userId;

    /**
     * 触发菜单，可多选，多选逗号分隔（产品季总览，技术中心看板）
     */
    @ApiModelProperty(value = "触发菜单，可多选，多选逗号分隔（产品季总览，技术中心看板）")
    private String triggerMenus;

}
