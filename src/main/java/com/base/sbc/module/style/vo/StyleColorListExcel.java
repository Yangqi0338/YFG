package com.base.sbc.module.style.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Optional;

@Data
public class StyleColorListExcel {

    @Excel(name = "SCM下发状态",replace={"_null","重新打开_3","发送失败_2","发送成功_1","未发送_0"})
    private String scmSendFlag;
    /**
     * 款式图
     */
    @ApiModelProperty(value = "款式图")
    private String  stylePic;

    @ApiModelProperty(value = "款式图")
    @Excel(name = "款式图",imageType = 2,type = 2)
    private  byte[] stylePic1;

    @Excel(name = "设计款号")
    private String   designNo;

    @Excel(name = "款式")
    private String  style;

    @ApiModelProperty(value = "款式名称")
    private String styleName;

    @Excel(name = "设计师")
    private String designer;

    @Excel(name = "工艺员")
    private String technicianName;

    @Excel(name = "颜色编码")
    private String colorCode;

    @Excel(name = "配色")
    private String  colorName;

    @Excel(name = "大货款号")
    private String styleNo;

    @Excel(name = "轻奢款",replace={"_null","是_1","否_0"} )
    private String isLuxury;

    @Excel(name = "细分")
    private String subdivideName;

    private String styleColorPic;

    @Excel(name = "大货款图",imageType = 2,type = 2)
    private  byte[] styleColorPic1;

    @Excel(name = "原大货款号")
    private String hisStyleNo;

    @Excel(name = "波段")
    private String  bandName;

    @Excel(name = "下主面料单",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sendMainFabricDate;

    @Excel(name = "下配料1",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sendBatchingDate1;

    @Excel(name = "下配料2",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sendBatchingDate2;

    @Excel(name = "下配料3",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sendBatchingDate3;

    @Excel(name = "下里布单",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sendSingleDate;

    @Excel(name = "设计下明细单",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date designDetailDate;

    @Excel(name = "设计下正确样",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date designCorrectDate;

    @Excel(name = "备注")
    private String remarks;

    @Excel(name = "产品细分")
    private String productSubdivideName;

    @Excel(name = "主款")
    private String principalStyle;

    @Excel(name = "主款款号")
    private String principalStyleNo;

    @Excel(name = "配饰")
    private String accessory;

    @Excel(name = "配饰款号")
    private String accessoryNo;

    @Excel(name = "状态",replace={"_null","停用_1","启用_0"} )
    private String status;

    @Excel(name = "是否下单",replace={"_null","是_1","否_0"} )
    private String orderFlag;

    @Excel(name = "上会",replace={"_null","是_1","否_0"} )
    private String meetFlag;

    @Excel(name = "大类")
    private String  prodCategory1stName;

    @Excel(name = "品类")
    private String  prodCategoryName;

    @Excel(name = "跟款设计师")
    private String merchDesignName;

    @Excel(name = "关联BOM")
    private String bom;

    @Excel(name = "BOM状态",replace={"_null","样品_0","大货_1"})
    private String bomStatus;

    @Excel(name = "是否配饰款",replace={"_null","否_0","是_1"})
    private String isTrim;

    @Excel(name = "销售类型")
    private String  salesTypeName;

    @Excel(name = "生产类型")
    private String devtTypeName;

    @Excel(name = "吊牌价")
    private String tagPrice;

    @Excel(name = "产品风格")
    private String  styleFlavourName;

    @Excel(name = "供应商")
    private String   supplier;

    @Excel(name = "供应商简称")
    private String supplierAbbreviation;

    @Excel(name = "供应商款号")
    private String supplierNo;

    @Excel(name = "供应商颜色")
    private String supplierColor;

    @Excel(name = "品名")
    private String productName;

    @Excel(name = "计控吊牌确定",replace={"未确定_null","未确定_0","已确定_1"})
    private String productHangtagConfirm;

    @Excel(name = "计控成本确认",replace={"未确定_null","未确定_0","已确定_1"})
    private String controlConfirm;

    @Excel(name = "品控吊牌价确定",replace={"未确定_null","未确定_0","已确定_1"})
    private String controlHangtagConfirm;

    @Excel(name = "款式类型")
    private String styleTypeName;

    @Excel(name = "款式沿用")
    private String   hisDesignNo;

    @Excel(name = "版式名称")
    private String  patternDesignName;


    public String getStyle() {
        return Optional.ofNullable(designNo).orElse("") + Optional.ofNullable(styleName).orElse("");
    }
}
