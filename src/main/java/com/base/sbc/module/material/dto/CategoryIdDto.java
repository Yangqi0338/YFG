package com.base.sbc.module.material.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/4/14 11:25:33
 */
@Data
public class CategoryIdDto {
    private String id;
    private List<String> categoryIds;
}
