/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import cn.hutool.core.text.StrJoiner;
import com.base.sbc.module.basicsdatum.enums.SeasonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * 类描述：基础资料-复盘管理 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.entity.ReplayConfig
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-3 18:43:08
 */
@Data
@NoArgsConstructor
public class ReplayConfigTimeDTO {
    /** 季节名称 */
    @ApiModelProperty(value = "季节名称")
    private SeasonEnum season;

    /** 开始月份 */
    @ApiModelProperty(value = "开始月份")
    @DateTimeFormat(pattern = "yyyy年MM月")
    @JsonFormat(pattern = "yyyy年MM月", timezone = "GMT+8")
    private YearMonth startMonth;

    /** 结束月份 */
    @ApiModelProperty(value = "结束月份")
    @DateTimeFormat(pattern = "yyyy年MM月")
    @JsonFormat(pattern = "yyyy年MM月", timezone = "GMT+8")
    private YearMonth endMonth;

    public ReplayConfigTimeDTO(SeasonEnum season) {
        this.season = season;
    }

    /**
     * 重写toString 为了复盘管理的列表显示,前端可以直接使用这个字符串
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");
        StrJoiner joiner = StrJoiner.of("~");
        if (startMonth != null) joiner.append(startMonth.format(formatter));
        if (endMonth != null) joiner.append(endMonth.format(formatter));
        return season.getName() + ": " + joiner;
    }
}
