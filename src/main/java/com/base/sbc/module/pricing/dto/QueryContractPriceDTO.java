package com.base.sbc.module.pricing.dto;

import lombok.Data;

@Data
public class QueryContractPriceDTO {
    /**
     * 大货款号
     */
    private String styleNo;
    /**
     * 是否记录日志
     */
    private Boolean writeLog;
    /**
     * 资料包类型，保存日志时传
     */
    private String packType;
    private String foreignId;
}
