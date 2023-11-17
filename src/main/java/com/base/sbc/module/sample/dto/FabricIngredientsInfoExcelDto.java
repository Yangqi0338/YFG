package com.base.sbc.module.sample.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
/*查询面料信息导出*/
public class FabricIngredientsInfoExcelDto {


    /** 开发类型名称 */
    @ApiModelProperty(value = "开发类型名称"  )
    @Excel(name = "开发类型")
    private String devTypeName;

    @Excel(name = "编码")
    private String codeName;
    public String getCodeName() {
        String format = String.format("%06d", this.getCode());
        return "fldyd"+format;
    }

    /** 品类名称 */
    @Excel(name = "品类")
    @ApiModelProperty(value = "品类名称"  )
    private String categoryName;

    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    @Excel(name = "图片",type = 2,orderNum ="-2")
    private String imageUrl;

    /**
     * 完成状态
     */
    @Excel(name = "完成状态",orderNum ="-1" )
    @ApiModelProperty(value = "完成状态"  )
    private String completionStatus;
    /** 厂家寄出时间 */
    @ApiModelProperty(value = "厂家寄出时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "厂家寄出时间",exportFormat = "yyyy-MM-dd")
    private Date sendDate;
    /** 开发厂家 */
    @ApiModelProperty(value = "开发厂家"  )
    @Excel(name = "开发厂家")
    private String devManufacturer;
    /** 开发情况 */
    @ApiModelProperty(value = "开发情况"  )
    @Excel(name = "开发情况")
    private String devCondition;
    /** 预估到样时间 */
    @ApiModelProperty(value = "预估到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "预估到样时间",exportFormat = "yyyy-MM-dd")
    private Date estimateAtactiformDate;
    /** 实际到样时间 */
    @ApiModelProperty(value = "实际到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "实际到样时间",exportFormat = "yyyy-MM-dd")
    private Date practicalAtactiformDate;
    /** 看样1 */
    @ApiModelProperty(value = "看样1"  )
    @Excel(name = "看样1")
    private String result1;
    /** 看样2 */
    @ApiModelProperty(value = "看样2"  )
    @Excel(name = "看样2")
    private String result2;
    /** 看样3 */
    @ApiModelProperty(value = "看样3"  )
    @Excel(name = "看样3")
    private String result3;
    /** 厂家 */
    @ApiModelProperty(value = "厂家"  )
    @Excel(name = "厂家")
    private String manufacturer;
    /** 厂家编号 */
    @ApiModelProperty(value = "厂家编号"  )
    @Excel(name = "厂家编号")
    private String manufacturerNumber;
    /** 首单期货 */
    @ApiModelProperty(value = "首单期货"  )
    @Excel(name = "首单期货")
    private String futures;
    /** 起订量 */
    @ApiModelProperty(value = "起订量"  )
    @Excel(name = "起订量")
    private Integer orderedQuantity;
    /** 规格 */
    @ApiModelProperty(value = "规格"  )
    @Excel(name = "规格")
    private String specification;

    @ApiModelProperty(value = "颜色名称"  )
    @Excel(name = "颜色名称")
    private String colorName;
    /** 数量 */
    @ApiModelProperty(value = "数量"  )
    @Excel(name = "数量")
    private String quantity;

    @ApiModelProperty(value = "大货含税价"  )
    @Excel(name = "大货含税价")
    private String containPrice;

    /** 设计到样时间 */
    @ApiModelProperty(value = "设计到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "设计到样时间",exportFormat = "yyyy-MM-dd")
    private Date designAtactiformDate;
    /** 调样设计师 */
    @ApiModelProperty(value = "调样设计师"  )
    @Excel(name = "调样设计师")
    private String atactiformStylist;

    /** 调样使用人 */
    @ApiModelProperty(value = "调样使用人"  )
    @Excel(name = "调样使用人")
    private String sampleUser;

    /** 到样检测时间 */
    @ApiModelProperty(value = "到样检测时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "到样检测时间",exportFormat = "yyyy-MM-dd")
    private Date testingDate;
    /** 检查结果 0不可用 1可用 */
    @ApiModelProperty(value = "检查结果 0不可用 1可用"  )
    @Excel(name = "检查结果", replace = {"不可用_0", "可用_1"} )
    private String testingResult;
    /** 是否下单 0否 1是 */
    @ApiModelProperty(value = "是否下单 0否 1是"  )
    @Excel(name = "是否下单", replace = {"否_0", "是_1"} )
    private String isBuy;
    /** 意见 */
    @ApiModelProperty(value = "意见"  )
    @Excel(name = "意见" )
    private String opinion;
    /** 使用款号 */
    @ApiModelProperty(value = "使用款号"  )
    @Excel(name = "使用款号" )
    private String styleNo;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    @Excel(name = "状态", replace = {"正常_0", "停用_1"} )
    private String status;

    private Integer code;

}
