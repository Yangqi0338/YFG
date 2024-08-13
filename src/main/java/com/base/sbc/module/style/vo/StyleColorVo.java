/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.vo;

import com.base.sbc.config.vo.EditPermissionReturnVo;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：样衣-款式配色 Vo类
 * @address com.base.sbc.module.sample.vo.SampleStyleColor
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 * @version 1.0
 */
@Data

@ApiModel("样衣-款式配色 SampleStyleColor")
public class StyleColorVo extends EditPermissionReturnVo {
    /**
     * 是否撞色
     */
    @ApiModelProperty(value = "是否撞色,0否 1是")
    private String colorCrash;

    private String id;

    @ApiModelProperty(value = "行id")
    private String issuerId;

    @ApiModelProperty(value = "bomId")
    private String packInfoId;

    private String styleId;
    /** 产品季节id */
    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;
    /**
     * 款式图
     */
    @ApiModelProperty(value = "款式图")
    private String  stylePic;

    @ApiModelProperty(value = "款式图")
    private String  style;

    public String getStyle() {
        return Optional.ofNullable(designNo).orElse("") + Optional.ofNullable(styleName).orElse("");
    }

    /*
        设计款号
        */
    @ApiModelProperty(value = "设计款号"  )
    private String   designNo;

    /*
     大类编码
     */
    @ApiModelProperty(value = "大类编码"  )
    private String  prodCategory1st;

    /*
   大类编码
   */
    @ApiModelProperty(value = "大类编码"  )
    private String  prodCategory1stName;

    @ApiModelProperty(value = "中类")
    private String prodCategory2ndName;

    @ApiModelProperty(value = "中类"  )
    private String prodCategory2nd;

    @ApiModelProperty(value = "小类编码")
    private String prodCategory3nd;

    @ApiModelProperty(value = "小类")
    private String prodCategory3ndName;


    /**
     * 品类编码
     */
    @ApiModelProperty(value = "品类编码")
    private String  prodCategory;
    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    private String  prodCategoryName;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 年份名称 */
    @ApiModelProperty(value = "年份名称"  )
    private String yearName;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 季节名称 */
    @ApiModelProperty(value = "季节名称"  )
    private String seasonName;

    /**
     * 历史款
     */
    @ApiModelProperty(value = "历史款")
    private String  hisDesignNo;

    @ApiModelProperty(value = "款式名称")
    private String styleName;

    /**
     * 样衣图(主图)
     */
    @ApiModelProperty(value = "样衣图(主图)")
    private String styleColorPic;

    /** 款式(大货款号)  */
    @ApiModelProperty(value = "款式(大货款号) "  )
    private String styleNo;

    /** 原大货款号  */
    @ApiModelProperty(value = "原大货款号"  )
    private String hisStyleNo;

    /*配色*/
    @ApiModelProperty(value = "配色"  )
    private String  colorName;

    /** 颜色规格 */
    @ApiModelProperty(value = "颜色规格"  )
    private String colorSpecification;

    /*颜色库id*/
    @ApiModelProperty(value = "颜色库id"  )
    private String colourLibraryId;

    /*颜色库编码*/
    @ApiModelProperty(value = "颜色库编码"  )
    private String colorCode;

    /*BOM阶段*/
    @ApiModelProperty(value = "BOM阶段"  )
    private String bomStatus;

    /*紧急程度*/
    @ApiModelProperty(value = "紧急程度"  )
    private String taskLevel;

    /*紧急程度名称*/
    @ApiModelProperty(value = "紧急程度名称"  )
    private String taskLevelName;

    /*款式类型*/
    @ApiModelProperty(value = "款式类型"  )
    private String styleType;

    /*款式类型名称*/
    @ApiModelProperty(value = "款式类型名称"  )
    private String styleTypeName;

    /** 生产类型 */
    @ApiModelProperty(value = "生产类型"  )
    private String devtType;

    /** 生产类型 */
    @ApiModelProperty(value = "生产类型"  )
    private String devtTypeName;

    /** 号型类型 */
    @ApiModelProperty(value = "号型类型"  )
    private String sizeRange;

    /** 号型类型名称 */
    @ApiModelProperty(value = "号型类型名称"  )
    private String sizeRangeName;

