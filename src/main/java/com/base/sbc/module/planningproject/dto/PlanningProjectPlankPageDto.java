package com.base.sbc.module.planningproject.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/11/17 17:05:20
 * @mail 247967116@qq.com
 */
@Data
public class PlanningProjectPlankPageDto {
    private String planningProjectId;
    private String planningBandCode;
    private String planningBulkStyleNo;
    /**
     * 产品季id
     */
    private String seasonId;
    /**
     * 渠道编码
     */
    private String planningChannelCode;
    /** 大类编码 */
    @ApiModelProperty(value = "大类编码"  )
    private String prodCategory1stCode;
    private String prodCategory1stName;
    /** 品类 */
    @ApiModelProperty(value = "品类"  )
    private String prodCategoryCode;
    private String prodCategoryName;
    /** 中类code */
    @ApiModelProperty(value = "中类code"  )
    private String prodCategory2ndCode;
    private String prodCategory2ndName;

}
