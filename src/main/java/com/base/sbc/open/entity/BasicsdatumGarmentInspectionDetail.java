/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述： 实体类
 * @address com.base.sbc.open.entity.BasicsdatumGarmentInspectionDetail
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-17 15:14:21
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_garment_inspection_detail")
@ApiModel(" BasicsdatumGarmentInspectionDetail")
public class BasicsdatumGarmentInspectionDetail extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 主表单据id */
    @ApiModelProperty(value = "主表单据id"  )
    private String billId;
    /** 大货款号 */
    @ApiModelProperty(value = "大货款号"  )
    private String styleNo;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 物料厂家编号 */
    @ApiModelProperty(value = "物料厂家编号"  )
    private String matFactoryCode;
    /** 校验百分比 */
    @ApiModelProperty(value = "校验百分比"  )
    private Integer checkPercentage;
    /** 材料类型名称 */
    @ApiModelProperty(value = "材料类型名称"  )
    private String matTypeCode;
    /** 材料类型 */
    @ApiModelProperty(value = "材料类型"  )
    private String matType;
    /** 二级分类名称 */
    @ApiModelProperty(value = "二级分类名称"  )
    private String secondCategory;
    /** 二级分类code */
    @ApiModelProperty(value = "二级分类code"  )
    private String secondCategoryCode;
    /** 百分比 */
    @ApiModelProperty(value = "百分比"  )
    private Integer percentageQty;
    /** 成分code */
    @ApiModelProperty(value = "成分code"  )
    private String ingredientCode;
    /** 成分名称 */
    @ApiModelProperty(value = "成分名称"  )
    private String ingredientName;
    /** 成分说明code */
    @ApiModelProperty(value = "成分说明code"  )
    private String ingredientExplainCode;
    /** 成分说明name */
    @ApiModelProperty(value = "成分说明name"  )
    private String ingredientExplainName;
    /** 检测单位code */
    @ApiModelProperty(value = "检测单位code"  )
    private String testUnitCode;
    /** 检测单位name */
    @ApiModelProperty(value = "检测单位name"  )
    private String testUnitName;
    /** 报告附件 */
    @ApiModelProperty(value = "报告附件"  )
    private String attachmentUrl;
    /** 排序 */
    @ApiModelProperty(value = "排序"  )
    private String sort;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
