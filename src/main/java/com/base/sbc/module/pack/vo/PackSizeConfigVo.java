package com.base.sbc.module.pack.vo;

import com.base.sbc.module.pack.entity.PackSizeConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackSizeConfigVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-01 11:09
 */

@Data
@ApiModel("资料包-尺寸表配置 PackSizeConfigVo")
public class PackSizeConfigVo extends PackSizeConfig {

    @ApiModelProperty(value = "大货款")
    private String styleNo;
}
