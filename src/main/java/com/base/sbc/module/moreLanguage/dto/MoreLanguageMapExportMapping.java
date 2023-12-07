package com.base.sbc.module.moreLanguage.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class MoreLanguageMapExportMapping {

    private String sheetName;

    private Map<Integer, String> headMap = new HashMap<>();

    private Map<Integer, String> codeMap = new HashMap<>();
    private Map<String, String> mappingJson;

    private List<StandardColumnCountryTranslate> sourceData;

    private StandardColumnCountryTranslate baseSourceData;

    private boolean isInit = false;

    public MoreLanguageMapExportMapping(String sheetName) {
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
        if (isInit) return null;

        Map<String, String> firstRowMap = new HashMap<>();

        Integer lastCell = firstRow.keySet().stream().max(Comparator.naturalOrder()).get();

        Map<String,String> keyNameMap = JSONUtil.toBean(firstRow.get(lastCell), HashMap.class);
        mappingJson = keyNameMap;
        headMap.forEach((key,value)->{
            if (!keyNameMap.containsKey(value)) throw new OtherException("312312");
            String code = keyNameMap.get(value);
            codeMap.put(key,code);
            firstRowMap.put(code, firstRow.get(key));
        });

        isInit = true;

        return BeanUtil.copyProperties(firstRowMap,clazz);
    }

    public Map<String, String> buildMap(Map<Integer, String> row) {
        if (!isInit) throw new OtherException("2132132");

        HashMap<String, String> map = new HashMap<>();
        row.forEach((key,value)->{
            map.put(codeMap.get(key), value);
        });

        return map;
    }
}

