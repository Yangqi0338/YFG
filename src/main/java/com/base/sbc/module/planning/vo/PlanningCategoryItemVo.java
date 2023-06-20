package com.base.sbc.module.planning.vo;

import cn.hutool.core.util.StrUtil;
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

    public List<String> getCategoryIdList() {
        if (StrUtil.isNotEmpty(getCategoryIds())) {
            return StrUtil.split(getCategoryIds(), StrUtil.COMMA);
        }
        return StrUtil.split(parentCategoryIds, StrUtil.COMMA);
    }

    public List<String> getCategoryNameList() {
        if (StrUtil.isNotEmpty(getCategoryName())) {
            return StrUtil.split(getCategoryName(), StrUtil.COMMA);
        }
        return StrUtil.split(parentCategoryName, StrUtil.COMMA);
    }

}
