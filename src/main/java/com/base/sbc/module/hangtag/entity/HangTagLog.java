/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：吊牌日志 实体类
 * @address com.base.sbc.module.hangTag.entity.HangTagLog
 * @author xhj
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:57
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_hang_tag_log")
@ApiModel("吊牌日志 HangTagLog")
public class HangTagLog extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 操作描述 */
    @ApiModelProperty(value = "操作描述"  )
    private String operationDescription;
    /** 吊牌id */
    @ApiModelProperty(value = "吊牌id"  )
    private String hangTagId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/

    public HangTagLog(String operationDescription, String hangTagId) {
        this.operationDescription = operationDescription;
        this.hangTagId = hangTagId;
    }
}

