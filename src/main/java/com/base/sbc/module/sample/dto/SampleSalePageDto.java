package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 类描述： 样衣销售分页查询
 * @address com.base.sbc.module.sample.dto.SampleSalePageDto
 */
@Data
@ApiModel("样衣销售分页查询 SampleSalePageDto")
public class SampleSalePageDto extends Page {

    /** 开始时间 */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;

    /** 结束时间 */
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;

    /** 企业编号 */
    @ApiModelProperty(value = "企业编号", example = "680014765321355265")
    private String companyCode;

    /** 样衣销售ID */
    @ApiModelProperty(value = "样衣销售ID", example = "680014765321355265")
    private String sampleSaleId;

    /** 样衣销售明细ID */
    @ApiModelProperty(value = "样衣销售明细ID", example = "680014765321355265")
    private String sampleSaleItemId;

    /** 在库状态：0-未入库，1-在库，2-借出，3-删除，4-售出 */
    @ApiModelProperty(value = "在库状态", example = "1")
    private String status;
}
