/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：ES订货本明细Vo 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.esorderbook.vo.EsOrderBookItemVo
 * @email your email
 * @date 创建时间：2024-3-28 16:21:15
 */
@Data
@ApiModel("ES订货本明细 EsOrderBookItemVo")
public class EsOrderBookItemVo {

    private static final long serialVersionUID = 1L;

    private String type;

    private String id;
    private String styleColorId;
    private String headId;
    private String headName;
    private String seasonId;
    private String stylePricingId;
    private String packId;
    private String groupId;
    private Integer sortIndex;

    private String packType;

    private String planningSeason;
    //搭配图片
    private String groupImg;
    //组
    private String groupName;
    private String newGroupName;
    //波段
    private String bandName;
    //上新时间
    private String newDate;
    //大类
    private String prodCategory1stName;
    //品类
    private String prodCategoryName;
    //中类
    private String prodCategory2ndName;
    //大货款号
    private String styleNo;
    //大货图片
    private String styleColorPic;
    //颜色
    private String colorName;
    //款式
    private String designNo;
    //毛纱加工费
    private BigDecimal woolenYarnProcessingFee;
    //车缝加工费
    private BigDecimal sewingProcessingFee;
    //外协加工费
    private BigDecimal coordinationProcessingFee;
    //总成本
    private BigDecimal totalCost;
    //倍价
    private BigDecimal multiplePrice;
    //吊牌价
    private BigDecimal tagPrice;
    //产品风格
    private String productStyleName;
    //实际倍率
    private BigDecimal actualMagnification;
    //设计师
    private String designer;
    //生产类型
    private String devtTypeName;
    //厂家
    private String supplierAbbreviation;
    //厂家款号
    private String supplierNo;
    //厂家款颜色
    private String supplierColor;
    //锁定
    private String isLock;

    /**
     * 列头筛选数量
     */
    private Integer groupCount;

    /**
     * 包装费
     */
    private BigDecimal packagingFee;
    /**
     * 检测费
     */
    private BigDecimal testingFee;
    /**
     * 物料费
     */
    private BigDecimal materialPrice;

    public String getProductStyleName() {
        if(actualMagnification.compareTo(new BigDecimal("6")) >= 0){
            return "B";
        }
        if(actualMagnification.compareTo(new BigDecimal("5.5")) >= 0 && actualMagnification.compareTo(new BigDecimal("6")) < 0) {
            return "F";
        }
        if(actualMagnification.compareTo(new BigDecimal("5")) >= 0 && actualMagnification.compareTo(new BigDecimal("5.5")) < 0) {
            return "A";
        }
        if(actualMagnification.compareTo(new BigDecimal("4.5")) >= 0 && actualMagnification.compareTo(new BigDecimal("5")) < 0) {
            return "C";
        }
        if(actualMagnification.compareTo(new BigDecimal("4")) >= 0 && actualMagnification.compareTo(new BigDecimal("4.5")) < 0) {
            return "D";
        }
        if(actualMagnification.compareTo(new BigDecimal("3")) >= 0 && actualMagnification.compareTo(new BigDecimal("4")) < 0) {
            return "E";
        }
        if(actualMagnification.compareTo(new BigDecimal("2")) >= 0 && actualMagnification.compareTo(new BigDecimal("3")) < 0) {
            return "G";
        }
        if(actualMagnification.compareTo(new BigDecimal("1.5")) >= 0 && actualMagnification.compareTo(new BigDecimal("2")) < 0) {
            return "H";
        }
        return "J";
    }
}
