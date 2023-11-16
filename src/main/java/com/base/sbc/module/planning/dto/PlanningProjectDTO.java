package com.base.sbc.module.planning.dto;

import com.base.sbc.module.planning.entity.PlanningProject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("企划看板规划筛选条件 PlanningBoardSearchDto")
public class PlanningProjectDTO {
    @ApiModelProperty(value = "产品季id")
    private String seasonId;

    @ApiModelProperty(value = "维度id")
    private String dimensionalityId;

    @ApiModelProperty(value = "款式id")
    private String styleColorId;

    @ApiModelProperty(value = "品类id")
    private String categoryId;

    @ApiModelProperty(value = "企划规划名称")
    private String planningProjectName;

    @ApiModelProperty(value = "渠道名称")
    private String planningChannel;

    @ApiModelProperty(value = "大类")
    private List<String> prodCategory1st;

    @ApiModelProperty(value = "大类名称")
    private List<String> prodCategory1stName;

    @ApiModelProperty(value = "品类")
    private List<String> prodCategory;

    @ApiModelProperty(value = "品类名称")
    private List<String> prodCategoryName;

    @ApiModelProperty(value = "中类code")
    private List<String> prodCategory2nd;

    @ApiModelProperty(value = "中类名称")
    private List<String> prodCategory2ndName;

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
}
