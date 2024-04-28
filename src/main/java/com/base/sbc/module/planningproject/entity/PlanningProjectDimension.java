
package com.base.sbc.module.planningproject.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/11/16 17:32:22
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_planning_project_dimension")
@ApiModel("企划看板-规划-维度 PlanningProjectDimension")
public class PlanningProjectDimension {
    /** 主键id */
    @ApiModelProperty(value = "主键id"  )
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /** 企划看板规划id */
    @ApiModelProperty(value = "企划看板规划id"  )
    private String planningProjectId;
    /** 大类编码 */
    @ApiModelProperty(value = "大类编码"  )
    private String prodCategory1stCode;
    /** 大类名称 */
    @ApiModelProperty(value = "大类名称"  )
    private String prodCategory1stName;
    /** 品类 */
    @ApiModelProperty(value = "品类"  )
    private String prodCategoryCode;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    private String prodCategoryName;
    /** 中类code */
    @ApiModelProperty(value = "中类code"  )
    private String prodCategory2ndCode;
    /** 中类名称 */
    @ApiModelProperty(value = "中类名称"  )
    private String prodCategory2ndName;
    /** 品类维度明细id */
    @ApiModelProperty(value = "品类维度明细id"  )
    private String categoryPlanningDetailsId;
    /** 第一维度id */
    @ApiModelProperty(value = "第一维度id"  )
    private String dimensionId;
    /** 第一维度名称 */
    @ApiModelProperty(value = "第一维度名称"  )
    private String dimensionName;
    /** 第一维度编码 */
    @ApiModelProperty(value = "第一维度编码"  )
    private String dimensionCode;
    /** 维度值 */
    @ApiModelProperty(value = "维度值"  )
    private String dimensionValue;
    /** 波段名称 */
    @ApiModelProperty(value = "波段名称"  )
    private String bandName;
    /** 波段编码 */
    @ApiModelProperty(value = "波段编码"  )
    private String bandCode;
    /** 数量 */
    @ApiModelProperty(value = "数量"  )
    private String number;
    private String dimensionTypeCode;
    /**
     * 维度等级
     */
    @ApiModelProperty(value = "维度等级")
    private String dimensionalityGrade;
    /**
     * 维度等级名称
     */
    @ApiModelProperty(value = "维度等级名称")
    private String dimensionalityGradeName;
    /** 是否开启中类(0:未开启,1:开启) */
    @ApiModelProperty(value = "是否开启中类(0:未开启,1:开启)"  )
    private String isProdCategory2nd;
    @TableField(fill = FieldFill.INSERT)
    private String companyCode;

    /**
     * 虚拟坑位数量
     */
    private String virtualNumber;

    /**
     * 已匹配数量
     */
    @TableField(exist = false)
    private Long matchedNumber;
}
