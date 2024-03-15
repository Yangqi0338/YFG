package com.base.sbc.module.planning.dto;

import com.base.sbc.module.common.dto.BaseDto;
import lombok.Data;

/**
 * @author 卞康
 * @date 2024-01-25 10:16:57
 * @mail 247967116@qq.com
 */
@Data
public class PlanningSummaryQueryDto extends BaseDto {
    /**
     * 产品季id
     */
    String planningSeasonId;
    /**
     * 品类编码
     */
    String categoryCode;
    /**
     * 创建人
     */
    String createId;
}
