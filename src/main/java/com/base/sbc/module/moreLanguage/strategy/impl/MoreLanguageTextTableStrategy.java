package com.base.sbc.module.moreLanguage.strategy.impl;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableStrategy;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@code 描述：多语言获取子数据的策略}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/12/4
 */
@Component
public class MoreLanguageTextTableStrategy implements MoreLanguageTableStrategy {
    @Override
    public boolean isThisStrategy(StandardColumn standardColumn) {
        return standardColumn.getModel() == StandardColumnModel.TEXT;
    }

    @Override
    public PageInfo<Map<String,Object>> findChildrenPage(Page page, StandardColumn standardColumn) {
        return new PageInfo<>();
    }
}
