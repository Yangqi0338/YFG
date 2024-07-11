/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.vo;

import com.base.sbc.module.patternmaking.entity.PatternMakingBarCode;
import io.swagger.annotations.ApiModel;
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

    private String designNo;
    private String patternNo;
    private String sampleType;
    private String sampleTypeName;
    private String patSeqName;
    private String yearName;
    private String seasonName;
    private String brandName;
    private String requirementNum;
    private String styleNo;
    private String colorName;
    private String styleColorPic;
    private String stylePic;
    private String patternRoom;
    private Date techReceiveTime;
    private String status;
    private String techRemarks;
    private Date processDepartmentDate;
    private String supplierName;

    public String getSeason() {
        return yearName + "-" + seasonName + "-" + brandName;
    }
}
