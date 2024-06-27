/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.storageSpace.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：存储空间 实体类
 * @address com.base.sbc.module.storageSpace.entity.StorageSpace
 * @author your name
 * @email your email
 * @date 创建时间：2024-6-27 10:17:48
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_storage_space")
@ApiModel("存储空间 StorageSpace")
public class StorageSpace extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 总空间大小(单位：GB) */
    @ApiModelProperty(value = "总空间大小(单位：GB)"  )
    private Integer totalSpace;
    /** 初始划分空间大小(单位：GB) */
    @ApiModelProperty(value = "初始划分空间大小(单位：GB)"  )
    private Integer initSpace;
    /** 初始倍率 */
    @ApiModelProperty(value = "初始倍率"  )
    private Integer initMagnification;
    /** 存储类型：1:素材库相关 */
    @ApiModelProperty(value = "存储类型：1:素材库相关"  )
    private String storageType;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
