package com.base.sbc.module.storageSpace.vo;

import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.storageSpace.entity.StorageSpacePerson;
import com.base.sbc.module.storageSpace.utlis.SpaceSizeConvertUtils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Create : 2024/6/28 15:16
 **/
@Data
public class StorageSpacePersonVo extends StorageSpacePerson {

    @ApiModelProperty("已用的空间大小")
    private String usedSpace;

    @ApiModelProperty("已用的空间大小Str")
    private String usedSpaceStr;


    public String getUsedSpaceStr() {
        if (null == usedSpace){
            return "0";
        }
        return SpaceSizeConvertUtils.convertSizeStr(Long.valueOf(usedSpace));
    }


}
