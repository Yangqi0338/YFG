package com.base.sbc.config.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QueryFieldDto extends Page {

    //列头全量匹配
    private String columnHeard;

    //列头查询
    Map<String,String> fieldQueryMap;

    //列头code
    private String tableCode;

    private String queryFieldColumn;

    private String orderBy;

    /*是否导出图片*/
    private String imgFlag;

    private List<String> idList;

    // 是否列头匹配
    private boolean columnGroupSearch;
}
