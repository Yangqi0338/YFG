package com.base.sbc.module.planning.vo;

import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class PlanningCategoryItemVo extends PlanningCategoryItem {

    @ApiModelProperty(value = "品类信息的大类品类id")
    private String parentCategoryIds;
    @ApiModelProperty(value = "品类信息的大类品类名称")
    private String parentCategoryName;
    @ApiModelProperty(value = "关联的素材库列表")
    List<PlanningCategoryItemMaterial> materialVoList;


}
