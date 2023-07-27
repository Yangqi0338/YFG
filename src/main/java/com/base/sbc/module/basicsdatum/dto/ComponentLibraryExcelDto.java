package com.base.sbc.module.basicsdatum.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
/*部件导出*/
public class ComponentLibraryExcelDto {


    /**编码*/
    @Excel(name = "编码")
    private String code;

    /**工艺项目(名称)*/
    @Excel(name = "工艺项目")
    private String processName;

    /**工艺类型*/
    private String processType;
    /**工艺类型名称*/
    @Excel(name = "部件类别")
    private String processTypeName;

    /**描述*/
    @Excel(name = "描述")
    private String description;

    /** 图片 */
    @Excel(name = "图片",type = 2)
    private String picture;

    /**状态*/
    @Excel(name = "可用的",replace={"false_0","true_1"})
    private String status;

    /** 更新者名称  */
    @Excel(name = "修改者")
    private String updateName;

    /**  创建者名称 */
    @Excel(name = "创建人")
    private String createName;
}
