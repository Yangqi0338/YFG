package com.base.sbc.module.patternmaking.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WorkLogVoExcel {



    /**
     * 登记编码
     */
    @ApiModelProperty(value = "登记编码")
    @Excel(name = "登记编码" , width=15)
    private String code;

    /**
     * 工作日期
     */
    @ApiModelProperty(value = "工作日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "工作日期", width=24,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date workDate;
    /**
     * 工作委派部门
     */
    @ApiModelProperty(value = "工作委派部门")
    @Excel(name = "工作委派部门", width=15)
    private String delegatedDepartment;
    /**
     * 工作委派部门id
     */
    @ApiModelProperty(value = "工作委派部门id")
    private String delegatedDepartmentId;
    /**
     * 工作人员
     */
    @ApiModelProperty(value = "样衣工")
    @Excel(name = "样衣工", width=15)
    private String worker;

    /**
     * 裁剪工
     */
    @ApiModelProperty(value = "裁剪工")
    @Excel(name = "裁剪工", width=15)
    private String cutterName;

    @Excel(name = "样衣条码", width=15)
    @ApiModelProperty(value = "样衣条码")
    private String sampleBarCode;

    /**
     * 工作人员Id
     */
    @ApiModelProperty(value = "工作人员Id")
    private String workerId;
    /**
     * 登记类型
     */
    @ApiModelProperty(value = "登记类型")
    private String logType;
    /**
     * 登记类型名称
     */
    @ApiModelProperty(value = "登记类型名称")
    @Excel(name = "修改类型", width=15)
    private String logTypeName;
    /**
     * 参考类型
     */
    @ApiModelProperty(value = "参考类型")
    private String numType;
    /**
     * 参考类型名称
     */
    @ApiModelProperty(value = "参考类型名称")
    @Excel(name = "参考类型", width=15)
    private String numTypeName;
    /**
     * 参考款号
     */
    @ApiModelProperty(value = "参考款号")
    @Excel(name = "参考款号", width=15)
    private String referenceNo;
    /**
     * 工作说明
     */
    @ApiModelProperty(value = "工作说明")
    @Excel(name = "工作说明", width=15)
    private String workDescription;
    /**
     * 计件数量
     */
    @ApiModelProperty(value = "计件数量")
    @Excel(name = "计件数量" ,numFormat = "#.#",type = 10, width=15)
    private BigDecimal pieceCount;
    /**
     * 计时时间（分钟）
     */
    @ApiModelProperty(value = "计时时间（分钟）")
    @Excel(name = "计时时间（分钟）",numFormat = "#.#",type = 10, width=15)
    private BigDecimal timeMinutes;
    /**
     * 工作分数
     */
    @ApiModelProperty(value = "工作分数")
    @Excel(name = "工作分数",numFormat = "#.#",type = 10,width=15)
    private BigDecimal score;

}
