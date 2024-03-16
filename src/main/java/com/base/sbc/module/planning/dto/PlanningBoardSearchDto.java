package com.base.sbc.module.planning.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：企划看板筛选条件
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.dto.PlanningBoardSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-27 09:25
 */
@Data
@ApiModel("企划看板筛选条件 PlanningBoardSearchDto")
public class PlanningBoardSearchDto {


    @ApiModelProperty(value = "产品季id", required = true, example = "1667076468196474882")
    @NotBlank(message = "产品季不能为空")
    private String planningSeasonId;

    @ApiModelProperty(value = "关键字筛选", required = false, example = "5CA232731")
    private String search;

    @ApiModelProperty(value = "月份", required = false, example = "01")
    private String month;

    @ApiModelProperty(value = "波段", required = false, example = "1")
    private String bandCode;

    @ApiModelProperty(value = "波段名称", required = false, example = "1A")
    private String bandName;

    @ApiModelProperty(value = "品类", required = false, example = "a1")
    private String prodCategory;

    @ApiModelProperty(value = "品类名称", required = false, example = "a1")
    private String prodCategoryName;

    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;

    @ApiModelProperty(value = "维度字段id", required = false, example = "122222")
    private String fieldId;

    @ApiModelProperty(value = "渠道", required = true)
    private String channel;

    // 新增查询条件
    /**
     * 设计师 ID，多个使用逗号分割
     */
    @ApiModelProperty(value = "设计师 ID，多个使用逗号分割", required = false)
    private String designerIds;

    /**
     * 下稿面料，维度系数中的面料类型
     */
    @ApiModelProperty(value = "下稿面料，维度系数中的面料类型，多个使用逗号分隔", required = false)
    private String fabricsUnderTheDrafts;

}
