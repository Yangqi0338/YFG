package com.base.sbc.module.style.vo;


import com.base.sbc.config.common.annotation.UserAvatar;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.sample.vo.MaterialVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StylePic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * 类描述：样衣明细
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.SampleVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-11 11:48
 */
@Data
@ApiModel("款式设计明细返回 SampleVo ")
public class StyleVo extends Style {

    @ApiModelProperty(value = "设计工艺员头像")
    @UserAvatar("technicianId")
    private String technicianAvatar;

    @ApiModelProperty(value = "材料专员头像")
    @UserAvatar("fabDevelopeId")
    private String fabDevelopeAvatar;
    @ApiModelProperty(value = "版师头像")
    @UserAvatar("patternDesignId")
    private String patternDesignAvatar;
    @ApiModelProperty(value = "跟款设计师头像")
    @UserAvatar("merchDesignId")
    private String merchDesignAvatar;
    @ApiModelProperty(value = "审版设计师头像")
    @UserAvatar("reviewedDesignId")
    private String reviewedDesignAvatar;
    @ApiModelProperty(value = "改版设计师头像")
    @UserAvatar("revisedDesignId")
    private String revisedDesignAvatar;
    @ApiModelProperty(value = "设计师头像")
    @UserAvatar("designerId")
    private String designerAvatar;
    @ApiModelProperty(value = "波段名称")
    private String bandName;
    @ApiModelProperty(value = "号型类型名称")
    private String sizeRangeName;

    @ApiModelProperty(value = "维度信息")
    private List<FieldManagementVo> dimensionLabels;

    @ApiModelProperty(value = "附件")
    private List<AttachmentVo> attachmentList;
    @ApiModelProperty(value = "附件")
    private List<AttachmentVo> attachmentList1;
    @ApiModelProperty(value = "款式图片信息")
    private List<StylePicVo> stylePicList;
    @ApiModelProperty(value = "关联的素材库")
    private List<MaterialVo> materialList;
    @ApiModelProperty(value = "物料信息")
    private List<PackBomVo> packBomVoList;
    @ApiModelProperty(value = "款式")
    public String getStyle() {
        return Optional.ofNullable(getDesignNo()).orElse("") + Optional.ofNullable(getStyleName()).orElse("");
    }

    @ApiModelProperty(value = "坑位信息图片")
    private String seatStylePic;

    @ApiModelProperty(value = "颜色企划数量")
    public Long colorPlanningCount;
    @ApiModelProperty(value = "主题企划数量")
    public Long themePlanningCount;

    @ApiModelProperty(value = "号型类型尺码")
    private String sizeRangeSizes;

    @ApiModelProperty(value = "号型类型尺码id")
    private String sizeRangeSizeIds;

    @ApiModelProperty(value = "号型类型尺码编码")
    private String sizeRangeSizeCodes;

    @ApiModelProperty(value = "号型类型尺码真实编码")
    private String sizeRangeSizeRealCodes;

    @ApiModelProperty(value = "款式设计详情-颜色")
    private List<StyleInfoColorVo> styleInfoColorVoList;
    /**
     * 正确样衣接收时间
     */
    @ApiModelProperty(value = "正确样衣接收时间")
    private String correctStyleDate;

    /**
     * 样衣图片（多个「,」分隔）
     */
    @ApiModelProperty(value = "样衣图片（多个「,」分隔）")
    private String samplePics;

    /**
     * 自定义款图上传配置 1开启
     */
    private Boolean customStylePicUploadOpen;
}
