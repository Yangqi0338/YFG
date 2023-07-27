package com.base.sbc.module.planning.vo;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.annotation.UserAvatar;
import com.base.sbc.module.planning.entity.PlanningChannel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.PlanningChannelVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-21 16:06
 */
@Data
public class PlanningChannelVo extends PlanningChannel {

    @ApiModelProperty("产品季名称")
    private String name;
    @ApiModelProperty(value = "用户头像", example = "https://sjkj-demo.oss-cn-shenzhen.aliyuncs.com/null/userHead/09/02/8361ea39-21d2-4944-b150-d2e69bb68254.png")
    @UserAvatar("createId")
    private String aliasUserAvatar;

    @ApiModelProperty(value = "skc数")
    private Long skcCount;

    public String getLabel() {
        return StrUtil.format("{} {}", name, getChannelName());
    }

}
