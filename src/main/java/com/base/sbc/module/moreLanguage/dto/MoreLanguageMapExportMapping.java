package com.base.sbc.module.moreLanguage.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.dto.MapExportMapping;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.HAVEN_T_IMPORT_FIRST_ROW;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.INCORRECT_IMPORT_MAPPING_KEY;


@EqualsAndHashCode(callSuper = true)
@Data
public class MoreLanguageMapExportMapping extends MapExportMapping {

    private Map<String, String> mappingJson;

    private List<StandardColumnCountryTranslate> sourceData = new ArrayList<>();

    private StandardColumnCountryTranslate baseSourceData;

    private List<CountryLanguageDto> countryLanguageList;

    private CountryLanguageType type;

    public MoreLanguageMapExportMapping(String sheetName) {
        super(sheetName);
    }

    public Map<String, String> initMappingJson(Map<Integer, String> firstRow){
        if (MapUtil.isNotEmpty(mappingJson)) return mappingJson;
        // 获取最后一列,即关键映射列
        Integer lastCell = firstRow.keySet().stream().max(Comparator.naturalOrder()).get();

        // 将关键映射列转为map
        Map<String,String> keyNameMap = JSONUtil.toBean(firstRow.get(lastCell), HashMap.class);
        if (keyNameMap.isEmpty()) {
            throw new OtherException(MoreLanguageProperties.getMsg(INCORRECT_IMPORT_MAPPING_KEY, this.getSheetName()));
        }
        mappingJson = keyNameMap;
        return mappingJson;
    }

    @Override
    public Function<String, String> findCode(Map<Integer, String> firstRow) {
        Map<String, String> map = initMappingJson(firstRow);
        return (value)-> {
            // 映射列和表头必须一一对应上,没对应上就是有人修改
            if (!map.containsKey(value)) throw new OtherException(MoreLanguageProperties.getMsg(HAVEN_T_IMPORT_FIRST_ROW, this.getSheetName()));
            return map.get(value);
        };
    }

    public List<CountryLanguageDto> getTotalCountryLanguageList() {
        return countryLanguageList;
    }

    public List<CountryLanguageDto> getCountryLanguageList() {
        if (CollUtil.isEmpty(countryLanguageList)) return new ArrayList<>();
        return countryLanguageList.stream().filter(it-> it.getType().equals(type)).collect(Collectors.toList());
    }
}

