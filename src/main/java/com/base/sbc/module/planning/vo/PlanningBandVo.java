package com.base.sbc.module.planning.vo;

import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("商品企划-波段企划 PlanningSeasonSaveVo")
public class PlanningBandVo extends PlanningBand {

    @ApiModelProperty(value = "用户头像", example = "https://sjkj-demo.oss-cn-shenzhen.aliyuncs.com/null/userHead/09/02/8361ea39-21d2-4944-b150-d2e69bb68254.png")
    private String aliasUserAvatar;

    @ApiModelProperty(value = "品类信息")
    private List<PlanningCategory> categoryData;
    @ApiModelProperty(value = "坑位信息")
    private List<PlanningCategoryItemVo> categoryItemData;

    @ApiModelProperty(value = "skc数")
    private Long skcCount;

}
