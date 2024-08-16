package com.base.sbc.module.httplog.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/*系统日志查询dto类*/
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryHttpLogDto extends Page {
    private  String  threadId;
    private String startTime;
    private String url;
    /** 文档名称 */
    private String reqName;

    private String userName;

    /** 是否异常(0正常1异常) */
    private String exceptionFlag;
}
