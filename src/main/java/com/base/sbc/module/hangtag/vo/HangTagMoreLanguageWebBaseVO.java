/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.constant.MoreLanguageProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.xpath.operations.Bool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.*;

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
        StringJoiner joiner = new StringJoiner(MoreLanguageProperties.checkMsgItemSeparator).setEmptyValue("");
        // 检查是否存在未翻译的数据
        this.getLanguageList().stream().filter(it-> it.getCannotFindStandardColumnContent() || it.getCannotFindPropertiesContent()).forEach(languageVO-> {
            // 合并提示信息
            StringJoiner fillJoiner = new StringJoiner(MoreLanguageProperties.checkMultiItemSeparator);
            if (languageVO.getCannotFindStandardColumnContent()) fillJoiner.add(FIELD.getText());
            if (languageVO.getCannotFindPropertiesContent()) fillJoiner.add(CONTENT.getText());
            joiner.add(languageVO.getLanguageName() + fillJoiner + TRANSLATE.getText());
        });
        if (joiner.length()<=0) return "";
        return MoreLanguageProperties.getMsg(NOT_EXIST_CONTENT,joiner.toString());
    }

    public Boolean getCannotFindStandardColumnContent(){
        return this.getLanguageList().stream().anyMatch(HangTagMoreLanguageVO::getCannotFindStandardColumnContent);
    }

    public Boolean getCannotFindPropertiesContent(){
        return this.getLanguageList().stream().anyMatch(HangTagMoreLanguageVO::getCannotFindPropertiesContent);
    }

    public Map<String, String> getContent() {
        // 返回每个语言对应的翻译
        Map<String, String> map = new HashMap<>(this.getLanguageList().size() + 1);
        this.getLanguageList().forEach(languageVo-> {
            map.put(languageVo.getLanguageCode(), languageVo.getContent());
        });
        return map;
    }

    private String getMergedContent(Function<HangTagMoreLanguageBaseVO, String> fieldFunc, Function<HangTagMoreLanguageVO, String> contentFunc){
        // 返回合并内容
        StringJoiner joiner = new StringJoiner(MoreLanguageProperties.multiSeparator);
        joiner.add(Opt.ofNullable(fieldFunc.apply(this)).orElse(""));
        List<HangTagMoreLanguageVO> languageList = this.getLanguageList();
        for (int i = 0, languageListSize = languageList.size(); i < languageListSize; i++) {
            HangTagMoreLanguageVO languageVO = languageList.get(i);
            joiner.add(String.format(MoreLanguageProperties.checkMergedSeparator, languageVO.getLanguageName()));
            String content = contentFunc.apply(languageVO);
            joiner.add(content);
            if (StrUtil.isBlank(content) && i == languageListSize - 1) {
                joiner.add("");
            }
        }
        return joiner.toString();
    }

    public String getMergedContent() {
        return getMergedContent(HangTagMoreLanguageBaseVO::getSourceContent, HangTagMoreLanguageVO::getContent);
    }

    public String getMergedPrefixContent() {
        return getMergedContent(HangTagMoreLanguageBaseVO::findStandardColumnName, HangTagMoreLanguageVO::findStandardColumnContent);
    }

    public String getMergedContentWithoutPrefix() {
        return getMergedContent(HangTagMoreLanguageBaseVO::getPropertiesName, HangTagMoreLanguageVO::getPropertiesContent);
    }

}
