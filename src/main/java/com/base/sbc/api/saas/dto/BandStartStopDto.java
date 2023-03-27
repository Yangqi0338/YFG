package com.base.sbc.api.saas.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("波段启动停止 BandStartStopDto")
public class BandStartStopDto {

    @ApiModelProperty(name = "编号", value = "编号:多个数组",  required = true, dataType = "String[]", example = "[111,22]")
    @NotNull(message = "编号必填")
    private String[] ids;

    @ApiModelProperty(value = "状态",  required = true, example = "0")
    @NotBlank(message = "状态必填")
    private String status;
}
