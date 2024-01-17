package com.base.sbc.module.moreLanguage.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
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

    private List<StandardColumnCountryTranslate> sourceData = new ArrayList<>();

    private StandardColumnCountryTranslate baseSourceData;

    private List<CountryLanguageDto> countryLanguageList;
    private List<CountryLanguageDto> singleLanguageList;
    private CountryLanguageType type;

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

        Map<String, String> firstRowMap = new HashMap<>(headMap.size());

        Integer lastCell = firstRow.keySet().stream().max(Comparator.naturalOrder()).get();

        Map<String,String> keyNameMap = JSONUtil.toBean(firstRow.get(lastCell), HashMap.class);
        if (keyNameMap.isEmpty()) {
            throw new OtherException(sheetName + "隐藏列的关键映射值被修改,请重新导入");
        }
        mappingJson = keyNameMap;
        headMap.forEach((key,value)->{
            if (!keyNameMap.containsKey(value)) throw new OtherException("请勿删除导出模板" + sheetName + "的首行数据,请重新导出一份");
            String code = keyNameMap.get(value);
            codeMap.put(key,code);
            firstRowMap.put(code, firstRow.get(key));
        });

        isInit = true;

        return BeanUtil.copyProperties(firstRowMap,clazz);
    }

    public Map<String, String> buildMap(Map<Integer, String> row) {
        if (!isInit) throw new OtherException("系统错误");

        HashMap<String, String> map = new HashMap<>(row.size());
        row.forEach((key,value)->{
            map.put(codeMap.get(key), value);
        });

        return map;
    }
}

