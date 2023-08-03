package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SampleSearchDTO extends Page {
    private String companyCode;

    /**
     * 状态
     */
    @ApiModelProperty("状态：0-未入库，1-在库，2-借出，3-删除，4-售出，5-盘点中，6.归还")
    private String status;
    /**
     * 是否通过搜索查询
     */
    @ApiModelProperty("是否通过搜索查询 1.是 0.否")
    private String searchFlag;
    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private String positionId;


}
