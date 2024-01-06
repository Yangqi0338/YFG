package com.base.sbc.module.moreLanguage.strategy;


import com.base.sbc.module.moreLanguage.dto.MoreLanguageTableTitle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
        return true;
    }

    public abstract MoreLanguageTableTitleHandler handler(MoreLanguageTableTitle tableTitle);

}
