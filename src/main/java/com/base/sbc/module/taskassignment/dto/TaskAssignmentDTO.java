/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.module.common.vo.BasePageInfo;
import com.base.sbc.module.taskassignment.entity.TaskAssignment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;

/**
 * 类描述：任务分配入参
 *
 * @author XHTE
 * @create 2024/6/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("任务分配入参")
@Validated
public class TaskAssignmentDTO extends TaskAssignment {

    private static final long serialVersionUID = 1L;

    /**
     * 筛选条件类型（1-品牌 2-虚拟部门 3-大类 4-品类 5-中类 6-小类 7-人员）
     */
    @ApiModelProperty("筛选条件类型（1-品牌 2-虚拟部门 3-大类 4-品类 5-中类 6-小类 7-人员）")
    private Integer type;

    /**
     * 模糊筛选字段（品牌，虚拟部门，大类，品类，中类，小类，人员）
     */
    @ApiModelProperty("模糊筛选字段（品牌，虚拟部门，大类，品类，中类，小类，人员）")
    private String search;

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
     * 数据 ID
     */
    @ApiModelProperty(value = "数据 ID")
    private String dataId;
}
