/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
/**
 * 类描述：正确样管理 实体类
 * @address com.base.sbc.module.style.entity.StyleColorCorrectInfo
 * @author your name
 * @email your email
 * @date 创建时间：2023-12-26 10:13:31
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_style_color_correct_info")
@ApiModel("正确样管理 StyleColorCorrectInfo")
public class StyleColorCorrectInfo extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    @TableField(exist = false)
    private String productionSampleId;

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 关联ID */
    @ApiModelProperty(value = "关联ID"  )
    private String styleColorId;
    /** 款式(大货款号) */
    @ApiModelProperty(value = "款式(大货款号)"  )
    private String styleNo;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 设计下正确样 */
    @ApiModelProperty(value = "设计下正确样"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designCorrectDate;
    /** 设计下明细单 */
    @ApiModelProperty(value = "设计下明细单"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designDetailDate;
    /** 技术接受正确样日期-正确样 */
    @ApiModelProperty(value = "技术接受正确样日期-正确样"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date technologyCorrectDate;
    /** 技术部查版完成日期-正确样 */
    @ApiModelProperty(value = "技术部查版完成日期-正确样"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date technologyCheckDate;
    /** 工艺部接收日期-正确样 */
    @ApiModelProperty(value = "工艺部接收日期-正确样"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date technicsDate;
    /** GST接收-正确样 */
    @ApiModelProperty(value = "GST接收-正确样"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gstDate;
    /** 计控接明细单日期-明细单 */
    @ApiModelProperty(value = "计控接明细单日期-明细单"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planControlDate;
    /** 采购需求日期-明细单 */
    @ApiModelProperty(value = "采购需求日期-明细单"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date purchaseNeedDate;
    /** 采购回复货期-明细单 */
    @ApiModelProperty(value = "采购回复货期-明细单"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date purchaseRecoverDate;
    /** 辅仓接收日期-明细单 */
    @ApiModelProperty(value = "辅仓接收日期-明细单"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auxiliaryDate;
    /** 设计回收正确样时间 */
    @ApiModelProperty(value = "设计回收正确样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designCorrectRecoveryDate;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
