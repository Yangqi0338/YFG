package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：资料包-尺寸表dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackSizeVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 10:33
 */
@Data
@ApiModel("资料包-图样附件保存Dto PackPatternAttachmentSaveDto")
public class PackPatternAttachmentSaveDto {

    @ApiModelProperty(value = "文件id")
    @NotBlank(message = "文件id不能为空")
    private String fileId;

    @ApiModelProperty(value = "主数据id")
    @NotBlank(message = "主数据id为空")
    private String foreignId;

    @ApiModelProperty(value = "资料包类型")
    @NotBlank(message = "资料包类型不能为空")
    private String packType;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
