/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：吊牌充绒量和特殊规格额外字段 实体类
 * @address com.base.sbc.module.basicsdatum.entity.SizeBulkStyle
 * @author your name
 * @email your email
 * @date 创建时间：2023-9-20 15:13:40
 * @version 1.0
 */
@Data
@TableName("t_size_bulk_style")
@ApiModel("吊牌充绒量和特殊规格额外字段 SizeBulkStyle")
public class SizeBulkStyle  {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    @ApiModelProperty(value = "主键id"  )
    private String id;

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 关联尺码id */
    @ApiModelProperty(value = "关联尺码id"  )
    private String sizeId;
    /** 大货款号id */
    @ApiModelProperty(value = "大货款号id"  )
    private String bulkStyleNo;
    /** 关联内容 */
    @ApiModelProperty(value = "关联内容"  )
    private String context;
    /** 类型 1:充绒量,2:特殊规格*/
    @ApiModelProperty(value = "类型 1:充绒量,2:特殊规格"  )
    private String type;
    private String companyCode;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
