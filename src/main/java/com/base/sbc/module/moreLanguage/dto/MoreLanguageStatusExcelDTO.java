package com.base.sbc.module.moreLanguage.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageStatusExcelDTO {

    @ExcelProperty("大货款号")
    private String bulkStyleNo;

}
