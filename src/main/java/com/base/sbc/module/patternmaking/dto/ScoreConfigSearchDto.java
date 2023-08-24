package com.base.sbc.module.patternmaking.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.ScoreConfigSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-24 13:39
 */
@Data
@ApiModel("评分配置查询Dto  ScoreConfigSearchDto")
public class ScoreConfigSearchDto extends Page {

    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;
    @ApiModelProperty(value = "品类code")
    private String prodCategory;
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;

}
