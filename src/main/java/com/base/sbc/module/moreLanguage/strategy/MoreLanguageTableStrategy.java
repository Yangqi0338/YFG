package com.base.sbc.module.moreLanguage.strategy;


import com.base.sbc.config.common.base.Page;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * {@code 描述：多语言获取子数据的策略}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/12/4
 */
public interface MoreLanguageTableStrategy {

    boolean isThisStrategy(StandardColumn standardColumn);
    PageInfo<Map<String,Object>> findChildrenPage(Page page, StandardColumn standardColumn);

}
