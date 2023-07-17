package com.base.sbc.module.pack.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

/**
 * 核价管理选择制版单列表查询
 */
@Data
public class PricingSelectSearchDTO extends Page {

    /**
     * 设计款号
     */
    private String designNo;
    /**
     * 大货款号
     */
    private String styleNo;
}
