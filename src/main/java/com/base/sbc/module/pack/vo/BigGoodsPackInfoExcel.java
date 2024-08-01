package com.base.sbc.module.pack.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BigGoodsPackInfoExcel {
    @Excel(name = "大货款图",orderNum="0",imageType = 2,type = 2)
    private  byte[]  stylePic1;

    @ApiModelProperty(value = "款式图")
    private String stylePic;

    @Excel(name = "款式",orderNum="1")
    private  String  style;

    @ApiModelProperty(value = "设计款号")
    @Excel(name = "设计款号",orderNum="2")
    private String designNo;

    @ApiModelProperty(value = "大货款号")
    @Excel(name = "大货款号",orderNum="3")
    private String styleNo;

    @Excel(name = "款式BOM",orderNum="4")
    private String code;

    @Excel(name = "BOM状态",replace={"样品_null","样品_0","大货_1"} ,orderNum="5")
    private String bomStatus;

    @Excel(name = "品类",orderNum="6")
    private String prodCategoryName;

    @Excel(name = "颜色" ,orderNum="7")
    @ApiModelProperty(value = "配色")
    private String color;

    @Excel(name = "生产类型",orderNum="8")
    private String devtTypeName;


    /**
     * 设计转后技术确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "设计转后技术确认:(0未确认,1已确认)")
    @Excel(name = "设计转后技术",replace={"未确认_null","未确认_0","已确认_1"},orderNum="9")
    private String designTechConfirm;

    /**
     * 大货制单员确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "大货制单员确认:(0未确认,1已确认)")
    @Excel(name = "设计转后技术",replace={"未确认_null","未确认_0","已确认_1"},orderNum="10")
    private String bulkOrderClerkConfirm;

    /**
     * 大货工艺员确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "大货工艺员确认:(0未确认,1已确认)")
    @Excel(name = "大货制单员确认",replace={"未确认_null","未确认_0","已确认_1"},orderNum="11")
    private String bulkProdTechConfirm;

    /**
     * 后技术确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "后技术确认:(0未确认,1已确认)")
    @Excel(name = "后技术确认",replace={"未确认_null","未确认_0","已确认_1"},orderNum="12")
    private String postTechConfirm;

    /**
     * 审核状态：待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "审核状态：待审核(1)、审核通过(2)、被驳回(-1)")
    @Excel(name = "审核状态",replace={"被驳回_-1","审核通过_2","待审核_1","未审核_0"},orderNum="13")
    private String confirmStatus;

    /**
     * 反审状态：待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "反审状态：待审核(1)、审核通过(2)、被驳回(-1)")
    @Excel(name = "反审状态",replace={"被驳回_-1","审核通过_2","待审核_1","未审核_0"} ,orderNum="14")
    private String reverseConfirmStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间",exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum="16")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    @Excel(name = "创建人",orderNum="15")
    private String createName;





}
