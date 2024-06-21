package com.base.sbc.module.patternlibrary.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Excel导入DTO
 *
 * @author xhte
 * @create 2024-04-07
 */
@Data
@ApiModel(value = "ExcelImportDTO对象", description = "Excel导入DTO")
public class ExcelImportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版型编码
     */
    @ExcelProperty("版型编码")
    @ApiModelProperty("版型编码")
    private String code;

    /**
     * 品牌名称（多个使用「/」分隔）
     */
    @ExcelProperty("所属品牌")
    @ApiModelProperty("品牌名称（多个使用「/」分隔）")
    private String brandName;

    /**
     * 启用状态（0-停用，1-启用)
     */
    @ExcelProperty("是否启用")
    @ApiModelProperty("启用状态（0-停用，1-启用)")
    private String enableFlag;

}
