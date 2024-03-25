/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 类描述：资料包-尺寸表 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackSize
 * @email your email
 * @date 创建时间：2023-10-30 2:32:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_size")
@ApiModel("资料包-尺寸表 PackSize")
public class PackSize extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    /**
     * 尺寸明细
     */
    @TableField(exist = false)
    private List<PackSizeDetail> packSizeDetailList;

    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    /**
     * 资料包类型:packDesign:设计资料包
     */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包")
    private String packType;
    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Integer sort;
    /**
     * 行类型:0默认,1分割
     */
    @ApiModelProperty(value = "行类型:0默认,1分割")
    private String rowType;
    /**
     * 部位编码
     */
    @ApiModelProperty(value = "部位编码")
    private String partCode;
    /**
     * 部位名称
     */
    @ApiModelProperty(value = "部位名称")
    private String partName;
    /**
     * 公差
     */
    @ApiModelProperty(value = "公差")
    private String tolerance;
    /**
     * 标准值
     */
    @ApiModelProperty(value = "标准值")
    private String standard;
    /**
     * 工单的所有尺码一一列出来，所有的尺码
     */
    @ApiModelProperty(value = "工单的所有尺码一一列出来，所有的尺码")
    private String size;
    /**
     * 量法
     */
    @ApiModelProperty(value = "量法")
    private String method;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String img;
    /**
     * 码差
     */
    @ApiModelProperty(value = "码差")
    private String codeError;
    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;
    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    /**
     * 代码
     */
    @ApiModelProperty(value = "代码")
    private String sizeCode;
    /**
     * 驳口
     */
    @ApiModelProperty(value = "驳口")
    private String splice;
    /**
     * 部位倍数
     */
    @ApiModelProperty(value = "部位倍数")
    private String partMultiple;
    /**
     * 正公差+
     */
    @ApiModelProperty(value = "正公差+")
    private String positive;
    /**
     * 负公差-
     */
    @ApiModelProperty(value = "负公差-")
    private String minus;
    /**
     * 档差设置，多档差
     */
    @ApiModelProperty(value = "档差设置，多档差")
    private String codeErrorSetting;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 收缩
     */
    @ApiModelProperty(value = "收缩")
    private String shrink;
    /**
     * 是否是迁移历史数据
     */
    @ApiModelProperty(value = "是否是迁移历史数据")
    private String historicalData;

    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

