package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackTechSpecBatchSaveDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-16 11:09
 */

@Data
@ApiModel("资料包-工艺说明批量保存DTO PackTechSpecBatchSaveDto")
public class PackTechSpecBatchSaveDto {

    @ApiModelProperty(value = "主数据id")
    @NotBlank(message = "主数据id为空")
    private String foreignId;


    @ApiModelProperty(value = "资料包类型")
    @NotBlank(message = "资料包类型为空")
    private String packType;

    @ApiModelProperty(value = "工艺类型:裁剪工艺、基础工艺、小部件、注意事项、整烫包装")
    @NotBlank(message = "工艺类型不能为空")
    private String specType;

    @ApiModelProperty(value = "工艺信息")
    @NotNull(message = "数据为空")
    List<PackTechSpecDto> list;

    @ApiModelProperty(value = "覆盖标志")
    private String overlayFlg;

    private String processTypeName;
}
