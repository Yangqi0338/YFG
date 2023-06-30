/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangTag.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
/**
 * 类描述：吊牌表 实体类
 * @address com.base.sbc.module.hangTag.entity.HangTag
 * @author xhj
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:52
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_hang_tag")
@ApiModel("吊牌表 HangTag")
public class HangTag extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.已确认 */
    @ApiModelProperty(value = "状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.已确认"  )
    private String status;
    /** 确认时间 */
    @ApiModelProperty(value = "确认时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmDate;
    /** 款式id */
    @ApiModelProperty(value = "款式id"  )
    private String styleId;
    /** 大货款号 */
    @ApiModelProperty(value = "大货款号"  )
    private String bulkStyleNo;
    /** 颜色编码 */
    @ApiModelProperty(value = "颜色编码"  )
    private String colorCode;
    /** 颜色 */
    @ApiModelProperty(value = "颜色"  )
    private String color;
    /** 执行标准编码 */
    @ApiModelProperty(value = "执行标准编码"  )
    private String executeStandardCode;
    /** 执行标准 */
    @ApiModelProperty(value = "执行标准"  )
    private String executeStandard;
    /** 质量等级编码 */
    @ApiModelProperty(value = "质量等级编码"  )
    private String qualityGradeCode;
    /** 质量等级 */
    @ApiModelProperty(value = "质量等级"  )
    private String qualityGrade;
    /** 安全标题编码 */
    @ApiModelProperty(value = "安全标题编码"  )
    private String saftyTitleCode;
    /** 安全标题 */
    @ApiModelProperty(value = "安全标题"  )
    private String saftyTitle;
    /** 安全列别编码 */
    @ApiModelProperty(value = "安全列别编码"  )
    private String saftyTypeCode;
    /** 安全列别 */
    @ApiModelProperty(value = "安全列别"  )
    private String saftyType;
    /** 外辅助工艺 */
    @ApiModelProperty(value = "外辅助工艺"  )
    private String extAuxiliaryTechnics;
    /** 包装形式编码 */
    @ApiModelProperty(value = "包装形式编码"  )
    private String packagingFormCode;
    /** 包装形式 */
    @ApiModelProperty(value = "包装形式"  )
    private String packagingForm;
    /** 包装袋标准编码 */
    @ApiModelProperty(value = "包装袋标准编码"  )
    private String packagingBagStandardCode;
    /** 包装袋标准 */
    @ApiModelProperty(value = "包装袋标准"  )
    private String packagingBagStandard;
    /** 注意事项 */
    @ApiModelProperty(value = "注意事项"  )
    private String mattersAttention;
    /** 充绒量 */
    @ApiModelProperty(value = "充绒量"  )
    private String downContent;
    /** 特殊规格 */
    @ApiModelProperty(value = "特殊规格"  )
    private String specialSpec;
    /** 洗唛材质备注 */
    @ApiModelProperty(value = "洗唛材质备注"  )
    private String washingMaterialRemarks;
    /** 面料详情 */
    @ApiModelProperty(value = "面料详情"  )
    private String fabricDetails;
    /** 模板部件 */
    @ApiModelProperty(value = "模板部件"  )
    private String templatePart;
    /** 下单员id */
    @ApiModelProperty(value = "下单员id"  )
    private String placeOrderStaffId;
    /** 下单员名称 */
    @ApiModelProperty(value = "下单员名称"  )
    private String placeOrderStaffName;
    /** 工艺师id */
    @ApiModelProperty(value = "工艺师id"  )
    private String technologistId;
    /** 工艺师名称 */
    @ApiModelProperty(value = "工艺师名称"  )
    private String technologistName;
    /** 放码师id */
    @ApiModelProperty(value = "放码师id"  )
    private String gradingId;
    /** 放码师名称 */
    @ApiModelProperty(value = "放码师名称"  )
    private String gradingName;
    /** 下单时间 */
    @ApiModelProperty(value = "下单时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date placeOrderDate;
    /** 样衣工id */
    @ApiModelProperty(value = "样衣工id"  )
    private String sampleMakerId;
    /** 样衣工名称 */
    @ApiModelProperty(value = "样衣工名称"  )
    private String sampleMakerName;
    /** 温馨提示 */
    @ApiModelProperty(value = "温馨提示"  )
    private String warmTips;
    /** 洗标 */
    @ApiModelProperty(value = "洗标"  )
    private String washingLabel;
    /** 贮藏要求 */
    @ApiModelProperty(value = "贮藏要求"  )
    private String storageDemand;
    /** 产地 */
    @ApiModelProperty(value = "产地"  )
    private String producer;
    /** 生产日期 */
    @ApiModelProperty(value = "生产日期"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date produceDate;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

