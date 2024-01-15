package com.base.sbc.open.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.access.method.P;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@EqualsAndHashCode(callSuper = true)
@Data
public class MoreLanguageTagPrinting extends TagPrintingSupportVO {
    /**
     * 大货款号标题
     */
    private String languageName;

    /**
     * 表头map
     */
    private Map<String, String> titleMap = new HashMap<>();

}
