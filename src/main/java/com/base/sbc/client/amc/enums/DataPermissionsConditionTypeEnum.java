package com.base.sbc.client.amc.enums;

import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DataPermissionsConditionTypeEnum {
    /**/
    IN(" in ", "包含") {
        @Override
        public String handleSql(String k, String fieldKey, List<String> fieldValue) {
            StrJoiner joiner = StrJoiner.of("','", "('", "')").setNullMode(StrJoiner.NullMode.IGNORE).setEmptyResult("()");
            return fieldKey + k + joiner.append(fieldValue).toString();
        }
    },
    EQ(" = ", "等于") {
        @Override
        public String handleSql(String fieldKey, List<String> fieldValue) {
            return IN.handleSql(fieldKey, fieldValue);
        }
    },
    NOT_IN(" not in ", "不包含") {
        @Override
        public String handleSql(String fieldKey, List<String> fieldValue) {
            return IN.handleSql(this.getK(), fieldKey, fieldValue);
        }
    },
    NE(" != ", "不等于") {
        @Override
        public String handleSql(String fieldKey, List<String> fieldValue) {
            return NOT_IN.handleSql(fieldKey, fieldValue);
        }
    },
    LIKE(" like ", "模糊") {
        @Override
        String handleSql(String k, String fieldKey, List<String> fieldValue) {
            String format = StrJoiner.of(k, "(", ")").append(fieldKey).append("%{}%").toString();
            StrJoiner joiner = StrJoiner.of(" OR ", "(", ")").setNullMode(StrJoiner.NullMode.IGNORE)
                    .setEmptyResult(StrUtil.format(format, "()"));

            return joiner.append(fieldValue.stream().map(it -> StrUtil.format(format, it)).collect(Collectors.toList())).toString();
        }
    },
    NOT_LIKE(" not like ", "模糊不含") {
        @Override
        public String handleSql(String fieldKey, List<String> fieldValue) {
            return LIKE.handleSql(this.getK(), fieldKey, fieldValue);
        }
    },
    FIND_IN_SET(" find_in_set ", "单字符包含") {
        @Override
        public String handleSql(String k, String fieldKey, List<String> fieldValue) {
            String format = "('{}',{})";
            StrJoiner joiner = StrJoiner.of(" OR ", "(", ")").setNullMode(StrJoiner.NullMode.IGNORE)
                    .setEmptyResult(StrUtil.format(format, "()", fieldKey));
            return joiner.append(fieldValue.stream().map(it -> k + StrUtil.format(format, it, fieldKey)).collect(Collectors.toList())).toString();
        }
    },
    ;
    private final String K;
    private final String V;

    DataPermissionsConditionTypeEnum(String k, String v) {
        K = k;
        V = v;
    }

    public String getK() {
        return K;
    }

    public String getV() {
        return V;
    }

    public static DataPermissionsConditionTypeEnum findByK(String K) {
        return Arrays.stream(DataPermissionsConditionTypeEnum.values()).filter(it -> it.K.trim().equals(K)).findFirst().orElse(null);
    }

    public String handleSql(String fieldKey, List<String> fieldValue) {
        return handleSql(this.getK(), fieldKey, fieldValue);
    }

    String handleSql(String k, String fieldKey, List<String> fieldValue) {
        return "";
    }
}
