package com.base.sbc.module.moreLanguage.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageStatusExcelResultDTO {
    @ApiModelProperty(value = "款号")
    private String bulkStyleNo;
    @ApiModelProperty(value = "状态: 0成功 1异常 2失败")
    private Integer status;
    @ApiModelProperty(value = "导入结果")
    private String message;

    public MoreLanguageStatusExcelResultDTO(String bulkStyleNo) {
        this.bulkStyleNo = bulkStyleNo;
        this.status = 0;
        this.message = "成功";
    }
}
