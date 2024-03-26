package com.base.sbc.module.report.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class MaterialSupplierQuoteQueryDto extends QueryFieldDto {
    /**
     * 物料号集合
     */
    private List<String> materialNos;
    /**
     * 年份
     */
    private String year;
    /**
     * 季节
     */
    private String season;
}
