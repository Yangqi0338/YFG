package com.base.sbc.module.basicsdatum.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
/*bom模板返回前端类*/
public class BasicsdatumBomTemplateVo {
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
}
