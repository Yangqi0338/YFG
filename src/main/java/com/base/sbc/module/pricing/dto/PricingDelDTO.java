package com.base.sbc.module.pricing.dto;

import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author xhj
 * @Date 2023/6/17 14:50
 */
@Data
public class PricingDelDTO extends BaseDataEntity<String> {

    private List<String> ids;

    /**
     * 业务单号
     */
    private String bizCode;

    private String bizId;
}
