package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：bom 查询条件
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackBomPageSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-03 10:12
 */
@Data
@ApiModel("资料包-物料清单筛选条件 PackBomSearchDto")
public class PackBomSearchDto {
    @ApiModelProperty(value = "版本id")
    @NotBlank(message = "版本id为空")
    private String bomVersionId;

    @ApiModelProperty(value = "保存类型:1覆盖,0或者空为不覆盖（追加）")
    private String overlayFlg;
}
