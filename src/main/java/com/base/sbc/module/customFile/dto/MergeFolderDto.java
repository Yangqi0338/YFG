package com.base.sbc.module.customFile.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Create  : 2024/6/24 14:04
 **/
@Data
public class MergeFolderDto {

    @ApiModelProperty("需要合并的文件夹id列表")
    private List<String> mergeFolderIds;

    @ApiModelProperty("合并文件夹的名称")
    private String name;

}
