package com.base.sbc.module.sample.dto;

import com.base.sbc.module.sample.entity.Sample;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：样衣dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.SampleDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-09 15:41
 */
@Data
@ApiModel("样衣保存修改 SampleDto")
public class SampleSaveDto extends Sample {


    @ApiModelProperty(value = "工艺信息")
    private TechnologySaveDto technology;

}
