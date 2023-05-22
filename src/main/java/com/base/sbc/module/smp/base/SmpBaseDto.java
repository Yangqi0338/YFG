package com.base.sbc.module.smp.base;

import lombok.Data;

import java.util.Date;

/**
 * @author 卞康
 * @date 2023/5/10 11:28:57
 * @mail 247967116@qq.com
 */
@Data
public class SmpBaseDto {
    /**创建人*/
    private String creator;
    /**创建时间*/
    private Date createTime;
    /**修改人*/
    private String modifiedPerson;
    /**修改时间*/
    private Date modifiedTime;
    /**PLMID*/
    private String plmId;
    /**同步ID*/
    private String syncId;
    /**是否启用*/
    private Boolean active;
}
