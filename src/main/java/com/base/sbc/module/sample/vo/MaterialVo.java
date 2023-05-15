package com.base.sbc.module.sample.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：样衣关联的素材库
 * @address com.base.sbc.module.sample.vo.MaterialVo
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-12 17:58
 * @version 1.0
 */
@Data
@ApiModel("样衣关联的素材库 MaterialVo ")
public class MaterialVo {

    @ApiModelProperty(value = "素材库id"  )
    private String materialCategoryId;
    /** 素材id */
    @ApiModelProperty(value = "素材id"  )
    private String materialId;
}
