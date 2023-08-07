/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 类描述：新增修改基础资料-材料成分 dto类
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialsIngredient
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00
 * @version 1.0
 */
@Data
@ApiModel("基础资料-材料成分 AddRevampBasicsdatumIngredientDto")
public class AddRevampBasicsdatumIngredientDto {

    private String id;
    @ApiModelProperty(value = "编码"  )
    private String code;
    /** 成分 */
    @ApiModelProperty(value = "成分"  )
    private String ingredient;
    /** OK for Material */
    @ApiModelProperty(value = "OK for Material"  )
    private String material;
    /** OK for Style */
    @ApiModelProperty(value = "OK for Style"  )
    private String style;
    /** SCM下发状态:0未发送,1发送成功，2发送失败,3可编辑 */
    @ApiModelProperty(value = "SCM下发状态:0未发送,1发送成功，2发送失败,3可编辑"  )
    private String scmSendFlag;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
}
