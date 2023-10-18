/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：款式-款式主款配饰表 实体类
 * @address com.base.sbc.module.style.entity.StyleMainAccessories
 * @author mengfanjiang
 * @email xx@qq.com
 * @date 创建时间：2023-10-17 11:06:10
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_style_main_accessories")
@ApiModel("款式-款式主款配饰表 StyleMainAccessories")
public class StyleMainAccessories extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 配色id */
    @ApiModelProperty(value = "配色id"  )
    private String styleColorId;
    /** 大货款号 */
    @ApiModelProperty(value = "大货款号"  )
    private String styleNo;
    /** 颜色 */
    @ApiModelProperty(value = "颜色"  )
    private String colorName;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String colorCode;
    /** 是否是内饰款(0否,1:是) */
    @ApiModelProperty(value = "是否是内饰款(0否,1:是)"  )
    private String isTrim;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
