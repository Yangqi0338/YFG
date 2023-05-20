/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 类描述：导入导出基础资料-洗涤图标与温馨提示 dto类
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumPressingPacking
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00
 * @version 1.0
 */
@Data
@ApiModel("基础资料-洗涤图标与温馨提示 BasicsdatumPressingPacking")
public class BasicsdatumPressingPackingExcelDto  {


    /** 类别 */
    @ApiModelProperty(value = "类别"  )
    @Excel(name = "类别")
    private String category;
    /** 温馨提示 */
    @ApiModelProperty(value = "温馨提示"  )
    @Excel(name = "温馨提示")
    private String reminder;
    /** 贮藏要求 */
    @ApiModelProperty(value = "贮藏要求"  )
    @Excel(name = "贮藏要求")
    private String demand;
    /** 有配饰款 */
    @ApiModelProperty(value = "有配饰款"  )
    @Excel(name = "有配饰款")
    private String accessories;
    /** 有毛领款 */
    @ApiModelProperty(value = "有毛领款"  )
    @Excel(name = "有毛领款")
    private String neckline;
    /** 有印花款 */
    @ApiModelProperty(value = "有印花款"  )
    @Excel(name = "有印花款")
    private String printing;
    /** 压皱款、辅料除外款 */
    @ApiModelProperty(value = "压皱款、辅料除外款"  )
    @Excel(name = "压皱款、辅料除外款")
    private String crumple;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    @Excel(name = "图片")
    private String picture;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    @Excel(name = "状态(0正常,1停用)")
    private String status;
}
