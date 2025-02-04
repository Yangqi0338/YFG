import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.PdmApplication;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.SystemSource;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailAuditDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailOldDTO;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryRelation;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService;
import com.base.sbc.module.moreLanguage.service.StyleCountryStatusService;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.base.sbc.module.common.convert.ConvertContext.MORE_LANGUAGE_CV;

/**
 * {@code 描述：添加数据}
 * @author KC
 * @since 2024/3/18
 * @CopyRight @ 广州尚捷科技有限公司
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PdmApplication.class)
public class AddData {

    @Autowired
    private StandardColumnService standardColumnService;
    @Autowired
    private StandardColumnCountryRelationService standardColumnCountryRelationService;
    @Autowired
    private StandardColumnCountryTranslateService standardColumnCountryTranslateService;
    @Autowired
    private CountryLanguageService countryLanguageService;
    @Autowired
    private StyleCountryStatusService styleCountryStatusService;

    @Test
    public void setPdmHangTagDbV2(){
        List<StandardColumn> standardColumnList = standardColumnService.list();
        List<StandardColumn> tagStandardColumnList = standardColumnList.stream().filter(it -> it.getType().equals(StandardColumnType.TAG)).collect(Collectors.toList());
        List<StandardColumn> washingStandardColumnList = standardColumnList.stream().filter(it-> it.getType().equals(StandardColumnType.WASHING)).collect(Collectors.toList());

        List<StandardColumn> newTagStandardColumnList = new ArrayList<>();
        List<StandardColumn> newWashingTagStandardColumnList = new ArrayList<>();



        tagStandardColumnList.forEach(standardColumn -> {
            if (washingStandardColumnList.stream().noneMatch(it-> it.getName().equals(standardColumn.getName()))) {
                StandardColumn newStandardColumn = BeanUtil.copyProperties(standardColumn, StandardColumn.class);
                newStandardColumn.setId(null);
                newStandardColumn.setShowFlag(YesOrNoEnum.NO);
                newStandardColumn.setType(StandardColumnType.WASHING);
                newWashingTagStandardColumnList.add(newStandardColumn);
            }
        });
        washingStandardColumnList.forEach(standardColumn -> {
            if (tagStandardColumnList.stream().noneMatch(it-> it.getName().equals(standardColumn.getName()))) {
                StandardColumn newStandardColumn = BeanUtil.copyProperties(standardColumn, StandardColumn.class);
                newStandardColumn.setId(null);
                newStandardColumn.setShowFlag(YesOrNoEnum.NO);
                newStandardColumn.setType(StandardColumnType.TAG);
                newTagStandardColumnList.add(newStandardColumn);

            }
        });

        List<CountryLanguage> countryLanguageList = countryLanguageService.list();
        List<StandardColumnCountryRelation> relationList = standardColumnCountryRelationService.list();
        List<StandardColumnCountryTranslate> translateList = standardColumnCountryTranslateService.list();


        if (CollUtil.isNotEmpty(newTagStandardColumnList)) {
            List<StandardColumnCountryRelation> newRelationList = new ArrayList<>();
            List<StandardColumnCountryTranslate> newTranslateList = new ArrayList<>();
            countryLanguageList.stream().collect(Collectors.groupingBy(CountryLanguage::getCode)).forEach((code,sameCodeList)-> {
                List<String> washingIdList = sameCodeList.stream().filter(it -> it.getType().equals(CountryLanguageType.WASHING)).map(CountryLanguage::getId).collect(Collectors.toList());
                sameCodeList.stream().filter(it -> it.getType().equals(CountryLanguageType.TAG)).forEach(countryLanguage-> {
                    relationList.stream().filter(it-> washingIdList.contains(it.getCountryLanguageId())).filter(relation-> newTagStandardColumnList.stream().anyMatch(it-> it.getCode().equals(relation.getStandardColumnCode()))).forEach(relation-> {
                        StandardColumnCountryRelation newStandardColumnCountryRelation = BeanUtil.copyProperties(relation, StandardColumnCountryRelation.class);
                        newStandardColumnCountryRelation.updateClear();
                        newStandardColumnCountryRelation.setId(null);
                        newStandardColumnCountryRelation.setCountryLanguageId(countryLanguage.getId());
                        if (newRelationList.stream().noneMatch(it-> it.getCountryLanguageId().equals(newStandardColumnCountryRelation.getCountryLanguageId()) && it.getStandardColumnCode().equals(newStandardColumnCountryRelation.getStandardColumnCode()))) {
                            if (relationList.stream().noneMatch(it-> it.getCountryLanguageId().equals(newStandardColumnCountryRelation.getCountryLanguageId()) && it.getStandardColumnCode().equals(newStandardColumnCountryRelation.getStandardColumnCode()))) {
                                newRelationList.add(newStandardColumnCountryRelation);
                            }
                        }
                    });
                    translateList.stream().filter(it-> washingIdList.contains(it.getCountryLanguageId())).filter(translate-> newTagStandardColumnList.stream().anyMatch(it-> it.getCode().equals(translate.getTitleCode()))).forEach(translate-> {
                        StandardColumnCountryTranslate newStandardColumnCountryTranslate = BeanUtil.copyProperties(translate, StandardColumnCountryTranslate.class);
                        newStandardColumnCountryTranslate.setId(null);
                        newStandardColumnCountryTranslate.setCountryLanguageId(countryLanguage.getId());
                        if (newTranslateList.stream().noneMatch(it-> it.getCountryLanguageId().equals(newStandardColumnCountryTranslate.getCountryLanguageId()) && it.getTitleCode().equals(newStandardColumnCountryTranslate.getTitleCode()))) {
                            newTranslateList.add(newStandardColumnCountryTranslate);
                        }
                    });
                });
            });
            System.out.println();

            if (CollUtil.isNotEmpty(newRelationList)) {
                standardColumnCountryRelationService.saveOrUpdateBatch(newRelationList);
            }
            if (CollUtil.isNotEmpty(newTranslateList)) {
                standardColumnCountryTranslateService.saveOrUpdateBatch(newTranslateList);
            }
            standardColumnService.saveOrUpdateBatch(newTagStandardColumnList);
        }
        if (CollUtil.isNotEmpty(newWashingTagStandardColumnList)) {
            List<StandardColumnCountryRelation> newRelationList = new ArrayList<>();
            List<StandardColumnCountryTranslate> newTranslateList = new ArrayList<>();
            countryLanguageList.stream().collect(Collectors.groupingBy(CountryLanguage::getCode)).forEach((code,sameCodeList)-> {
                List<String> tagIdList = sameCodeList.stream().filter(it -> it.getType().equals(CountryLanguageType.TAG)).map(CountryLanguage::getId).collect(Collectors.toList());
                sameCodeList.stream().filter(it -> it.getType().equals(CountryLanguageType.WASHING)).forEach(countryLanguage-> {
                    relationList.stream().filter(it-> tagIdList.contains(it.getCountryLanguageId())).filter(relation-> newWashingTagStandardColumnList.stream().anyMatch(it-> it.getCode().equals(relation.getStandardColumnCode()))).forEach(relation-> {
                        StandardColumnCountryRelation newStandardColumnCountryRelation = BeanUtil.copyProperties(relation, StandardColumnCountryRelation.class);
                        newStandardColumnCountryRelation.updateClear();
                        newStandardColumnCountryRelation.setId(null);
                        newStandardColumnCountryRelation.setCountryLanguageId(countryLanguage.getId());
                        newRelationList.add(newStandardColumnCountryRelation);
                    });
                    if (!countryLanguage.getLanguageCode().equals("ZH")) {
                        translateList.stream().filter(it-> tagIdList.contains(it.getCountryLanguageId())).filter(translate-> newWashingTagStandardColumnList.stream().anyMatch(it-> it.getCode().equals(translate.getTitleCode()))).forEach(translate-> {
                            StandardColumnCountryTranslate newStandardColumnCountryTranslate = BeanUtil.copyProperties(translate, StandardColumnCountryTranslate.class);
                            newStandardColumnCountryTranslate.setId(null);
                            newStandardColumnCountryTranslate.setCountryLanguageId(countryLanguage.getId());
                            newTranslateList.add(newStandardColumnCountryTranslate);
                        });
                    }
                });
            });
            System.out.println();
            if (CollUtil.isNotEmpty(newRelationList)) {
                standardColumnCountryRelationService.saveOrUpdateBatch(newRelationList);
            }
            if (CollUtil.isNotEmpty(newTranslateList)) {
                standardColumnCountryTranslateService.saveOrUpdateBatch(newTranslateList);
            }
            standardColumnService.saveOrUpdateBatch(newWashingTagStandardColumnList);
        }
    }

    @Test
    public void setPdmHangTagDbV3(){
        List<CountryLanguage> languageList = countryLanguageService.list();
        List<StandardColumnCountryTranslate> tagList1 = new ArrayList<>();
        List<StandardColumnCountryTranslate> washingList1 = new ArrayList<>();
        languageList.forEach(countryLanguage -> {
            if (countryLanguage.getType() == CountryLanguageType.TAG) {
                languageList.stream().filter(it->
                        it.getSingleLanguageFlag() == YesOrNoEnum.YES &&
                                it.getLanguageCode().equals(countryLanguage.getLanguageCode()) &&
                                it.getType() == CountryLanguageType.WASHING
                ).findFirst().ifPresent(wasingCountryLanguage-> {
                    List<StandardColumnCountryTranslate> washingList = standardColumnCountryTranslateService.list(new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                            .in(StandardColumnCountryTranslate::getCountryLanguageId, wasingCountryLanguage.getId())
                            .eq(StandardColumnCountryTranslate::getTitleCode, "XM00")
                    );
                    washingList.forEach(it-> {
                        it.setId(null);
                        it.updateClear();
                        it.setCountryLanguageId(countryLanguage.getId());
                        it.setTitleCode("DP00");
                    });
                    washingList1.addAll(washingList);
                });
            }
            if (countryLanguage.getType() == CountryLanguageType.WASHING) {
                languageList.stream().filter(it->
                        it.getSingleLanguageFlag() == YesOrNoEnum.YES &&
                                it.getLanguageCode().equals(countryLanguage.getLanguageCode()) &&
                                it.getType() == CountryLanguageType.TAG
                ).findFirst().ifPresent(wasingCountryLanguage-> {
                    List<StandardColumnCountryTranslate> tagList = standardColumnCountryTranslateService.list(new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                            .in(StandardColumnCountryTranslate::getCountryLanguageId, wasingCountryLanguage.getId())
                            .eq(StandardColumnCountryTranslate::getTitleCode, "DP00")
                    );
                    tagList.forEach(it-> {
                        it.setId(null);
                        it.updateClear();
                        it.setCountryLanguageId(countryLanguage.getId());
                        it.setTitleCode("XM00");
                    });
                    tagList1.addAll(tagList);
                });
            }
        });
        System.out.println();

        standardColumnCountryTranslateService.saveOrUpdateBatch(tagList1);
        standardColumnCountryTranslateService.saveOrUpdateBatch(washingList1);
    }

    @Test
    public void setPdmHangTagDbV4(){
        List<StyleCountryStatus> countryStatusList = styleCountryStatusService.list();
        List<StyleCountryStatus> countryStatusList1 = new ArrayList<>();
        countryStatusList.forEach(it-> {
            String checkDetailJson = it.getCheckDetailJson();
            List<MoreLanguageStatusCheckDetailOldDTO> list = JSONUtil.toList(checkDetailJson, MoreLanguageStatusCheckDetailOldDTO.class);
            list.stream().collect(Collectors.groupingBy(MoreLanguageStatusCheckDetailOldDTO::getType)).forEach((type,oldDTOList)-> {
                StyleCountryStatus newStatus = it;
                if (type.equals(CountryLanguageType.WASHING.getCode())) {
                    newStatus = MORE_LANGUAGE_CV.copyMyself(it);
                    newStatus.setId(null);
                    newStatus.updateClear();
                }
                newStatus.setType(CountryLanguageType.findByCode(type));
                List<MoreLanguageStatusCheckDetailDTO> checkDetailDTOS = new ArrayList<>();
                oldDTOList.stream().collect(Collectors.groupingBy(MoreLanguageStatusCheckDetailOldDTO::getLanguageCode)).forEach((languageCode,sameCodeList)-> {
                    MoreLanguageStatusCheckDetailDTO checkDetailDTO = new MoreLanguageStatusCheckDetailDTO();
                    checkDetailDTO.setLanguageCode(languageCode);

                    List<MoreLanguageStatusCheckDetailAuditDTO> auditDTOList = new ArrayList<>();
                    sameCodeList.get(0).getStandardColumnCodeList().forEach(standardColumnCode-> {
                        MoreLanguageStatusCheckDetailAuditDTO auditDTO = new MoreLanguageStatusCheckDetailAuditDTO();
                        auditDTO.setStandardColumnCode(standardColumnCode);
                        auditDTO.setContent("");
                        auditDTO.setSource("");
                        auditDTO.setStatus(YesOrNoEnum.YES.getValueStr());
                        auditDTOList.add(auditDTO);
                    });
                    checkDetailDTO.setAuditList(auditDTOList);
                    checkDetailDTOS.add(checkDetailDTO);
                });
                newStatus.setCheckDetailJson(JSONUtil.toJsonStr(checkDetailDTOS));
                countryStatusList1.add(newStatus);
            });
        });

        styleCountryStatusService.saveOrUpdateBatch(countryStatusList1);
    }

    @Test
    public void setPdmHangTagDbV5(){
        List<StandardColumnCountryTranslate> list = standardColumnCountryTranslateService.list(new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                .eq(StandardColumnCountryTranslate::getCountryLanguageId, "1747439257242419202"
                ).eq(StandardColumnCountryTranslate::getTitleCode, "DP13"));
        String s = "1747439257368248321,1750450374088609794";
        List<StandardColumnCountryTranslate> list1 = new ArrayList<>();
        for (String newId : s.split(",")) {
            list.forEach(it-> {
                StandardColumnCountryTranslate translate = BeanUtil.copyProperties(it, StandardColumnCountryTranslate.class);
                translate.updateClear();
                translate.setId(null);
                translate.setTitleCode("XM11");
                translate.setCountryLanguageId(newId);
                list1.add(translate);
            });
        }
        System.out.println();
        if (CollUtil.isNotEmpty(list1)) {
            standardColumnCountryTranslateService.saveOrUpdateBatch(list1);
        }

        List<StandardColumnCountryTranslate> list2 = standardColumnCountryTranslateService.list(new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                .eq(StandardColumnCountryTranslate::getCountryLanguageId, "1768897160130129922"
                ).eq(StandardColumnCountryTranslate::getTitleCode, "DP13"));
        String s1 = "1747433462421372929,1750450235617857538";
        List<StandardColumnCountryTranslate> list3 = new ArrayList<>();
        for (String newId : s1.split(",")) {
            list2.forEach(it-> {
                StandardColumnCountryTranslate translate = BeanUtil.copyProperties(it, StandardColumnCountryTranslate.class);
                translate.updateClear();
                translate.setId(null);
                translate.setTitleCode("XM11");
                translate.setCountryLanguageId(newId);
                list3.add(translate);
            });
        }
        System.out.println();
        if (CollUtil.isNotEmpty(list3)) {
            standardColumnCountryTranslateService.saveOrUpdateBatch(list3);
        }
    }
}
