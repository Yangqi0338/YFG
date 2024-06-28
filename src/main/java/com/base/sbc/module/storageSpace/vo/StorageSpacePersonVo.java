package com.base.sbc.module.storageSpace.vo;

import com.base.sbc.module.storageSpace.entity.StorageSpacePerson;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Create : 2024/6/28 15:16
 **/
@Data
public class StorageSpacePersonVo extends StorageSpacePerson {

    @ApiModelProperty("已用的空间大小")
    private String usedSpace;

}
