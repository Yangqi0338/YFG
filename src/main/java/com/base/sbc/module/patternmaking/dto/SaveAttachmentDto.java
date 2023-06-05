package com.base.sbc.module.patternmaking.dto;

import com.base.sbc.module.common.dto.AttachmentSaveDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 类描述：保存附件dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.SaveAttachmentDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 15:48
 */
@Data
@ApiModel("保存附件Dto  SaveAttachmentDto")
public class SaveAttachmentDto {
    @ApiModelProperty(value = "打版id", required = true)
    @NotBlank(message = "打版id不能为空")
    private String id;

    @ApiModelProperty(value = "附件信息")
    List<AttachmentSaveDto> attachmentList;
}
