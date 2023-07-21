/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：基础资料-部件 vo类
 * @address com.base.sbc.module.basicsdatum.vo.BasicsdatumComponent
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-17 11:37:55
 * @version 1.0
 */
@Data

public class BasicsdatumMeasurementVo extends BaseDataEntity<String>   {


    private String id;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
    @ApiModelProperty(value = "测量点"  )
    private String measurement;
    /** POM类型 */
    @ApiModelProperty(value = "POM类型"  )
    private String pdmType;
    /** POM类型名称 */
    @ApiModelProperty(value = "POM类型名称"  )
    private String pdmTypeName;
    /** 描述 */
    @ApiModelProperty(value = "描述"  )
    private String description;
    /** 描述(Alt) */
    @ApiModelProperty(value = "描述(Alt)"  )
    private String descriptionAlt;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String image;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;



}
