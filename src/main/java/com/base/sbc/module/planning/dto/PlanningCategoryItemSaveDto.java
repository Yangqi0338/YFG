package com.base.sbc.module.planning.dto;

import com.base.sbc.module.formType.entity.FieldVal;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

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
    /**
     * 品类id(小类)
     */
    @ApiModelProperty(value = "品类id(小类)")
    @NotBlank(message = "品类不能为空")
    private String categoryId;
    /**
     * 品类名称路径:(中类/小类)
     */
    @NotBlank(message = "品类不能为空")
    @ApiModelProperty(value = "品类名称路径:(中类/小类)")
    private String categoryName;
    /**
     * 品类id路径:(中类/小类)
     */
    @ApiModelProperty(value = "品类id路径:(中类/小类)")
    @NotBlank(message = "品类不能为空")
    private String categoryIds;

    @ApiModelProperty(value = "维度值")
    List<FieldVal> fieldVals;
}
