/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.enums.business.RFIDType;
import com.base.sbc.module.smp.dto.SmpGoodsDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 类描述：款式-款式配色 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.entity.StyleColor
 * @email your email
 * @date 创建时间：2023-10-18 15:18:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_style_color")
@ApiModel("款式-款式配色 StyleColor")
public class StyleColor extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    public SmpGoodsDto toSmpGoodsDto() {
        SmpGoodsDto smpGoodsDto = new SmpGoodsDto();
        smpGoodsDto.setBandId(bandCode);
        smpGoodsDto.setDesignNumber(designNo);
        smpGoodsDto.setMainPush("1".equals(isMainly));
        smpGoodsDto.setColorCode(colorCode);
        smpGoodsDto.setColorName(colorName);
        smpGoodsDto.setPrice(tagPrice);
        smpGoodsDto.setManufacture(supplier);
        smpGoodsDto.setManufactureCode(supplierCode);
        smpGoodsDto.setLuxury("1".equals(isLuxury));
        smpGoodsDto.setSupplierArticle(supplierNo);
        smpGoodsDto.setSupplierArticleColor(supplierColor);
        smpGoodsDto.setSaleType(salesType);
        smpGoodsDto.setBulkNumber(styleNo);
        smpGoodsDto.setCode(styleNo);
        smpGoodsDto.setMainCode(principalStyleNo);
        smpGoodsDto.setSecCode(accessoryNo);
        IdGen idGen = new IdGen();
        smpGoodsDto.setId(id);
        smpGoodsDto.setName(colorName);
        smpGoodsDto.setCreator(getCreateName());
        smpGoodsDto.setCreateTime(getCreateDate());
        smpGoodsDto.setModifiedPerson(getUpdateName());
        smpGoodsDto.setModifiedTime(getUpdateDate());
        smpGoodsDto.setPlmId(null);
        smpGoodsDto.setSyncId(String.valueOf(idGen.nextId()));
        smpGoodsDto.setActive("0".equals(status));
        smpGoodsDto.setDesignCorrectDate(designCorrectDate);
        smpGoodsDto.setDesignDetailDate(designDetailDate);
        smpGoodsDto.setRfidFlag(this.rfidFlag);
        smpGoodsDto.setRfidType(this.rfidType);
        return smpGoodsDto;
    }
    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 款式(大货款号) */
    @ApiModelProperty(value = "款式(大货款号)"  )
    private String styleNo;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 产品季节id */
    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;
    /** 款式设计id（款式表） */
    @ApiModelProperty(value = "款式设计id（款式表）"  )
    private String styleId;
    /** 款式配色图 */
    @ApiModelProperty(value = "款式配色图"  )
    private String styleColorPic;
    /** 配色 */
    @ApiModelProperty(value = "配色"  )
    private String colorName;
    /** 颜色规格 */
    @ApiModelProperty(value = "颜色规格"  )
    private String colorSpecification;
    /** 颜色id */
    @ApiModelProperty(value = "颜色id"  )
    private String colourLibraryId;
    /** 颜色编码 */
    @ApiModelProperty(value = "颜色编码"  )
    private String colorCode;
    /** 设计款号 */
    @ApiModelProperty(value = "设计款号"  )
    private String designNo;
    /** 关联的设计款号 */
    @ApiModelProperty(value = "关联的设计款号"  )
    private String seatDesignNo;
    /** 轻奢款(0否,1:是) */
    @ApiModelProperty(value = "轻奢款(0否,1:是)"  )
    private String isLuxury;
    /** 细分 */
    @ApiModelProperty(value = "细分"  )
    private String subdivide;
    /** 细分名称 */
    @ApiModelProperty(value = "细分名称"  )
    private String subdivideName;
    /** 产品细分 */
    @ApiModelProperty(value = "产品细分"  )
    private String productSubdivide;
    /** 产品细分 */
    @ApiModelProperty(value = "产品细分"  )
    private String productSubdivideName;
    /** 配色风格 */
    @ApiModelProperty(value = "配色风格"  )
    private String colorStyleFlavour;
    /** 配色风格名称 */
    @ApiModelProperty(value = "配色风格名称"  )
    private String colorStyleFlavourName;
    /** 波段编码 */
    @ApiModelProperty(value = "波段编码"  )
    private String bandCode;
    /** 波段名称 */
    @ApiModelProperty(value = "波段名称"  )
    private String bandName;
    /** 关联bom */
    @ApiModelProperty(value = "关联bom"  )
    private String bom;
    /** 生产类型 */
    @ApiModelProperty(value = "生产类型"  )
    private ProductionType devtType;
    /** 生产类型名称 */
    @ApiModelProperty(value = "生产类型名称"  )
    private String devtTypeName;
    /** 销售类型 */
    @ApiModelProperty(value = "销售类型"  )
    private String salesType;
    /** 销售类型名称 */
    @ApiModelProperty(value = "销售类型名称"  )
    private String salesTypeName;
    /** 原大货款号 */
    @ApiModelProperty(value = "原大货款号"  )
    private String hisStyleNo;
    /** 是否是内饰款(0否,1:是) */
    @ApiModelProperty(value = "是否是内饰款(0否,1:是)"  )
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
    /** 配饰款号 */
    @ApiModelProperty(value = "配饰款号"  )
    private String accessoryNo;
    /** 下主面料单 */
    @ApiModelProperty(value = "下主面料单"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendMainFabricDate;
    /** 下配料1 */
    @ApiModelProperty(value = "下配料1"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendBatchingDate1;
    /** 下配料2 */
    @ApiModelProperty(value = "下配料2"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendBatchingDate2;
    /** 下配料3 */
    @ApiModelProperty(value = "下配料3"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendBatchingDate3;
    /** 下里布单 */
    @ApiModelProperty(value = "下里布单"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendSingleDate;
    /** 设计下明细单 */
    @ApiModelProperty(value = "设计下明细单"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designDetailDate;
    /** 设计下正确样 */
    @ApiModelProperty(value = "设计下正确样"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designCorrectDate;
    /** 吊牌价 */
    @ApiModelProperty(value = "吊牌价"  )
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal tagPrice;
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
    /** 波段企划id */
    @ApiModelProperty(value = "波段企划id"  )
    private String planningBandId;
    /** 品类信息id */
    @ApiModelProperty(value = "品类信息id"  )
    private String planningCategoryId;
    /** 坑位信息id */
    @ApiModelProperty(value = "坑位信息id"  )
    private String planningCategoryItemId;
    /** SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开 */
    @ApiModelProperty(value = "SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开"  )
    private String scmSendFlag;
    /** 是否报次款0否 1是 */
    @ApiModelProperty(value = "是否报次款0否 1是"  )
    private String isDefective;
    /** 上新时间 */
    @ApiModelProperty(value = "上新时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date newDate;
    /** 唯一id */
    @ApiModelProperty(value = "唯一id"  )
    private String wareCode;
    /** 品名名称 */
    @ApiModelProperty(value = "品名名称"  )
    private String productName;
    /** 品名编码 */
    @ApiModelProperty(value = "品名编码"  )
    private String productCode;
    /** 上会标记(0未上会，1已上会) */
    @ApiModelProperty(value = "上会标记(0未上会，1已上会)"  )
    private String meetFlag;
    /** bom状态:(0样品,1大货) */
    @ApiModelProperty(value = "bom状态:(0样品,1大货)"  )
    private String bomStatus;
    /** 下稿设计师 */
    @ApiModelProperty(value = "下稿设计师"  )
    private String senderDesignerId;
    /** 下稿设计师名称 */
    @ApiModelProperty(value = "下稿设计师名称")
    private String senderDesignerName;
    /**
     * 下单标记（0否 1是）
     */
    @ApiModelProperty(value = "下单标记（0否 1是）")
    private String orderFlag;
    /**
     * rfid标记(0否 1是)
     */
    @ApiModelProperty(value = "rfid标记(0否 1是)")
    private String rfidFlag;
    /**
     * rfid标记(0否 1是)
     */

    @ApiModelProperty(value = "rfid类型")
    private RFIDType rfidType;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 正确样衣接收时间
     */
    @ApiModelProperty(value = "正确样衣接收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date correctStyleDate;
    /**
     * 产品风格编码
     */
    @ApiModelProperty(value = "产品风格编码")
    private String productStyle;
    /**
     * 产品风格
     */
    @ApiModelProperty(value = "产品风格")
    private String productStyleName;

    /**
     * 是否撞色
     */
    @ApiModelProperty(value = "是否撞色,0否 1是")
    private String colorCrash;

    /**
     * 是否是迁移历史数据 0否 1是
     */
    private String historicalData;

    /**
     * 正确样样衣码
     */
    @ApiModelProperty(value = "正确样样衣码")
    private String correctBarCode;

    /**
     * 工艺接收明细单时间
     */
    @ApiModelProperty(value = "工艺接收明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date techReceiveTime;
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

    @ApiModelProperty(value = "设计下面料详单逾期原因"  )
    private String sendMainFabricOverdueReason;

    @ApiModelProperty(value = "设计下明细单逾期原因"  )
    private String designDetailOverdueReason;

    @ApiModelProperty(value = "设计下正确样逾期原因"  )
    private String designCorrectOverdueReason;

    /** 发送部门 */
    @ApiModelProperty(value = "发送部门"  )
    private String sendDeptId;
    /** 接收部门 */
    @ApiModelProperty(value = "接收部门"  )
    private String receiveDeptId;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

