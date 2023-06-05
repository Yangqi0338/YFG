package com.base.sbc.module.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：附件保存dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.common.dto.AttachmentSaveDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 15:40
 */
@Data
@ApiModel("附件保存Dto AttachmentSaveDto ")
public class AttachmentSaveDto {

    @ApiModelProperty(value = "文件id")
    @NotBlank(message = "文件id不能为空")
    private String fileId;
}
