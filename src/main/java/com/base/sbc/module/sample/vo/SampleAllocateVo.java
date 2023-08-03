package com.base.sbc.module.sample.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：样衣调拨 vo
 *
 * @address com.base.sbc.module.sample.vo.SampleAllocateVo
 */
@Data
@ApiModel("样衣调拨 SampleAllocateVo")
public class SampleAllocateVo {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 编号
     */
    @ApiModelProperty(value = "单号")
    private String code;

    /**
     * 调拨时间
     */
    @ApiModelProperty(value = "调拨时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date allocateDate;

    /**
     * 经手人
     */
    @ApiModelProperty(value = "经手人")
    private String operateName;

    /**
     * 客户
     */
    @ApiModelProperty(value = "客户")
    private String custmerName;

    /**
     * 调出位置
     */
    @ApiModelProperty(value = "调出位置")
    private String fromPosition;
    /**
     * 调出位置
     */
    @ApiModelProperty(value = "调出位置")
    private String fromPositionId;

    /**
     * 调入位置
     */
    @ApiModelProperty(value = "调入位置")
    private String toPosition;
    /**
     * 调入位置
     */
    @ApiModelProperty(value = "调入位置")
    private String toPositionId;

    /**
     * 状态：1-正常，2-作废，3-删除
     */
    @ApiModelProperty(value = "状态：1-正常，2-作废，3-删除")
    private Integer status;

    /**
     * 审核状态：0-草稿，1-待审核、2-审核通过、3-驳回
     */
    @ApiModelProperty(value = "审核状态：0-草稿，1-待审核、2-审核通过、3-驳回")
    private Integer examineStatus;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "关联的明细")
    private List<SampleAllocateItemVo> sampleItemList;
}
