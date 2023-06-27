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
 * 类描述：新增修改基础资料-颜色组 dto类
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumColourGroup
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 20:23:03
 * @version 1.0
 */
@Data
@ApiModel("基础资料-颜色组 BasicsdatumColourGroup")
public class AddRevampBasicsdatumColourGroupDto  {

    private String id;

    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String colourName;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
}
