package com.base.sbc.module.planning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_planning_project")
@ApiModel("企划规划表 PlanningProject")
public class PlanningProject extends BaseDataEntity<String> {
    private static final long serialVersionUID = 1L;

    /**
     * 产品季id
     */
    @ApiModelProperty(value = "产品季id")
    private String seasonId;
    /**
     * 维度id
     */
    @ApiModelProperty(value = "维度id")
    private String dimensionalityId;
    /**
     * 款式id
     */
    @ApiModelProperty(value = "款式id")
    private String styleColorId;
    /**
     * 品类id
     */
    @ApiModelProperty(value = "品类id")
    private String categoryId;
    /**
     * 企划规划名称
     */
    @ApiModelProperty(value = "企划规划名称")
    private String planningProjectName;
    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    private String planningChannel;
    /**
     * 大类
     */
    @ApiModelProperty(value = "大类")
    private String prodCategory1st;
    /**
     * 大类名称
     */
    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;
    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    private String prodCategory;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    /**
     * 中类code
     */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;
    /**
     * 中类名称
     */
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;
    /**
     * 维度名称
     */
    @ApiModelProperty(value = "维度名称")
    private String dimensionalityName;
    /**
     * 坑位数量
     */
    @ApiModelProperty(value = "坑位数量")
    private String categoryCount;
    /**
     * 1停用、0启用
     */
    @ApiModelProperty(value = "1停用、0启用")
    private String status;
}
