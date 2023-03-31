package com.base.sbc.module.goodscolor.entity;


import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 启用、停用状态
 */
@SuppressWarnings({"ALL", "AlibabaPojoMustOverrideToString"})
public class UsingStatusVO implements Serializable {

    @NotBlank(message = "ids不能为空")
    private String ids;

    @NotBlank(message = "状态不能为空")
    private String status;


    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
