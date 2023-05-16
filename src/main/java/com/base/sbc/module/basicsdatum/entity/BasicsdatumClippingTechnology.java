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
 * 类描述：基础资料-裁剪工艺 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumClippingTechnology
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-15 17:31:31
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_clipping_technology")
@ApiModel("基础资料-裁剪工艺 BasicsdatumClippingTechnology")
public class BasicsdatumClippingTechnology extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String coding;
    /** 工艺项目 */
    @ApiModelProperty(value = "工艺项目"  )
    private String technologyProject;
    /** 描述 */
    @ApiModelProperty(value = "描述"  )
    private String description;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String image;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
