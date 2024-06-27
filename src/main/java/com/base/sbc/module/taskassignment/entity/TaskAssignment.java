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
 * 类描述：任务分配 实体类
 * @author XHTE
 * @create 2024/6/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_task_assignment")
@ApiModel("任务分配 TaskAssignment")
public class TaskAssignment extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌 code"  )
    private String brand;
    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /**
     * 虚拟部门 ID
     */
    @ApiModelProperty(value = "虚拟部门 ID"  )
    private String virtualDeptId;
    /**
     * 大类 code
     */
    @ApiModelProperty(value = "大类code"  )
    private String prodCategory1st;
    /**
     * 大类名称
     */
    @ApiModelProperty(value = "大类名称"  )
    private String prodCategory1stName;
    /**
     * 品类 code
     */
    @ApiModelProperty(value = "品类code"  )
    private String prodCategory;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称"  )
    private String prodCategoryName;
    /**
     * 中类 code
     */
    @ApiModelProperty(value = "中类code"  )
    private String prodCategory2nd;
    /**
     * 中类名称
     */
    @ApiModelProperty(value = "中类名称"  )
    private String prodCategory2ndName;
    /**
     * 小类 code
     */
    @ApiModelProperty(value = "小类code"  )
    private String prodCategory3rd;
    /**
     * 小类名称
     */
    @ApiModelProperty(value = "小类名称"  )
    private String prodCategory3rdName;
    /**
     * 用户 ID（取值范围是属于这个虚拟部门下面的）
     */
    @ApiModelProperty(value = "用户 ID（取值范围是属于这个虚拟部门下面的）"  )
    private String userId;
    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称"  )
    private String userName;
    /**
     * 触发菜单，可多选，多选逗号分隔（产品季总览，技术中心看板）
     */
    @ApiModelProperty(value = "触发菜单，可多选，多选逗号分隔（产品季总览，技术中心看板）"  )
    private String triggerMenu;
    /**
     * 启用状态（ 0-未启用 1-启用）
     */
    @ApiModelProperty(value = "启用状态（ 0-未启用 1-启用）"  )
    private String enableFlag;
}
