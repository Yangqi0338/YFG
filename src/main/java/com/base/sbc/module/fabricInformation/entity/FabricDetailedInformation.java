/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricInformation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 类描述：面料详细信息 实体类
 * @address com.base.sbc.module.fabricInformation.entity.FabricDetailedInformation
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-19 18:23:28
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_detailed_information")
public class FabricDetailedInformation extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 面料是否可用(0是，1否) */
    @ApiModelProperty(value = "面料是否可用(0是，1否)"  )
    private String fabricIsUsable;
    /** 面料价格 */
    @ApiModelProperty(value = "面料价格"  )
    private Integer fabricPrice;
    /** 纱支规格 */
    @ApiModelProperty(value = "纱支规格"  )
    private String specification;
    /** 密度 */
    @ApiModelProperty(value = "密度"  )
    private String density;
    /** 面料成分 */
    @ApiModelProperty(value = "面料成分"  )
    private String ingredient;
    /** 货期 */
    @ApiModelProperty(value = "货期"  )
    private Integer leadtime;
    /** 起订量 */
    @ApiModelProperty(value = "起订量"  )
    private Integer minimumOrderQuantity;
    /** 门幅 */
    @ApiModelProperty(value = "门幅"  )
    private Double larghezza;
    /** 克重 */
    @ApiModelProperty(value = "克重"  )
    private Double gramWeight;
    /** 胚布情况 */
    @ApiModelProperty(value = "胚布情况"  )
    private String germinalCondition;
    /** 调样日期 */
    @ApiModelProperty(value = "调样日期"  )
    private Date atactiformDate;
    /** 预估到样时间 */
    @ApiModelProperty(value = "预估到样时间"  )
    private Date estimateAtactiformDate;
    /** 实际到样时间 */
    @ApiModelProperty(value = "实际到样时间"  )
    private Date practicalAtactiformDate;
    /** 留样送检时间 */
    @ApiModelProperty(value = "留样送检时间"  )
    private Date inspectDate;
    /** 理化检测结果（0是1否 */
    @ApiModelProperty(value = "理化检测结果（0是1否"  )
    private String physicochemistryDetectionResult;
    /** 样衣试穿洗涤送检时间 */
    @ApiModelProperty(value = "样衣试穿洗涤送检时间"  )
    private Date sampleWashingInspectionDate;
    /** 洗涤检测结果（0是1否 */
    @ApiModelProperty(value = "洗涤检测结果（0是1否"  )
    private String washDetectionResult;
    /** 图片地址 */
    @ApiModelProperty(value = "图片地址"  )
    private String imageUrl;
    /** 是否草稿（0是：1否 */
    @ApiModelProperty(value = "是否草稿（0是：1否"  )
    private String isDraft;
    /*理化报告地址*/
    private String  reportUrl;
    /*理化报告名*/
    private String reportName;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
