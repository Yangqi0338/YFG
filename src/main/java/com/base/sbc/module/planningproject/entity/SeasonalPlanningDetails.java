package com.base.sbc.module.planningproject.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 季节企划详情
 * @author 卞康
 * @date 2024-01-20 18:24:24
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_seasonal_planning_details")
public class SeasonalPlanningDetails extends BaseDataEntity<String> {
    @ApiModelProperty(value = "季节企划名称")
    private String  seasonalPlanningName;
    @ApiModelProperty(value = "季节企划Id")
    private String  seasonalPlanningId;

    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName ;
    @ApiModelProperty(value = "大类编码")
    private String prodCategory1stCode;
    @ApiModelProperty(value = "品类编码")
    private String prodCategoryCode;
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;

    @Excel(name = "中类名称"  )
    private String prodCategory2ndName;
    @Excel(name = "中类编码"  )
    private String prodCategory2ndCode;


    @ApiModelProperty(value = "波段名称")
    private String bandName;
    @ApiModelProperty(value = "波段编码")
    private String bandCode;
    @ApiModelProperty(value = "SKC数量")
    private String skcCount;
    @ApiModelProperty(value = "样式类别")
    private String styleCategory;
    @ApiModelProperty(value = "样式类别")
    private String styleCategoryCode;
    @ApiModelProperty(value = "下单时间")
    private String orderTime;
    @ApiModelProperty(value = "上市时间")
    private String launchTime;
    @ApiModelProperty(value = "行位置index")
    private String rowIndex;
    @ApiModelProperty(value = "列位置index")
    private String columnIndex;
}
