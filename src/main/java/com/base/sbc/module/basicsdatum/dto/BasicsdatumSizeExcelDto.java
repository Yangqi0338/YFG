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
    /**
     * 编码
     */
    @Excel(name = "吊牌显示")
    private String hangtags;

    /**
     * 部件类别
     */
    @Excel(name = "号型")
    private String model;

    /**
     * 工艺项目
     */
    @Excel(name = "Internal Size" ,width = 120)
    private String internalSize;

    /**
     * 描述
     */
    @Excel(name = "External Size",width = 120)
    private String externalSize;

    /**
     * 图片
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

    @Excel(name = "状态", replace = {"FALSE_1","TRUE_0"})
    private String status;

    @Excel(name = "Dimension Type",width = 120)
    private String dimensionType;

    @Excel(name = "翻译名称")
    private String translateName;

    @Excel(name = "欧码")
    private String europeanSize;

    @Excel(name = "US标签")
    private String labelName;

    @Excel(name = "显示尺码标识",replace = {"FALSE_1","TRUE_0"},width = 120)
    private String showSizeStatus;

    private String    sizeLabelId;

}
