/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.base.sbc.config.enums.business.HangTagStatusEnum;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：吊牌表 实体类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.entity.HangTag
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:52
 */
@Data
public class HangTagListVO extends HangTag {

    private static final long serialVersionUID = 1L;

    private String id;

    private String packInfoId;
    /** 洗标名称 */
    @ApiModelProperty(value = "洗标名称"  )
    private String washingLabelName;

    /**
     * 款式id
     */
    @ApiModelProperty(value = "款式id")
    private String styleId;

    @ApiModelProperty(value = "bom状态:(0样品,1大货)")
    private String bomStatus;


    /**
     * 款式
     */
    @ApiModelProperty(value = "款式")
    private String style;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;

    /**
     * 生产类型
     */
    @ApiModelProperty(value = "生产类型")
    private String produceType;

    /** 波段名称 */
    @ApiModelProperty(value = "波段名称"  )
    private String bandName;

    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;

    /** 品牌mc */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;

    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String  season;

    /**
     * 生产类型名称
     */
    @ApiModelProperty(value = "生产类型名称")
    private String  produceTypeName;
    /**
     * 款式类型
     */
    @ApiModelProperty(value = "款式类型")
    private String styleType;

    /**
     * 款式类型名称
     */
    @ApiModelProperty(value = "款式类型名称")
    private String  styleTypeName;
    /**
     * 颜色编码
     */
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;
    /**
     * 颜色
     */
    @ApiModelProperty(value = "颜色")
    private String color;

    /**
     * 大货
     */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

    /**
     * 号型类型
     */
    @ApiModelProperty(value = "号型类型")
    private String modelType;

    /**
     * 号型类型名称
     */
    @ApiModelProperty(value = "号型类型名称")
    private String modelTypeName;

    /**
     * 状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.待翻译确认,6.不通过, 7.已确认
     * */
    @ApiModelProperty(value = "状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.待翻译确认,6.不通过, 7.已确认"  )
    private HangTagStatusEnum status;

    @ApiModelProperty(value = "当前审核用户")
    private String  examineUserNema;

    @ApiModelProperty(value = "当前审核用户id")
    private String examineUserId;
    /**
     * 确认时间
     */
    @ApiModelProperty(value = "确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmDate;
    /**
     * 翻译确认时间
     */
    @ApiModelProperty(value = "翻译确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date translateConfirmDate;

    /**
     * 品名编码
     */
    @ApiModelProperty(value = "品名编码")
    private String productCode;
    /**
     * 品名
     */
    @ApiModelProperty(value = "品名")
    private String productName;

    @ApiModelProperty(value = "品类")
    private String prodCategoryName;

    /**
     * 执行标准编码
     */
    @ApiModelProperty(value = "执行标准编码")
    private String executeStandardCode;

    /**
     * 执行标准
     */
    @ApiModelProperty(value = "执行标准")
    private String executeStandard;

    /**
     * 质量等级编码
     */
    @ApiModelProperty(value = "质量等级编码")
    private String qualityGradeCode;
    /**
     * 质量等级
     */
    @ApiModelProperty(value = "质量等级")
    private String qualityGrade;
    /**
     * 安全标题编码
     */
    @ApiModelProperty(value = "安全标题编码")
    private String saftyTitleCode;
    /**
     * 安全标题
     */
    @ApiModelProperty(value = "安全标题")
    private String saftyTitle;
    /**
     * 安全类别编码
     */
    @ApiModelProperty(value = "安全类别编码")
    private String saftyTypeCode;
    /**
     * 安全类别
     */
    @ApiModelProperty(value = "安全类别")
    private String saftyType;

    /**
     * 成分信息
     */
    @ApiModelProperty(value = "成分信息")
    public String ingredient;
    private String repIngredient;

    /**
     * 温馨提示
     */
    @ApiModelProperty(value = "温馨提示")
    private String warmTips;
    /**
     * 洗标
     */
    @ApiModelProperty(value = "洗标")
    private String washingLabel;

	@ApiModelProperty(value = "洗标编码")
	private String washingCode;
    /**
     * 贮藏要求
     */
    @ApiModelProperty(value = "贮藏要求")
    private String storageDemand;
    /**
     * 充绒量
     */
    @ApiModelProperty(value = "充绒量")
    private String downContent;

    /**
     * 特殊规格
     */
    @ApiModelProperty(value = "特殊规格")
    private String specialSpec;
    /**
     * 洗唛材质备注
     */
    @ApiModelProperty(value = "洗唛材质备注")
    private String washingMaterialRemarks;
    /**
     * 放码师名称
     */
    @ApiModelProperty(value = "放码师名称")
    private String gradingName;

    /**
     * 工艺师名称
     */
    @ApiModelProperty(value = "工艺师名称")
    private String technologistName;

    /**
     * 样衣工名称
     */
    @ApiModelProperty(value = "样衣工名称")
    private String sampleMakerName;

    /**
     * 下单员名称
     */
    @ApiModelProperty(value = "下单员名称")
    private String placeOrderStaffName;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date placeOrderDate;

    /**
     * 吊牌价
     */
    @ApiModelProperty(value = "吊牌价")
    private BigDecimal tagPrice;
    /**
     * 工艺包颜色规格
     */
    @ApiModelProperty(value = "工艺包颜色规格")
    private String artBagColorSpec;


    /**
     * 商品吊牌价是否确认
     */
    @ApiModelProperty(value = "商品吊牌价是否确认")
    private String productTagPriceConfirm;
    /**
     * 计控吊牌价是否确认
     */
    @ApiModelProperty(value = "计控吊牌价是否确认")
    private String planTagPriceConfirm;

    /**
     * 是否计控成本确认
     */
    @ApiModelProperty(value = "是否计控成本确认")
    private String planCostConfirm;

    /**
     * 附件
     */
    private String attachment;

    private String isTrim;

}

