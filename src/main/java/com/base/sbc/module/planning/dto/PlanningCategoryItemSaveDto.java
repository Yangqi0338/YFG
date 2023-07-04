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

    @ApiModelProperty(value = "品类名称路径:(大类/品类/中类/小类)")
    private String categoryName;

    @ApiModelProperty(value = "品类id路径:(大类,品类,中类,小类)")
    private String categoryIds;

    @ApiModelProperty(value = "大类id")
    @NotBlank(message = "大类不能为空")
    private String prodCategory1st;

    @ApiModelProperty(value = "品类id")
    @NotBlank(message = "品类不能为空")
    private String prodCategory;

    @ApiModelProperty(value = "中类id")
    @NotBlank(message = "中类不能为空")
    private String prodCategory2nd;

    @ApiModelProperty(value = "小类")
    @NotBlank(message = "小类不能为空")
    private String prodCategory3rd;

    @ApiModelProperty(value = "维度值")
    List<FieldVal> fieldVals;
}
