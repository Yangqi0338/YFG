package com.base.sbc.module.planning.dto;


import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 类描述：产品季总览-波段展开查询
 * @address com.base.sbc.module.planning.dto.ProductSeasonBandSearchDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-20 14:52
 * @version 1.0
 */
@Data
@ApiModel("产品季总览-展开按品类查询 ProductSeasonExpandByCategorySearchDto")
public class ProductSeasonExpandByCategorySearchDto {
    @ApiModelProperty(value = "产品季id" ,required = true,example = "122222")
    @NotNull(message = "产品季id不能为空")
    private String planningSeasonId;
}
