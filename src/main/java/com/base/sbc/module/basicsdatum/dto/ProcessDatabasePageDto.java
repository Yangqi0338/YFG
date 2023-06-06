package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/6/5 11:48:13
 * @mail 247967116@qq.com
 */
@Data
public class ProcessDatabasePageDto extends Page {
    private String code;
    private String type;
    private String processName;
    private String processType;
    private String description;
    private String createName;
    private String[] time;
    private String status;
}
