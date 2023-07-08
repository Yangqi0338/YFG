package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/*
* 用于基础资料查询dto类
*
* */
@Data
@ApiModel(" QueryDto")
public class QueryRevampBasicsdatumSupplierDto extends Page {

    private String id;

    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String picture;
    /** 供应商编码 */
    @ApiModelProperty(value = "供应商编码"  )
    private String supplierCode;
    /** 统一社会信用代码 */
    @ApiModelProperty(value = "统一社会信用代码"  )
    private String creditCode;
    /** 供应商类型 */
    @ApiModelProperty(value = "供应商类型"  )
    private String supplierType;
    /** 是否是内部供应商 */
    @ApiModelProperty(value = "是否是内部供应商"  )
    private String isInteriorSupplier;
    /** 是否是虚拟供应商 */
    @ApiModelProperty(value = "是否是虚拟供应商"  )
    private String isVirtualSupplier;
    /** 旧供应商编码 */
    @ApiModelProperty(value = "旧供应商编码"  )
    private String formerSupplierCode;
    /** 供应商 */
    @ApiModelProperty(value = "供应商"  )
    private String supplier;
    /** 供应商简称 */
    @ApiModelProperty(value = "供应商简称"  )
    private String supplierAbbreviation;
    /** 拼音代号 */
    @ApiModelProperty(value = "拼音代号"  )
    private String pinyinCode;
    /** 开发年份 */
    @ApiModelProperty(value = "开发年份"  )
    private String year;
    /** 发送状态(0 已发送,1未发送 */
    @ApiModelProperty(value = "发送状态(0 已发送,1未发送"  )
    private String sendState;
    /** 数据状态(0新增2更新 */
    @ApiModelProperty(value = "数据状态(0新增2更新"  )
    private String dataState;
    /** 是 供应商 */
    @ApiModelProperty(value = "是 供应商"  )
    private String isSupplier;
    /** 联系人 */
    @ApiModelProperty(value = "联系人"  )
    private String contact;
    /** 个体供应商 */
    @ApiModelProperty(value = "个体供应商"  )
    private String isIndividualSupplier;
    /** 地址 */
    @ApiModelProperty(value = "地址"  )
    private String address;
    /** 结算方式 */
    @ApiModelProperty(value = "结算方式"  )
    private String clearingForm;
    /** 省份/城市 */
    @ApiModelProperty(value = "省份/城市"  )
    private String provinceCity;
    /** 英文地址 */
    @ApiModelProperty(value = "英文地址"  )
    private String englishAddress;
    /** 对公开户行 */
    @ApiModelProperty(value = "对公开户行"  )
    private String publicBank;
    /** 对公账户名 */
    @ApiModelProperty(value = "对公账户名"  )
    private String publicAccount;
    /** 对公开户账号 */
    @ApiModelProperty(value = "对公开户账号"  )
    private String publicAccountName;
    /** 代理商 Images */
    @ApiModelProperty(value = "代理商 Images"  )
    private String agentImages;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

    private String createName;

    private String   createDate;
}
