package com.base.sbc.module.patternmaking.dto;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：滞留款导出
 */
@Data
@ApiModel("滞留款导出 TechnologyCenterTaskExcelDto ")
public class TechnologyCenterTaskExcelDto {

    @Excel(name = "版房")
    private String patternRoom;

    @Excel(name = "打板类型")
    private String sampleTypeName;

    @Excel(name = "产品季")
    private String planningSeason;

    public String getPlanningSeason() {
        return StrUtil.join(" ", yearName, seasonName, brandName);
    }

    @Excel(name = "波段")
    private String bandName;

    @Excel(name = "品类")
    private String prodCategoryName;

    @Excel(name = "款式")
    private String designNo;

    private String stylePic;

    @Excel(name = "款式图", type = 2, imageType = 2)
    private byte[] stylePic1;

    @ApiModelProperty(value = "打样设计师")
    private String patternDesignerName;

    @Excel(name = "收到样衣日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveSampleDate;

    @Excel(name = "停留时间")
    private Integer receiveDay;

    public Integer getReceiveDay() {
        if (receiveSampleDate != null) {
            return Math.toIntExact(DateUtil.betweenDay(receiveSampleDate, new Date(), false));
        }
        return null;
    }

    @Excel(name = "停留原因")
    private String receiveReason;


    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    @ApiModelProperty(value = "年份名称")
    private String yearName;
    @ApiModelProperty(value = "季节名称")
    private String seasonName;


}
