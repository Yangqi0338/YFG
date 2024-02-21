package com.base.sbc.module.moreLanguage.strategy.impl;

import com.base.sbc.module.moreLanguage.dto.MoreLanguageTableTitle;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableTitleHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * {@code 描述：多语言获取子数据的策略}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/12/4
 */
@Order
@Component
public class MoreLanguageEndTableTitle extends MoreLanguageTableTitleHandler {
    @Override
    public boolean needHandler(MoreLanguageTableTitle tableTitle) {
        // 仅作为结束节点
        return false;
    }
}
