package com.base.sbc.module.basicsdatum.dto;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/*
* 测量点Excel导入 dto类
* */
@Data
@TableName(value = "BasicsdatumMeasurementExcelDto")
public class BasicsdatumMeasurementExcelDto {


    @Excel(name  = "ID"  )
    private String id;

    /** 编码 */
    @Excel(name  = "测量点"  )
    private String measurement;

    /** POM类型 */
    @Excel(name = "POM类型"  )
    private String pdmType;

    /** 描述 */
    @Excel(name  = "描述"  )
    private String description;

    @Excel(name = "描述(Alt)"  )
    private String descriptionAlt;

    /** 图片 */
    @Excel(name = "图片", type = 2)
    private String image;

    /** 可用的 */
    @Excel(name  = "可用的" , replace = {"true_0", "false_1"})
    private String status;

}
