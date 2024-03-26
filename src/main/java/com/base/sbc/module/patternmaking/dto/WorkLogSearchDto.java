package com.base.sbc.module.patternmaking.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：工作小账搜索条件
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.WorkLogSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-10 19:31
 */
@Data
@ApiModel("工作小账搜索条件 WorkLogSearchDto ")
public class WorkLogSearchDto extends QueryFieldDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "工作日期")
    private String workDate;
    @ApiModelProperty(value = "登记类型:修改、小样、其他")
    private String logType;

    @ApiModelProperty(value = "参考类型")
    private String numType;

    @ApiModelProperty(value = "工作人员Id")
    private String workerId;
}
