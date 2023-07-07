package com.base.sbc.module.pack.dto;

import com.base.sbc.module.pack.entity.PackBomVersion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：资料包-物料清单Dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackSizeVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 10:33
 */
@Data
@ApiModel("资料包-物料清单Dto PackBomVersionDto")
public class PackBomVersionDto extends PackBomVersion {

    @ApiModelProperty(value = "主数据id")
    @NotBlank(message = "主数据id为空")
    private String foreignId;

    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包")
    @NotBlank(message = "资料包类型为空")
    private String packType;
}
