package com.base.sbc.module.formtype.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
/**
 * 用于字段配置导入
 */
public class FieldOptionConfigExcelDto {
    /** 字段id */
    @ApiModelProperty(value = "字段id"  )
    private String fieldManagementId;
    /*表单类型id*/
    @ApiModelProperty(value = "表单类型id"  )
    private String formTypeId;
    /** 选项 */
    @ApiModelProperty(value = "选项"  )
    @Excel(name = "选项数据编码")
    private String optionCode;
    /** 选项名称 */
    @ApiModelProperty(value = "选项名称"  )
    @Excel(name = "选项数据")
    private String optionName;
    /** 品类 */
    @ApiModelProperty(value = "品类"  )
    @Excel(name = "品类编码")
    private String categoryCode;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    @Excel(name = "品类")
    private String categoryName;
    /** 中类 */
    @ApiModelProperty(value = "中类"  )
    @Excel(name = "中类编码")
    private String prodCategory2nd;
    /** 中类名称 */
    @ApiModelProperty(value = "中类名称"  )
    @Excel(name = "中类")
    private String prodCategory2ndName;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    @Excel(name = "季节编码")
    private String season;
    /** 季节名称 */
    @ApiModelProperty(value = "季节名称"  )
    @Excel(name = "季节")
    private String seasonName;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    @Excel(name = "品牌编码")
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    @Excel(name = "品牌")
    private String brandName;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
}
