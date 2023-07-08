/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：导入导出基础资料-材料成分 dto类
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialsIngredient
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00
 * @version 1.0
 */
@Data
@ApiModel("基础资料-材料成分 BasicsdatumMaterialsIngredient")
public class BasicsdatumMaterialsIngredientExcelDto  {
    // @Excel(name = "id")
    private String id;
    @ApiModelProperty(value = "编码"  )
    @Excel(name = "编码")
    private String code;
    /** 成分 */
    @ApiModelProperty(value = "成分"  )
    @Excel(name = "成分")
    private String ingredient;
    /** OK for Material */
    @ApiModelProperty(value = "OK for Material"  )
    @Excel(name = "OK for Material" , replace = {"false_0", "true_1"} )
    private String material;
    /** OK for Style */
    @ApiModelProperty(value = "OK for Style"  )
    @Excel(name = "OK for Style" , replace = {"false_0", "true_1"} )
    private String style;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    @Excel(name = "状态" , replace = {"true_0", "false_1"} )
    private String status;
}
