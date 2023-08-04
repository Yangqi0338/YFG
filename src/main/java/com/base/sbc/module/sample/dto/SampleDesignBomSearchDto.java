package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：款式设计-物料信息 筛选dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.SampleDesignBomSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-03 14:03
 */
@Data
@ApiModel("款式设计-物料信息 筛选dto SampleDesignBomSearchDto")
public class SampleDesignBomSearchDto extends Page {

    @ApiModelProperty(value = "款式设计id")
    private String styleId;
}
