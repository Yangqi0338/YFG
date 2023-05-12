package com.base.sbc.module.sample.vo;


import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.entity.Technology;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：样衣明细
 * @address com.base.sbc.module.sample.vo.SampleVo
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-11 11:48
 * @version 1.0
 */
@Data
@ApiModel("样衣分页返回 SampleVo ")
public class SampleVo extends Sample {
    @ApiModelProperty(value = "工艺信息"  )
    private Technology technology;
}
