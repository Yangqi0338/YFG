package com.base.sbc.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WarehouseStatusEnum {
    WAIT_WAREHOUSING("0", "待入库"),
    IN_STORAGE("1", "入库中"),
    COMPLETED_WAREHOUSING("2", "入库完成"),
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
