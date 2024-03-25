package com.base.sbc.module.report.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import lombok.Data;

import java.util.List;

@Data
public class HangTagReportQueryDto extends QueryFieldDto {
    /**
     * 大货款集合
     */
    private List<String> bulkStyleNos;
    /**
     * 年份
     */
    private String year;
    /**
     * 季节
     */
    private String season;
}
