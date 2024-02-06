package com.base.sbc.module.moreLanguage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.common.convert.ConvertContext;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageSystemDTO;
import com.base.sbc.module.hangtag.mapper.HangTagMapper;
import com.base.sbc.module.hangtag.vo.HangTagVO;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.LanguageSaveDto;
import com.base.sbc.module.moreLanguage.dto.StyleCountryPrintRecordDto;
import com.base.sbc.module.moreLanguage.dto.TypeLanguageDto;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StyleCountryPrintRecord;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.base.sbc.module.moreLanguage.mapper.StyleCountryPrintRecordMapper;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StyleCountryPrintRecordService;
import com.base.sbc.module.moreLanguage.service.StyleCountryStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.base.sbc.module.common.convert.ConvertContext.OPEN_CV;

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
    private CountryLanguageService countryLanguageService;

    @Autowired
    private StyleCountryStatusService styleCountryStatusService;

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
        String code = baseCountryLanguageDto.getCode();

        StyleCountryPrintRecordDto recordDto = new StyleCountryPrintRecordDto();
        recordDto.setBulkStyleNo(bulkStyleNo);
        recordDto.setCode(code);
        recordDto.setCountryCode(baseCountryLanguageDto.getCountryCode());
        recordDto.setCountryName(baseCountryLanguageDto.getCountryName());

        List<TypeLanguageDto> typeLanguageDtoList = new ArrayList<>();

        List<String> countryLanguageIdList = countryLanguageDtoList.stream().map(CountryLanguageDto::getId).collect(Collectors.toList());
        List<StyleCountryPrintRecord> printRecordList = this.list(new LambdaQueryWrapper<StyleCountryPrintRecord>()
                .eq(StyleCountryPrintRecord::getBulkStyleNo, bulkStyleNo)
                .in(StyleCountryPrintRecord::getCountryLanguageId, countryLanguageIdList)
        );

        countryLanguageDtoList.stream().sorted(CommonUtils.comparing(CountryLanguage::getType))
                .collect(CommonUtils.groupingBy(CountryLanguageDto::getType)).forEach((type, sameTypeList)-> {
            TypeLanguageDto typeLanguageDto = new TypeLanguageDto();
            typeLanguageDto.setType(type);
            typeLanguageDto.setLanguageList( sameTypeList.stream().map(languageSaveDto -> {
                LanguageSaveDto saveDto = ConvertContext.MORE_LANGUAGE_CV.copy2Save(languageSaveDto);
                Date printTime = printRecordList.stream().filter(it -> it.getCountryLanguageId().equals(languageSaveDto.getId()))
                        .findFirst().map(StyleCountryPrintRecord::getUpdateDate).orElse(null);
                saveDto.setPrintTime(printTime);
                return saveDto;
            }).collect(Collectors.toList()));
            typeLanguageDtoList.add(typeLanguageDto);
        });
        recordDto.setTypeLanguageDtoList(typeLanguageDtoList);

        // 获取状态
        StyleCountryStatusEnum status = styleCountryStatusService.findOneField(new LambdaQueryWrapper<StyleCountryStatus>()
                .eq(StyleCountryStatus::getBulkStyleNo, bulkStyleNo)
                .eq(StyleCountryStatus::getCountryCode, code), StyleCountryStatus::getStatus
        );
        recordDto.setStatusCode(Opt.ofNullable(status).orElse(StyleCountryStatusEnum.UNCHECK));

        return recordDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePrintRecord(HangTagMoreLanguageSystemDTO languageDTO) {
        String bulkStyleNo = languageDTO.getBulkStyleNo();
        CountryQueryDto countryQueryDto = OPEN_CV.copy2CountryQuery(languageDTO);
        List<CountryLanguageDto> countryLanguageDtoList = countryLanguageService.listQuery(countryQueryDto);
        if (CollectionUtil.isEmpty(countryLanguageDtoList)) {
            log.error("无效的国家或语言");
            return;
        }

        List<String> countryLanguageIdList = countryLanguageDtoList.stream().map(CountryLanguageDto::getId).collect(Collectors.toList());
        List<StyleCountryPrintRecord> oldPrintRecordList = this.list(new LambdaQueryWrapper<StyleCountryPrintRecord>()
                .eq(StyleCountryPrintRecord::getBulkStyleNo, bulkStyleNo)
                .in(StyleCountryPrintRecord::getCountryLanguageId, countryLanguageIdList)
        );
        List<StyleCountryPrintRecord> recordList = countryLanguageDtoList.stream().map(countryLanguageDto -> {
            String countryLanguageId = countryLanguageDto.getId();
            StyleCountryPrintRecord countryPrintRecord = oldPrintRecordList.stream()
                    .filter(it -> it.getCountryLanguageId().equals(countryLanguageId))
                    .findFirst().orElse(new StyleCountryPrintRecord());
            countryPrintRecord.setBulkStyleNo(bulkStyleNo);
            countryPrintRecord.setCountryLanguageId(countryLanguageId);
            return countryPrintRecord;
        }).collect(Collectors.toList());

        this.saveOrUpdateBatch(recordList);
    }
}
