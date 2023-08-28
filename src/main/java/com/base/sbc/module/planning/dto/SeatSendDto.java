package com.base.sbc.module.planning.dto;

import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("坑位下发设计dto SeadSendDto")
public class SeatSendDto extends PlanningCategoryItem {


    @ApiModelProperty(value = "预计上市时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expectedLaunchDate;
}
