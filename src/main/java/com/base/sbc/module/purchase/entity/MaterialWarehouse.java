/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：物料仓库 实体类
 * @address com.base.sbc.module.purchase.entity.MaterialWarehouse
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-4 14:46:11
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_material_warehouse")
@ApiModel("物料仓库 MaterialWarehouse")
public class MaterialWarehouse extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 状态（0禁用 1启用） */
    @ApiModelProperty(value = "状态（0禁用 1启用）"  )
    private String status;
    /** 仓库名称 */
    @ApiModelProperty(value = "仓库名称"  )
    private String warehouseName;
    /** 是否默认（0否 1是） */
    @ApiModelProperty(value = "是否默认（0否 1是）"  )
    private String defaultSetting;
    /** 电话 */
    @ApiModelProperty(value = "电话"  )
    private String phone;
    /** 联系人 */
    @ApiModelProperty(value = "联系人"  )
    private String contacts;
    /** 地址 */
    @ApiModelProperty(value = "地址"  )
    private String address;
    /** 说明 */
    @ApiModelProperty(value = "说明"  )
    private String introduce;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

