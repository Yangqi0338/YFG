package com.base.sbc.module.pack.dto;

import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomColor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 类描述：资料包-物料清单dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackSizeVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 10:33
 */
@Data
@ApiModel("资料包-物料清单dto PackBomDto")
public class PackBomDto extends PackBom {

    @ApiModelProperty(value = "版本id")
    @NotBlank(message = "版本id不能为空")
    private String bomVersionId;

    @ApiModelProperty(value = "尺码信息")
    List<PackBomSizeDto> packBomSizeList;

    @ApiModelProperty(value = "物料清单-配色列表")
    private List<PackBomColorDto> packBomColorDtoList;

    @ApiModelProperty(value = "核价信息是否下发标识")
    private String pricingSendFlag;

    @ApiModelProperty(value = "替换物料标记")
    private String replaceMaterialFlag;

    @ApiModelProperty(value = "替换之前的搭配")
    private String replaceCollocationName;

    @ApiModelProperty(value = "是否拷贝")
    private YesOrNoEnum copy;

}
