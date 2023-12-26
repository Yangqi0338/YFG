package com.base.sbc.module.sample.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Data
/*查询面料信息导出*/
public class FabricInformationExcelDto {


    @ApiModelProperty(value = "图片地址"  )
//    @Excel(name = "图片1",type = 2)
    private List<String> imageUrlList;

    @Excel(name = "图片1",type = 2)
    private String imageUrl1;

    @Excel(name = "图片2",type = 2)
    private String imageUrl2;

    @Excel(name = "图片3",type = 2)
    private String imageUrl3;

    @Excel(name = "图片4",type = 2)
    private String imageUrl4;

    @Excel(name = "图片5",type = 2)
    private String imageUrl5;

    @ApiModelProperty(value = "登记时间"  )
    @Excel(name = "登记时间",exportFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date registerDate;

    @Excel(name = "编码")
    private String codeName;

    @Excel(name = "年份")
    private String yearName;

    @Excel(name = "季节")
    private String seasonName;

    @Excel(name = "品牌")
    private String brandName;

    /** 调样设计师 */
    @Excel(name = "调样设计师")
    private String  atactiformStylist;

    /** 供应商 */
    @Excel(name = "供应商")
    private String supplierName;
    /** 供应商料号 */
    @Excel(name = "供应商料号")
    private String supplierMaterialCode;
    /** 供应商色号 */
    @ApiModelProperty(value = "供应商色号"  )
    @Excel(name = "供应商色号")
    private String supplierColor;
    /** 是否新面料（0是 1否 */
    @ApiModelProperty(value = "是否新面料（1是 0否"  )
    @Excel(name = "是否新面料", replace = {"否_0", "是_1", "否_null"} )
    private String isNewFabric;

    /** 持续环保（0是 1否 */
    @ApiModelProperty(value = "持续环保（1是 0否"  )
    @Excel(name = "持续环保", replace = {"否_0", "是_1", "否_null"} )
    private String isProtection;

    /** 数量（米） */
    @ApiModelProperty(value = "数量（米）"  )
    @Excel(name = "数量(米)")
    private Integer quantity;
    /** 面料价格 */
    @ApiModelProperty(value = "面料价格"  )
    @Excel(name = "价格")
    private BigDecimal fabricPrice;

    /** 面料价格 */
    @ApiModelProperty(value = "纱支"  )
    @Excel(name = "纱支")
    private String specification;

    /** 厂家成分 */
    @ApiModelProperty(value = "厂家成分"  )
    @Excel(name = "厂家成分")
    private String supplierFactoryIngredient;
    /** 货期 */
    @ApiModelProperty(value = "货期"  )
    @Excel(name = "货期")
    private Integer leadtime;
    /** 起订量 */
    @ApiModelProperty(value = "起订量"  )
    @Excel(name = "起订量")
    private Integer minimumOrderQuantity;
    /** 厂家有效门幅 */
    @ApiModelProperty(value = "厂家有效门幅"  )
    @Excel(name = "厂家有效门幅")
    private String translate;
    /** 克重 */
    @ApiModelProperty(value = "克重"  )
    @Excel(name = "克重")
    private String gramWeight;
    /** 胚布情况 */
    @ApiModelProperty(value = "胚布情况"  )
    @Excel(name = "胚布情况", replace = {"否_0", "是_1","否_null"})
    private String germinalCondition;

    /** 发送供应商标记(1是0否 */
    @ApiModelProperty(value = "发送供应商标记(1是0否"  )
    @Excel(name = "发送供应商", replace = {"否_0", "是_1","否_null"} )
    private String sendingSupplierFlag;
    /** 调样日期 */
    @ApiModelProperty(value = "调样日期"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "调样日期",exportFormat = "yyyy-MM-dd")
    private Date atactiformDate;
    /** 预估到样时间 */
    @ApiModelProperty(value = "预估到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "预估到样时间",exportFormat = "yyyy-MM-dd")
    private Date estimateAtactiformDate;
    /** 实际到样时间 */
    @ApiModelProperty(value = "实际到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "实际到样时间",exportFormat = "yyyy-MM-dd")
    private Date practicalAtactiformDate;
    /** 留样送检时间 */
    @ApiModelProperty(value = "留样送检时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "留样送检时间",exportFormat = "yyyy-MM-dd")
    private Date inspectDate;

    @ApiModelProperty(value = "面料是否可用(1是，0否)"  )
    @Excel(name = "面料是否可用", replace = {"否_0", "是_1","否_null"} )
    private String fabricIsUsable;


    /** 理化检测结果（0是1否 */
    @ApiModelProperty(value = "理化检测结果（0是1否"  )
    @Excel(name = "理化检测结果", replace = {"否_0", "是_1","否_null"} )
    private String physicochemistryDetectionResult;

    /** 样衣试穿洗涤送检时间 */
    @ApiModelProperty(value = "样衣试穿洗涤送检时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "样衣试穿洗涤送检时间",exportFormat = "yyyy-MM-dd")
    private Date sampleWashingInspectionDate;

    /** 洗涤检测结果（0是1否 */
    @ApiModelProperty(value = "洗涤检测结果（0是1否"  )
    @Excel(name = "洗涤检测结果",  replace = {"否_0", "是_1","否_null"} )
    private String washDetectionResult;
}
