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
 * 类描述：基础资料-纬度系数模板 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumCoefficientTemplate
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-15 14:34:41
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_coefficient_template")
@ApiModel("基础资料-纬度系数模板 BasicsdatumCoefficientTemplate")
public class BasicsdatumCoefficientTemplate extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 模板名称 */
    @ApiModelProperty(value = "模板名称"  )
    private String name;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 季节名称 */
    @ApiModelProperty(value = "季节名称"  )
    private String seasonName;
    /** 渠道 */
    @ApiModelProperty(value = "渠道"  )
    private String channel;
    /** 渠道名称 */
    @ApiModelProperty(value = "渠道名称"  )
    private String channelName;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
