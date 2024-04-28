package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class FabricSummaryGroupDto extends Page {

    /** 组名称 */
    @ApiModelProperty(value = "组名称"  )
    private String name;

    /**  创建者名称 */
    private String createName;

    /** 开始日期 */
    private Date startDate;

    /** 结束日期 */
    private Date endDate;
}
