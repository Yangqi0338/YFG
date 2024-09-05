package com.base.sbc.module.sample.vo;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.annotation.UserAvatar;
import com.base.sbc.module.patternmaking.vo.NodeStatusVo;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private List<NodeStatusVo> nodeStatusList;
    @ApiModelProperty(value = "节点信息Map")
    private Map<String, NodeStatusVo> nodeStatus;


    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "年份")
    private String yearName;

    @ApiModelProperty(value = "季节")
    private String seasonName;

    @ApiModelProperty(value = "月份")
    private String monthName;


    @ApiModelProperty(value = "波段")
    private String bandName;


    @ApiModelProperty(value = "设计款号")
    private String designNo;

    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "配色")
    private String color;
    @ApiModelProperty(value = "品类")
    private String prodCategoryName;
    @ApiModelProperty(value = "大类名称"  )
    private String prodCategory1stName;
    @ApiModelProperty(value = "中类名称"  )
    private String prodCategory2ndName;
    @ApiModelProperty(value = "小类名称"  )
    private String prodCategory3rdName;
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
    @ApiModelProperty(value = "大货款号")
    private String styleName;
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "M月d日HH:mm", timezone = "GMT+8")
    private Date startDate;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "M月d日HH:mm", timezone = "GMT+8")
    private Date updateDate;

//    private List<WorkloadRatingConfigVO> ratingConfigList;
//
//    private WorkloadRatingDetailDTO ratingDetailDTO;
//
//    @JsonIgnore
//    private Style styleEntity;
//
//    private String prodCategory;

    public Map<String, NodeStatusVo> getNodeStatus() {
        return Optional.ofNullable(nodeStatusList).map(ns -> {
            return ns.stream().collect(Collectors.toMap(k -> k.getNode() + StrUtil.DASHED + k.getStatus(), v -> v, (a, b) -> b));
        }).orElse(new HashMap<>(4));
    }

    public String getDesigner() {
        if (StrUtil.contains(designer, StrUtil.COMMA)) {
            return designer.split(",")[0];
        }
        return designer;
    }

    public String getStyle() {
        return Optional.ofNullable(designNo).orElse("") + Optional.ofNullable(styleName).orElse("");
    }

    /**
     * 列头筛选数量
     */
    private Integer groupCount;

    private String barCodeStatus;
    private String suggestion;
    private String suggestionImg;
    private String suggestionImg1;
    private String suggestionImg2;
    private String suggestionImg3;
    private String suggestionImg4;
    private String suggestionVideo;
    private Date confrimDate;
    private String patternMakingDevtType;
    private String sampleBarCodeQrCode;
    private String sampleStyleId;

    private String orderUser;

    private String orderUserId;

     private String fobStatus;
     private String techRemarks;
    @JsonFormat(pattern = "M月d日HH:mm", timezone = "GMT+8")
    private Date processDepartmentDate;

    private String barCodeId;

}
