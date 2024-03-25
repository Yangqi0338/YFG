package com.base.sbc.module.moreLanguage.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.config.constant.MoreLanguageProperties;
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

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.HAVEN_T_IMPORT_FIRST_ROW;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.INCORRECT_IMPORT_MAPPING_KEY;


@Data
public class MoreLanguageMapExportMapping {

    private String sheetName;

    private Map<Integer, String> headMap = new HashMap<>();

    private Map<Integer, String> codeMap = new HashMap<>();
    private Map<String, String> mappingJson;

    private List<StandardColumnCountryTranslate> sourceData = new ArrayList<>();

    private StandardColumnCountryTranslate baseSourceData;

    private List<CountryLanguageDto> countryLanguageList;

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
        // 若已经初始化就立刻返回
        if (isInit) return null;

        Map<String, String> firstRowMap = new HashMap<>(headMap.size());
        // 获取最后一列,即关键映射列
        Integer lastCell = firstRow.keySet().stream().max(Comparator.naturalOrder()).get();

        // 将关键映射列转为map
        Map<String,String> keyNameMap = JSONUtil.toBean(firstRow.get(lastCell), HashMap.class);
        if (keyNameMap.isEmpty()) {
            throw new OtherException(MoreLanguageProperties.getMsg(INCORRECT_IMPORT_MAPPING_KEY, sheetName));
        }
        mappingJson = keyNameMap;
        headMap.forEach((key,value)->{
            // 映射列和表头必须一一对应上,没对应上就是有人修改
            if (!keyNameMap.containsKey(value)) throw new OtherException(MoreLanguageProperties.getMsg(HAVEN_T_IMPORT_FIRST_ROW, sheetName));
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

