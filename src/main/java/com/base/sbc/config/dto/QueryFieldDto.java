package com.base.sbc.config.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

import java.util.Map;

@Data
public class QueryFieldDto extends Page {



    private String search;


    //列头全量匹配
    private String columnHeard;

    //列头查询
    Map<String,String> fieldQueryMap;

    //列头code
    private String tableCode;

}
