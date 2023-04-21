package com.base.sbc.module.planning.vo;

import com.base.sbc.module.common.vo.UserInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：波段企划汇总信息
 * @address com.base.sbc.module.planning.vo.PlanningBandSummaryInfoVo
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-21 09:27
 * @version 1.0
 */
@Data
@ApiModel("商品企划-波段企划汇总信息 PlanningBandSummaryInfoVo")
public class PlanningBandSummaryInfoVo {

    @ApiModelProperty(value = "企划需求数"  ,example = "100")
    private Long planRequirementNum;

    @ApiModelProperty(value = "企划"  ,example = "100")
    private Integer planReceiveNum;

    @ApiModelProperty(value = "设计完成数"  ,example = "100")
    private Integer designCompletionNum;
    @ApiModelProperty(value = "参与用户信息"   )
    public List<UserInfoVo> userList;

}
