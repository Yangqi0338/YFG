package com.base.sbc.module.sample.dto;

import com.base.sbc.module.sample.entity.FabricBasicInformation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


@Data
@ApiModel("保存修改面料基本信息 SaveUpdateFabricBasicDto")
public class SaveUpdateFabricBasicInformationDto extends FabricBasicInformation {



}
