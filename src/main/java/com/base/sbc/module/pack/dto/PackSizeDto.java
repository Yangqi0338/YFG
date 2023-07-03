package com.base.sbc.module.pack.dto;

import com.base.sbc.module.pack.entity.PackSize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：资料包-尺寸表dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackSizeVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 10:33
 */
@Data
@ApiModel("资料包-尺寸表dto PackSizeDto")
public class PackSizeDto extends PackSize {

    @ApiModelProperty(value = "外键id")
    @NotBlank(message = "主数据id为空")
    private String foreignId;

    @ApiModelProperty(value = "资料包类型:design:设计资料包/bigGoods:标准资料包(大货资料包)")
    @NotBlank(message = "资料包类型为空")
    private String packType;
}
