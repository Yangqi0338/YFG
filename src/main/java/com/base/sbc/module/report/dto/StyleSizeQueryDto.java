package com.base.sbc.module.report.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class StyleSizeQueryDto extends QueryFieldDto {
    /**
     * 大货款集合
     */
    private List<String> styleColorNos;
}
