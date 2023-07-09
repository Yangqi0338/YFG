/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.difference.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("t_difference")
@ApiModel("公差，档差 Difference")
public class Difference extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 制版ID */
    @ApiModelProperty(value = "档差管理id"  )
    private String rangeDifferenceId;
    /** 制版ID */
    @ApiModelProperty(value = "制版ID"  )
    private String plateBillId;
    /** 制版号 */
    @ApiModelProperty(value = "制版号"  )
    private String plateBillCode;
    /** 序号 */
    @ApiModelProperty(value = "序号"  )
    private Integer sort;
    /** 部位名称 */
    @ApiModelProperty(value = "部位名称"  )
    private String partName;
    /** 公差 */
    @ApiModelProperty(value = "公差"  )
    private String tolerance;
    /** 标准值 */
    @ApiModelProperty(value = "标准值"  )
    private String standard;
    /** 工单的所有尺码一一列出来，所有的尺码 */
    @ApiModelProperty(value = "工单的所有尺码一一列出来，所有的尺码"  )
    private String size;
    /** 量法 */
    @ApiModelProperty(value = "量法"  )
    private String method;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    /** 图片（1张） */
    @ApiModelProperty(value = "图片（1张）"  )
    private String img;
    /** 码差 */
    @ApiModelProperty(value = "码差"  )
    private String codeError;
    /** 单位 */
    @ApiModelProperty(value = "单位"  )
    private String unit;
    /** 代码 */
    @ApiModelProperty(value = "代码"  )
    private String sizeCode;
    /** 驳口 */
    @ApiModelProperty(value = "驳口"  )
    private String splice;
    /** 部位倍数 */
    @ApiModelProperty(value = "部位倍数"  )
    private String partMultiple;
    /** 分组名称:default=尺寸量法,工艺明细尺寸=工艺明细尺寸 */
    @ApiModelProperty(value = "分组名称:default=尺寸量法,工艺明细尺寸=工艺明细尺寸"  )
    private String groupName;
    /** 正公差+ */
    @ApiModelProperty(value = "正公差+"  )
    private String positive;
    /** 负公差- */
    @ApiModelProperty(value = "负公差-"  )
    private String minus;
    /** 档差设置，多档差 */
    @ApiModelProperty(value = "档差设置，多档差"  )
    private String codeErrorSetting;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

