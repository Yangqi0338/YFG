package com.base.sbc.module.sample.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：样衣分页返回（样衣明细维度）
 * @address com.base.sbc.module.sample.vo.SamplePageByItemVo
 */
@Data
@ApiModel("样衣分页返回（样衣明细维度） SamplePageByItemVo ")
public class SamplePageByItemVo {
    @ApiModelProperty(value = "编号")
    private String id;

    /** 图片 */
    @ApiModelProperty(value = "图片")
    private String images;

    /** 状态：0-禁用，1-启用，2-删除 */
    @ApiModelProperty(value = "状态：0-禁用，1-启用，2-删除")
    private Integer status;

    /** 款式名称 */
    @ApiModelProperty(value = "款式名称")
    private String styleName;

    /** 设计款号 */
    @ApiModelProperty(value = "设计款号")
    private String designNo;

    /** 样衣编号 */
    @ApiModelProperty(value = "样衣编号")
    private String code;

    /** 样衣版 */
    @ApiModelProperty(value = "样衣版")
    private String sampleType;

    /** 款式品类 */
    @ApiModelProperty(value = "款式品类")
    private String categoryName;

    /** 季节 */
    @ApiModelProperty(value = "季节")
    private String season;

    /** 客款号 */
    @ApiModelProperty(value = "客款号")
    private String customerNo;

    /** 样衣ID */
    @ApiModelProperty(value = "样衣ID")
    private String sampleId;

    /** 审核状态：0-草稿，1-待审核、2-审核通过、3-驳回 */
    @ApiModelProperty(value = "审核状态：0-草稿，1-待审核、2-审核通过、3-驳回")
    private Integer examineStatus;

    /** 借出单号 */
    @ApiModelProperty(value = "借出单号")
    private String borrowCode;

    /** 样衣类型：1-内部研发，2-外采，2-ODM提供 */
    @ApiModelProperty(value = "样衣类型：1-内部研发，2-外采，2-ODM提供")
    private Integer type;

    /** 在库状态：0-未入库，1-在库，2-借出，3-删除 */
    @ApiModelProperty(value = "在库状态：0-未入库，1-在库，2-借出，3-删除")
    private Integer itemStatus;

    /** 来源：1-新增，2-导入，3-外部 */
    @ApiModelProperty(value = "来源：1-新增，2-导入，3-外部")
    private Integer fromType;

    /** 颜色 */
    @ApiModelProperty(value = "颜色")
    private String color;

    /** 尺码 */
    @ApiModelProperty(value = "尺码")
    private String size;

    /** 借出人名称 */
    @ApiModelProperty(value = "借出人名称")
    private String borrowName;

    /** 经手人名称 */
    @ApiModelProperty(value = "经手人名称")
    private String operateName;

    /** 借出时间 */
    @ApiModelProperty(value = "借出时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date borrowDate;

    /** 预计归还时间 */
    @ApiModelProperty(value = "预计归还时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectReturnDate;

    /** 实际归还时间 */
    @ApiModelProperty(value = "实际归还时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date returnDate;

    /** 借出天数 */
    @ApiModelProperty(value = "借出天数")
    private String borrowDays;

    /** 剩余天数 */
    @ApiModelProperty(value = "剩余天数")
    private String remainingDays;

    /** 位置信息ID */
    @ApiModelProperty(value = "位置信息ID")
    private String positionId;

    /** 位置信息 */
    @ApiModelProperty(value = "位置信息")
    private String position;

    /** 设计来源名称 */
    @ApiModelProperty(value = "设计来源名称")
    private String fromName;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private String createName;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /** 修改人 */
    @ApiModelProperty(value = "修改人")
    private String updateName;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    /** 价格 */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    /** 数量 */
    @ApiModelProperty(value = "数量")
    private Integer count;

}
