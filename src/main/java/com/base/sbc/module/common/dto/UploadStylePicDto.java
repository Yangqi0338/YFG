package com.base.sbc.module.common.dto;

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
    @NotBlank(message = "款式配色id不能为空")
    private String styleColorId;


}
