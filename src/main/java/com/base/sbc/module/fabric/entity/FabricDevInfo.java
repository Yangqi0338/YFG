/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：面料开发信息 实体类
 * @address com.base.sbc.module.fabric.entity.FabricDevInfo
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-7 11:01:37
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_dev_info")
@ApiModel("面料开发信息 FabricDevInfo")
public class FabricDevInfo extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 开发配置id */
    @ApiModelProperty(value = "开发配置id"  )
    private String devConfigId;
    /** 实际开始时间 */
    @ApiModelProperty(value = "实际开始时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date practicalStartDate;
    /** 实际开始时间 */
    @ApiModelProperty(value = "实际开始时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date practicalEndDate;
    /** 状态(0.未处理,1.通过，2.失败) */
    @ApiModelProperty(value = "状态(0.未处理,1.通过，2.失败)"  )
    private String status;
    /** 操作人 */
    @ApiModelProperty(value = "操作人"  )
    private String operator;
    /** 操作人id */
    @ApiModelProperty(value = "操作人id"  )
    private String operatorId;
    /** 附件 */
    @ApiModelProperty(value = "附件"  )
    private String attachmentUrl;
    /** 面料开发主id */
    @ApiModelProperty(value = "面料开发主id"  )
    private String devMainId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
