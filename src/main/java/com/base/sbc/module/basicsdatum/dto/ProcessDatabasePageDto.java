package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.enums.business.ProcessDatabaseType;
import com.base.sbc.module.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/6/5 11:48:13
 * @mail 247967116@qq.com
 */
@Data
public class ProcessDatabasePageDto extends BaseDto {
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "类型")
    private ProcessDatabaseType type;
    @ApiModelProperty(value = "类型")
    private String processName;
    private String processNameList;
    private String processType;
    private String description;
    private String createName;
    private String[] time;
    private String status;
    @ApiModelProperty(value = "部件编码")
    private String component;

    @ApiModelProperty(value = "品类编码")
    private String categoryCode;

    @ApiModelProperty(value = "品类名称")
    private String  categoryName;

    private String search;


    private String categoryId;

    @ApiModelProperty(value = "工艺要求")
    private String  processRequire;

}
