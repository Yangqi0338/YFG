package com.base.sbc.client.ccm.entity;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

@Data
public class BasicDictDependsQueryDto extends Page {
    /** 字典类型 */
    private String dictType;
    /** 字典类型名称 */
    private String dictTypeName;
    /** 字典名称 */
    private String dictName;
    /** 字典编码 */
    private String dictCode;
    /** 依赖状态0字典，1结构管理 */
    private String dependStatus;
    /** 依赖字典类型 */
    private String dependDictType;
    /** 依赖字典类型名称 */
    private String dependDictTypeName;
    /** 依赖名称 */
    private String dependName;
    /** 依赖编码 */
    private String dependCode;

}
