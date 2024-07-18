/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pokaYoke.entity;
import java.util.Date;

import com.base.sbc.module.pokaYoke.enums.PokBusinessTypeEnum;
import com.base.sbc.module.pokaYoke.enums.YokeExecuteLevelEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：防呆防错配置 实体类
 * @address com.base.sbc.module.pokaYoke.entity.PokaYokeConfig
 * @author your name
 * @email your email
 * @date 创建时间：2024-7-17 14:23:30
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_poka_yoke_config")
@ApiModel("防呆防错配置 PokaYokeConfig")
public class PokaYokeConfig extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /** 类型 */
    @ApiModelProperty(value = "类型"  )
    private PokBusinessTypeEnum type;
    /** 执行级别 */
    @ApiModelProperty(value = "执行级别"  )
    private YokeExecuteLevelEnum executeLevel;
    /** 提示语 */
    @ApiModelProperty(value = "提示语"  )
    private String hintMsg;
    /** 大类编码 */
    @ApiModelProperty(value = "大类编码"  )
    private String category1Code;
    /** 大类名称 */
    @ApiModelProperty(value = "大类名称"  )
    private String category1Name;
    /** 中类 */
    @ApiModelProperty(value = "中类"  )
    private String category2Code;
    /** 中类名称 */
    @ApiModelProperty(value = "中类名称"  )
    private String category2Name;
    /** 小类编码 */
    @ApiModelProperty(value = "小类编码"  )
    private String category3Code;
    /** 小类名称 */
    @ApiModelProperty(value = "小类名称"  )
    private String category3Name;
    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
    private String materialCode;
    /** 控制条件 */
    @ApiModelProperty(value = "控制条件"  )
    private String controlCondition;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
