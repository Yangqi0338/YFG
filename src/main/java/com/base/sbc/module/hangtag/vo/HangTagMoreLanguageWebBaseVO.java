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
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public String getWarnMsg() {
        StringJoiner joiner = new StringJoiner("、", "【提示：", "为空】").setEmptyValue("");
        this.getLanguageList().stream().filter(it-> it.getCannotFindStandardColumnContent() || it.getCannotFindPropertiesContent()).forEach(languageVO-> {
            joiner.add(languageVO.getLanguageName()+"翻译");
        });
        return joiner.toString();
    }

    public Map<String, String> getContent() {
        Map<String, String> map = new HashMap<>(this.getLanguageList().size() + 1);
        this.getLanguageList().forEach(languageVo-> {
            map.put(languageVo.getLanguageCode(), languageVo.getContent());
        });
        return map;
    }

    public String getMergedContent() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("（原文）");
        joiner.add(this.getSourceContent());
        this.getLanguageList().forEach(languageVO-> {
            joiner.add("（" + languageVO.getLanguageName() + "）");
            joiner.add(languageVO.getContent());
        });
        return joiner.toString();
    }

    public String getMergedContentWithoutPrefix() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("（原文）");
        joiner.add(this.getPropertiesName());
        this.getLanguageList().forEach(languageVO-> {
            joiner.add("（" + languageVO.getLanguageName() + "）");
            joiner.add(languageVO.getPropertiesContent());
        });
        return joiner.toString();
    }

}
