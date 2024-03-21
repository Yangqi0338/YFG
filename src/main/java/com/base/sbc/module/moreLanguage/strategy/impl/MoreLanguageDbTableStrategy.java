package com.base.sbc.module.moreLanguage.strategy.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.config.constant.BusinessProperties;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageTableTitle;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableStrategy;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.config.common.base.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.config.constant.Constants.SQL_RIGHT_FILL;

/**
 * {@code 描述：多语言获取子数据的策略}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/12/4
 */
@Component
public class MoreLanguageDbTableStrategy implements MoreLanguageTableStrategy {

    @Autowired
    public MoreLanguageService moreLanguageService;

    @Override
    public boolean isThisStrategy(StandardColumn standardColumn) {
        String tableName = standardColumn.getTableName();
        // tableName属性是否有值
        return standardColumn.getModel() == StandardColumnModel.RADIO && StrUtil.isNotBlank(tableName) && tableName.startsWith(BusinessProperties.tablePrefix);
    }

    @Override
    public PageInfo<Map<String,Object>> findChildrenPage(Page page, StandardColumn standardColumn) {
        // 走一遍redis
        // 解析表头
        String json = standardColumn.getTableTitleJson();
        List<MoreLanguageTableTitle> tableTitleList = JSONUtil.toList(json, MoreLanguageTableTitle.class);
        String tableFields = tableTitleList.stream().map(it-> StrUtil.toUnderlineCase(it.getCode())).collect(Collectors.joining(COMMA));
        com.github.pagehelper.Page<Map<String,Object>> startPage = page.startPage();
        // 直接分页查询动态数据库
        moreLanguageService.listAllByTable(tableFields, standardColumn.getTableName(), Opt.ofNullable(standardColumn.getTableCode()).orElse(SQL_RIGHT_FILL));
        return startPage.toPageInfo();
    }


}
