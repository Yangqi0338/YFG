/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：导入导出基础资料-供应商 dto类
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumSupplier
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-22 10:51:07
 * @version 1.0
 */
@Data
@ApiModel("基础资料-供应商 BasicsdatumSupplier")
public class BasicsdatumSupplierExcelDto  {

   // @Excel(name = "id")
   private String id;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    @Excel(name = "图片",type = 2)
    private String picture;
    /** 供应商编码 */
    @ApiModelProperty(value = "供应商编码"  )
    @Excel(name = "供应商编码")
    private String supplierCode;
    /** 统一社会信用代码 */
    @ApiModelProperty(value = "统一社会信用代码"  )
    @Excel(name = "统一社会信用代码")
    private String creditCode;
    /** 供应商类型 */
    @ApiModelProperty(value = "供应商类型"  )
    @Excel(name = "供应商类型")
    private String supplierType;
    /** 是否是内部供应商 */
    @ApiModelProperty(value = "是否是内部供应商"  )
    @Excel(name = "是否是内部供应商", replace = {"false_0","true_1"})
    private String isInteriorSupplier;
    /** 是否是虚拟供应商 */
    @ApiModelProperty(value = "是否是虚拟供应商"  )
    @Excel(name = "是否是虚拟供应商", replace = {"false_0","true_1"})
    private String isVirtualSupplier;
    /** 旧供应商编码 */
    @ApiModelProperty(value = "旧供应商编码"  )
    @Excel(name = "旧供应商编码")
    private String formerSupplierCode;
    /** 供应商 */
    @ApiModelProperty(value = "供应商"  )
    @Excel(name = "供应商")
    private String supplier;
    /** 供应商简称 */
    @ApiModelProperty(value = "供应商简称"  )
    @Excel(name = "供应商简称")
    private String supplierAbbreviation;
    /** 拼音代号 */
    @ApiModelProperty(value = "拼音代号"  )
    @Excel(name = "拼音代号")
    private String pinyinCode;
    /** 开发年份 */
    @ApiModelProperty(value = "开发年份"  )
    @Excel(name = "开发年份")
    private String year;
    /** 发送状态(0 已发送,1未发送 */
    @ApiModelProperty(value = "发送状态(0 已发送,1未发送"  )
    @Excel(name = "发送状态", replace = {"重新打开_2","未发送_1","已发送_0"})
    private String sendState;
    /** 数据状态(0新增2更新 */
    @ApiModelProperty(value = "数据状态(0新增1更新"  )
    @Excel(name = "数据状态(0新增1更新", replace = {"更改_1","更改_0"})
    private String dataState;
    /** 是 供应商 */
    @ApiModelProperty(value = "是 供应商"  )
    @Excel(name = "是 供应商", replace = {"false_1","true_0"})
    private String isSupplier;
    /** 联系人 */
    @ApiModelProperty(value = "联系人"  )
    @Excel(name = "联系人")
    private String contact;
    /** 个体供应商 */
    @ApiModelProperty(value = "个体供应商"  )
    @Excel(name = "个体供应商", replace = {"false_0","true_1"})
    private String isIndividualSupplier;
    /** 地址 */
    @ApiModelProperty(value = "地址"  )
    @Excel(name = "地址")
    private String address;
    /** 结算方式 */
    @ApiModelProperty(value = "结算方式"  )
    @Excel(name = "结算方式")
    private String clearingForm;
    /** 省份/城市 */
    @ApiModelProperty(value = "省份/城市"  )
    @Excel(name = "省份/城市")
    private String provinceCity;
    /** 英文地址 */
    @ApiModelProperty(value = "英文地址"  )
    @Excel(name = "英文地址")
    private String englishAddress;
    /** 对公开户行 */
    @ApiModelProperty(value = "对公开户行"  )
    @Excel(name = "对公开户行")
    private String publicBank;
    /** 对公开户账号 */
    @ApiModelProperty(value = "对公开户账号"  )
    @Excel(name = "对公开户账号")
    private String publicAccount;
    /** 对公账户名 */
    @ApiModelProperty(value = "对公账户名"  )
    @Excel(name = "对公账户名")
    private String publicAccountName;
    /** 代理商 Images */
    @ApiModelProperty(value = "代理商 Images"  )
    @Excel(name = "代理商 Images",type = 2)
    private String agentImages;
/*    *//** 状态(0正常,1停用) *//*
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    @Excel(name = "状态", replace = {"false_1","true_0"})
    private String status;*/
}
