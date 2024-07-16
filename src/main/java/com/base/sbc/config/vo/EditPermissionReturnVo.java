package com.base.sbc.config.vo;

import lombok.Data;

@Data
public class EditPermissionReturnVo {

    /**
     * 是否可编辑,0：可编辑,1：不可编辑
     */
    private Integer isEdit = 0;

}
