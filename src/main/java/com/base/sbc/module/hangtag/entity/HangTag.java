/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.HangTagStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    /** 状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.待翻译确认,6.不通过, 7.已确认 */
    @ApiModelProperty(value = "状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.待翻译确认,6.不通过, 7.已确认"  )
    private HangTagStatusEnum status;
    /** 确认时间 */
    @ApiModelProperty(value = "确认时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmDate;
    /**
     * 翻译确认时间
     */
    @ApiModelProperty(value = "翻译确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date translateConfirmDate;
    /** 款式 */
    @ApiModelProperty(value = "款式"  )
    private String styleNo;
    /** 大货款号 */
    @ApiModelProperty(value = "大货款号"  )
    private String bulkStyleNo;
    /**
     * 是否打印 0.否, 1.是
     */
    private String printOrNot;
    /**
     * 品名编码
     */
    @ApiModelProperty(value = "品名编码")
    private String productCode;

    /**
     * 成分信息
     */
    @ApiModelProperty(value = "成分信息")
    private String ingredient;
    /**
     * 品名
     */
    @ApiModelProperty(value = "品名")
    private String productName;
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
    /** 外辅助工艺编码 */
    @ApiModelProperty(value = "外辅助工艺编码"  )
    private String extAuxiliaryTechnicsCode;
    /** 外辅助工艺 */
    @ApiModelProperty(value = "外辅助工艺"  )
    private String extAuxiliaryTechnics;
    /** 包装形式编码 */
    @ApiModelProperty(value = "包装形式编码"  )
    private String packagingFormCode;
    /** 二检包装形式 */
    @ApiModelProperty(value = "二检包装形式"  )
    private String secondPackagingForm;
    @ApiModelProperty(value = "二检包装形式编码"  )
    private String secondPackagingFormCode;
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

    @ApiModelProperty(value = "洗唛材质备注名称"  )
    private String washingMaterialRemarksName;
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
    /**
     * 温馨提示编码
     */
    @ApiModelProperty(value = "温馨提示编码")
    private String warmTipsCode;
    /**
     * 洗标
     */
    @ApiModelProperty(value = "洗标")
    private String washingLabel;

	@ApiModelProperty(value = "洗标编码")
	private String washingCode;
    /**
     * 洗标名称
     */
    @ApiModelProperty(value = "洗标名称")
    private String washingLabelName;
    /**
     * 贮藏要求
     */
    @ApiModelProperty(value = "贮藏要求编码")
    private String storageDemand;
    @ApiModelProperty(value = "贮藏要求内容")
    private String storageDemandName;
    /**
     * 产地
     */
    @ApiModelProperty(value = "产地")
    private String producer;

    private String repIngredient;
    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date produceDate;

    private String historicalData;


    /**
     * 外发工厂
     */
    private String outFactory;

    /**
     * 温馨提示是否换行
     */
    @ApiModelProperty(value = "温馨提示是否换行")
    private YesOrNoEnum warmTipsDefaultWrap;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

