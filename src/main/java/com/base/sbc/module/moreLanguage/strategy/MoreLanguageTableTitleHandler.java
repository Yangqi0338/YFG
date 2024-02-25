package com.base.sbc.module.moreLanguage.strategy;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageTableTitle;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableContext.MoreLanguageTableTitleHandlerEnum;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * {@code 描述：多语言获取子数据的策略}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/12/4
 */

public abstract class MoreLanguageTableTitleHandler {

    public MoreLanguageTableTitleHandler next;

    protected CopyOnWriteArrayList<MoreLanguageTableTitle> tableTitleList;

    public MoreLanguageTableTitleHandler setTableTitleList(List<MoreLanguageTableTitle> tableTitleList) {
        this.tableTitleList = new CopyOnWriteArrayList<>(tableTitleList);
        return this;
    }

    public boolean needHandler(MoreLanguageTableTitle tableTitle) {
        return MoreLanguageTableTitleHandlerEnum.checkKey(tableTitle.getHandler(), this.getClass());
    }

    public MoreLanguageTableTitleHandler handler(MoreLanguageTableTitle tableTitle) {
        if (needHandler(tableTitle)) {
            doHandler(tableTitle);
            String handler = tableTitle.getHandler();
            if (StrUtil.isNotBlank(handler)) {
                MoreLanguageTableTitleHandlerEnum handlerEnum = MoreLanguageTableTitleHandlerEnum.findHandler(handler, this.getClass());
                List<String> handlerKeyList = CollUtil.toList(handler.split(COMMA));
                handlerKeyList.remove(handlerEnum.getHandlerKey());
                tableTitle.setHandler(String.join(COMMA,handlerKeyList));
            }
        }
        if (this.next == null) return this;
        return this.next.setTableTitleList(tableTitleList).handler(tableTitle);
    }
    public MoreLanguageTableTitleHandler doHandler(MoreLanguageTableTitle tableTitle) {
        return this;
    };

}
