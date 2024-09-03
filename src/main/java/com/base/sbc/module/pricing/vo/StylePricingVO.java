package com.base.sbc.module.pricing.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.base.sbc.config.enums.business.ProductionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 款式定价
 */
@Data
public class StylePricingVO {
    private String stylePricingId;
    /**
     * 款式配色 ID
     */
    private String styleColorId;
    /**
     * 资料包id
     */
    private String id;
    private String packType;

    private String ingredient;

    private String downContent;

    private String styleId;

    /**
     * 计控确认成本时间
     */
    @ApiModelProperty(value = "计控确认成本时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date controlConfirmTime;
    /**
     * 工时部确认工价时间
     */
    @ApiModelProperty(value = "工时部确认工价时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date wagesConfirmTime;
    /**
     * 是否商品吊牌确认时间
     */
    @ApiModelProperty(value = "商品吊牌确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productHangtagConfirmTime;
    /**
     * 是否计控吊牌确认时间
     */
    @ApiModelProperty(value = "计控吊牌确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date controlHangtagConfirmTime;
    /**
     * 样衣图片
     */
    @ApiModelProperty(value = "样衣图片")
    private String styleColorPic;

    @Excel(name = "样衣图片", type = 2, imageType = 2,orderNum = "0")
    private byte[] styleColorPic1;
    /**
     * 代码
     */

    @ApiModelProperty(value = "代码")
    private String code;
    /**
     * 中分类
     */
    @ApiModelProperty(value = "中分类")
    private String middleClassif;
    /**
     * 颜色
     */
    @ApiModelProperty(value = "颜色")
    @Excel(name = "颜色",orderNum = "2")
    private String color;
    /**
     * 色号
     */
    @ApiModelProperty(value = "色号")
    private String colourCode;

    /**
     * 颜色编码
     */
    @ApiModelProperty(value = "颜色编码")
    @Excel(name = "颜色编码",orderNum = "1")
    private String colorCode;
    /**
     * 是否上会
     */
    @ApiModelProperty(value = "是否上会")
    @Excel(name = "是否上会",replace = {"是_1","否_0"},orderNum = "18")
    private String meetFlag;
    /**
     * 款式
     */
    @ApiModelProperty(value = "款式")
    @Excel(name = "款式",orderNum = "3")
    private String style;

/*    public String getStyle() {
        return Optional.ofNullable(designNo).orElse("") + Optional.ofNullable(styleName).orElse("");
    }*/

    @ApiModelProperty(value = "款式名称")
//    @Excel(name = "款式名称")
    private String styleName;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    @Excel(name = "设计款号",orderNum = "4")
    private String designNo;
    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    @Excel(name = "大货款号",orderNum = "5")
    private String bulkStyleNo;
    /**
     * 品名
     */
    @ApiModelProperty(value = "品名")
    @Excel(name = "品名",orderNum = "6")
    private String productName;
    /**
     * BOM阶段 1:研发阶段 2:大货阶段
     */
    @ApiModelProperty(value = "BOM阶段")
    @Excel(name = "BOM阶段", replace = {"研发阶段_0", "大货阶段_1","研发阶段_null","研发阶段_"},orderNum = "7")
    private String bomStage;
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    @Excel(name = "供应商名称",orderNum = "16")
    private String supplierName;
    /**
     * 供应商简称
     */
    @ApiModelProperty(value = "供应商简称")
    @Excel(name = "供应商简称",orderNum = "17")
    private String supplierAbbreviation;
    /**
     * 目标成本
     */
    @ApiModelProperty(value = "目标成本")
    @Excel(name = "目标成本",orderNum = "8")
    private String targetCost;
    /**
     * 材料成本
     */
    @ApiModelProperty(value = "材料成本")
    @Excel(name = "材料成本", numFormat = "#.###",type = 10,orderNum = "9")
    private BigDecimal materialCost;
    /**
     * 包装费
     */
    @ApiModelProperty(value = "包装费")
    @Excel(name = "包装费", numFormat = "#.###",type = 10,orderNum = "10")
    private BigDecimal packagingFee;
    /**
     * 检测费
     */
    @ApiModelProperty(value = "检测费")
    @Excel(name = "检测费", numFormat = "#.###",type = 10,orderNum = "11")
    private BigDecimal testingFee;
    /**
     * 车缝加工费
     */
    @ApiModelProperty(value = "车缝加工费")
    @Excel(name = "车缝加工费", numFormat = "#.###",type = 10,orderNum = "12")
    private BigDecimal sewingProcessingFee;
    /**
     * 加工费
     */
    @ApiModelProperty(value = "加工费")
//    @Excel(name = "加工费", numFormat = "#.###",type = 10)
    private BigDecimal processingFee;
    /**
     * 毛纱加工费
     */
    @ApiModelProperty(value = "毛纱加工费")
    @Excel(name = "毛纱加工费", numFormat = "#.###",type = 10,orderNum = "13")
    private BigDecimal woolenYarnProcessingFee;
    /**
     * 外协加工费
     */
    @ApiModelProperty(value = "外协加工费")
    @Excel(name = "外协加工费", numFormat = "#.###",type = 10,orderNum = "14")
    private BigDecimal coordinationProcessingFee;
    /**
     * 二次加工费
     */
    @ApiModelProperty(value = "二次加工费")
//    @Excel(name = "二次加工费", numFormat = "#.###",type = 10)
    private BigDecimal secondaryProcessingFee;
    /**
     * 总成本
     */
    @ApiModelProperty(value = "总成本")
    @Excel(name = "总成本", numFormat = "#.###",type = 10,orderNum = "15")
    private BigDecimal totalCost;
    /**
     * 企划倍率
     */
    @ApiModelProperty(value = "企划倍率")
    @Excel(name = "企划倍率", numFormat = "#.###",type = 10,orderNum = "19")
    private BigDecimal planningRatio;
    /**
     * 预计销售价
     */
    @ApiModelProperty(value = "预计销售价")
    @Excel(name = "预计销售价", numFormat = "#.###",type = 10,orderNum = "20")
    private BigDecimal expectedSalesPrice;

    /** 工时部确认工价 0.否、1.是 */
    @ApiModelProperty(value = "工时部确认工价 0.否、1.是"  )
    @Excel(name = "工时部确认工价", replace = {"是_1", "否_0","否_null"},orderNum = "22")
    private String wagesConfirm;

    /**
     * 计控实际成本
     */
    @ApiModelProperty(value = "计控实际成本")
    @Excel(name = "计控实际成本", numFormat = "#.###",type = 10,orderNum = "21")
    private BigDecimal planCost;

    /**
     * 计控实际成本
     */
    @ApiModelProperty(value = "计控实际成本")
    private BigDecimal controlPlanCost;
    /**
     * 是否计控成本确认
     */
    @ApiModelProperty(value = "是否计控成本确认")
    @Excel(name = "是否计控成本确认", replace = {"是_1", "否_0","否_null"},orderNum = "22")
    private String planCostConfirm;
    /**
     * 吊牌价
     */
    @ApiModelProperty(value = "吊牌价")
    @Excel(name = "吊牌价", numFormat = "#.###",type = 10,orderNum = "23")
    private BigDecimal tagPrice;
    /**
     * 吊牌是否打印
     */
    @ApiModelProperty(value = "吊牌是否打印")
    @Excel(name = "吊牌是否打印", replace = {"是_1", "否_0","否_null"},orderNum = "24")
    private String tagPrintFlag;
    /**
     * 商品吊牌价是否确认
     */
    @ApiModelProperty(value = "商品吊牌价是否确认")
    @Excel(name = "商品吊牌价是否确认", replace = {"是_1", "否_0","否_null"},orderNum = "25")
    private String productTagPriceConfirm;
    /**
     * 计控吊牌价是否确认
     */
    @ApiModelProperty(value = "计控吊牌价是否确认")
    @Excel(name = "计控吊牌价是否确认", replace = {"是_1", "否_0","否_null"},orderNum = "26")
    private String planTagPriceConfirm;
    /**
     * 计控实际倍率
     */
    @ApiModelProperty(value = "计控实际倍率")
    @Excel(name = "计控实际倍率",orderNum = "27")
    private BigDecimal planActualMagnification;
    /**
     * 实际倍率
     */
    @ApiModelProperty(value = "实际倍率")
    @Excel(name = "实际倍率",orderNum = "28")
    private BigDecimal actualMagnification;
    /**
     * 产品风格
     */
    @ApiModelProperty(value = "产品风格")
    @Excel(name = "产品风格",orderNum = "29")
    private String productStyle;
    /**
     * 设计师
     */
    @ApiModelProperty(value = "设计师")
    @Excel(name = "设计师",orderNum = "30")
    private String designer;
    /**
     * 款式类型
     */
    @ApiModelProperty(value = "款式类型")
    @Excel(name = "款式类型",orderNum = "31")
    private String styleType;
    /**
     * 生产类型
     */
    @ApiModelProperty(value = "生产类型")
    @Excel(name = "生产类型",orderNum = "32")
    private String productionType;


    @ApiModelProperty(value = "系列编码")
//    @Excel(name = "系列编码")
    private String series;
    /**
     * 系列名称
     */
    @ApiModelProperty(value = "系列名称")
//    @Excel(name = "系列名称")
    private String seriesName;


    @ApiModelProperty(value = "产品风格编码产品风格编码")
//    @Excel(name = "产品风格编码")
    private String productStyleName;


    private String calcItemVal;

    private String calcItemVal1;

    private String calcItemVal2;

    /**
     * 列头筛选数量
     */
    private Integer groupCount;
    /** 设计工艺员id */
    @ApiModelProperty(value = "设计工艺员id")
    private String technicianId;
    /**
     * 设计工艺员名称
     */
    @ApiModelProperty(value = "设计工艺员名称")
    private String technicianName;


    /** 大类code */
    @ApiModelProperty(value = "大类code"  )
    private String prodCategory1st;
    /** 大类名称 */
    @ApiModelProperty(value = "大类名称"  )
    private String prodCategory1stName;
    /** 品类code */
    @ApiModelProperty(value = "品类code"  )
    private String prodCategory;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    private String prodCategoryName;
    /** 中类code */
    @ApiModelProperty(value = "中类code"  )
    private String prodCategory2nd;
    /** 中类名称 */
    @ApiModelProperty(value = "中类名称"  )
    private String prodCategory2ndName;
    /** 小类code */
    @ApiModelProperty(value = "小类code"  )
    private String prodCategory3rd;
    /** 小类名称 */
    @ApiModelProperty(value = "小类名称"  )
    private String prodCategory3rdName;

    public boolean isCmt(){
        return ProductionType.findByCode(this.getProductionType()) == ProductionType.CMT;
    }
}

