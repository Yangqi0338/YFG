/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.entity;

import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.module.difference.entity.Difference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ListRangeDifferenceVO {

      /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
    /** 档差 */
    @ApiModelProperty(value = "档差"  )
    private String rangeDifference;
    /** 测量点 */
    @ApiModelProperty(value = "测量点"  )
    private String measurement;
    /** 号型类型 */
    @ApiModelProperty(value = "号型类型"  )
    private String modelType;
    /** 号型类型编码 */
    @ApiModelProperty(value = "号型类型编码"  )
    private String modelTypeCode;
    /** 品牌编码 */
    @ApiModelProperty(value = "品牌编码"  )
    private String brandCode;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /** 品类测量组编码 */
    @ApiModelProperty(value = "品类测量组编码"  )
    private String categoryMeasureCode;
    /** 尺码 */
    @ApiModelProperty(value = "尺码"  )
    private String size;
    /** 测量数据集合 */
    @ApiModelProperty(value = "测量数据集合"  )
    private String positionData;
    /** 测量部位id集合 */
    @ApiModelProperty(value = "测量部位id集合"  )
    private String measurementIds;
    /**  品类id */
    @ApiModelProperty(value = "品类id"  )
    private String categoryId;
    /**  品类名 */
    @ApiModelProperty(value = "品类名"  )
    private String categoryName;
    /** 基础尺码 */
    @ApiModelProperty(value = "基础尺码"  )
    private String basicsSize;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String picture;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

    private String  remarks;

    @ApiModelProperty(value = "公差，档差"  )
    private  List<Difference> differenceList;

}
