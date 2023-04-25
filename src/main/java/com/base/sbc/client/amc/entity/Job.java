package com.base.sbc.client.amc.entity;

import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;

@Data
public class Job extends BaseDataEntity<String> {
    /** 职位名称 */
    private String name;
    /** 职位介绍 */
    private String tips;
    /** 状态正常(0),停用(1) */
    private String status;
    /** 默认(0表示是企业默认职位角色)(1表示不是) */
    private String isDefault;
    /** 应用ID */
    private String applyId;
}
