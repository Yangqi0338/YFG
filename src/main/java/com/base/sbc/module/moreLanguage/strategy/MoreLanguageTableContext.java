package com.base.sbc.module.moreLanguage.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageTableTitle;
import com.base.sbc.module.moreLanguage.strategy.impl.MoreLanguageCopyLanguageTableTitle;
import com.base.sbc.module.moreLanguage.strategy.impl.MoreLanguageEndTableTitle;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

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
    private static MoreLanguageTableTitleHandler tableTitleHandlerChain = new MoreLanguageEndTableTitle() ;

    private static final ThreadLocal<Map<String, String>> paramMap = new ThreadLocal<>();

    public MoreLanguageTableContext(List<MoreLanguageTableStrategy> tableStrategyList, List<MoreLanguageTableTitleHandler> tableTitleHandlerList) {
        MoreLanguageTableContext.tableStrategyList = tableStrategyList;

        for (int i = tableTitleHandlerList.size(); i > 0; i--) {
            MoreLanguageTableTitleHandler titleHandler = tableTitleHandlerList.get(i - 1);
            titleHandler.next = MoreLanguageTableContext.tableTitleHandlerChain;
            MoreLanguageTableContext.tableTitleHandlerChain = titleHandler;
        }
    }

    public static PageInfo<Map<String,Object>> getTableData(Page page, StandardColumn standardColumn){
        PageInfo<Map<String,Object>> defaultValue = new PageInfo<>();
        if (CollUtil.isEmpty(tableStrategyList)) {
            return defaultValue;
        }
        if (page.getPageNum() == 0 || page.getPageSize() == 0){
            throw new OtherException("缺少必要的分页参数");
        }
        // 有顺序问题 注意!
        PageInfo<Map<String, Object>> pageInfo = tableStrategyList.stream().filter(it -> it.isThisStrategy(standardColumn))
                .findFirst().map(it -> it.findChildrenPage(page, standardColumn)).orElse(defaultValue);
        clear();
        return pageInfo;
    }

    public static String decorateTitleJson(StandardColumn standardColumn){
        List<MoreLanguageTableTitle> tableTitleList = JSONUtil.toList(standardColumn.getTableTitleJson(), MoreLanguageTableTitle.class);
        tableTitleHandlerChain.setTableTitleList(tableTitleList);

        tableTitleList.forEach(it-> {
            List<MoreLanguageTableTitle> newTableTitleList = JSONUtil.toList(standardColumn.getTableTitleJson(), MoreLanguageTableTitle.class);
            tableTitleHandlerChain.setTableTitleList(newTableTitleList);
            standardColumn.setTableTitleJson(JSONUtil.toJsonStr(tableTitleHandlerChain.handler(it).tableTitleList));
        });
        return standardColumn.getTableTitleJson();
    }

    public static String findParam(String code){
        Map<String, String> paramMap = MoreLanguageTableContext.paramMap.get();
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        return paramMap.get(code);
    }

    public static void setParam(Map<String, String> reflectMap){
        Map<String, String> paramMap = MoreLanguageTableContext.paramMap.get();
        if (paramMap == null) {
            paramMap = new HashMap<>(reflectMap.size());
        }
        paramMap.putAll(reflectMap);
        MoreLanguageTableContext.paramMap.set(paramMap);
    }

    public static void clear(){
        Map<String, String> paramMap = MoreLanguageTableContext.paramMap.get();
        if (paramMap != null) {
            MoreLanguageTableContext.paramMap.remove();
        }
    }

    @AllArgsConstructor
    @Getter
    public enum MoreLanguageTableTitleHandlerEnum {
        COPY("copy", MoreLanguageCopyLanguageTableTitle.class),

        ;
        private final String handlerKey;
        private final Class<? extends MoreLanguageTableTitleHandler> handlerClass;

        public static boolean checkKey(String handlerKey, Class<? extends MoreLanguageTableTitleHandler> clazz) {
            if (StrUtil.isBlank(handlerKey)) return false;
            return Arrays.stream(MoreLanguageTableTitleHandlerEnum.values()).anyMatch(it-> handlerKey.equals(it.handlerKey) && it.handlerClass == clazz);
        }
    }

    @AllArgsConstructor
    @Getter
    public enum MoreLanguageTableParamEnum {
        NO_DECORATE("noDecorate", MoreLanguageCopyLanguageTableTitle.class),
        IN_EXCEL("inExcel", MoreLanguageCopyLanguageTableTitle.class),
        OWNER_TAG_CODE("ownerTagCode", MoreLanguageCopyLanguageTableTitle.class),

        ;
        private final String paramKey;
        private final Object invoke;

        public void setBooleanParam(YesOrNoEnum value){
            MoreLanguageTableContext.setParam(MapUtil.of(this.paramKey,value.getValueStr()));
        }

        public void setParam(String value){
            MoreLanguageTableContext.setParam(MapUtil.of(this.paramKey,value));
        }

        public boolean isTrue(){
            return YesOrNoEnum.YES.getValueStr().equals(MoreLanguageTableContext.findParam(this.paramKey));
        }
        public boolean isFalse(){
            return YesOrNoEnum.NO.getValueStr().equals(MoreLanguageTableContext.findParam(this.paramKey));
        }

    }
}
