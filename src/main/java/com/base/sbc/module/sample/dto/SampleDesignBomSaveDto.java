package com.base.sbc.module.sample.dto;

import com.base.sbc.module.pack.dto.PackBomDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：样衣设计物料清单保存Dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.SampleDesignBomSaveDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-03 14:00
 */
@Data
@ApiModel("样衣设计物料清单保存Dto SampleDesignBomSaveDto")
public class SampleDesignBomSaveDto {

    @ApiModelProperty(value = "样衣设计id")
    private String sampleDesignId;

    @ApiModelProperty(value = "物料信息")
    private List<PackBomDto> bomList;


}
