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

    public String getSeason() {
        return yearName + "-" + seasonName + "-" + brandName;
    }
}
