/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("打标检查列表查询 StyleMarkingCheckVo")
public class StyleMarkingCheckVo {


    @ApiModelProperty(value = "年份名称"  )
    private String yearName;
    @ApiModelProperty(value = "季节名称"  )
    private String seasonName;
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    @ApiModelProperty(value = "波段"  )
    private String bandCode;
    @ApiModelProperty(value = "波段"  )
    private String bandName;
    @ApiModelProperty(value = "大类编码"  )
    private String  prodCategory1st;
    @ApiModelProperty(value = "大类编码"  )
    private String  prodCategory1stName;
    @ApiModelProperty(value = "品类编码")
    private String  prodCategory;
    @ApiModelProperty(value = "品类")
    private String  prodCategoryName;
    @ApiModelProperty(value = "生产类型"  )
    private String devtType;
    @ApiModelProperty(value = "生产类型"  )
    private String devtTypeName;
    @ApiModelProperty(value = "销售类型"  )
    private String salesType;
    @ApiModelProperty(value = "销售类型名称"  )
    private String  salesTypeName;
    @ApiModelProperty(value = "设计阶段未打标款数")
    private Integer designCount;
    @ApiModelProperty(value = "设计阶段部分打标款数")
    private Integer designCount1;
    @ApiModelProperty(value = "设计阶段已打标款数")
    private Integer designCount2;
    @ApiModelProperty(value = "下单阶段未打标款数")
    private Integer orderCount;
    @ApiModelProperty(value = "下单阶段部分打标款数")
    private Integer orderCount1;
    @ApiModelProperty(value = "下单阶段已打标款数")
    private Integer orderCount2;
    @ApiModelProperty(value = "总数")
    private Integer totalCount;


}
