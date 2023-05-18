package com.base.sbc.module.basicsdatum.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BasicsdatumSizeLabelVo extends BaseDataEntity<String> {

    private String id;

    /** 标签名称 */
    @ApiModelProperty(value = "标签名称"  )
    private String labelName;
}
