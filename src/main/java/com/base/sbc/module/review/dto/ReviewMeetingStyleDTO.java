package com.base.sbc.module.review.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("评审会拆单")
public class ReviewMeetingStyleDTO {
    /** 款式Bom */
    private String plateBillCode;
    /** 款式Bom Id */
    private String plateBillId;
    /** 设计款号 */
    private String styleId;
    /** 设计款号 */
    private String styleNo;
    /** 图片地址 */
    private String pictureUrl;
}
