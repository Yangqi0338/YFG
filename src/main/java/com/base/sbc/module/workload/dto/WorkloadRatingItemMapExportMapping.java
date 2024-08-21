package com.base.sbc.module.workload.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.dto.MapExportMapping;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.workload.entity.WorkloadRatingItem;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigQO;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.HAVEN_T_IMPORT_FIRST_ROW;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.INCORRECT_IMPORT_MAPPING_KEY;
import static com.base.sbc.module.common.convert.ConvertContext.WORKLOAD_CV;


@EqualsAndHashCode(callSuper = true)
@Data
public class WorkloadRatingItemMapExportMapping extends MapExportMapping {

    private Map<String, String> mappingJson;

    private WorkloadRatingConfigVO configVO;

    private List<WorkloadRatingItem> sourceData = new ArrayList<>();

    public WorkloadRatingItemMapExportMapping(String sheetName) {
        super(sheetName);
    }

    @Override
    public Function<String, String> findCode(Map<Integer, String> firstRow) {
        if (MapUtil.isEmpty(mappingJson)) {
            List<WorkloadRatingTitleFieldDTO> configTitleFieldList = configVO.getConfigTitleFieldList();
            mappingJson = new HashMap<>(this.getHeadMap().size());
            firstRow.forEach((key,value)-> {
                String code = configTitleFieldList.stream().filter(it -> it.getName().equals(value))
                        .map(WorkloadRatingTitleFieldDTO::getCode).findFirst().orElseThrow(() -> new OtherException("不可知的数据列"));
                mappingJson.put(value,code);
            });
        }
        return (value)-> mappingJson.get(value);
    }
}

