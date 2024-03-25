package com.base.sbc.module.basicsdatum.dto;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 尺码导入
 */
@Data
@TableName(value = "BasicsdatumComponentExcelDto")
public class BasicsdatumSizeExcelDto {

    // @Excel(name = "id")
    private String id;
    /*号型类型编码*/
    private String modelTypeCode;
    /**
     * 吊牌显示
     */
    @Excel(name = "吊牌显示")
    private String hangtags;

    /**
     * 号型
     */
    @Excel(name = "号型")
    private String model;

    /**
     * 工艺项目
     */
    @Excel(name = "Internal Size" )
    private String internalSize;

    /**
     * External Size
     */
    @Excel(name = "External Size")
    private String externalSize;

    /**
     * 代码
     */
    @Excel(name = "代码")
    private String code;

    @Excel(name = "排序")
    private Integer sort;

    @Excel(name = "发送状态",replace = {"已发送_1","未发送_0"})
    private String sendStatus;

    @Excel(name = "数据状态",replace = {"更改_1","新建_0"})
    private String dataStatus;

    @Excel(name = "创建")
    @DateTimeFormat("yyyy/MM/dd hh:mm:ss")
    protected Date createDate;

    @Excel(name = "状态", replace = {"false_1","true_0"})
    private String status;

    @Excel(name = "Dimension Type")
    private String dimensionType;

    @Excel(name = "欧码[EUR]")
    private String translateName;

    @Excel(name = "外部尺码")
    private String europeanSize;

    /*号型类型*/
    @Excel(name = "US标签")
    private String modelType;

    @Excel(name = "显示尺码标识",replace = {"false_1","true_0"})
    private String showSizeStatus;

    private String    sizeLabelId;

}
