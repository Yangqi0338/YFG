package com.base.sbc.config.enums.business.orderBook;


import com.base.sbc.config.exception.OtherException;
import lombok.Getter;

@Getter
public enum OrderBookDepartmentEnum {

    DESIGN("设计部",  "design"),
    OFFLINE("线下企划", "offline"),
    ONLINE("线上企划", "online"),
            ;

    private final String name;

    private final String code;

    OrderBookDepartmentEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static OrderBookDepartmentEnum getByCode(String code) {
        for (OrderBookDepartmentEnum e : OrderBookDepartmentEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        throw new OtherException("部门不存在");
    }

}
