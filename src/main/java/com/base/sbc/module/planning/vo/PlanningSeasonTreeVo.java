package com.base.sbc.module.planning.vo;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.planning.entity.PlanningSeason;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：树
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.PlanningSeasonTreeVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-21 15:13
 */
@Data
@ApiModel("商品企划-产品季树 PlanningSeasonTreeVo")
public class PlanningSeasonTreeVo extends PlanningSeason {

    @ApiModelProperty(value = "skc数")
    private Long skcCount;

    public String getLabel() {
        return StrUtil.format("{} ({})", getName(), skcCount);
    }
}
