package com.base.sbc.module.planningproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;
/**
 * 企划看板坑位实体类
 * @author 卞康
 * @date 2023/11/17 9:43:54
 * @mail 247967116@qq.com
 */
@Data
@TableName("planning_project_plank")
public class PlanningProjectPlank extends BaseDataEntity<String> {
    /**
     * 企划看板规划ID
     */
    private String planningProjectId;

    /**
     * 大货款号
     */
    private String bulkStyleNo;

    /**
     * 图片
     */
    private String pic;

    /**
     * 波段名称
     */
    private String bandName;

    /**
     * 波段编码
     */
    private String bandCode;

    /**
     * 色系
     */
    private String colorSystem;

    /**
     * 定销
     */
    private String fixedSales;

    /**
     * 面料
     */
    private String fabric;

    /**
     * 匹配款式状态:(0:未匹配,1:手动匹配,2:自动匹配
     */
    private String matchingStyleStatus;


}
