/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.customFile.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：自定义文件夹 实体类
 * @address com.base.sbc.module.customFile.entity.FileTree
 * @author your name
 * @email your email
 * @date 创建时间：2024-6-11 11:30:33
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_file_tree")
@ApiModel("自定义文件夹 FileTree")
public class FileTree extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件数量")
    @TableField(exist = false)
    private long fileCount;

    @ApiModelProperty(value = "文件所占空间大小")
    @TableField(exist = false)
    private String fileSize;

	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 文件夹名称 */
    @ApiModelProperty(value = "文件夹名称"  )
    private String name;
    /** 文件夹业务类型：1：素材库相关 */
    @ApiModelProperty(value = "文件夹业务类型：1：素材库相关"  )
    private String businessType;
    /** 父级id */
    @ApiModelProperty(value = "父级id"  )
    private String parentId;
    /** 父级ids */
    @ApiModelProperty(value = "父级ids"  )
    private String parentIds;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
