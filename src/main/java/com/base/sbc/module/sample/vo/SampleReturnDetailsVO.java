package com.base.sbc.module.sample.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("样衣归还明细")
public class SampleReturnDetailsVO {
    @ApiModelProperty("样衣借还明细id")
    private String id;
    private String sampleCirculateId;
    @ApiModelProperty("设计款号")
    private String designNo;
    @ApiModelProperty("数量")
    private String count;
    @ApiModelProperty("借出类型：1-内部借出，2-外部借出")
    private String borrowType;
    @ApiModelProperty("借出类型名称")
    private String borrowTypeName;
    @ApiModelProperty("归还日期")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date expectReturnDate;
    @ApiModelProperty("借出原因")
    private String borrowReason;
    @ApiModelProperty("借出人")
    private String borrowName;
    @ApiModelProperty("经手人")
    private String operateName;
    @ApiModelProperty("借出日期")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date borrowDate;
    @ApiModelProperty("借出天数")
    private String borrowDays;

}
