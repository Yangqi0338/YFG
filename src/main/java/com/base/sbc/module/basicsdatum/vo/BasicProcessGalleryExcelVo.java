package com.base.sbc.module.basicsdatum.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2024-03-11 15:50:45
 * @mail 247967116@qq.com
 */
@Data
public class BasicProcessGalleryExcelVo {
    @Excel(name = "图片",type = 2)
    private String image;
    @Excel(name = "名称")
    private String name;
    @Excel(name = "编码")
    private String code;
    @Excel(name = "类型名称")
    private String typeName;
    @Excel(name = "状态",replace = {"启用_0","停用_1"})
    private String status;
}
