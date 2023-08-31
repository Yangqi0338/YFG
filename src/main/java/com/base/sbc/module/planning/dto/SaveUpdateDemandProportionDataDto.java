package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/*新增修改需求数据*/
@Data
@ApiModel("企划需求-新增修改需求数据 SaveUpdateDemandProportionDataDto")
public class SaveUpdateDemandProportionDataDto {
    private String id;

    @ApiModelProperty(value = "需求维度id" ,required = true,example = "122222")
    @NotBlank(message = "需求维度id不能为空")
    private String demandId;

    @ApiModelProperty(value = "分类" ,required = true,example = "122222")
    @NotBlank(message = "分类不能为空")
    private String   classify;

    /** 渠道 */
    @ApiModelProperty(value = "渠道"  )
    private String channel;
    /** 渠道名称 */
    @ApiModelProperty(value = "渠道名称"  )
    private String channelName;
    /** 波段 */
    @ApiModelProperty(value = "波段"  )
    private String bandCode;
    /** 波段名称 */
    @ApiModelProperty(value = "波段名称"  )
    private String bandName;

    /** 数量 */
    @ApiModelProperty(value = "数量"  )
    private Integer num;

    @ApiModelProperty(value = "分类名称" ,required = true,example = "122222")
    private String   classifyName;

    @ApiModelProperty(value = "占比" ,required = true,example = "122222")
    @NotBlank(message = "占比不能为空")
    private String   proportion;
}
