package com.base.sbc.module.basicsdatum.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
/*外辅工艺*/
public class ExternalCraftExcelDto {


    /**编码*/
    @Excel(name = "编码")
    private String code;

    /**工艺项目(名称)*/
    @Excel(name = "工艺项目")
    private String processName;

    /**工艺要求*/
    @Excel(name = "工艺要求")
    private String processRequire;

    /**大类*/
    @Excel(name = "大类")
    private String broadCategory;

    /**中类*/
    @Excel(name = "中类")
    private String middleCategory;

    /**小类*/
    @Excel(name = "小类")
    private String smallCategory;

    /**描述*/
    @Excel(name = "描述")
    private String description;

    /** 图片 */
    @Excel(name = "图片",type = 2)
    private String picture;

    /**状态*/
    @Excel(name = "可用的",replace={"false_1","true_0"})
    private String status;

    /** 更新者名称  */
    @Excel(name = "修改者")
    private String updateName;

    /**  创建者名称 */
    @Excel(name = "创建人")
    private String createName;
}
