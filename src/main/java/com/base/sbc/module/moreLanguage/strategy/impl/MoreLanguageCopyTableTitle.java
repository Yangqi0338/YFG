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

/**
 * {@code 描述：多语言获取子数据的策略}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/12/4
 */
@Order(0)
@Component
public class MoreLanguageCopyTableTitle extends MoreLanguageTableTitleHandler {
    private List<CountryLanguage> countryLanguageList = new ArrayList<>();

    @Override
    public boolean needHandler(MoreLanguageTableTitle tableTitle) {
        String param = MoreLanguageTableContext.findParam(tableTitle.getHandler());
        countryLanguageList = JSONUtil.toList(param, CountryLanguage.class);
//        if (MoreLanguageTableContext.MoreLanguageTableParamEnum.NO_DECORATE.isTrue()) return false;
        return super.needHandler(tableTitle) && CollectionUtil.isNotEmpty(countryLanguageList);
    }

    @Override
    public MoreLanguageTableTitleHandler doHandler(MoreLanguageTableTitle tableTitle) {
        int baseIndex = 0;
        for (int i = 0; i < tableTitleList.size(); i++) {
            MoreLanguageTableTitle it = tableTitleList.get(i);
            if (tableTitle.getCode().equals(it.getCode())) {
                baseIndex = i;
                tableTitleList.remove(i);
                break;
            }
        }

        for (int i = 0; i < countryLanguageList.size(); i++) {
            CountryLanguage countryLanguage = countryLanguageList.get(i);
            MoreLanguageTableTitle countryLanguageTableTitle = BeanUtil.copyProperties(tableTitle, MoreLanguageTableTitle.class);
            countryLanguageTableTitle.setText(countryLanguage.getLanguageName() + tableTitle.getText());
            countryLanguageTableTitle.setCode(countryLanguage.getLanguageCode() + "-" + tableTitle.getCode());
            tableTitleList.add(baseIndex+i, countryLanguageTableTitle);
        }
        return this;
    }


}
