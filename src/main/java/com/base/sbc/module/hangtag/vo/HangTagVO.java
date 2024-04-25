/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.vo;

import java.util.Date;
import java.util.List;

import com.base.sbc.config.enums.business.HangTagStatusEnum;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.util.StrUtil;
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
public class HangTagVO extends HangTag {

    private static final long serialVersionUID = 1L;

    private String id;

    private List<BasicsdatumMaterial> basicsdatumMaterials;

    /*检测报告*/
    private List<EscmMaterialCompnentInspectCompanyDto> compnentInspectCompanyDtoList;

    private String prodCategory1stName;
    private String prodCategory1st;

    /**
     * 款式id
     */
    @ApiModelProperty(value = "款式id")
    private String styleId;

    /**
     * 产品季id
     */
    private String planningSeasonId;

    /**
     * 款式
     */
    @ApiModelProperty(value = "款式")
    private String style;

    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

    /**
     * 号型类型
     */
    @ApiModelProperty(value = "号型类型编码")
    private String modelType;

    @ApiModelProperty(value = "号型类型名称")
    private String modelTypeName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String describe;

    /**
     * 设计师
     */
    @ApiModelProperty(value = "设计师")
    private String designer;
    /**
     * 版师
     */
    @ApiModelProperty(value = "版师")
    private String patternDesign;

    /**
     * 洗标名称
     */
    @ApiModelProperty(value = "洗标名称")
    private String washingLabelName;
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
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.已确认
     */
    @ApiModelProperty(value = "状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.已确认")
    private HangTagStatusEnum status;
    /**
     * 确认时间
     */
    @ApiModelProperty(value = "确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmDate;

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
     * 安全列别编码
     */
    @ApiModelProperty(value = "安全列别编码")
    private String saftyTypeCode;
    /**
     * 安全列别
     */
    @ApiModelProperty(value = "安全列别")
    private String saftyType;
    /**
     * 外辅助工艺
     */
    @ApiModelProperty(value = "外辅助工艺")
    private String extAuxiliaryTechnics;

    /**
     * 款式图片地址
     */
    private String stylePic;
    private String styleColorPic;
    /**
     * 外辅助工艺
     */
    @ApiModelProperty(value = "外辅助工艺编码")
    private String extAuxiliaryTechnicsCode;
    /**
     * 包装形式编码
     */
    @ApiModelProperty(value = "包装形式编码")
    private String packagingFormCode;
    /**
     * 包装形式
     */
    @ApiModelProperty(value = "包装形式")
    private String packagingForm;
    /**
     * 包装形式编码
     */
    @ApiModelProperty(value = "二检包装形式编码")
    private String secondPackagingFormCode;
    /**
     * 包装形式
     */
    @ApiModelProperty(value = "二检包装形式")
    private String secondPackagingForm;
    /**
     * 包装袋标准编码
     */
    @ApiModelProperty(value = "包装袋标准编码")
    private String packagingBagStandardCode;
    /**
     * 包装袋标准
     */
    @ApiModelProperty(value = "包装袋标准")
    private String packagingBagStandard;
    /**
     * 注意事项
     */
    @ApiModelProperty(value = "注意事项")
    private String mattersAttention;
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
     * 面料详情
     */
    @ApiModelProperty(value = "面料详情")
    private String fabricDetails;
    /**
     * 模板部件
     */
    @ApiModelProperty(value = "模板部件")
    private String templatePart;
    /**
     * 下单员id
     */
    @ApiModelProperty(value = "下单员id")
    private String placeOrderStaffId;
    /**
     * 下单员名称
     */
    @ApiModelProperty(value = "下单员名称")
    private String placeOrderStaffName;
    /**
     * 工艺师id
     */
    @ApiModelProperty(value = "工艺师id")
    private String technologistId;
    /**
     * 工艺师名称
     */
    @ApiModelProperty(value = "工艺师名称")
    private String technologistName;
    /**
     * 放码师id
     */
    @ApiModelProperty(value = "放码师id")
    private String gradingId;
    /**
     * 放码师名称
     */
    @ApiModelProperty(value = "放码师名称")
    private String gradingName;
    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date placeOrderDate;
    /**
     * 样衣工id
     */
    @ApiModelProperty(value = "样衣工id")
    private String sampleMakerId;
    /**
     * 样衣工名称
     */
    @ApiModelProperty(value = "样衣工名称")
    private String sampleMakerName;
    /**
     * 温馨提示
     */
    @ApiModelProperty(value = "温馨提示")
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
     * 贮藏要求
     */
    @ApiModelProperty(value = "贮藏要求Code")
    private String storageDemand;
    @ApiModelProperty(value = "贮藏要求内容")
    private String storageDemandName;
    /**
     * 贮藏要求
     */
    @ApiModelProperty(value = "产地")
    private String producer;
    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date produceDate;

    /**
     * 成分信息
     */
    @ApiModelProperty(value = "成分信息")
    private String ingredient;

    private String repIngredient;


    /** 开发分类 */
    @ApiModelProperty(value = "开发分类"  )
    private String devClass;
    /** 开发分类名称 */
    @ApiModelProperty(value = "开发分类名称"  )
    private String devClassName;

    /**
     * bom状态
     */
    @ApiModelProperty(value = "bom状态")
    private String bomStatus;

    /**
     * 附件
     */
    private String attachment;

    private String isTrim;

    private String produceTypeName;

    public String getStylePic() {
        if (StrUtil.isNotBlank(styleColorPic)) {
            return styleColorPic;
        }
        return stylePic;
    }
}

