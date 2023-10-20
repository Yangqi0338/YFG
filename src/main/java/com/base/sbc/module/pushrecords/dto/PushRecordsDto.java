package com.base.sbc.module.pushrecords.dto;

import com.base.sbc.module.common.dto.BaseDto;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/7/11 10:52:26
 * @mail 247967116@qq.com
 */
@Data
public class PushRecordsDto extends BaseDto {
    private String moduleName;

    private String functionName;

    private String relatedId;

    private String relatedName;

}
