package com.base.sbc.module.httpLog.dto;

import lombok.Data;

import java.util.Date;

/*系统日志查询dto类*/
@Data
public class QueryHttpLogDto {
    private  String  threadId;
    private String startTime;
}
