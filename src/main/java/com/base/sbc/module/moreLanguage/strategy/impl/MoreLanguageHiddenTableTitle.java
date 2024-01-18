package com.base.sbc.module.moreLanguage.strategy.impl;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageTableTitle;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableTitleHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * {@code 描述：多语言获取子数据的策略}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/12/4
 */
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
public class MoreLanguageHiddenTableTitle extends MoreLanguageTableTitleHandler {

    @Override
    public boolean needHandler(MoreLanguageTableTitle tableTitle) {
        return StrUtil.isNotBlank(tableTitle.getHidden()) && tableTitle.hiddenRemove();
    }

    @Override
    public MoreLanguageTableTitleHandler doHandler(MoreLanguageTableTitle tableTitle) {
        tableTitleList.remove(tableTitle);
        return this;
    }
}
