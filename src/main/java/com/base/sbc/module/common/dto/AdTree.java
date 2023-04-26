package com.base.sbc.module.common.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/4/25 10:50:50
 * @mail 247967116@qq.com
 */
@Data
public class AdTree {
    private String id;
    private String title;
    private String key;
    private Boolean disabled=false;
    private List<AdTree> children;
}
