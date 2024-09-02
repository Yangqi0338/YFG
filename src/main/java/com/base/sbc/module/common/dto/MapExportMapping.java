package com.base.sbc.module.common.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.HAVEN_T_IMPORT_FIRST_ROW;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.INCORRECT_IMPORT_MAPPING_KEY;


@Data
public abstract class MapExportMapping {

    private String sheetName;

    private Map<Integer, String> headMap = new HashMap<>();

    private Map<Integer, String> codeMap = new HashMap<>();

    private boolean isInit = false;

    public MapExportMapping() {
        throw new UnsupportedOperationException();
    }

    public MapExportMapping(String sheetName) {
        this.sheetName = sheetName;
    }

    public void setHeadMap(Map<Integer, String> headMap) {
        headMap.forEach((key,value)-> {
            if (StrUtil.isNotBlank(value)) {
                this.headMap.put(key,value);
            }
        });
    }

    public <T> T initByFirstRow(Map<Integer, String> firstRow, Class<T> clazz) {
        // 若已经初始化就立刻返回
        if (isInit) return null;

        Map<String, String> firstRowMap = new HashMap<>(headMap.size());
        headMap.forEach((key,value)->{
            String code = findCode(firstRow).apply(value);
            codeMap.put(key,code);
            firstRowMap.put(code, firstRow.get(key));
        });

        isInit = true;

        if (clazz == null) return null;
        return BeanUtil.copyProperties(firstRowMap,clazz);
    }

    public abstract Function<String, String> findCode(Map<Integer, String> firstRow);

    public Map<String, String> buildMap(Map<Integer, String> row) {
        if (!isInit) throw new OtherException("系统错误");

        HashMap<String, String> map = new HashMap<>(row.size());
        row.forEach((key,value)->{
            map.put(codeMap.get(key), value);
        });
        return map;
    }

}

