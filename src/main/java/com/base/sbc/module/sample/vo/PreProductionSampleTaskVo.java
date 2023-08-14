package com.base.sbc.module.sample.vo;

import com.base.sbc.config.common.annotation.UserAvatar;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 类描述：产前样-任务
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.PreProductionSampleTaskVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-18 15:47
 */
@Data
@ApiModel("产前样-任务 PreProductionSampleTask")
public class PreProductionSampleTaskVo extends PreProductionSampleTask {


    @UserAvatar("cutterId")
    @ApiModelProperty(value = "裁剪工头像")
    private String cutterAvatar;

    @UserAvatar("stitcherId")
    @ApiModelProperty(value = "车缝工头像")
    private String stitcherAvatar;


    @UserAvatar("gradingId")
    @ApiModelProperty(value = "放码师头像")
    private String gradingAvatar;

    @UserAvatar("technologistId")
    @ApiModelProperty(value = "工艺师头像")
    private String technologistAvatar;

    @ApiModelProperty(value = "节点信息list")
    private List<NodeStatus> nodeStatusList;
    @ApiModelProperty(value = "节点信息Map")
    private Map<String, NodeStatus> nodeStatus;


    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "年份")
    private String yearName;

    @ApiModelProperty(value = "季节")
    private String seasonName;

    @ApiModelProperty(value = "月份")
    private String monthName;


    @ApiModelProperty(value = "波段")
    private String bandCode;


    @ApiModelProperty(value = "设计款号")
    private String designNo;

    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "配色")
    private String color;
    @ApiModelProperty(value = "品类")
    private String prodCategoryName;
    @ApiModelProperty(value = "款式图")
    private String stylePic;

    @ApiModelProperty(value = "版师id")
    private String patternDesignId;

    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;
    @UserAvatar("patternDesignId")
    @ApiModelProperty(value = "版师头像")
    private String patternDesignAvatar;

    @ApiModelProperty(value = "设计师头像")
    @UserAvatar("designerId")
    private String designerAvatar;
    @ApiModelProperty(value = "设计师名称")
    private String designer;
    @ApiModelProperty(value = "设计师id")
    private String designerId;
}