    /** 波段 */
    @ApiModelProperty(value = "波段"  )
    private String bandCode;

    /** 波段 */
    @ApiModelProperty(value = "波段"  )
    private String bandName;

    /** 设计师id */
    @ApiModelProperty(value = "设计师i"  )
    private String designerId;

    /** 设计师 */
    @ApiModelProperty(value = "设计师i"  )
    private String designer;

    /**工艺园 */
    @ApiModelProperty(value = "工艺园"  )
    private String technicianId;


    /** 工艺园*/
    @ApiModelProperty(value = "工艺园"  )
    private String technicianName;

    /** 轻奢款(0否,1:是) */
    @ApiModelProperty(value = "轻奢款(0否,1:是)"  )
    private String isLuxury;

    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /** 创建人 */
    @ApiModelProperty(value = "创建人"  )
    private String createName;


    /**  更新者id */
    private String updateId;

    /** 更新者名称  */
    private String updateName;

    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    /*是否上会*/
    @ApiModelProperty(value = "上会"  )
    private String meetFlag;

    /** 细分 */
    @ApiModelProperty(value = "细分"  )
    private String subdivide;
    /*细分名称*/
    @ApiModelProperty(value = "细分名称"  )
    private String subdivideName;

    /** 产品细分 */
    @ApiModelProperty(value = "产品细分"  )
    private String productSubdivide;
    /** 产品细分 */
    @ApiModelProperty(value = "产品细分"  )
    private String productSubdivideName;

    /** bom */
    @ApiModelProperty(value = "bom"  )
    private String bom;
    /** 销售类型 */
    @ApiModelProperty(value = "销售类型"  )
    private String salesType;

    /** 销售类型名称 */
    @ApiModelProperty(value = "销售类型名称"  )
    private String  salesTypeName;

    /** SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开 */
    @ApiModelProperty(value = "SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开"  )
    private String  scmSendFlag;

    /** 是否配饰款 */
    @ApiModelProperty(value = "是否配饰款"  )
    private String isTrim;

    /** 主款 */
    @ApiModelProperty(value = "主款"  )
    private String principalStyle;

    /** 主款款号 */
    @ApiModelProperty(value = "主款款号"  )
    private String principalStyleNo;

    /** 配饰 */
    @ApiModelProperty(value = "配饰"  )
    private String accessory;

    private String  accessory1;

    /** 配饰款号 */
    @ApiModelProperty(value = "配饰款号"  )
    private String accessoryNo;

    /** 吊牌价 */
    @ApiModelProperty(value = "吊牌价"  )
    private String tagPrice;

