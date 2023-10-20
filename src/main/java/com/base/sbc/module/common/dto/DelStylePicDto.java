package com.base.sbc.module.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DelStylePicDto {

    @ApiModelProperty(value = "款式配色id")
    private String styleColorId;

    @ApiModelProperty(value = "款式id")
    private String styleId;

    @ApiModelProperty(value = "款式图片id")
    private String stylePicId;


}
