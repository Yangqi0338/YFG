package com.base.sbc.module.sample.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：样衣分页返回（设计款号维度）
 * @address com.base.sbc.module.sample.vo.SamplePageByDesignNoVo
 */
@Data
@ApiModel("样衣分页返回（设计款号维度） SamplePageByDesignNoVo ")
public class SamplePageByDesignNoVo {
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

    /** 打版编号 */
    @ApiModelProperty(value = "打版编号")
    private String patternMakingCode;

    /** 样衣版 */
    @ApiModelProperty(value = "样衣版")
    private String samplaType;

    /** 款式品类 */
    @ApiModelProperty(value = "款式品类")
    private String categoryName;

    /** 季节 */
    @ApiModelProperty(value = "季节")
    private String season;

    /** 客款号 */
    @ApiModelProperty(value = "客款号")
    private String customerNo;

    /** 数量 */
    @ApiModelProperty(value = "数量")
    private String count;

    /** 样衣类型：1-内部研发，2-外采，2-ODM提供 */
    @ApiModelProperty(value = "样衣类型：1-内部研发，2-外采，2-ODM提供")
    private Integer type;

    /** 库存状态：0-完全借出，1-部分借出，2-全部在库 */
    @ApiModelProperty(value = "库存状态：0-完全借出，1-部分借出，2-全部在库")
    private Integer completeStatus;

    /** 审核状态：0-草稿，1-待审核、2-审核通过、3-驳回 */
    @ApiModelProperty(value = "审核状态：0-草稿，1-待审核、2-审核通过、3-驳回")
    private Integer examineStatus;

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
}
