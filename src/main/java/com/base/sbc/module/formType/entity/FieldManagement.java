/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formType.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：字段管理表 实体类
 * @address com.base.sbc.module.formType.entity.FieldManagement
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-22 19:52:55
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_field_management")
@ApiModel("字段管理表 FieldManagement")
public class FieldManagement extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 表单类型主键 */
    @ApiModelProperty(value = "表单类型主键"  )
    private String formTypeId;
    /** 分组名称 */
    @ApiModelProperty(value = "分组名称"  )
    private String groupName;
    /** 字段名称 */
    @ApiModelProperty(value = "字段名称"  )
    private String fieldName;
    /** 字段类型 */
    @ApiModelProperty(value = "字段类型"  )
    private String fieldType;
    /** 字段类型id 对应结构管理id */
    @ApiModelProperty(value = "字段类型id 对应结构管理id"  )
    private String fieldTypeId;
    /** 字段类型名 */
    @ApiModelProperty(value = "字段类型名"  )
    private String fieldTypeName;
    /** 字段类型编码 */
    @ApiModelProperty(value = "字段类型编码"  )
    private String fieldTypeCoding;
    /** 默认提示 */
    @ApiModelProperty(value = "默认提示"  )
    private String defaultHint;
    /** 选项（0:自定义选项，1字典) */
    @ApiModelProperty(value = "选项（0:自定义选项，1字典)"  )
    private String isOption;
    /** 字典key */
    @ApiModelProperty(value = "字典key"  )
    private String optionDictKey;
    /** 是否必填(0是，1否) */
    @ApiModelProperty(value = "是否必填(0是，1否)"  )
    private String isMustFill;
    /** 品类使用范围对应结构树id */
    @ApiModelProperty(value = "品类使用范围对应结构树id"  )
    private String categoryId;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 是否检查(0是，1否) */
    @ApiModelProperty(value = "是否检查(0是，1否)"  )
    private String isExamine;
    /** 对象类型 对应表单类型表 */
    @ApiModelProperty(value = "对象类型 对应表单类型表"  )
    private String  formObjectId;
    /** 检查顺序 */
    @ApiModelProperty(value = "检查顺序"  )
    private String examineOrder;
    /** 字段说明 */
    @ApiModelProperty(value = "字段说明"  )
    private String fieldExplain;
    /** 是否启用(0是，1否) */
    @ApiModelProperty(value = "是否启用(0是，1否)"  )
    private String status;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    /** 顺序 */
    @ApiModelProperty(value = "顺序"  )
    private Integer sequence;
    /** 是否能编辑（0可1否) */
    @ApiModelProperty(value = "是否能编辑（0可1否)"  )
    private String isCompile;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

