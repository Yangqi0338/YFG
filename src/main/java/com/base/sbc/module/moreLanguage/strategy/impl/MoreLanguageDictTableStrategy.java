package com.base.sbc.module.moreLanguage.strategy.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableStrategy;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
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
@Component
public class MoreLanguageDictTableStrategy implements MoreLanguageTableStrategy {

    @Autowired
    public CcmFeignService ccmFeignService;

    @Override
    public boolean isThisStrategy(StandardColumn standardColumn) {
        return standardColumn.getModel() == StandardColumnModel.DICT && StrUtil.isNotBlank(standardColumn.getTableCode());
    }

    @Override
    public PageInfo<Map<String,Object>> findChildrenPage(Page page, StandardColumn standardColumn) {
        // 没有分页,手动分页,后续再联调ccm做
        List<BasicBaseDict> dictInfoList = ccmFeignService.getDictInfoToList(standardColumn.getTableCode());
        dictInfoList = dictInfoList.stream().collect(Collectors.groupingBy(BasicBaseDict::getValue))
                .values().stream().map(it-> it.get(0))
                .sorted(Comparator.comparing(BasicBaseDict::getId))
                .collect(Collectors.toList());

        int pageSize = page.getPageSize();
        int pageNum = page.getPageNum();
        int maxSize = dictInfoList.size();
        long maxPageSize = (maxSize / pageSize);
        long currentPageSize = Math.max(0, pageNum - 1);
        long formIndex = Math.min(maxPageSize, currentPageSize) * pageSize;
        long toIndex = Math.min(formIndex + pageSize, maxSize);

        com.github.pagehelper.Page startPage = new com.github.pagehelper.Page<>(pageNum, pageSize);
        PageInfo<Map<String,Object>> pageInfo = startPage.toPageInfo();
        pageInfo.setList(dictInfoList.subList((int) formIndex, (int) toIndex).stream().map(BeanUtil::beanToMap).collect(Collectors.toList()));
        pageInfo.setTotal(maxSize);
        return pageInfo;
    }
}
