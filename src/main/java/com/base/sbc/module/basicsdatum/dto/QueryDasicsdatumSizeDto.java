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
    private String internalSize;
    private String createName;
    private String[] createDate;

    /*1导出，0模板导出*/
    private String  isDerive;

}
