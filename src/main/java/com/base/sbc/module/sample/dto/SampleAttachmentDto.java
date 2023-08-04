package com.base.sbc.module.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：样衣附件
 * @address com.base.sbc.module.sample.dto.SampleAttachmentDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-12 16:30
 * @version 1.0
 */


@Data
@ApiModel("款式设计附件保存修改 SampleDto")
public class SampleAttachmentDto {

    @ApiModelProperty(value = "附件id"  )
    private String id;
    @ApiModelProperty(value = "文件id"  )
    private String fileId;
}
