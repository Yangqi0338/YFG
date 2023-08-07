/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;

import java.util.Arrays;
import java.util.Date;

import com.base.sbc.module.smp.dto.SmpSampleDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：样衣表 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.Sample
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-4 13:15:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample")
@ApiModel("样衣表 Sample")
public class Sample extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;

    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 状态：0-禁用，1-启用，2-删除
     */
    @ApiModelProperty(value = "状态：0-禁用，1-启用，2-删除")
    private Integer status;
    /**
     * 图片。多图英文“,”逗号分隔
     */
    @ApiModelProperty(value = "图片。多图英文“,”逗号分隔")
    private String images;
    /**
     * 打版管理ID，对应 t_pattern_making.id 主键ID
     */
    @ApiModelProperty(value = "打版管理ID，对应 t_pattern_making.id 主键ID")
    private String patternMakingId;
    /**
     * 打版编号，对应 t_pattern_making.code 打板编号
     */
    @ApiModelProperty(value = "打版编号，对应 t_pattern_making.code 打板编号")
    private String patternMakingCode;
    /**
     * 样衣版，对应 t_pattern_making.sampla_type 打版类型
     */
    @ApiModelProperty(value = "样衣版，对应 t_pattern_making.sampla_type 打版类型")
    private String sampleType;
    /**
     * 纸样师ID，对应 t_pattern_making.pattern_design_id 版师ID
     */
    @ApiModelProperty(value = "纸样师ID，对应 t_pattern_making.pattern_design_id 版师ID")
    private String patternDesignId;
    /**
     * 纸样师名称，对应 t_pattern_making.pattern_design_name 版师名称
     */
    @ApiModelProperty(value = "纸样师名称，对应 t_pattern_making.pattern_design_name 版师名称")
    private String patternDesignName;
    /**
     * 客款号
     */
    @ApiModelProperty(value = "客款号")
    private String customerNo;
    /**
     * 款式设计ID，对应 t_style.id 主键ID
     */
    @ApiModelProperty(value = "款式设计ID，对应 t_style.id 主键ID")
    private String styleId;
    /**
     * 款式名称，对应t_style.style_name 款式名称
     */
    @ApiModelProperty(value = "款式名称，对应t_style.style_name 款式名称")
    private String styleName;
    /**
     * 设计款号，对应 t_style.design_no 设计款号
     */
    @ApiModelProperty(value = "设计款号，对应 t_style.design_no 设计款号")
    private String designNo;
    /**
     * 季节ID，对应 t_planning_saason.id 主键ID
     */
    @ApiModelProperty(value = "季节ID，对应 t_planning_saason.id 主键ID")
    private String seasonId;
    /**
     * 大类code
     */
    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;
    /**
     * 大类名称
     */
    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;
    /**
     * 品类code
     */
    @ApiModelProperty(value = "品类code")
    private String prodCategory;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    /**
     * 中类名称
     */
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;
    /**
     * 中类code
     */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;
    /**
     * 小类code
     */
    @ApiModelProperty(value = "小类code")
    private String prodCategory3rd;
    /**
     * 小类名称
     */
    @ApiModelProperty(value = "小类名称")
    private String prodCategory3rdName;
    /**
     * 季节，对应 t_planning_saason.season 季节
     */
    @ApiModelProperty(value = "季节，对应 t_planning_saason.season 季节")
    private String season;
    /**
     * 设计来源类型：1-内部人员，2-供应商
     */
    @ApiModelProperty(value = "设计来源类型：1-内部人员，2-供应商")
    private Integer fromType;
    /**
     * 设计来源ID
     */
    @ApiModelProperty(value = "设计来源ID")
    private String fromId;
    /**
     * 设计来源名称，人员名称或供应商名称
     */
    @ApiModelProperty(value = "设计来源名称，人员名称或供应商名称")
    private String fromName;
    /**
     * 样衣类型：1-内部研发，2-外采，2-ODM提供
     */
    @ApiModelProperty(value = "样衣类型：1-内部研发，2-外采，2-ODM提供")
    private Integer type;
    /**
     * 样衣数量
     */
    @ApiModelProperty(value = "样衣数量")
    private Integer count;
    /**
     * 借出数量
     */
    @ApiModelProperty(value = "借出数量")
    private Integer borrowCount;
    /**
     * 库存状态：0-完全借出，1-部分借出，2-全部在库
     */
    @ApiModelProperty(value = "库存状态：0-完全借出，1-部分借出，2-全部在库")
    private Integer completeStatus;
    /**
     * 审核人ID
     */
    @ApiModelProperty(value = "审核人ID")
    private String examineId;
    /**
     * 审核人名称
     */
    @ApiModelProperty(value = "审核人名称")
    private String examineName;
    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examineDate;
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
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

