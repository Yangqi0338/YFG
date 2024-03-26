package com.base.sbc.module.patternmaking.vo;

import com.base.sbc.module.patternmaking.entity.WorkLog;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.WorkLogVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-10 21:24
 */
@Data
@ApiModel("工作小账Vo WorkLogVo ")
public class WorkLogVo extends WorkLog {
    @ApiModelProperty(value = "工作日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workDate;

    /**
     * 列头筛选数量
     */
    private Integer groupCount;
}
