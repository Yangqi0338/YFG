package com.base.sbc.module.planningproject.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2024-01-20 18:21:25
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_category_planning_details")
public class CategoryPlanningDetails extends BaseDataEntity<String> {
    @ApiModelProperty(value = "季节企划名称")
    private String seasonalPlanningName;
    @ApiModelProperty(value = "季节企划Id")
    private String seasonalPlanningId;

    @ApiModelProperty(value = "品类企划名称")
    private String categoryPlanningName;
    @ApiModelProperty(value = "品类企划Id")
    private String categoryPlanningId;
    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;
    @ApiModelProperty(value = "大类编码")
    private String prodCategory1stCode;
    @ApiModelProperty(value = "品类编码")
    private String prodCategoryCode;
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;

    @Excel(name = "中类名称")
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;
    @Excel(name = "中类编码")
    @ApiModelProperty(value = "中类编码")
    private String prodCategory2ndCode;

    /**
     * 维度类型的 code
     */
    @ApiModelProperty(value = "维度类型的 code")
    private String dimensionTypeCode;

    /**
     * 维度系数 id
     */
    @ApiModelProperty(value = "维度系数 id")
    private String dimensionId;

    /**
     * 维度系数名称
     */
    @ApiModelProperty(value = "维度系数名称")
    private String dimensionName;

    /**
     * 维度 code
     */
    @ApiModelProperty(value = "维度 code")
    private String dimensionCode;

    /**
     * 维度 value
     */
    @ApiModelProperty(value = "维度 value")
    private String dimensionValue;

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

    @ApiModelProperty(value = "波段名称")
    private String bandName;
    @ApiModelProperty(value = "波段编码")
    private String bandCode;
    /**
     * 具体品类企划的需求数
     */
    @ApiModelProperty(value = "具体品类企划的需求数")
    private String number;
    /**
     * SKC数量 取自季节企划各个款式类别的需求数（以一个中类-波段为颗粒度）
     */
    @ApiModelProperty(value = "SKC数量 取自季节企划各个款式类别的需求数（以一个中类-波段为颗粒度）")
    private String skcCount;
    /**
     * 合计数量 合并季节企划详情到品类-中类的数据
     */
    @ApiModelProperty(value = "合计数量 合并季节企划详情到品类-中类的数据")
    private String total;
    @ApiModelProperty(value = "样式类别")
    private String styleCategory;
    @ApiModelProperty(value = "下单时间")
    private String orderTime;
    @ApiModelProperty(value = "上市时间")
    private String launchTime;

    private String dataJson;

    /**
     * 是否已生成数据（到品类维度）（0-已保存 1-已审核 2-已作废）
     */
    @ApiModelProperty(value = "是否已生成数据（到品类维度）（0-已保存 1-已审核 2-已作废）")
    private String isGenerate;
}
