package com.base.sbc.module.patternmaking.vo;


import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.patternmaking.entity.PatternMakingWorkLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

/**
 * 类描述：打版管理-工作记录
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternMakingWorkLogVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 19:44
 */
@Data
@ApiModel("打版管理-工作记录Vo PatternMakingWorkLogVo")
public class PatternMakingWorkLogVo extends PatternMakingWorkLog {


    @ApiModelProperty(value = "打工人")
    private String worker;

    public String getMsg() {
        String tmp = "{}在{}至{}耗费了{}{}";
        return StrUtil.format(tmp,
                Optional.ofNullable(worker).orElse(""),
                DateUtil.format(getStartDate(), DatePattern.NORM_DATETIME_MINUTE_FORMATTER),
                DateUtil.format(getEndDate(), DatePattern.NORM_DATETIME_MINUTE_FORMATTER),
                DateUtil.formatBetween(getStartDate(), getEndDate(), BetweenFormatter.Level.SECOND),
                Opt.ofBlankAble(getRemarks()).orElse("")
        );

    }
}
