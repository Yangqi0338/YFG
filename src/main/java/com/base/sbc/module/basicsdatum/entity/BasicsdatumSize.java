/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：基础资料-尺码表 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumSize
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-9-15 10:08:49
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_size")
@ApiModel("基础资料-尺码表 BasicsdatumSize")
public class BasicsdatumSize extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
    private String extSizeCode;
    /** 吊牌显示 */
    @ApiModelProperty(value = "吊牌显示"  )
    private String hangtags;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 号型 */
    @ApiModelProperty(value = "号型"  )
    private String model;
    /** 号型类型 */
    @ApiModelProperty(value = "号型类型"  )
    private String modelType;
    /** 号型类型编码 */
    @ApiModelProperty(value = "号型类型编码"  )
    private String modelTypeCode;
    /** Internal Size */
    @ApiModelProperty(value = "Internal Size"  )
    private String internalSize;
    /** External Size */
    @ApiModelProperty(value = "External Size"  )
    private String externalSize;
    /** 排序 */
    @ApiModelProperty(value = "排序"  )
    private String sort;
    /** 发送状态(0未发送,1已发送) */
    @ApiModelProperty(value = "发送状态(0未发送,1已发送)"  )
    private String sendStatus;
    /** 数据状态(0新建,1更改) */
    @ApiModelProperty(value = "数据状态(0新建,1更改)"  )
    private String dataStatus;
    /** Dimension Type */
    @ApiModelProperty(value = "Dimension Type"  )
    private String dimensionType;
    /** 翻译名称 */
    @ApiModelProperty(value = "欧码[EUR]"  )
    private String translateName;
    /** 欧码 */
    @ApiModelProperty(value = "外部尺码"  )
    private String europeanSize;
    /** US标签 */
    @ApiModelProperty(value = "US标签"  )
    private String labelName;
    /** 显示尺码标识(0显示，1隐藏) */
    @ApiModelProperty(value = "显示尺码标识(0显示，1隐藏)"  )
    private String showSizeStatus;
    /** 吊牌打印 显示尺码标识(0显示，1隐藏)*/
    @ApiModelProperty(value = "吊牌打印 显示尺码标识(1显示，0隐藏)"  )
    private String hangTagShowSizeStatus;
    /** 编码（笛莎） */
    @ApiModelProperty(value = "编码（笛莎）"  )
    private String realCode;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

