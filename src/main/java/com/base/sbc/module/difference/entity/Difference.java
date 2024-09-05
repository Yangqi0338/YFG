/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.difference.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：公差，档差 实体类
 * @address com.base.sbc.module.difference.entity.Difference
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 11:01:36
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_difference")
@ApiModel("公差，档差 Difference")
public class Difference extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 制版ID */
    @ApiModelProperty(value = "档差管理id"  )
    private String rangeDifferenceId;
    /** 部位编码 */
    @ApiModelProperty(value = "部位编码"  )
    private String partCode;
    /** 部位名称 */
    @ApiModelProperty(value = "部位名称"  )
    private String partName;
    /** 量法 */
    @ApiModelProperty(value = "量法"  )
    private String method;
    /** 档差 */
    @ApiModelProperty(value = "档差"  )
    private String  codeError;
    /** 公差 */
    @ApiModelProperty(value = "公差"  )
    private String tolerance;
    /** 正公差+ */
    @ApiModelProperty(value = "正公差+"  )
    private String positive;
    /** 负公差- */
    @ApiModelProperty(value = "负公差-"  )
    private String minus;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    /** 档差设置，多档差 */
    @ApiModelProperty(value = "档差设置，多档差"  )
    private String codeErrorSetting;
    //尺码设置
    @ApiModelProperty(value = "尺码设置"  )
    private String sizeSetting;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

