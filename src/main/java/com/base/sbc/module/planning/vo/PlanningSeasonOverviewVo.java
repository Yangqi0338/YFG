package com.base.sbc.module.planning.vo;

import cn.hutool.core.date.DateUtil;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.sample.vo.SampleUserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：产品季总览 列表vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-18 14:53
 */

@Data
@ApiModel("产品季总览-列表vo PlanningSeasonOverviewVo")
public class PlanningSeasonOverviewVo extends PlanningCategoryItem {
    @ApiModelProperty(value = "产品季名称")
    private String name;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "年份")
    private String year;
    @ApiModelProperty(value = "年份名称")
    private String yearName;

    @ApiModelProperty(value = "季节")
    private String season;
    @ApiModelProperty(value = "季节名称")
    private String seasonName;

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "月份名称")
    private String monthName;

    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "性别名称")
    private String sexName;

    @ApiModelProperty(value = "波段(编码)")
    private String bandCode;
    @ApiModelProperty(value = "波段(名称)")
    private String bandCodeName;

    @ApiModelProperty(value = "生产模式")
    private String devtType;
    @ApiModelProperty(value = "生产模式名称")
    private String devtTypeName;

    @ApiModelProperty(value = "渠道")
    private String channel;
    @ApiModelProperty(value = "渠道名称")
    private String channelName;


    @ApiModelProperty(value = "样衣状态:0未开款，1已开款，2已下发打板(完成)")
    private String sdStatus;
    @ApiModelProperty(value = "样衣审批状态:草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)")
    private String sdConfirmStatus;

    public String getRemainingTime() {
        if (getPlanningFinishDate() == null || getSendDate() == null) {
            return "";
        }
        Date now = new Date();
        String abs = DateUtil.compare(getPlanningFinishDate(), now) < 0 ? "-" : "";
        return abs + DateUtil.betweenDay(getPlanningFinishDate(), now, false) + "天";

    }

    @ApiModelProperty(value = "设计师")
    public List<SampleUserVo> designers;
}
