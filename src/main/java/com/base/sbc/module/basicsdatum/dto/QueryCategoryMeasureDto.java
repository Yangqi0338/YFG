package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

@Data
public class QueryCategoryMeasureDto extends Page {

    /** 编码 */
    private String code;
    /*名称*/
    private  String name;
    /*档差*/
    private String rangeDifferenceName;
}
