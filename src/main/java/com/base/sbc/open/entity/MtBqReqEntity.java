package com.base.sbc.open.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/25 15:30:56
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_smp_supplier")
public class MtBqReqEntity extends BaseDataEntity<String> {
    /** 联系人 */
    protected String contact;
    /** 联系人职位 */
    protected String jobTitle;
    /** 供应商编码 */
    private String partner;
    /** 供应商名称 */
    private String bpName;
    /** 简称 */
    private String searchTerm1;
    /** 个体商户=1 */
    private String partnerCategory;
    /** 供应商分组 */
    private String partnerGroup;
    /** 供应商类型 */
    private String partnerType;
    /** 订单货币 */
    private String orderCurrency;
    /** 统一社会信用代码 */
    private String identificationNumber;
    /** 国家编码 */
    private String country;
    /** 城市 */
    private String city;
    /** 对公账户 */
    private String bankAcct;
    /** 对公开户行 */
    private String bankAccountName;
    /** 对公账户名 */
    private String accountHolder;
    /** 联行号 */
    private String bankKey;
    /** 联行信息集合 */
    private String bankInfos;
}
