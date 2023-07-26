package com.base.sbc.module.planning.vo;

import com.base.sbc.client.amc.TeamVo;
import com.base.sbc.module.planning.entity.PlanningSeason;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("商品企划-产品季列表 PlanningSeasonSaveVo")
public class PlanningSeasonVo extends PlanningSeason {

    @ApiModelProperty(value = "用户头像", example = "https://sjkj-demo.oss-cn-shenzhen.aliyuncs.com/null/userHead/09/02/8361ea39-21d2-4944-b150-d2e69bb68254.png")
    private String aliasUserAvatar;
    @ApiModelProperty(value = "skc数")
    private Long skcCount;

    @ApiModelProperty(value = "团队")
    private List<TeamVo> teamList;
}
