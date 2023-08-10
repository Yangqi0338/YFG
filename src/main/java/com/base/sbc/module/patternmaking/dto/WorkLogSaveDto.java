package com.base.sbc.module.patternmaking.dto;

import com.base.sbc.module.patternmaking.entity.WorkLog;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.WorkLogSaveDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-10 20:12
 */
@Data
@ApiModel("工作小账保存 WorkLogSaveDto ")
public class WorkLogSaveDto extends WorkLog {
}
