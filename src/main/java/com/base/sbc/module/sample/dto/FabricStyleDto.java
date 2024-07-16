package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@ApiModel("物料款式列表")
@AllArgsConstructor
@NoArgsConstructor
public class FabricStyleDto extends Page {

    @ApiModelProperty(value = "企业编码")
    private String companyCode;

    @ApiModelProperty(value = "汇总Id"  )
    private String fabricSummaryId;

    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
    private String materialCode;

    @ApiModelProperty(value = "款式(大货款号)"  )
    private String styleNo;

    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;

    @ApiModelProperty(value = "开始时间"  )
    private Date startTime;

    @ApiModelProperty(value = "结束时间"  )
    private Date endTime;





}
