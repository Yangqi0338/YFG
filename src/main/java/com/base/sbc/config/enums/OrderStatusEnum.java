package com.base.sbc.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    DRAFT("0", "草稿"),
    AUDIT("1", "待审核"),
    APPROVED("2", "审核通过"),
    REJECT("-1", "驳回"),
    ;

    private final String key;

    private final String value;

    public static String keyGetValue(String key){
        for(OrderStatusEnum item : OrderStatusEnum.values()){
            if(item.getKey().equals(key)){
                return item.getValue();
            }
        }
        return null;
    }
}
