/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.dto;

import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.module.style.entity.StyleColor;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 类描述：新增修改样衣-款式配色 dto类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.SampleStyleColor
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 */
@Data
@ApiModel("样衣-款式配色 SampleStyleColor")
public class AddRevampStyleColorDto extends StyleColor {

    private String id;
    /**
     * 是否撞色
     */
    @ApiModelProperty(value = "是否撞色,0否 1是")
    private String colorCrash;
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
    /** 吊牌价 */
    @ApiModelProperty(value = "吊牌价"  )
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date newDate;
    /** 唯一id */
    @ApiModelProperty(value = "唯一id"  )
    private String wareCode;
    /** 下稿设计师 */
    @ApiModelProperty(value = "下稿设计师"  )
    private String  senderDesignerId;
    /**
     * 下稿设计师名称
     */
    @ApiModelProperty(value = "下稿设计师名称")
    private String senderDesignerName;
    /**
     * 正确样衣接收时间
     */
    @ApiModelProperty(value = "正确样衣接收时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date correctStyleDate;
    /**
     * rfid标记(0否 1是)
     */
    @ApiModelProperty(value = "rfid标记(0否 1是)")
    private String rfidFlag;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;

    /** 修改款式主款配饰标记 */
    @ApiModelProperty(value = "修改款式主款配饰标记"  )
    private String saveDtoFlag;

    /** 款式主款配饰 */
    @ApiModelProperty(value = "款式主款配饰"  )
    List<StyleMainAccessoriesSaveDto> saveDtoList;
}
