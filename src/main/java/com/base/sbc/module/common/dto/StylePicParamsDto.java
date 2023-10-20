package com.base.sbc.module.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Locale;

/**
 * 类描述：yfg 上传图片 dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.common.dto.StylePicUploadDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-10-20 14:25
 */
@Data
public class StylePicParamsDto {

    private Object img;
    private String picname;
    private String pictype;
    private String folderName;
    private String brand;
    private String year;
    @ApiModelProperty(value = "季节")
    private String quarter;
    private String debug;
    private String useraccount;
    private String model;
    private String key;
    private String md5;

    public void setPicTypeByFileName(String fileName) {
        if (fileName.toLowerCase(Locale.ROOT).endsWith(".jpg")) {
            pictype = "0";
        } else if (fileName.toLowerCase(Locale.ROOT).endsWith(".png")) {
            pictype = "1";
        }
    }

}
