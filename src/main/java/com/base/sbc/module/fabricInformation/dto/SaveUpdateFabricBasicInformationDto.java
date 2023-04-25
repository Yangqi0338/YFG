package com.base.sbc.module.fabricInformation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


@Data
@ApiModel("保存修改面料基本信息 SaveUpdateFabricBasicDto")
public class SaveUpdateFabricBasicInformationDto {

    /*存在为修改*/
    @ApiModelProperty(name = "编号id", value = "编号:为空时新增、不为空时修改",  required = false, example = "")
    private String id;

    /** 年份 */
    @ApiModelProperty(value = "年份" ,  required = true )
    @NotBlank(message = "年份必填")
    private String year;

    /** 季节 */
    @ApiModelProperty(value = "季节"  ,  required = true )
    @NotBlank(message = "季节必填")
    private String season;

    /** 品牌 */
    @ApiModelProperty(value = "品牌"  ,  required = true )
    @NotBlank(message = "品牌必填")
    private String brand;

    /** 厂家 */
    @ApiModelProperty(value = "厂家" ,  required = true  )
    @NotBlank(message = "厂家必填")
    private String manufacturer;

    /** 厂家编号 */
    @ApiModelProperty(value = "厂家编号" ,  required = true  )
    @NotBlank(message = "厂家编号必填")
    private String manufacturerNumber;

    /** 厂家色号 */
    @ApiModelProperty(value = "厂家色号" ,  required = true   )
    @NotBlank(message = "厂家色号必填")
    private String manufacturerColour;

    /** 是否新面料（0是 1否 */
    @ApiModelProperty(value = "是否新面料（0是 1否" ,  required = true  )
    @NotBlank(message = "是否新面料必填")
    private String isNewFabric;

    /** 数量（米） */
    @ApiModelProperty(value = "数量（米）" ,  required = true  )
    @NotNull(message = "数量必填")
    private Integer quantity;


}
