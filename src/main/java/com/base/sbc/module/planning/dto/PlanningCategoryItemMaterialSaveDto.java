package com.base.sbc.module.planning.dto;

import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial;
import lombok.Data;

import java.util.List;

/** 关联素材保存
 * 类描述：
 * @address com.base.sbc.module.planning.dto.PlanningCategoryItemMaterialSaveDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-18 14:39
 * @version 1.0
 */
@Data
public class PlanningCategoryItemMaterialSaveDto extends PlanningCategoryItem {

    private List<PlanningCategoryItemMaterial> item;
}
