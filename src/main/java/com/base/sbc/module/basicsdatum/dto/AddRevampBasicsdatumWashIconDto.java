/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：新增修改基础资料-洗涤图标 dto类
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumWashIcon
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-7-27 17:27:54
 * @version 1.0
 */
@Data
@ApiModel("基础资料-洗涤图标 BasicsdatumWashIcon")
public class AddRevampBasicsdatumWashIconDto  {

    private String id;

    /** 名称 */
    @ApiModelProperty(value = "名称"  )
    private String name;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
    /** 图片地址 */
    @ApiModelProperty(value = "图片地址"  )
    private String url;
    /** 描述 */
    @ApiModelProperty(value = "描述"  )
    private String remarks;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
}
