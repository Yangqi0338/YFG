/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：面料企划 实体类
 * @address com.base.sbc.module.fabric.entity.FabricPlanning
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-23 11:03:00
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_planning")
@ApiModel("面料企划 FabricPlanning")
public class FabricPlanning extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 面料企划 */
    @ApiModelProperty(value = "面料企划"  )
    private String fabricPlanningName;
    /** 品牌编码 */
    @ApiModelProperty(value = "品牌编码"  )
    private String brandCode;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
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
    /** 品类 */
    @ApiModelProperty(value = "品类"  )
    private String category;
    /** 品类编码 */
    @ApiModelProperty(value = "品类编码"  )
    private String categoryCode;
    /** 类型 */
    @ApiModelProperty(value = "类型"  )
    private String type;
    /** 类型编码 */
    @ApiModelProperty(value = "类型编码"  )
    private String typeCode;
    /** 审核状态：1.未提交、2.审核中、3.审核通过、4.审核失败 */
    @ApiModelProperty(value = "审核状态：1.未提交、2.审核中、3.审核通过、4.审核失败"  )
    private String approveStatus;
    /** 审核时间 */
    @ApiModelProperty(value = "审核时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approveDate;
    /** 审核人id */
    @ApiModelProperty(value = "审核人id"  )
    private String approveUserId;
    /** 审核人名称 */
    @ApiModelProperty(value = "审核人名称"  )
    private String approveUserName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
