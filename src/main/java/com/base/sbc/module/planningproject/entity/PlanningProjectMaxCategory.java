package com.base.sbc.module.planningproject.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 企划看板规划大类
 * @author 卞康
 * @date 2023/11/16 17:00:55
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_planning_project_max_category")
public class PlanningProjectMaxCategory {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 企划看板规划id
     */
    private String planningProjectId;

    /**
     * 大类名称
     */
    private String prodCategory1stName;

    /**
     * 大类编码
     */
    private String prodCategory1stCode;

    /**
     * 数量
     */
    private String number;
}
