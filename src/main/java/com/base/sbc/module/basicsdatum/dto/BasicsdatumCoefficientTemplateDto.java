package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BasicsdatumCoefficientTemplateDto extends Page {

    @ApiModelProperty(value = "id"  )
    private String id;

    @ApiModelProperty(value = "模板名称"  )
    private String name;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 季节名称 */
    @ApiModelProperty(value = "季节名称"  )
    private String seasonName;
    /** 渠道 */
    @ApiModelProperty(value = "渠道"  )
    private String channel;
    /** 渠道名称 */
    @ApiModelProperty(value = "渠道名称"  )
    private String channelName;
}
