package com.base.sbc.module.common.dto;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Data
public class UploadStylePicDto {


    @ApiModelProperty(value = "文件")
    private MultipartFile file;

    @ApiModelProperty(value = "款式配色id")
    private String styleColorId;

    @ApiModelProperty(value = "款式id")
    private String styleId;

    @ApiModelProperty(value = "替换主图")
    private String replaceFlag;

}
