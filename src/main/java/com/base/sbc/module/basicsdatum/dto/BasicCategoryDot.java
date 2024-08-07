package com.base.sbc.module.basicsdatum.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class BasicCategoryDot {

    private String id;

    private String name;

    private String value;
    /** 第几级树(0为第一级) */
    private Integer level;

    private List<BasicCategoryDot> children;

    public BasicCategoryDot findByValue(String value) {
        if (StrUtil.equals(this.value, value)) return this;
        if (CollUtil.isEmpty(this.children)) return null;
        return this.children.stream().map(it -> it.findByValue(value)).filter(Objects::nonNull).findFirst().orElse(null);
    }
}
