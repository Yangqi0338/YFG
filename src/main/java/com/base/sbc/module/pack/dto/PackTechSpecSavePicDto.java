package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：资料包-工艺说明-图片信息保存dto
 *
 * @author lxl
 * @version 1.0
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 15:41:45
 */
@Data
@ApiModel("资料包-工艺说明-图片信息保存dto PackTechSpecSavePicDto")
public class PackTechSpecSavePicDto {
    @ApiModelProperty(value = "外键id")
    @NotBlank(message = "主数据id为空")
    private String foreignId;

    @ApiModelProperty(value = "资料包类型")
    @NotBlank(message = "资料包类型为空")
    private String packType;

    @ApiModelProperty(value = "工艺类型:裁剪工艺、基础工艺、小部件、注意事项、整烫包装")
    @NotBlank(message = "工艺类型不能为空")
    private String specType;

    @ApiModelProperty(value = "文件id")
    @NotBlank(message = "文件id为空")
    private String fileId;

}
