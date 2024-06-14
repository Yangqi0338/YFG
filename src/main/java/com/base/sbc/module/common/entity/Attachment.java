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

/**
 * 类描述：附件 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.common.entity.Attachment
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-4 17:14:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_attachment")
@ApiModel("附件 Attachment")
public class Attachment extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 上传文件id
     */
    @ApiModelProperty(value = "上传文件id")
    private String fileId;
    /**
     * 外键
     */
    @ApiModelProperty(value = "外键")
    private String foreignId;
    /**
     * 类型:样衣/制版/等
     */
    @ApiModelProperty(value = "类型:样衣/制版/等")
    private String type;
    /**
     * 状态:(0正常,1停用)
     */
    @ApiModelProperty(value = "状态:(0正常,1停用)")
    private String status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

