package com.base.sbc.module.smp.dto;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/7/21 9:58:59
 * @mail 247967116@qq.com
 */
@Data
public class FabricCompositionDto {
    private String id;
    private String name;
    /** 成分编号 */
    private String code;

    /** 面料成分（丙纶，亚麻，兔毛） */
    private String ingredient;
}
