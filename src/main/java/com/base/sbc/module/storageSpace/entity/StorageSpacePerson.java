/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.storageSpace.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：个人空间划分 实体类
 * @address com.base.sbc.module.storageSpace.entity.StorageSpacePerson
 * @author your name
 * @email your email
 * @date 创建时间：2024-6-27 10:26:28
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_storage_space_person")
@ApiModel("个人空间划分 StorageSpacePerson")
public class StorageSpacePerson extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 父空间id */
    @ApiModelProperty(value = "父空间id"  )
    private String parentSpaceId;
    /** 初始划分空间大小(单位：GB) */
    @ApiModelProperty(value = "初始划分空间大小(单位：GB)"  )
    private Integer initSpace;
    /** 拥有的空间大小(单位：GB) */
    @ApiModelProperty(value = "拥有的空间大小(单位：GB)"  )
    private Integer ownerSpace;
    /** 倍率 */
    @ApiModelProperty(value = "倍率"  )
    private Integer magnification;
    /** 空间所属人id */
    @ApiModelProperty(value = "空间所属人id"  )
    private String ownerId;
    /** 空间所属人工号 */
    @ApiModelProperty(value = "空间所属人工号"  )
    private String userName;
    /** 空间所属人姓名 */
    @ApiModelProperty(value = "空间所属人姓名"  )
    private String ownerName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
