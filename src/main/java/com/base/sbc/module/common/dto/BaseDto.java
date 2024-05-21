package com.base.sbc.module.common.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/6/26 10:21
 * @mail 247967116@qq.com
 */
@Data
public class BaseDto extends Page {

    @ApiModelProperty(value = "id数组")
    private String ids;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "创建人")
    private String createName;

    @ApiModelProperty(value = "创建时间")
    private String[] createDate;

    @ApiModelProperty(value = "更新时间")
    private String[] updateDate;

}
