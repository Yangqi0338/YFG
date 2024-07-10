package com.base.sbc.module.tasklist.enums;

import com.base.sbc.module.patternlibrary.enums.PatternLibraryStatusEnum;
import lombok.Getter;

/**
 * 任务列表任务状态枚举
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Getter
public enum TaskListTaskStatusEnum {

    TO_DO(1, "待办"),
    DONE(2, "已办");

    private final Integer code;
    private final String value;

    TaskListTaskStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValueByCode(Integer code) {
        for (PatternLibraryStatusEnum value : PatternLibraryStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getValue();
            }
        }
        return "未知状态";
    }

}
