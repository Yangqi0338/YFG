package com.base.sbc.client.amc;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("团队 TeamVo")
public class TeamVo {
    @ApiModelProperty(value = "编号" ,example = "689467740238381056")
    private String id;
    @ApiModelProperty(value = "名称" ,example = "团队1")
    private String name;
}
