/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 类描述：吊牌表 实体类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.entity.HangTag
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HangTagMoreLanguageWebBaseVO extends HangTagMoreLanguageBaseVO {

    public Map<String, String> getContent() {
        Map<String, String> map = new HashMap<>(this.getLanguageCodeList().size());
        if (CollUtil.isNotEmpty(this.getLanguageCodeList())) {
            this.getLanguageCodeList().forEach(languageCode-> {
                Optional<HangTagMoreLanguageVO> voOpt = this.getLanguageList().stream().filter(it -> languageCode.equals(it.getLanguageCode())).findFirst();
                String value;
                if (voOpt.isPresent()) {
                    value = voOpt.get().getContent();
                }else value= "";
                map.put(languageCode, value);
            });
        }
        return map;
    }

//    /**
//     * 是否是温馨提示
//     */
//    public Boolean isWarnTips = super.isWarnTips;
//
//    /**
//     * 是否是洗标
//     */
//    public Boolean isWashingMark = super.isWashingMark;

}
