package com.base.sbc.module.tasklist.enums;

import com.base.sbc.module.patternlibrary.enums.PatternLibraryStatusEnum;
import lombok.Getter;

/**
 * 任务列表任务类型枚举
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Getter
public enum TaskListTaskTypeEnum {

    STYLE_MARKING_ISSUED(1, "款式打标下发");

    private final Integer code;
    private final String value;

    TaskListTaskTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValueByCode(Integer code) {
        for (PatternLibraryStatusEnum value : PatternLibraryStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getValue();
            }
        }
        return "";
    }

}
