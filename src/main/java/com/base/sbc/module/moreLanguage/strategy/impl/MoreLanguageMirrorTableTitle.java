package com.base.sbc.module.moreLanguage.strategy.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageTableTitle;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableContext;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableTitleHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
public class MoreLanguageMirrorTableTitle extends MoreLanguageTableTitleHandler {

    @Override
    public boolean needHandler(MoreLanguageTableTitle tableTitle) {
//        if (MoreLanguageTableContext.MoreLanguageTableParamEnum.NO_DECORATE.isTrue()) return false;
        return MoreLanguageTableContext.MoreLanguageTableTitleHandlerEnum.checkKey(tableTitle.getHandler(), this.getClass());
    }

    @Override
    public MoreLanguageTableTitleHandler handler(MoreLanguageTableTitle tableTitle) {
        if (needHandler(tableTitle)) {
            String param = MoreLanguageTableContext.findParam(tableTitle.getHandler());
            List<CountryLanguage> countryLanguageList = JSONUtil.toList(param, CountryLanguage.class);
            if (CollectionUtil.isNotEmpty(countryLanguageList))  {
                tableTitle.setHandler(tableTitle.getHandler().replace(MoreLanguageTableContext.MoreLanguageTableTitleHandlerEnum.COPY.getHandlerKey(),""));
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
                    countryLanguageTableTitle.setSource(tableTitle);
                    tableTitleList.add(baseIndex+i, countryLanguageTableTitle);
                }
            }
        }
        return this.next.setTableTitleList(tableTitleList).handler(tableTitle);
    }


}
