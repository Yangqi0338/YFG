package com.base.sbc.module.moreLanguage.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.converters.date.DateStringConverter;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageStatusCountryDto {

    @ExcelIgnore
    @ApiModelProperty(value = "模板编码")
    private String code;

    @Excel(name = "模板名称", width = 10.0)
    @ApiModelProperty(value = "模板名称")
    private String name;

    @Excel(name = "审核状态", width = 15.0)
    private String status;

    @Excel(name = "审核时间", format = "yyyy/MM/dd hh:mm:ss", width = 30.0)
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss", timezone = "GMT+8")
    private Date time;

    @Excel(name = "审核人", width = 15.0)
    private String person;

}
