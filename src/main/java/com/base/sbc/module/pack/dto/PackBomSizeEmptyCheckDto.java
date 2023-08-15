package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackBomEmptyCheckDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-14 19:59
 */
@Data
@ApiModel("资料包-物料清单非空校验dto PackBomEmptyCheckDto")
public class PackBomSizeEmptyCheckDto {

    /**
     * 尺码
     */
    @ApiModelProperty(value = "尺码")
    @NotBlank(message = "尺码不能为空")
    private String size;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")

    @NotNull(message = "用量不能为空")
    private BigDecimal quantity;
    /**
     * 门幅/规格
     */
    @ApiModelProperty(value = "门幅/规格")
    @NotEmpty(message = "门幅/规格不能为空")
    private String width;

}
