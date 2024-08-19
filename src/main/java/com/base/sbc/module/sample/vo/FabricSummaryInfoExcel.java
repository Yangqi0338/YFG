package com.base.sbc.module.sample.vo;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.base.sbc.config.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class FabricSummaryInfoExcel extends FabricSummaryInfoVo{

    @ApiModelProperty(value = "品牌名称"  )
    @Excel(name = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "物料图片"  )
    private String imageUrl;

    @ApiModelProperty(value = "物料图片"  )
    @Excel(name = "物料图片",imageType = 2,type = 2)
    private byte[] imageUrl1;

    @ApiModelProperty(value = "详单编号"  )
    @Excel(name = "详单编号")
    private String fabricSummaryCode;

    @ApiModelProperty(value = "物料编号"  )
    @Excel(name = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "供应商物料编号"  )
    @Excel(name = "供应商物料编号")
    private String supplierFabricCode;

    @ApiModelProperty(value = "规格"  )
    @Excel(name = "规格")
    private String widthName;

    @ApiModelProperty(value = "面料检测"  )
    @Excel(name = "面料检测",replace={"_null","合格_1","不合格_0"})
    private String physicochemistryDetectionResult;

    @ApiModelProperty(value = "试穿报告"  )
    @Excel(name = "试穿报告")
    private String fittingResult;

    @ApiModelProperty(value = "询价编号"  )
    @Excel(name = "询价编号")
    private String enquiryCode;

    @ApiModelProperty(value = "生产周期-期货"  )
    @Excel(name = "期货")
    private String productionDay;

    @ApiModelProperty(value = "含税价格"  )
    @Excel(name = "含税价格")
    private BigDecimal supplierQuotationPrice;

    @ApiModelProperty(value = "年份尾缀"  )
    @Excel(name = "年份尾缀")
    private String yearSuffix;

    @ApiModelProperty(value = "图片"  )
    private String stylePic;

    @ApiModelProperty(value = "图片"  )
    @Excel(name = "图片",imageType = 2,type = 2)
    private byte[] stylePic1;

    @ApiModelProperty(value = "款号"  )
    @Excel(name = "款号")
    private String styleNo;

    @ApiModelProperty(value = "是否撞色"  )
    @Excel(name = "是否撞色",replace={"_null","是_1","否_0"})
    private String colorCrash;

    @ApiModelProperty(value = "投产件数"  )
    @Excel(name = "投产件数")
    private String totalProduction;

    @ApiModelProperty(value = "单件用量"  )
    @Excel(name = "单件用量")
    private BigDecimal unitUse;

    @ApiModelProperty(value = "需求米数")
    @Excel(name = "需求米数")
    private BigDecimal demandNumber;

    @ApiModelProperty(value = "备注"  )
    @Excel(name = "备注")
    private String remarks;

    @ApiModelProperty(value = "创建人"  )
    @Excel(name = "创建人")
    private String createName;

    public BigDecimal getDemandNumber(){
        if (null != unitUse && StringUtils.isNotBlank(totalProduction)){
            return unitUse.multiply(BigDecimal.valueOf(Long.parseLong(totalProduction))).setScale(2, RoundingMode.HALF_UP);
        }
        return demandNumber;
    }
}
