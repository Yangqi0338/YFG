package com.base.sbc.module.style.vo;


import cn.hutool.core.date.DateUtil;
import com.base.sbc.module.style.entity.Style;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：款式设计分页返回
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.SamplePageVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-10 11:54
 */
@Data
@ApiModel("样衣分页返回 SamplePageVo ")
public class StylePageVo extends Style {
    @ApiModelProperty(value = "设计师头像", example = "https://sjkj-demo.oss-cn-shenzhen.aliyuncs.com/null/userHead/09/02/8361ea39-21d2-4944-b150-d2e69bb68254.png")
    private String aliasUserAvatar;

    @JsonIgnore
    /*款式配色*/
    private List<StyleColorVo> styleColorVoList;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "相差天数")
    public String getRemainingTime() {
        if (getPlanningFinishDate() == null) {
            return "";
        }
        Date now = new Date();
        String abs = DateUtil.compare(getPlanningFinishDate(), now) < 0 ? "-" : "";
        return abs + DateUtil.betweenDay(getPlanningFinishDate(), now, false);

    }

    /**
     * 下单标记（0否 1是）
     */
    @ApiModelProperty(value = "下单标记（0否 1是）")
    private String orderFlag;
}
