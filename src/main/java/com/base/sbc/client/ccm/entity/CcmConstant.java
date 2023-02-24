package com.base.sbc.client.ccm.entity;
/**
 * @author Youkehai
 * @data 创建时间:2021/4/25
 */
public class CcmConstant {
    /**系统参数配置-样衣出入确认开关*/
    public static final String SAMPLE_CLOTHES_CONFIRM="SAMPLE_CLOTHES_CONFIRM";

    /**系统参数配置-样衣盘点未盘点的按不在库处理*/
    public static final String SAMPLE_CLOTHES_INVENTORY_AUTO_OUT="SAMPLE_CLOTHES_INVENTORY_AUTO_OUT";

    /**编码规则--设计档案单据编码*/
    public static final String CODE_RULE_DESIGN_DATA="21000";

    /**编码规则--商品档案单据编码*/
    //TODO 因为商品档案没有审核，目前随便编码
    public static final String CODE_RULE_PRODUCT_DATA="21001";
    /**系统参数配置-审核状态显示或隐藏，关闭则全部生成采购需求*/
    public static final String AUDIT_STATUS_SHOW="AUDIT_STATUS_SHOW";
}
