/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：导入导出基础资料-洗涤图标与温馨提示 dto类
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumLavationReminder
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00
 * @version 1.0
 */
@Data
@ApiModel("基础资料-洗涤图标与温馨提示 BasicsdatumLavationReminder")
public class BasicsdatumLavationReminderExcelDto  {

    private String id;

    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String url;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    @Excel(name = "编码")
    private String code;
    @ApiModelProperty(value = "洗标"  )
    @Excel(name = "洗标")
    private String washIconCode;
    /** 有配饰款 */
    @ApiModelProperty(value = "洗护类别"  )
    @Excel(name = "洗护类别")
    private String washType;
    /** 温馨提示 */
    @ApiModelProperty(value = "温馨提示"  )
    private String reminderCode;
    /** 温馨提示名称 */
    @ApiModelProperty(value = "温馨提示名称"  )
    @Excel(name = "温馨提示")
    private String reminderName;
}
