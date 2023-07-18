/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 类描述：基础资料-档差 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumRangeDifference
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-18 19:42:16
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_range_difference")
@ApiModel("基础资料-档差 BasicsdatumRangeDifference")
public class BasicsdatumRangeDifference extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
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
    /** 品类测量组编码 */
    @ApiModelProperty(value = "品类测量组编码"  )
    private String categoryMeasureCode;
    /**  备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** 测量数据集合 */
    @ApiModelProperty(value = "测量数据集合"  )
    private String positionData;
    /** 测量部位id集合 */
    @ApiModelProperty(value = "测量部位id集合"  )
    private String measurementIds;
    /** 尺码 */
    @ApiModelProperty(value = "尺码")
    private String size;
    /** 基础尺码 */
    @ApiModelProperty(value = "基础尺码"  )
    private String basicsSize;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String picture;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

    @TableField(exist = false)
    private List<BasicsdatumCompanyRelation> basicsdatumCompanyRelation;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
