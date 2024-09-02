/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.vo;

import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.patternmaking.entity.PatternMakingBarCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：Vo 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternMakingBarCodeVo
 * @email your email
 * @date 创建时间：2024-7-3 15:56:26
 */
@Data
@ApiModel(" PatternMakingBarCodeVo")
public class PatternMakingBarCodeVo extends PatternMakingBarCode {

    private static final long serialVersionUID = 1L;

    //设计款号
    private String designNo;
    //样板号
    private String patternNo;
    //打版类型
    private String sampleType;
    //打版类型名称
    private String sampleTypeName;
    //打样顺序名称
    private String patSeqName;
    //年份
    private String yearName;
    //季节
    private String seasonName;
    //品牌
    private String brandName;
    //需求数量
    private String requirementNum;
    //大货款号
    private String styleNo;
    //颜色名称
    private String colorName;
    //大货款图
    private String styleColorPic;
    //设计款图
    private String stylePic;
    //供应商名称
    private String patternRoom;
    //外发部绑定时间
    private Date techReceiveTime;
    //产前样外发部状态
    private String fobStatus;
    //改版意见-外发部
    private String techRemarks;
    //确认时间
    private Date processDepartmentDate;
    //供应商
    private String supplierName;
    private String supplierAbbreviation;
    private String supplierId;

    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String originalImg;
    /** 意见图片0 */
    @ApiModelProperty(value = "意见图片0"  )
    private String originalSuggestionImg;
    /** 意见视频 */
    @ApiModelProperty(value = "意见视频"  )
    private String originalSuggestionVideo;
    /** 意见图片1 */
    @ApiModelProperty(value = "意见图片1"  )
    private String originalSuggestionImg1;
    /** 意见图片2 */
    @ApiModelProperty(value = "意见图片2"  )
    private String originalSuggestionImg2;
    /** 意见图片3 */
    @ApiModelProperty(value = "意见图片3"  )
    private String originalSuggestionImg3;
    /** 意见图片4 */
    @ApiModelProperty(value = "意见图片4"  )
    private String originalSuggestionImg4;
    /**
     * 打样设计师id
     */
    @ApiModelProperty(value = "打样设计师id")
    private String patternDesignerId;
    /**
     * 打样设计师
     */
    @ApiModelProperty(value = "打样设计师")
    private String patternDesignerName;
    /**
     * 打样工艺员id
     */
    @ApiModelProperty(value = "打样工艺员id")
    private String patternTechnicianId;
    /**
     * 打样工艺员
     */
    @ApiModelProperty(value = "打样工艺员")
    private String patternTechnicianName;

    /**
     * 列头筛选数量
     */
    private Integer groupCount;

    private String code;

    private Integer makingCount;

    private String bandName;
    private String styleOriginName;
    private String prodCategoryName;
    private String prodCategory2ndName;
    private String prodCategory3rdName;

    private String season;
    private String sampleFile;

    private String sampleUrl;

    public String getSampleUrl() {
        if (StringUtils.isEmpty(sampleUrl)){
            this.sampleUrl = getSampleFile();
        }
        return sampleUrl;
    }
}
