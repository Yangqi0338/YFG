package com.base.sbc.module.storageSpace.vo;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Create : 2024/7/1 17:13
 **/
@Data
public class StorageSpacePersonBo  extends PageInfo<StorageSpacePersonVo>{

    @ApiModelProperty("分配空间")
    private Long allocationSpace;

}
