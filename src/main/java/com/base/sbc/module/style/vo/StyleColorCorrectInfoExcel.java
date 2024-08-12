/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：样衣-款式配色 Vo类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.SampleStyleColor
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 */
@Data

@ApiModel("样衣-款式配色 StyleColorCorrectInfoExcel")
public class StyleColorCorrectInfoExcel {

    //款图
    private String stylePic;
    @Excel(name = "款图", imageType = 2, type = 2)
    private byte[] stylePic1;

    //产品季
    @Excel(name = "产品季")
    private String planningSeason;

    public String getPlanningSeason() {
        return this.yearName + " " + this.seasonName + " " + this.bandName;
    }

    //品类
    private String prodCategory;
    @Excel(name = "品类")
    private String prodCategoryName;

    //生产类型
    private String devtType;
    @Excel(name = "生产类型")
    private String devtTypeName;

    //设计师
    @Excel(name = "设计师")
    private String designer;

    //波段
    private String bandCode;
    @Excel(name = "波段")
    private String bandName;

    //紧急程度
    private String taskLevel;
    @Excel(name = "紧急程度")
    private String taskLevelName;

    //设计款号
    @Excel(name = "设计款号")
    private String designNo;

    //大货款号
    @Excel(name = "大货款号")
    private String styleNo;

    //BOM阶段
    @Excel(name = "BOM阶段", replace = {"研发阶段_0", "大货阶段_1"})
    private String bomStatus;

    //工艺员
    @Excel(name = "工艺员")
    private String technicianName;

    //设计下正确样
    @Excel(name = "设计下正确样", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date designCorrectDate;

    //设计下明细单
    @Excel(name = "设计下明细单", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date designDetailDate;

    @Excel(name = "设计回收正确样时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date designCorrectRecoveryDate;

    //技术接收正确样日期
    @Excel(name = "技术接收正确样日期", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date technologyCorrectDate;

    //技术部查版完成日期
    @Excel(name = "技术部查版完成日期", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date technologyCheckDate;

    //工艺部接收日期
    @Excel(name = "工艺部接收日期", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date technicsDate;

    //GST接收
    @Excel(name = "GST接收", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date gstDate;

    //计控接明细单日期
    @Excel(name = "计控接明细单日期", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date planControlDate;

    //采购需求日期
    @Excel(name = "采购需求日期", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date purchaseNeedDate;

    //采购回复货期
    @Excel(name = "采购回复货期", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date purchaseRecoverDate;

    //辅仓接收日期
    @Excel(name = "辅仓接收日期", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date auxiliaryDate;

    //修改人
    @Excel(name = "修改人")
    private String updateName;

    //修改时间
    @Excel(name = "修改时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    private String yearName;
    private String seasonName;
}
