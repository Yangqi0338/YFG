package com.base.sbc.module.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;
/**
 * @author 卞康
 * @date 2023/5/16 18:49:25
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_http_log")
public class HttpLog extends BaseDataEntity<String> {
    /**
     * 请求类型 (1: 发出的请求, 2: 收到的请求)
     */
    private Integer type;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private String requestParameters;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 请求头
     */
    private String requestHeaders;

    /**
     * 响应状态码
     */
    private Integer responseStatus;

    /**
     * 响应体
     */
    private String responseBody;

    /**
     * 响应头
     */
    private String responseHeaders;

    /**
     * 备注
     */
    private String remarks;
}
