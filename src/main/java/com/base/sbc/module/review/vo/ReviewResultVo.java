package com.base.sbc.module.review.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;

@Data
public class ReviewResultVo extends BaseDataEntity<String> {
    /** 禁用 0 启用 1 */
    private String status;
    /** 会议类型id */
    private String meetingType;
    /** 会议类型名称 */
    private String meetingTypeName;
    /** 通过 */
    private String passThrough;
    /** 通过评审结果 */
    private String passResult;
    /** 不通过 */
    private String noPass;
    /** 不通过评审结果 */
    private String noPassResult;
    /** 备注 */
    private String remarks;

}
