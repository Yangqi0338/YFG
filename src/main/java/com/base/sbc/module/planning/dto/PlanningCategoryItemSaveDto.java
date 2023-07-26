package com.base.sbc.module.planning.dto;

import com.base.sbc.module.formType.entity.FieldVal;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 类描述：坑位信息保存dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.dto.PlanningCategoryItemSaveDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-08 11:40
 */
@Data
@ApiModel("坑位信息保存dto PlanningCategoryItemSaveDto")
public class PlanningCategoryItemSaveDto extends PlanningCategoryItem {

    @ApiModelProperty(value = "维度值")
    List<FieldVal> fieldVals;
}
