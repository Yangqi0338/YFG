package com.base.sbc.module.style.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Optional;

@Data
public class StyleColorExcel {

    /**
     * 款式图
     */
    @ApiModelProperty(value = "款式图")
    private String  stylePic;

    @ApiModelProperty(value = "款式图")
    @Excel(name = "款式图",imageType = 2,type = 2)
    private  byte[] stylePic1;

    @Excel(name = "设计款号")
    private String   designNo;

    @Excel(name = "款式")
    private String  style;

    @ApiModelProperty(value = "款式名称")
    private String styleName;

    @Excel(name = "大类")
    private String  prodCategory1stName;

    @Excel(name = "品类")
    private String  prodCategoryName;

    @Excel(name = "SCM下发状态",replace={"重新打开_3","发送失败_2","发送成功_1","未发送_0"})
    private String scmSendFlag;

    private String styleColorPic;

    @Excel(name = "大货款图",imageType = 2,type = 2)
    private  byte[] styleColorPic1;

    @Excel(name = "大货款号")
    private String styleNo;

    @Excel(name = "颜色编码")
    private String colorCode;

    @Excel(name = "配色")
    private String  colorName;

    @Excel(name = "是否配饰款",replace={"否_0","是_1"})
    private String isTrim;

    @Excel(name = "配饰")
    private String accessory;

    @Excel(name = "配饰款号")
    private String accessoryNo;

    @Excel(name = "主款")
    private String principalStyle;

    @Excel(name = "主款款号")
    private String principalStyleNo;

    @Excel(name = "是否下单",replace={"是_1","否_0"} )
    private String orderFlag;

    @Excel(name = "BOM状态",replace={"样品_0","大货_1"})
    private String bomStatus;

    @Excel(name = "紧急程度")
    private String taskLevelName;

    @Excel(name = "款式类型")
    private String styleTypeName;

    @Excel(name = "生产类型")
    private String devtTypeName;

    @Excel(name = "号型类型")
    private String sizeRangeName;

    @Excel(name = "波段")
    private String bandName;

    @Excel(name = "设计师")
    private String designer;

    @Excel(name = "工艺员")
    private String technicianName;

    @Excel(name = "状态",replace={"停用_1","启用_0"} )
    private String status;


    @Excel(name = "创建时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @Excel(name = "创建人")
    private String createName;











    public String getStyle() {
        return Optional.ofNullable(designNo).orElse("") + Optional.ofNullable(styleName).orElse("");
    }
}
