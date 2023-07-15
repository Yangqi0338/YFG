package com.base.sbc.module.pack.vo;

import com.base.sbc.module.planning.utils.PlanningUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：资料包-基础信息列表Vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackInfoLisVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-06 17:41
 */
@Data
@ApiModel("资料包-基础信息列表Vo PackInfoLisVo")
public class PackInfoListVo {


    @JsonIgnore
    @ApiModelProperty(value = "主数据id(样衣设计id)")
    private String foreignId;

    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "款式图")
    private String stylePic;
    /**
     * 款式名称
     */
    @ApiModelProperty(value = "款式名称")
    private String styleName;

    @ApiModelProperty(value = "品类名称路径:(大类/品类/中类/小类)")
    private String categoryName;

    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;

    @ApiModelProperty(value = "生产模式")
    private String devtType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "创建人")
    private String createName;

    @ApiModelProperty(value = "配色")
    private String color;
    @ApiModelProperty(value = "编号")
    private String code;
    /**
     * 状态:1启用,0未启用
     */
    @ApiModelProperty(value = "状态:1启用,0未启用")
    private String enableFlag;
    /**
     * SCM下发状态:0未下发,1已下发
     */
    @ApiModelProperty(value = "SCM下发状态:0未下发,1已下发")
    private String scmSendFlag;
    /**
     * bom状态:(0样品,1大货)
     */
    @ApiModelProperty(value = "bom状态:(0样品,1大货)")
    private String bomStatus;
    /**
     * 反审状态：待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "反审状态：待审核(1)、审核通过(2)、被驳回(-1)")
    private String reverseConfirmStatus;
    /**
     * 设计转后技术确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "设计转后技术确认:(0未确认,1已确认)")
    private String designTechConfirm;
    /**
     * 设计转后技术确认时间
     */
    @ApiModelProperty(value = "设计转后技术确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designTechConfirmDate;
    /**
     * 大货制单员确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "大货制单员确认:(0未确认,1已确认)")
    private String bulkOrderClerkConfirm;
    /**
     * 大货制单员确认时间
     */
    @ApiModelProperty(value = "大货制单员确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bulkOrderClerkConfirmDate;
    /**
     * 大货工艺员确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "大货工艺员确认:(0未确认,1已确认)")
    private String bulkProdTechConfirm;
    /**
     * 大货工艺员确认时间
     */
    @ApiModelProperty(value = "大货工艺员确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bulkProdTechConfirmDate;
    /**
     * 后技术确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "后技术确认:(0未确认,1已确认)")
    private String postTechConfirm;
    /**
     * 后技术确认时间
     */
    @ApiModelProperty(value = "后技术确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date postTechConfirmDate;
    /**
     * 品控部确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "品控部确认:(0未确认,1已确认)")
    private String qcConfirm;
    /**
     * 品控部确认时间
     */
    @ApiModelProperty(value = "品控部确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date qcConfirmDate;
    /**
     * 尺寸表锁定flg:(0未锁定,1锁定)
     */
    @ApiModelProperty(value = "尺寸表锁定flg:(0未锁定,1锁定)")
    private String sizeLockFlag;
    /**
     * 工艺说明锁定flg:(0未锁定,1锁定)
     */
    @ApiModelProperty(value = "工艺说明锁定flg:(0未锁定,1锁定)")
    private String techSpecLockFlag;
    /**
     * 工艺说明审批状态:待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "工艺说明审批状态:待审核(1)、审核通过(2)、被驳回(-1)")
    private String techSpecConfirmStatus;
    /**
     * 尺寸表洗后尺寸跳码:(0不跳，1跳)
     */
    @ApiModelProperty(value = "尺寸表洗后尺寸跳码:(0关闭,1开启)")
    private String washSkippingFlag;

    public String getProdCategoryName() {
        return PlanningUtils.getProdCategoryName(categoryName);
    }

    public String getStyle() {
        return designNo + styleName;
    }

}
