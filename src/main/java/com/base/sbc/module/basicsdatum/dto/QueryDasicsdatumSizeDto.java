package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 尺码查询dto类
 */
@Data
public class QueryDasicsdatumSizeDto  extends Page {
    /*前端传入查询*/
    private String  search;

    @ApiModelProperty(value = "尺码标签Id"  )
    private String sizeLabelId;

    @ApiModelProperty(value = "尺码标签"  )
    private String labelName;
    private String hangtags;
    private String model;
    private String modelType;
    private String internalSize;
    private String createName;
    private String[] createDate;

    private String all;
    /**
     * 号型类型扩展
     */
    private String modelTypeExt;

    @ApiModelProperty(value = "号型类型编码"  )
    private String   modelTypeCode;

    /*1导出，0模板导出*/
    private String  isDerive;

}
