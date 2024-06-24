package com.base.sbc.module.customFile.dto;

import com.base.sbc.module.customFile.entity.FileTree;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileTreeDto extends FileTree {

    @ApiModelProperty("是否展示前几个：true展示")
    private Boolean showTopSeveral;

}
