package com.base.sbc.module.moreLanguage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.mapper.HangTagMapper;
import com.base.sbc.module.hangtag.vo.HangTagVO;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.LanguageSaveDto;
import com.base.sbc.module.moreLanguage.dto.StyleCountryPrintRecordDto;
import com.base.sbc.module.moreLanguage.dto.TypeLanguageDto;
import com.base.sbc.module.moreLanguage.entity.StyleCountryPrintRecord;
import com.base.sbc.module.moreLanguage.mapper.StyleCountryPrintRecordMapper;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StyleCountryPrintRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@code 描述：}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/12/28
 */
@Service
public class StyleCountryPrintRecordServiceImpl extends BaseServiceImpl<StyleCountryPrintRecordMapper, StyleCountryPrintRecord> implements StyleCountryPrintRecordService {

    @Autowired
    private HangTagMapper hangTagMapper;

    @Autowired
    private CountryLanguageService countryLanguageService;

    @Override
    public StyleCountryPrintRecordDto findPrintRecordByStyleNo(HangTagMoreLanguageDTO languageDTO) {
        String bulkStyleNo = languageDTO.getBulkStyleNo();
//        HangTagVO hangTagVO = hangTagMapper.getDetailsByBulkStyleNo(Collections.singletonList(bulkStyleNo), languageDTO.getUserCompany(), languageDTO.getSelectType())
//                .stream().findFirst().orElse(null);
//        if (hangTagVO == null) {
//            throw new OtherException("大货款号:" + bulkStyleNo + " 不存在");
//        }
        CountryQueryDto countryQueryDto = BeanUtil.copyProperties(languageDTO,CountryQueryDto.class);
        List<CountryLanguageDto> countryLanguageDtoList = countryLanguageService.listQuery(countryQueryDto);
        if (CollectionUtil.isEmpty(countryLanguageDtoList)) {
            throw new OtherException("无效的国家或语言");
        }
        CountryLanguageDto baseCountryLanguageDto = countryLanguageDtoList.get(0);

        StyleCountryPrintRecordDto recordDto = new StyleCountryPrintRecordDto();
        recordDto.setBulkStyleNo(bulkStyleNo);
        recordDto.setCode(baseCountryLanguageDto.getCode());
        recordDto.setCountryCode(baseCountryLanguageDto.getCountryCode());
        recordDto.setCountryName(baseCountryLanguageDto.getCountryName());

        List<TypeLanguageDto> typeLanguageDtoList = new ArrayList<>();
        countryLanguageDtoList.stream().collect(Collectors.groupingBy(CountryLanguageDto::getType)).forEach((type,sameTypeList)-> {
            TypeLanguageDto typeLanguageDto = new TypeLanguageDto();
            typeLanguageDto.setType(type);
            typeLanguageDto.setLanguageList(BeanUtil.copyToList(sameTypeList, LanguageSaveDto.class));
            typeLanguageDtoList.add(typeLanguageDto);
        });
        recordDto.setTypeLanguageDtoList(typeLanguageDtoList);

        return recordDto;
    }
}
