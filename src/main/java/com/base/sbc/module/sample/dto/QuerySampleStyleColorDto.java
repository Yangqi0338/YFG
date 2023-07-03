package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
/*查询款式配色*/
public class QuerySampleStyleColorDto extends Page {
    /** 样衣id */
    @ApiModelProperty(value = "样衣id"  )
    private String sampleDesignId;

    @ApiModelProperty(value = "大货编号多个使用，分割"  )
    private String styleNo;
}
