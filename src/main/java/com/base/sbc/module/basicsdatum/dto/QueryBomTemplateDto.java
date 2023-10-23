package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryBomTemplateDto extends Page {

    /** bova */
    @ApiModelProperty(value = "bom模板id"  )
    private String bomTemplateId;

    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
    /** 模板名称 */
    @ApiModelProperty(value = "模板名称"  )
    private String name;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /** 唛类信息 */
    @ApiModelProperty(value = "唛类信息"  )
    private String apparelLabels;
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;

    @ApiModelProperty(value = "厂家成分"  )
    private String  supplierFactoryIngredient;

    @ApiModelProperty(value = "材料"  )
    private String    materialCodeName;

    @ApiModelProperty(value = "成分"  )
    private String  ingredient;

    @ApiModelProperty(value = "bom类型"  )
    private String bomStatus;

}
