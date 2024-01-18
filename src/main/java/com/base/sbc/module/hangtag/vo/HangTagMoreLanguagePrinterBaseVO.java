/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.StringJoiner;

/**
 * 类描述：吊牌表 实体类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.entity.HangTag
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:52
 */
@Data
public class HangTagMoreLanguagePrinterBaseVO extends HangTagMoreLanguageBaseVO {

    /**
     * 打印系统专门检查信息
     */
    @ApiModelProperty(value = "打印系统专门检查信息")
    public String getPrinterCheckMessage(){
        String messageFormat = "%s未翻译";
        StringJoiner message = new StringJoiner("/");
//        if (this.cannotFindStandardColumnContent) message.add(String.format(messageFormat, this.getStandardColumnName() + message + "字段"));
//        if (this.getCannotFindPropertiesContent())  message.add(String.format(messageFormat, this.getStandardColumnName() + message + "内容"));
        return message.toString();
    };

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}
