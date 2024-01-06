package com.base.sbc.module.moreLanguage.strategy.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageTableTitle;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableStrategy;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableTitleHandler;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public MoreLanguageTableTitleHandler handler(MoreLanguageTableTitle tableTitle) {
        if (needHandler(tableTitle) && tableTitle.hiddenRemove()) {
            tableTitleList.remove(tableTitle);
        }
        return this.next.setTableTitleList(tableTitleList).handler(tableTitle);
    }
}
