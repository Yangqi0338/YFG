package com.base.sbc.module.basicsdatum.dto;

import lombok.Data;

@Data
public class BasicCategoryDot {

    private String id;

    private String name;

    private String value;
    /** 第几级树(0为第一级) */
    private Integer level;
}
