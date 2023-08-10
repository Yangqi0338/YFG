/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.entity;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：面料开发物料信息 实体类
 * @address com.base.sbc.module.fabric.entity.FabricDevMaterialInfo
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-7 11:01:46
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_dev_material_info")
@ApiModel("面料开发物料信息 FabricDevMaterialInfo")
public class FabricDevMaterialInfo extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 业务id */
    @ApiModelProperty(value = "业务id"  )
    private String bizId;
    /** 旧料号 */
    @ApiModelProperty(value = "旧料号"  )
    private String oldMaterialCode;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 年份编码 */
    @ApiModelProperty(value = "年份编码"  )
    private String yearCode;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 季节编码 */
    @ApiModelProperty(value = "季节编码"  )
    private String seasonCode;
    /** 材料类型 */
    @ApiModelProperty(value = "材料类型"  )
    private String materialType;
    /** 材料类型编码 */
    @ApiModelProperty(value = "材料类型编码"  )
    private String materialTypeCode;
    /** 形状功能描述 */
    @ApiModelProperty(value = "形状功能描述"  )
    private String shapeFunctionDescription;
    /** 色号和型号 */
    @ApiModelProperty(value = "色号和型号"  )
    private String colorAndModel;
    /** 色号和型号编码 */
    @ApiModelProperty(value = "色号和型号编码"  )
    private String colorAndModelCode;
    /** 厂家成分 */
    @ApiModelProperty(value = "厂家成分"  )
    private String factoryIngredient;
    /** 面料成分 */
    @ApiModelProperty(value = "面料成分"  )
    private String fabricIngredient;
    /** 面料成分说明 */
    @ApiModelProperty(value = "面料成分说明"  )
    private String fabricIngredientSay;
    /** 门幅 */
    @ApiModelProperty(value = "门幅"  )
    private BigDecimal width;
    /** 克重 */
    @ApiModelProperty(value = "克重"  )
    private BigDecimal gramWeight;
    /** 工艺要求 */
    @ApiModelProperty(value = "工艺要求"  )
    private String technicsRequirements;
    /** 辅料材质 */
    @ApiModelProperty(value = "辅料材质"  )
    private String auxiliaryMaterial;
    /** 经缩 */
    @ApiModelProperty(value = "经缩"  )
    private BigDecimal longitudeShrink;
    /** 纬缩 */
    @ApiModelProperty(value = "纬缩"  )
    private BigDecimal latitudeShrink;
    /** 损耗% */
    @ApiModelProperty(value = "损耗%"  )
    private BigDecimal lossRate;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
