/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.standard.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StandardColumnType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：吊牌&洗唛全量标准表 实体类
 * @address com.base.sbc.module.moreLanguage.entity.StandardColumn
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:01:58
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_standard_column")
@ApiModel("吊牌&洗唛全量标准表 StandardColumn")
public class StandardColumn extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 吊牌属性编码 */
    @ApiModelProperty(value = "吊牌属性编码"  )
    private String code;
    /** 吊牌属性名称 */
    @ApiModelProperty(value = "吊牌属性名称"  )
    private String name;
    /** 是否是默认吊牌属性（决定是否能删除） */
    @ApiModelProperty(value = "是否是默认吊牌属性（决定是否能删除）"  )
    private String isDefault;
    /** 标准类型 */
    @ApiModelProperty(value = "标准类型"  )
    private StandardColumnType type;
    /** 标准模式 */
    @ApiModelProperty(value = "标准模式"  )
    private StandardColumnModel model;
    /** 子表头对应的字典表名 */
    @ApiModelProperty(value = "子表头对应的字典表名"  )
    private String tableName;
    /** 子表头对应的表头json */
    @ApiModelProperty(value = "子表头对应的表头json"  )
    private String tableTitleJson;
    /** 子表头对应的关键code */
    @ApiModelProperty(value = "子表头对应的关键code"  )
    private String tableCode;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** 展示字段 */
    @ApiModelProperty(value = "展示字段"  )
    private YesOrNoEnum showFlag;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
