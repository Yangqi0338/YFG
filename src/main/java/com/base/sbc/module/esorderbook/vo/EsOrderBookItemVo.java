/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

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


    //搭配图片
    private String img;
    //组
    private String name;
    //波段
    private String bandName;
    //上新时间
    private String newDate;
    //大类
    private String prodCategory1stName;
    //品类
    private String prodCategoryName;
    //中类*
    private String prodCategory2ndName;
    //大货款号
    private String styleNo;
    //大货图片
    private String stylePic;
    //颜色
    private String colorName;
    //款式
    private String designNo;
    //毛纱加工费
    private String a1;
    //车缝加工费
    private String a2;
    //外协加工费
    private String a3;
    //总成本
    private String a4;
    //倍价
    private String a5;
    //吊牌价
    private String a8;
    //产品风格
    private String a6;
    //实际倍率
    private String a7;
    //设计师*
    private String designer;
    //生产类型*
    private String devtTypeName;
    //厂家
    private String supplierAbbreviation;
    //厂家款号
    private String supplierNo;
    //厂家款颜色
    private String supplierColor;
    //锁定
    private String isLock;

}
