package com.base.sbc.module.fabric.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("面料企划列表")
public class FabricPlanningListVO {
    private String id;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;
    @ApiModelProperty(value = "修改人")
    private String updateName;
    @ApiModelProperty(value = "创建人")
    private String createName;
    /**
     * 面料企划
     */
    @ApiModelProperty(value = "面料企划")
    private String fabricPlanningName;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    private String category;

    /**
     * 面料数
     */
    @ApiModelProperty(value = "面料数")
    private String fabricCount;

    /**
     * 审核状态：1.未提交、2.审核中、3.审核通过、4.审核失败
     */
    @ApiModelProperty(value = "审核状态：1.未提交、2.审核中、3.审核通过、4.审核失败")
    private String approveStatus;


}
