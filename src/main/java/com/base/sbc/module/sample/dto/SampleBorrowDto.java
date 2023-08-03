package com.base.sbc.module.sample.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("样衣借出保存 SampleDto")
public class SampleBorrowDto {

    @ApiModelProperty("样衣明细id")
    @NotEmpty(message = "样衣为空")
    private List<String> sampleItemIds;

    @ApiModelProperty("设计款号")
    @NotEmpty(message = "设计款号不可为空")
    private String designNo;
    @ApiModelProperty("借出数量")
    private Integer borrowCount;
    @ApiModelProperty("借出类型 1-内部借出，2-外部借出")
    private String borrowType;
    @ApiModelProperty("归还日期")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date expectReturnDate;

    @ApiModelProperty("借出原因")
    private String borrowReason;

    @ApiModelProperty("借出人")
    private String borrowId;
    @ApiModelProperty("借出人名称")
    private String borrowName;

    @ApiModelProperty("经手人ID")
    private String operateId;
    @ApiModelProperty("经手人")
    private String operateName;

    @ApiModelProperty("借出日期")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date borrowDate;
    @ApiModelProperty("备注")
    private String borrowRemarks;

}
