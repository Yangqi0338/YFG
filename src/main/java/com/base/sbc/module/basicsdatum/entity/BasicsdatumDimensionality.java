/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：基础资料-纬度系数表 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumDimensionality
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-15 14:34:41
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_dimensionality")
@ApiModel("基础资料-纬度系数表 BasicsdatumDimensionality")
public class BasicsdatumDimensionality extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 字段管理id */
    @ApiModelProperty(value = "字段管理id"  )
    private String fieldId;
    /** 纬度系数模板id */
    @ApiModelProperty(value = "纬度系数模板id"  )
    private String coefficientTemplateId;
    /** 渠道 */
    @ApiModelProperty(value = "渠道"  )
    private String channel;
    /** 渠道名称 */
    @ApiModelProperty(value = "渠道名称"  )
    private String channelName;
    /** 大类 */
    @ApiModelProperty(value = "大类"  )
    private String prodCategory1st;
    /** 大类名称 */
    @ApiModelProperty(value = "大类名称"  )
    private String prodCategory1stName;
    /** 品类 */
    @ApiModelProperty(value = "品类"  )
    private String prodCategory;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    private String prodCategoryName;
    /** 维度名称 */
    @ApiModelProperty(value = "维度名称"  )
    private String dimensionalityName;
    /** 显示标记 */
    @ApiModelProperty(value = "显示标记"  )
    private String showFlag;
    /** 是否检查 */
    @ApiModelProperty(value = "是否检查"  )
    private String isExamine;
    /** 排序 */
    @ApiModelProperty(value = "排序"  )
    private Integer sort;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
