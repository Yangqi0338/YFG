package com.base.sbc.module.storageSpace.utlis;

import java.util.Objects;

/**
 * @Create : 2024/7/10 10:43
 **/
public class SpaceSizeConvertUtils {

    public static String convertSizeStr(Long size){
        if (Objects.isNull(size)){
            return null;
        }
        if (size >= 1073741824){
            return Math.ceil((double) size / 1073741824) + "GB";
        }

        if (size >= 1048576){
            return Math.ceil((double) size / 1048576) + "MB";
        }
        return Math.ceil((double) size / 1024) + "KB";
    }






}
