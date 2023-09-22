package com.base.sbc.open.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/23 9:43
 */
@Data
public class OpenMaterialNoticeDto {
    //采购单号
    private String purchaseCode;
    //外部单号
    private String externalCode;
    //物料供应商
    private String supplierName;
    //采购员
    private String purchaserName;
    //采购日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date purchaseDate;
    //物料交期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date deliveryDate;
    //款号
    private String designStyleCode;
    //制版号
    private String plateBillCode;
    //物料编码
    private String materialCode;
    //物料名称
    private String materialName;
    //规格名称
    private String materialSpecifications;
    //物料颜色名称
    private String materialColor;
    //预计采购数量
    private BigDecimal expectedPurchaseNum;
    //采购单价
    private BigDecimal price;
    //采购单位
    private String purchaseUnit;
    //采购送货数量
    private BigDecimal deliveryQuantity;
    //采购收货数量
    private BigDecimal receivedQuantity;
    //金额
    private BigDecimal money;
    //供应商色号
    private String supplierColor;
    //备注
    private String remarks;
}
