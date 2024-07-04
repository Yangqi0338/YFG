package com.base.sbc.module.material.vo;

import lombok.Data;

/**
 * @Create : 2024/7/4 09:53
 **/
@Data
public class MaterialSpaceInfoVo {

    private Long totalSpaceSize;

    private Long usageSpaceSize;

    private String totalSpace;

    private String usageSpace;

    public String getTotalSpace() {
        return getFileSize(totalSpaceSize);
    }

    public String getUsageSpace() {
        return getFileSize(usageSpaceSize);
    }

    private String getFileSize(Long fileSize) {
        if (null == fileSize){
            return null;
        }
        if (fileSize > 1073741824){
            return Math.ceil((double) fileSize / 1073741824) + "GB";
        }

        if (fileSize > 1048576){
            return Math.ceil((double) fileSize / 1048576) + "MB";
        }
        return Math.ceil((double) fileSize / 1024) + "KB";
    }

}
