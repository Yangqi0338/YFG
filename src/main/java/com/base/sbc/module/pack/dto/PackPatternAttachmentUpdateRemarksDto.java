package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：资料包-图样附件修改备注Dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackSizeVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 10:33
 */
@Data
@ApiModel("资料包-图样附件修改备注Dto PackPatternAttachmentUpdateRemarksDto")
public class PackPatternAttachmentUpdateRemarksDto {

    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;


    @ApiModelProperty(value = "备注")
    private String remarks;
}
