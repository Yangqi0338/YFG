/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pushRecords.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：推送记录表 实体类
 * @address com.base.sbc.module.pushRecords.entity.PushRecords
 * @author your name
 * @email your email
 * @date 创建时间：2023-7-9 17:33:46
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_push_records")
@ApiModel("推送记录表 PushRecords")
public class PushRecords extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 关联id，与其他表或数据关联的标识符 */
    @ApiModelProperty(value = "关联id，与其他表或数据关联的标识符"  )
    private String relatedId;
    /** 关联名称，与关联id相关的名称或描述 */
    @ApiModelProperty(value = "关联名称，与关联id相关的名称或描述"  )
    private String relatedName;
    /** 模块名称，标识推送的模块 */
    @ApiModelProperty(value = "模块名称，标识推送的模块"  )
    private String moduleName;
    /** 功能名称 */
    @ApiModelProperty(value = "功能名称"  )
    private String functionName;
    /** 推送地址，推送消息的目标地址 */
    @ApiModelProperty(value = "推送地址，推送消息的目标地址"  )
    private String pushAddress;
    /** 推送内容，要推送的具体内容 */
    @ApiModelProperty(value = "推送内容，要推送的具体内容"  )
    private String pushContent;
    /** 推送状态，记录推送的状态，如待推送、已推送、推送失败等 */
    @ApiModelProperty(value = "推送状态，记录推送的状态，如待推送、已推送、推送失败等"  )
    private String pushStatus;
    /** 返回消息，推送结果的返回消息或错误信息 */
    @ApiModelProperty(value = "返回消息，推送结果的返回消息或错误信息"  )
    private String responseMessage;
    /** 响应状态码，推送结果的响应状态码 */
    @ApiModelProperty(value = "响应状态码，推送结果的响应状态码"  )
    private String responseStatusCode;
    /** 推送次数，记录推送尝试的次数，用于追踪推送的重试情况 */
    @ApiModelProperty(value = "推送次数，记录推送尝试的次数，用于追踪推送的重试情况"  )
    private String pushCount;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

