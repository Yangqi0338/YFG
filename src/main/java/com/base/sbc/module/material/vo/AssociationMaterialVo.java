package com.base.sbc.module.material.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：关联的素材库（展示）
 * @address com.base.sbc.module.material.vo.AssociationMateralVo
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-23 11:14
 * @version 1.0
 */
@Data
@ApiModel("关联的素材库（展示） AssociationMateralVo ")
public class AssociationMaterialVo {
    @ApiModelProperty(value = "素材库id")
    private String materialCategoryId;
    @ApiModelProperty(value = "素材库名称")
    private String name;
    @ApiModelProperty(value = "素材信息")
    List<MaterialVo> materialList;

}
