package com.base.sbc.module.moreLanguage.strategy.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageTableTitle;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableContext;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableContext.MoreLanguageTableTitleHandlerEnum;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableTitleHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * {@code 描述：多语言获取子数据的策略}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/12/4
 */
@Order(0)
@Component
public class MoreLanguageMirrorTableTitle extends MoreLanguageTableTitleHandler {
    private List<CountryLanguage> countryLanguageList = new ArrayList<>();

    @Override
    public boolean needHandler(MoreLanguageTableTitle tableTitle) {
        // 复用copy,但是必须在copyHandler处理器后面,暂时没用 TODO
        String param = MoreLanguageTableContext.findParam(MoreLanguageTableTitleHandlerEnum.COPY.getHandlerKey());
        countryLanguageList = JSONUtil.toList(param, CountryLanguage.class);
//        if (MoreLanguageTableContext.MoreLanguageTableParamEnum.NO_DECORATE.isTrue()) return false;
        return super.needHandler(tableTitle) && CollectionUtil.isNotEmpty(countryLanguageList);
    }

    @Override
    public MoreLanguageTableTitleHandler doHandler(MoreLanguageTableTitle tableTitle) {
        tableTitleList.forEach(newTableTitle-> {
            if (newTableTitle.getCode().equals(tableTitle.getCode())) {
                newTableTitle.setCode(countryLanguageList.stream()
                        .map(it-> it.getLanguageCode() + "-" + tableTitle.getCode()).collect(Collectors.joining(COMMA)));
            }
        });
        return this;
    }


}
