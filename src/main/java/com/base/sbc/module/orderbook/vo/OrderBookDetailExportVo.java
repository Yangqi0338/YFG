package com.base.sbc.module.orderbook.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 卞康
 * @date 2023/12/8 14:14:03
 * @mail 247967116@qq.com
 */
@Data
public class OrderBookDetailExportVo {
    @Excel(name = "序号")
    private String serialNumber;

    @Excel(name = "大货款号")
    private String bulkStyleNo;

    // @Excel(name = "款式图",type = 2,imageType = 2)
    private String stylePic;

    @Excel(name = "款式图",type = 2,imageType = 2)
    private byte[] stylePic1;

    @Excel(name = "投产日期")
    private Date commissioningDate;

    @Excel(name = "是否延续")
    private String continuationPoint;

    @Excel(name = "研发类型")
    private String devtTypeName;

    @Excel(name = "FOB成衣厂家编码")
    private String fobClothingFactoryCode;

    @Excel(name = "FOB成衣厂家名称")
    private String fobClothingFactoryName;

    @Excel(name = "设计师名称")
    private String designerName;

    @Excel(name = "波段名称")
    private String bandName;

    @Excel(name = "波段编码")
    private String bandCode;

    @Excel(name = "品类编码")
    private String categoryCode;

    @Excel(name = "品类名称")
    private String categoryName;



    @Excel(name = "后五位")
    private String lastFiveDigits;

    public String getLastFiveDigits() {
        if(bulkStyleNo==null){
            return null;
        }
        return bulkStyleNo.substring(bulkStyleNo.length() - 5);
    }

    @Excel(name = "颜色")
    private String colorName;

    /**
     * 颜色编码
     */
    @Excel(name = "颜色编码")
    private String colorCode;

    @Excel(name = "色号")
    private String supplierColor;

    @Excel(name = "吊牌价")
    private BigDecimal tagPrice;

    @Excel(name = "线下投产")
    private String offlineProduction;

    @Excel(name = "线上投产")
    private String onlineProduction;

    @Excel(name = "总投产")
    private String totalProduction;

    @Excel(name = "备料")
    private String material;

    @Excel(name = "备胚")
    private String braiding;

    @Excel(name = "倍率")
    private String rate;

    @Excel(name = "系数")
    private String coefficientName;

    @Excel(name = "CMT成本")
    private String cmtCost;

    @Excel(name = "CMT总成本（含税）")
    private String cmtTotalCost;

    @Excel(name = "4倍价")
    private String multiplePrice;

    @Excel(name = "目标入仓日期")
    private String targetTime;

    @Excel(name = "生产紧急程度名称")
    private String productionUrgencyName;

    @Excel(name = "备注")
    private String remark;

    @Excel(name = "CMT车缝工价")
    private String cmtCarpetCost;

    @Excel(name = "FOB成本价")
    private String fobCost;

    @Excel(name = "后道")
    private String honest;

    @Excel(name = "其他")
    private String other;

    @Excel(name = "单件面料用量/米")
    private String unitFabricDosage;

    @Excel(name = "单件用量/里")
    private BigDecimal unitDosage;

    @Excel(name = "面料状态")
    private String fabricState;

    @Excel(name = "公司面料编号")
    private String companyFabricCode;

    @Excel(name = "供应商名称")
    private String supplierName;

    @Excel(name = "供应商编号")
    private String supplierCode;

    @Excel(name = "面料厂家色号")
    private String fabricFactoryColorNumber;

    @Excel(name = "面料类型")
    private String fabricType;

    @Excel(name = "含税单价/米")
    private BigDecimal unitPrice;

    @Excel(name = "货期")
    private String deliveryTime;

    @Excel(name = "库存面料米数")
    private String inventoryFabricMeter;

    @Excel(name = "库存可做件数")
    private String inventoryDoableNumber;

    @Excel(name = "面料备注")
    private String fabricRemarks;

    @Excel(name = "设计款号"  )
    private String designNo;

    @Excel(name = "旧设计款号"  )
    private String oldDesignNo;

    @Excel(name = "套装款号"  )
    private String suitNo;

    /** 中类名称 */
    @Excel(name = "中类名称"  )
    private String prodCategory2ndName;

}
