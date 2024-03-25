package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

/**
 * @author 卞康
 * @date 2024-03-11 11:12:58
 * @mail 247967116@qq.com
 */
@Data
public class BasicProcessGalleryDto extends Page {
    private String id;
    private String code;
    private String name;
    private String typeCode;
    private String typeName;
    private String status;
    private String createDate;
}
