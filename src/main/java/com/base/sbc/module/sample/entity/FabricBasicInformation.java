/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：面料基本信息 实体类
 * @address com.base.sbc.module.sample.entity.FabricBasicInformation
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-8-8 10:16:01
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_basic_information")
@ApiModel("面料基本信息 FabricBasicInformation")
public class FabricBasicInformation extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 年份名称 */
    @ApiModelProperty(value = "年份名称"  )
    private String yearName;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 季节名称 */
    @ApiModelProperty(value = "季节名称"  )
    private String seasonName;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;
    /** 供应商料号 */
    @ApiModelProperty(value = "供应商料号"  )
    private String supplierMaterialCode;
    /** 厂家色号 */
    @ApiModelProperty(value = "厂家色号"  )
    private String supplierColor;
    /** 是否新面料（1是 0否 */
    @ApiModelProperty(value = "是否新面料（1是 0否"  )
    private String isNewFabric;
    /** 数量（米） */
    @ApiModelProperty(value = "数量（米）"  )
    private Integer quantity;
    /** 登记时间 */
    @ApiModelProperty(value = "登记时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerDate;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

