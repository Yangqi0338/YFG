package com.base.sbc.module.planningproject.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企划看板坑位实体类
 *
 * @author 卞康
 * @date 2023/11/17 9:43:54
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_planning_project_plank")
public class PlanningProjectPlank extends BaseDataEntity<String> {
    /**
     * 企划看板规划ID
     */
    private String planningProjectId;

    /**
     * 对应企划维度id
     */
    private String planningProjectDimensionId;

    /**
     * 大货款号
     */
    private String bulkStyleNo;


    /**
     * 配色id
     */
    private String styleColorId;

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
     * 匹配款式状态:(0:未匹配,1:手动匹配,2:自动匹配,3:匹配历史款)
     */
    private String matchingStyleStatus;

    private String hisDesignNo;

    @TableField(exist = false)
    @ApiModelProperty(value = "样式类别")
    private String styleCategory;

}
