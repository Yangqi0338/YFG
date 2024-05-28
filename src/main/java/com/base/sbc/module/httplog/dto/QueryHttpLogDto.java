package com.base.sbc.module.httplog.dto;

import lombok.Data;

/*系统日志查询dto类*/
@Data
public class QueryHttpLogDto {
    private  String  threadId;
    private String startTime;
    private String url;
    /** 文档名称 */
    private String reqName;

    private String userName;
}
