package com.base.sbc.module.moreLanguage.strategy.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableContext;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableStrategy;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
@Slf4j
@Component
public class MoreLanguageReflectTableStrategy implements MoreLanguageTableStrategy {

    @Autowired
    public MoreLanguageService moreLanguageService;

    @Override
    public boolean isThisStrategy(StandardColumn standardColumn) {
        String tableName = standardColumn.getTableName();
        return standardColumn.getModel() == StandardColumnModel.RADIO && StrUtil.isNotBlank(tableName) && !tableName.startsWith("t_");
    }

    @Override
    public PageInfo<Map<String,Object>> findChildrenPage(Page page, StandardColumn standardColumn) {
        // findStandardColumn
        String tableName = standardColumn.getTableName();
        String param = MoreLanguageTableContext.findParam(standardColumn.getTableCode());
        try {
            Class<?> clazz = moreLanguageService.getClass();
            Method[] declaredMethods = clazz.getDeclaredMethods();
            Method invokeMethod = Arrays.stream(declaredMethods).filter(it -> it.getName().equals(tableName)).findFirst().orElse(null);
            if (invokeMethod != null) {
                com.github.pagehelper.Page startPage = page.startPage();
                List<Object> returnValue = (List<Object>) invokeMethod.invoke(moreLanguageService, param);
                PageInfo<Map<String,Object>> pageInfo = startPage.toPageInfo();
                pageInfo.setList(returnValue.stream().map(BeanUtil::beanToMap).collect(Collectors.toList()));
                return pageInfo;
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("该属性%s的反射执行方法错误,方法:%s,参数:%s",standardColumn.getCode(),tableName, param);
        }
        return new PageInfo<>();
    }
}
