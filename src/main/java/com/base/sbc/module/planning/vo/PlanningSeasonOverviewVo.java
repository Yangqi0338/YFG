package com.base.sbc.module.planning.vo;

import cn.hutool.core.date.DateUtil;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ApiModelProperty(value = "预计上市时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expectedLaunchDate;

    @ApiModelProperty(value = "样衣到仓时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sampleDeliveryDate;

    @ApiModelProperty(value = "样衣状态:0未开款，1已开款，2已下发打板(完成)")
    private String sdStatus;
    @ApiModelProperty(value = "样衣审批状态:草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)")
    private String sdConfirmStatus;

    @ApiModelProperty(value = "企划图")
    private String planningPic;

    @ApiModelProperty(value = "款式id")
    private String  styleId;

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

    @ApiModelProperty(value = "关联的素材库列表")
    List<PlanningCategoryItemMaterial> materialVoList;
    @ApiModelProperty(value = "渠道序号")
    Integer sort;

    /**
     * 产品线
     */
    private String productLineName;
}
