package com.base.sbc.module.tasklist.enums;

import com.base.sbc.module.patternlibrary.enums.PatternLibraryStatusEnum;
import lombok.Getter;

/**
 * 任务列表详情同步结果枚举
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Getter
public enum TaskListDetailSyncResultEnum {

    SUCCESS(1, "成功"),
    FAILED(2, "失败");

    private final Integer code;
    private final String value;

    TaskListDetailSyncResultEnum(Integer code, String value) {
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
