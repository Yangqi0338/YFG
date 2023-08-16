/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：主题企划 实体类
 * @address com.base.sbc.module.planning.entity.ThemePlanning
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-15 13:58:35
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_theme_planning")
@ApiModel("主题企划 ThemePlanning")
public class ThemePlanning extends BaseDataEntity<String> {

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
    /** 主题企划 */
    @ApiModelProperty(value = "主题企划"  )
    private String themeName;
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
    /** 产品季 */
    @ApiModelProperty(value = "产品季"  )
    private String planningSeason;
    /** 产品季id */
    @ApiModelProperty(value = "产品季id"  )
    private String planningSeasonId;
    /** 主题描述 */
    @ApiModelProperty(value = "主题描述"  )
    private String themeDescription;
    /** 参考附件 */
    @ApiModelProperty(value = "参考附件"  )
    private String referAttachment;
    /** 审核状态：0：未提交，1：待审核，2：审核通过，-1：驳回） */
    @ApiModelProperty(value = "审核状态：0：未提交，1：待审核，2：审核通过，-1：驳回）"  )
    private String confirmStatus;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

