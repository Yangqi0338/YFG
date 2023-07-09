package com.base.sbc.module.smp.dto;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/7/9 17:21:45
 * @mail 247967116@qq.com
 */
@Data
public class HttpResp {
    private String statusCode;
    private String code;
    private boolean success;
    private String message;
    private String url;
}
