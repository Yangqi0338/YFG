package com.base.sbc.module.band.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

@Data
public class QueryBandDto extends Page {
    /*波段名称*/
    private String bandName;
    /*季节*/
    private String season;
    /*月份*/
    private String month;
    private String code;

    private String  seasonName;
}
