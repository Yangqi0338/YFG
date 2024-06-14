package com.base.sbc.module.planningproject.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("企划看板规划筛选条件 PlanningBoardSearchDto")
public class PlanningProjectPageDTO extends Page{

    @ApiModelProperty(value = "企划规划看板id")
    private String planningProjectId;

    @ApiModelProperty(value = "企划需求看板id")
    private String planningChannelId;
    @ApiModelProperty(value = "产品季id")
    private String seasonId;
    @ApiModelProperty(value = "年份")
    private String year;
    @ApiModelProperty(value = "维度id")
    private String dimensionalityId;

    @ApiModelProperty(value = "款式id")
    private String styleColorId;

    @ApiModelProperty(value = "品类id")
    private String categoryId;

    @ApiModelProperty(value = "企划规划名称")
    private String planningProjectName;

    @ApiModelProperty(value = "渠道编码")
    private String planningChannelCode;

    @ApiModelProperty(value = "大类")
    private String prodCategory1st;

    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;

    @ApiModelProperty(value = "品类")
    private String prodCategory;

    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;

    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;

    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;

    @ApiModelProperty(value = "维度名称")
    private String dimensionalityName;

    @ApiModelProperty(value = "坑位数量")
    private String categoryCount;

    @ApiModelProperty(value = "1停用、0启用")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "修改时间")
    private Date updateDate;

    /**
     * 维度数据 id，多选逗号分隔
     */
    @ApiModelProperty(value = "维度数据 id，多选逗号分隔")
    private String dimensionIds;
}
