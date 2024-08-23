package com.base.sbc.module.storageSpace.dto;

import com.base.sbc.config.common.base.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Create : 2024/6/27 15:26
 **/
@Data
public class StorageSpacePersonDto extends Page {

    @ApiModelProperty("存储类型：1:素材库相关")
    private String storageType;

    @ApiModelProperty(value = "父空间id"  )
    private String parentSpaceId;


    /** 初始划分空间大小(单位：GB) */
    @ApiModelProperty(value = "初始划分空间大小(单位：GB)"  )
    private String initSpace;
    /** 拥有的空间大小(单位：GB) */
    @ApiModelProperty(value = "拥有的空间大小(单位：GB)"  )
    private String ownerSpace;
    /** 倍率 */
    @ApiModelProperty(value = "倍率"  )
    private String magnification;

    /** 空间所属人工号 */
    @ApiModelProperty(value = "空间所属人工号"  )
    private String userName;
    /** 空间所属人姓名 */
    @ApiModelProperty(value = "空间所属人姓名"  )
    private String ownerName;
}