    /** 产品风格 */
    @ApiModelProperty(value = "产品风格"  )
    private String  styleFlavourName;
    /** 下主面料单 */
    @ApiModelProperty(value = "下主面料单"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sendMainFabricDate;
    /** 下配料1 */
    @ApiModelProperty(value = "下配料1"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sendBatchingDate1;
    /** 下配料2 */
    @ApiModelProperty(value = "下配料2"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sendBatchingDate2;
    /** 下配料3 */
    @ApiModelProperty(value = "下配料3"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sendBatchingDate3;
    /** 下里布单 */
    @ApiModelProperty(value = "下里布单"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sendSingleDate;
    /** 设计下明细单 */
    @ApiModelProperty(value = "设计下明细单"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date designDetailDate;
    /** 设计下正确样 */
    @ApiModelProperty(value = "设计下正确样"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date designCorrectDate;

    /** 供应商 */
    @ApiModelProperty(value = "供应商"  )
    private String supplier;

    /** 供应商编码 */
    @ApiModelProperty(value = "供应商编码"  )
    private String supplierCode;

    /** 供应商简称 */
    @ApiModelProperty(value = "供应商简称"  )
    private String supplierAbbreviation;

    /** 供应商款号 */
    @ApiModelProperty(value = "供应商款号"  )
    private String supplierNo;

    /** 供应商颜色 */
    @ApiModelProperty(value = "供应商颜色"  )
    private String supplierColor;

    /** 次品编号 */
    @ApiModelProperty(value = "次品编号"  )
    private String defectiveNo;

    /** 次品名称 */
    @ApiModelProperty(value = "次品名称"  )
    private String defectiveName;

    /** 是否主推(0否,1:是) */
    @ApiModelProperty(value = "是否主推(0否,1:是)"  )
    private String isMainly;

    /** 品名 */
    @ApiModelProperty(value = "品名"  )
    private String productName;

    /** 计控吊牌确定 */
    @ApiModelProperty(value = "计控吊牌确定"  )
    private String productHangtagConfirm;

    @ApiModelProperty(value = "是否商品吊牌确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productHangtagConfirmTime;

    /** 计控成本确认 */
    @ApiModelProperty(value = "计控成本确认"  )
    private String controlConfirm;

    @ApiModelProperty(value = "计控确认成本时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date controlConfirmTime;

    /** 品控吊牌价确定 */
    @ApiModelProperty(value = "品控吊牌价确定"  )
    private String controlHangtagConfirm;

    @ApiModelProperty(value = "是否计控吊牌确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date controlHangtagConfirmTime;

    /** 跟款设计师 */
    @ApiModelProperty(value = "跟款设计师"  )
    private String merchDesignName;

    /** 版式名称 */
    @ApiModelProperty(value = "版式名称"  )
    private String patternDesignName;

    /** 是否报次款0否 1是 */
    @ApiModelProperty(value = "是否报次款0否 1是"  )
    private String isDefective;


    /** 上新时间 */
    @ApiModelProperty(value = "上新时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date newDate;

    /** 下稿设计师 */
    @ApiModelProperty(value = "下稿设计师"  )
    private String  senderDesignerId;

    /** 下稿设计师名称 */
    @ApiModelProperty(value = "下稿设计师名称"  )
    private String  senderDesignerName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 下单标记（0否 1是）
     */
    @ApiModelProperty(value = "下单标记（0否 1是）")
    private String orderFlag;

    private String wareCode;
    @ApiModelProperty(value = "款式风格")
    private String productStyle;

    @ApiModelProperty(value = "rfid标记(0否 1是)")
    private String rfidFlag;

    @ApiModelProperty(value = "资料包名称")
    private String infoName;

    /**
     * 款式来源
     */
    private String styleOriginName;

    /**
     * 工艺师
     */
    private String technologistName;


    /**
     * 是否是迁移历史数据 0否 1是
     */
    private String historicalData;

    public String   getAccessory(){
        return getAccessory1();
    }

    /**
     * 默认条形码  唯一码  wareCode  +颜色编码  colorCode  +默认尺码的尺码编号[取设计属性中的默认尺码]
     */
    private String defaultBarCode;

    /**
     * 正确样样衣码
     */
    @ApiModelProperty(value = "正确样样衣码")
    private String correctBarCode;

    /**
     * 设计阶段-审批状态:草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "设计阶段-审批状态:草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)"  )
    private String designAuditStatus;
    /**
     * 设计阶段-打标状态:未打标(0)、部分打标(1)、全部打标(2)
     */
    @ApiModelProperty(value = "设计阶段-打标状态:未打标(0)、部分打标(1)、全部打标(2)"  )
    private String designMarkingStatus;
    /**
     * 下单阶段-审批状态:草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "下单阶段-审批状态:草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)"  )
    private String orderAuditStatus;
    /**
     * 下单阶段-打标状态:未打标(0)、部分打标(1)、全部打标(2)
     */
    @ApiModelProperty(value = "下单阶段-打标状态:未打标(0)、部分打标(1)、全部打标(2)"  )
    private String orderMarkingStatus;

    /**
     * 列头筛选数量
     */
    private Integer groupCount;

    /**
     * 产品季
     */
    private String planningSeason;

    private String channel;

    @ApiModelProperty(value = "工时部确认工价 0.否、1.是"  )
    private String wagesConfirm;

    @ApiModelProperty(value = "工时部确认工价时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date wagesConfirmTime;

    @ApiModelProperty(value = "计控实际成本")
    private BigDecimal controlPlanCost;

    /** 企划倍率 */
    @ApiModelProperty(value = "企划倍率"  )
    private BigDecimal planningRate;

    @ApiModelProperty("款式难度 ")
    private String styleDifficulty;


    @ApiModelProperty("面料难度 ")
    private String fabricDifficulty;


    @ApiModelProperty("工艺难度 ")
    private String processDifficulty;

}
