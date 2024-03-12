package com.base.sbc.module.report.vo;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class StyleAnalyseVo {

    private String id;

    private String styleColorId;

    private Map<String, String> dynamicColumn;

    private String designNo;
    private String stylePic;
    private String brand;
    private String brandName;
    private String year;
    private String yearName;
    private String season;
    private String seasonName;
    private String month;
    private String monthName;
    private String bandCode;
    private String bandName;
    private String positioning;
    private String positioningName;
    private String styleFlavour;
    private String styleFlavourName;
    private String sizeRange;
    private String sizeRangeName;
    private String designer;
    private String designerId;
    private String technicianName;
    private String technicianId;
    private String fabDevelopeId;
    private String fabDevelopeName;
    private String patternDesignId;
    private String patternDesignName;
    private String patDiffName;
    private String patDiff;
    private String styleUnit;
    private String styleUnitCode;
    private String styleType;
    private String styleTypeName;
    private String prodCategory1st;
    private String prodCategory1stName;
    private String prodCategory;
    private String prodCategoryName;
    private String prodCategory2nd;
    private String prodCategory2ndName;
    private String prodCategory3rd;
    private String prodCategory3rdName;
    private String devClass;
    private String devClassName;
    private String styleOrigin;
    private String styleOriginName;
    private String devtType;
    private String devtTypeName;
    private String registeringNo;
    private String salesType;
    private String salesTypeName;
    private String styleBandCode;
    private String styleBandName;
    private String styleNo;
    private String styleColorPic;

    private Date createDate;

    private Integer groupCount;

    private String valName;

}
