package com.base.sbc.module.style.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
/*查询代理货品资料*/
public class QueryStyleColorAgentDto extends Page {

    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 样衣id
     */
    @ApiModelProperty(value = "样衣id")
    private String styleId;
    @ApiModelProperty(value = "品类")
    private String categoryName;

    @ApiModelProperty(value = "紧急状态")
    private String taskLevelName;

    @ApiModelProperty(value = "生产类型")
    private String  devtTypeName;

    @ApiModelProperty(value = "设计师")
    private String designerId;

    @ApiModelProperty(value = "工艺员")
    private String  technicianId;

    @ApiModelProperty(value = "大货编号多个使用，分割"  )
    private String styleNo;

    @ApiModelProperty(value = "ids"  )
    private  String ids;

    @ApiModelProperty(value = "颜色规格"  )
    private String  colorSpecification;

    @ApiModelProperty(value = "细分"  )
    private String subdivide;

    @ApiModelProperty(value = "是否款式"  )
    private String isTrim;

    @ApiModelProperty(value = "是否上会")
    private String meetFlag;

    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    @ApiModelProperty(value = "大类")
    private String prodCategory1st;

    @ApiModelProperty(value = "品类编码")
    private String prodCategory;

    @ApiModelProperty(value = "品类")
    private String prodCategoryName;

    @ApiModelProperty(value = "款式状态")
    private String styleStatus;

    @ApiModelProperty(value = "配色")
    private String colorName;

    @ApiModelProperty(value = "款号"  )
    private String   designNo;

    @ApiModelProperty(value = "配色列表标记"  )
    private String colorListFlag;

    @ApiModelProperty(value = "款式类型"  )
    private String  styleTypeName;

    @ApiModelProperty(value = "款式沿用")
    private String hisDesignNo;

    @ApiModelProperty(value = "颜色编码")
    private String colorCode;

    @ApiModelProperty(value = "号型类型")
    private String sizeRangeName;

    @ApiModelProperty(value = "波段")
    private String bandName;

    @ApiModelProperty(value = "波段code")
    private String bandCode;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "设计师")
    private String designer;

    @ApiModelProperty(value = "工艺员")
    private String technicianName;

    @ApiModelProperty(value = "状态")
    private String status;

    private String sendStatus;

    @ApiModelProperty(value = "创建时间")
    private String[] createDate;

    @ApiModelProperty(value = "创建人")
    private String createName;

    @ApiModelProperty(value = "上新时间")
    private String  newDate;

    @ApiModelProperty(value = "轻奢款")
    private String  isLuxury;

    @ApiModelProperty(value = "细分")
    private String  subdivideName;

    @ApiModelProperty(value = "下主面料单")
    private String sendMainFabricDate;

    @ApiModelProperty(value = "下配料1")
    private String sendBatchingDate1;

    @ApiModelProperty(value = "下配料2")
    private String sendBatchingDate2;

    @ApiModelProperty(value = "下配料3")
    private String sendBatchingDate3;

    @ApiModelProperty(value = "下里布单")
    private String sendSingleDate;

    @ApiModelProperty(value = "设计下明细单")
    private String designDetailDate;

    @ApiModelProperty(value = "设计下正确样")
    private String designCorrectDate;

    @ApiModelProperty(value = "产品细分")
    private String productSubdivideName;


    @ApiModelProperty(value = "主款")
    private String principalStyle;

    @ApiModelProperty(value = "主款款号")
    private String principalStyleNo;

    @ApiModelProperty(value = "配饰")
    private String accessory;

    @ApiModelProperty(value = "配饰款号")
    private String accessoryNo;

    @ApiModelProperty(value = "下稿设计师")
    private String senderDesignerName;

    @ApiModelProperty(value = "跟款设计师")
    private String merchDesignName;

    @ApiModelProperty(value = "关联BOM")
    private String bom;

    @ApiModelProperty(value = "BOM阶段")
    private String bomStatus;

    @ApiModelProperty(value = "销售类型")
    private String salesTypeName;

    @ApiModelProperty(value = "吊牌价")
    private String tagPrice;

    @ApiModelProperty(value = "产品风格")
    private String styleFlavourName;

    @ApiModelProperty(value = "供应商名称")
    private String supplier;

    @ApiModelProperty(value = "供应商简称")
    private String supplierAbbreviation;

    @ApiModelProperty(value = "供应商款号")
    private String supplierNo;

    @ApiModelProperty(value = "供应商款色")
    private String supplierColor;

    @ApiModelProperty(value = "品名")
    private String productName;

    @ApiModelProperty(value = "次品编号")
    private String defectiveName;

    @ApiModelProperty(value = "计控吊牌确定")
    private String productHangtagConfirm;

    @ApiModelProperty(value = "计控成本确定")
    private String controlConfirm;

    @ApiModelProperty(value = "品控吊牌价确定")
    private String controlHangtagConfirm;

    @ApiModelProperty(value = "版式名称")
    private String patternDesignName;

    @ApiModelProperty(value = "下单标记")
    private String orderFlag;

    @ApiModelProperty(value = "大类名称"  )
    private String  prodCategory1stName;

    @ApiModelProperty(value = "中类")
    private String prodCategory2ndName;

    @ApiModelProperty(value = "中类"  )
    private String prodCategory2nd;

    @ApiModelProperty(value = "小类编码")
    private String prodCategory3nd;

    @ApiModelProperty(value = "小类")
    private String prodCategory3ndName;

    private String seasonName;

    private String yearName;

    /*是否导出图片*/
    private String imgFlag;

    /*导出标记*/
    private String excelFlag;

    /**
     * 外部颜色code
     */
    @ApiModelProperty(value = "外部颜色code")
    private String outsideColorCode;
    /**
     * 外部颜色名称
     */
    @ApiModelProperty(value = "外部颜色名称")
    private String outsideColorName;
    /**
     * 外部尺码code
     */
    @ApiModelProperty(value = "外部尺码code")
    private String outsideSizeCode;

    /**
     * 工艺师
     */
    private String technologistName;

    @ApiModelProperty(value = "打标下单标记")
    private String markingOrderFlag;

    /**
     * 设计阶段-打标状态:未打标(0)、部分打标(1)、全部打标(2)
     */
    @ApiModelProperty(value = "设计阶段-打标状态:未打标(0)、部分打标(1)、全部打标(2)"  )
    private String designMarkingStatus;
    /**
     * 下单阶段-打标状态:未打标(0)、部分打标(1)、全部打标(2)
     */
    @ApiModelProperty(value = "下单阶段-打标状态:未打标(0)、部分打标(1)、全部打标(2)"  )
    private String orderMarkingStatus;
}
