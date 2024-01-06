package com.base.sbc.module.hangtag.enums;

/**
 * @author huangqiang
 * @date 2023-12-06
 */
public enum HangTagDeliverySCMStatusEnum {
    TAG_LIST_CANCEL(0, "吊牌列表反审"),
    TECHNOLOGIST_CONFIRM(1, "工艺员确认"),
    TECHNICAL_CONFIRM(2, "技术确认"),
    QUALITY_CONTROL_CONFIRM(3, "品控确认"),
    PLAN_COST_CONFIRM(4, "计控成本确认"),
    PRODUCT_TAG_PRICE_CONFIRM(5, "商品吊牌价确认"),
    PLAN_TAG_PRICE_CONFIRM(6, "计控吊牌价确认");

    private final Integer code;
    private final String description;

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    HangTagDeliverySCMStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static HangTagDeliverySCMStatusEnum getByCode(int code) {
        for (HangTagDeliverySCMStatusEnum statusEnum : values()) {
            if (statusEnum.getCode() == code) {
                return statusEnum;
            }
        }
        return null;
    }
}
