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

    /*存在为修改*/
    @ApiModelProperty(name = "编号id", value = "编号:为空时新增、不为空时修改",  required = false, example = "")
    private String id;

    /** 年份 */
    @ApiModelProperty(value = "年份" ,  required = true )
    @NotBlank(message = "年份必填")
    private String year;

    private String yearName;

    /** 季节 */
    @ApiModelProperty(value = "季节"  ,  required = true )
    @NotBlank(message = "季节必填")
    private String season;

    private String seasonName;

    /** 品牌 */
    @ApiModelProperty(value = "品牌"  ,  required = true )
    @NotBlank(message = "品牌必填")
    private String brand;

    private String brandName;

    /** 供应商 */
    @ApiModelProperty(value = "供应商"  )
    @NotBlank(message = "供应商")
    private String supplierName;

    /** 供应商料号 */
    @ApiModelProperty(value = "供应商料号"  )
    @NotBlank(message = "供应商料号必填")
    private String supplierMaterialCode;

    /** 供应商色号 */
    @ApiModelProperty(value = "供应商色号"  )
    @NotBlank(message = "供应商色号必填")
    private String supplierColor;

    /** 是否新面料（0是 1否 */
    @ApiModelProperty(value = "是否新面料（0是 1否" ,  required = true  )
    @NotBlank(message = "是否新面料必填")
    private String isNewFabric;

    /** 数量（米） */
    @ApiModelProperty(value = "数量（米）" ,  required = true  )
    @NotNull(message = "数量必填")
    private Integer quantity;


}
