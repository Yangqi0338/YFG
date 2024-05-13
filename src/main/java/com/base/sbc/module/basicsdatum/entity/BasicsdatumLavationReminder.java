/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：基础资料-洗涤图标与温馨提示 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumLavationReminder
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_lavation_reminder")
@ApiModel("基础资料-洗涤图标与温馨提示 BasicsdatumLavationReminder")
public class BasicsdatumLavationReminder extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    private String id;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String code;
//    /** 类别 */
//    @ApiModelProperty(value = "洗标"  )
//    private String careLabel;
//    /** 贮藏要求 */
//    @ApiModelProperty(value = "描述"  )
//    private String description;
    /** 有配饰款 */
    @ApiModelProperty(value = "洗护类别"  )
    private String washType;
    /** 洗涤图标编码 */
    @ApiModelProperty(value = "洗涤图标编码"  )
    private String washIconCode;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
//    /** 温馨提示 */
//    @ApiModelProperty(value = "语言"  )
//    private String language;
    /** 温馨提示 */
    @ApiModelProperty(value = "温馨提示"  )
    private String reminderCode;
    /** 温馨提示名称 */
    @ApiModelProperty(value = "温馨提示名称"  )
    private String reminderName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
