package com.base.sbc.module.moreLanguage.strategy;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@code 描述：多语言子表数据策略上下文}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/12/4
 */
@Component
public class MoreLanguageTableContext {

    private static List<MoreLanguageTableStrategy> tableStrategyList;

    private static ThreadLocal<Map<String, String>> paramMap = new ThreadLocal<>();

    public MoreLanguageTableContext(List<MoreLanguageTableStrategy> tableStrategyList) {
        MoreLanguageTableContext.tableStrategyList = tableStrategyList;
    }

    public static PageInfo<Map<String,Object>> getTableData(Page page, StandardColumn standardColumn, Map<String, String> reflectMap){
        PageInfo<Map<String,Object>> defaultValue = new PageInfo<>();
        if (CollUtil.isEmpty(tableStrategyList)) {
            return defaultValue;
        }
        if (page.getPageNum() == 0 || page.getPageSize() == 0){
            throw new OtherException("缺少必要的分页参数");
        }
        paramMap.set(reflectMap);
        // 有顺序问题 注意!
        PageInfo<Map<String, Object>> pageInfo = tableStrategyList.stream().filter(it -> it.isThisStrategy(standardColumn))
                .findFirst().map(it -> it.findChildrenPage(page, standardColumn)).orElse(defaultValue);
        paramMap.remove();
        return pageInfo;
    }

    public static String findParam(String code){
        Map<String, String> paramMap = MoreLanguageTableContext.paramMap.get();
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        return paramMap.get(code);
    }
}
