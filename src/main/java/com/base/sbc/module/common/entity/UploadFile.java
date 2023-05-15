/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述：上传文件 实体类
 * @address com.base.sbc.module.common.entity.UploadFile
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-12 15:40:11
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_upload_file")
@ApiModel("上传文件 UploadFile")
public class UploadFile extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 名称 */
    @ApiModelProperty(value = "名称"  )
    private String name;
    /** 类型 */
    @ApiModelProperty(value = "类型"  )
    private String type;
    /** 大小 */
    @ApiModelProperty(value = "大小"  )
    private BigDecimal size;
    /** 文件地址 */
    @ApiModelProperty(value = "文件地址"  )
    private String url;
    /** 存储:aliyun oss,minio,ftp,本地等 */
    @ApiModelProperty(value = "存储:aliyun oss,minio,ftp,本地等"  )
    private String storage;
    /** md5 */
    @ApiModelProperty(value = "md5"  )
    private String md5;
    /** 状态:(0正常,1停用) */
    @ApiModelProperty(value = "状态:(0正常,1停用)"  )
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

