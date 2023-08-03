package com.base.sbc.module.sample.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：样衣盘点 vo
 * @address com.base.sbc.module.sample.vo.SampleInventoryVo
 */
@Data
@ApiModel("样衣盘点 SampleInventoryVo")
public class SampleInventoryVo {
    /** id */
    @ApiModelProperty(value = "id")
    private String id;

    /** 编号 */
    @ApiModelProperty(value = "单号")
    private String code;

    /** 状态：1-正常，2-作废，3-删除 */
    @ApiModelProperty(value = "状态：1-正常，2-作废，3-删除")
    private Integer status;

    /** 盘点状态：0-未开始，1-盘点中，2-完成，3-过期 */
    @ApiModelProperty(value = "盘点状态：0-未开始，1-盘点中，2-完成，3-过期")
    private String inventoryStatus;

    /** 审核状态：0-草稿，1-待审核、2-审核通过、3-驳回 */
    @ApiModelProperty(value = "审核状态：0-草稿，1-待审核、2-审核通过、3-驳回")
    private Integer examineStatus;

    /** 审核状态原因 */
    @ApiModelProperty(value = "审核状态原因")
    private String examineReason;

    /** 盘点名称 */
    @ApiModelProperty(value = "盘点名称")
    private String name;

    /** 经手人 */
    @ApiModelProperty(value = "经手人")
    private String operateName;

    /** 开始时间 */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;

    /** 结束时间 */
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private String createName;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    /** 更新人 */
    @ApiModelProperty(value = "更新人")
    private String updateName;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "关联的明细")
    private List<SampleInventoryItemVo> sampleItemList;
}
