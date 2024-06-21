package com.base.sbc.module.smp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 卞康
 * @date 2023/7/9 17:21:45
 * @mail 247967116@qq.com
 */
@Accessors(chain = true)
@Data
public class HttpReq {
    private String userId;
    private String url;
    private String code;
    private String name;
    private String moduleName;
    private String functionName;
    private String data;

    private String businessId;

    private String businessCode;

    public HttpReq(String url) {
        this.url = url;
    }
}
