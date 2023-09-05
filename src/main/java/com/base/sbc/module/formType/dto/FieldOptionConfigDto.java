/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formType.dto;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：字段选项配置表 实体类
 * @address com.base.sbc.module.formType.entity.FieldOptionConfig
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-9-4 13:02:46
 * @version 1.0
 */
@Data

public class FieldOptionConfigDto  {
    /** id */
    @ApiModelProperty(value = "id"  )
    private String id;
    /** 字段id */
    @ApiModelProperty(value = "字段id"  )
    private String fieldManagementId;
    /*表单类型id*/
    @ApiModelProperty(value = "表单类型id"  )
    private String formTypeId;
    /** 选项 */
    @ApiModelProperty(value = "选项"  )
    private String optionCode;
    /** 选项名称 */
    @ApiModelProperty(value = "选项名称"  )
    private String optionName;
    /** 品类 */
    @ApiModelProperty(value = "品类"  )
    private String categoryCode;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    private String categoryName;
    /** 中类 */
    @ApiModelProperty(value = "中类"  )
    private String prodCategory2nd;
    /** 中类名称 */
    @ApiModelProperty(value = "中类名称"  )
    private String prodCategory2ndName;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 季节名称 */
    @ApiModelProperty(value = "季节名称"  )
    private String seasonName;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
}

