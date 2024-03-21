package com.base.sbc.module.report.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import lombok.Data;

import java.util.List;

@Data
public class StyleAnalyseQueryDto extends QueryFieldDto {

    private String id;
    /**
     * 设计款/大货款 集合
     */
    private List<String> designNoAndStyleNos;
    /**
     * 年份
     */
    private String year;
    /**
     * 季节
     */
    private String season;
}
