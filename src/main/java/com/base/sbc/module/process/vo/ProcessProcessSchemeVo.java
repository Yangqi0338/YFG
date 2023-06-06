/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：流程配置-流程方案 Vo类
 * @address com.base.sbc.module.process.vo.ProcessProcessScheme
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-2 20:15:15
 * @version 1.0
 */
@Data

@ApiModel("流程配置-流程方案 ProcessProcessScheme")
public class ProcessProcessSchemeVo  {

    private String id;
    /** 方案名称 */
    @ApiModelProperty(value = "方案名称"  )
    private String schemeName;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌id */
    private String brandId;
    /** 描述 */
    @ApiModelProperty(value = "描述"  )
    private String description;
    /** 创建者头像 */
    @ApiModelProperty(value = "创建者头像"  )
    private String createPicture;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

    private String createName;
}
