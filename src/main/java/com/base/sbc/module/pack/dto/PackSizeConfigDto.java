package com.base.sbc.module.pack.dto;

import com.base.sbc.module.pack.entity.PackSizeConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackSizeConfigDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-01 14:07
 */
@Data
@ApiModel("资料包-尺寸表配置 PackSizeConfigDto")
public class PackSizeConfigDto extends PackSizeConfig {

    @ApiModelProperty(value = "当差id")
    private String differenceId;

    /*保存的尺码*/
    @ApiModelProperty(value = "保存的尺码")
    private List<String> activeSizesList;

    @ApiModelProperty(value = "测量编码")
    private String partCoed;
}
