package com.base.sbc.module.moreLanguage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.HangTagStatusEnum;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.hangtag.dto.HangTagSearchDTO;
import com.base.sbc.module.hangtag.dto.HangTagUpdateStatusDTO;
import com.base.sbc.module.hangtag.mapper.HangTagMapper;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.hangtag.vo.HangTagListVO;
import com.base.sbc.module.moreLanguage.dto.CountryDTO;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelResultDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusQueryDto;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryRelation;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.base.sbc.module.moreLanguage.mapper.StyleCountryStatusMapper;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService;
import com.base.sbc.module.moreLanguage.service.StyleCountryStatusService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.module.common.convert.ConvertContext.BASE_CV;
import static com.base.sbc.module.common.convert.ConvertContext.MORE_LANGUAGE_CV;

/**
 * {@code 描述：}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/12/28
 */
@Service
public class StyleCountryStatusServiceImpl extends BaseServiceImpl<StyleCountryStatusMapper, StyleCountryStatus> implements StyleCountryStatusService {

    @Autowired
    private CountryLanguageService countryLanguageService;

    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Autowired
    private HangTagMapper hangTagMapper;

    @Autowired
    private HangTagService hangTagService;

    @Autowired
    private UserUtils userUtils;

    private final static SFunction<StyleCountryStatus, String> bulkStyleNoFunc = StyleCountryStatus::getBulkStyleNo;

    @Override
    public List<MoreLanguageStatusExcelDTO> exportExcel() {
        MoreLanguageStatusQueryDto queryDto = new MoreLanguageStatusQueryDto();
        queryDto.reset2QueryList();
        return MORE_LANGUAGE_CV.copyList2ExcelDTO(listQuery(queryDto).getList());
    }

    private List<HangTagListVO> findHangTagList(HangTagSearchDTO searchDTO){
        String authSql = dataPermissionsService
                .getDataPermissionsSql(DataPermissionsBusinessTypeEnum.hangTagList.getK(), "tsd.", null, false);

        searchDTO.setCompanyCode(userUtils.getCompanyCode());
        return hangTagMapper.queryList(searchDTO, authSql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MoreLanguageStatusExcelResultDTO> importExcel(List<MoreLanguageStatusExcelDTO> dataList) {
        List<MoreLanguageStatusExcelResultDTO> result = new ArrayList<>();
        // 检查数据库条数是否一样
        List<String> bulkStyleNoList = dataList.stream().map(MoreLanguageStatusExcelDTO::getBulkStyleNo).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(bulkStyleNoList)) return result;

        long count = getBaseMapper().countByBulkStyleNo(bulkStyleNoList);
        if (count == bulkStyleNoList.size()) return result;
        else {
            List<String> dbBulkStyleNoList = this.listOneField(new LambdaQueryWrapper<StyleCountryStatus>()
                    .in(bulkStyleNoFunc,bulkStyleNoList)
                    .groupBy(bulkStyleNoFunc), bulkStyleNoFunc);
            result.addAll(MORE_LANGUAGE_CV.copyList2ResultDTO(dbBulkStyleNoList));
            bulkStyleNoList.removeIf(dbBulkStyleNoList::contains);
            if (CollectionUtil.isEmpty(bulkStyleNoList)) return result;

            // 检查是否存在于吊牌列表
            HangTagSearchDTO searchDTO = new HangTagSearchDTO();
            searchDTO.setBulkStyleNos(bulkStyleNoList.toArray(new String[]{}));
            // 模糊查询, 要去掉不相等的
            List<HangTagListVO> voList = findHangTagList(searchDTO);
            voList.removeIf(hangTagListVO-> bulkStyleNoList.stream().anyMatch(bulkStyleNo-> hangTagListVO.getBulkStyleNo().contains(bulkStyleNo) && !hangTagListVO.getBulkStyleNo().equals(bulkStyleNo)));
            bulkStyleNoList.removeIf(bulkStyleNo-> voList.stream().anyMatch(it-> it.getBulkStyleNo().equals(bulkStyleNo)));
            if (CollectionUtil.isNotEmpty(bulkStyleNoList)) {
                result.addAll(bulkStyleNoList.stream()
                        .map(bulkStyleNo-> new MoreLanguageStatusExcelResultDTO(bulkStyleNo, 2, "失败,没有找到对应款号"))
                        .collect(Collectors.toList()));
            }
            // 检查状态是否是品控确认
            List<HangTagListVO> notRightHangTagList = voList.stream()
                    .filter(it -> !Arrays.asList(HangTagStatusEnum.TRANSLATE_CHECK, HangTagStatusEnum.FINISH).contains(it.getStatus()))
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(notRightHangTagList)) {
                result.addAll(notRightHangTagList.stream()
                        .map(hangTag-> new MoreLanguageStatusExcelResultDTO(hangTag.getBulkStyleNo(), 1,
                                "成功,审核流程仅进行到"+Opt.ofNullable(hangTag.getStatus()).orElse(HangTagStatusEnum.NOT_INPUT).getText()))
                        .collect(Collectors.toList()));
                voList.removeAll(notRightHangTagList);
            }
            if (CollectionUtil.isEmpty(voList)) return result;

            List<String> handlerBulkStyleNoList = voList.stream().map(HangTagListVO::getBulkStyleNo).collect(Collectors.toList());
            result.addAll(MORE_LANGUAGE_CV.copyList2ResultDTO(handlerBulkStyleNoList));
            // 获取所有国家
            if (CollectionUtil.isEmpty(countryList.get())) {
                countryList.set(countryLanguageService.getAllCountry(null));
            }
            List<CountryDTO> allCountry = countryList.get();

            List<StyleCountryStatus> styleCountryStatusList = handlerBulkStyleNoList.stream().flatMap(bulkStyleNo ->
                    allCountry.stream().map(countryDTO -> {
                        StyleCountryStatus status = new StyleCountryStatus();
                        status.setBulkStyleNo(bulkStyleNo);
                        status.setStatus(StyleCountryStatusEnum.UNCHECK);
                        status.setCountryCode(countryDTO.getCode());
                        status.setCountryName(countryDTO.getCountryName());
                        return status;
                    })
            ).collect(Collectors.toList());

            this.saveOrUpdateBatch(styleCountryStatusList);
            return result;
        }
    }

    private LambdaQueryWrapper<StyleCountryStatus> buildGroupQueryWrapper(MoreLanguageStatusQueryDto statusQueryDto){
        return new BaseLambdaQueryWrapper<StyleCountryStatus>()
                .notEmptyIn(StyleCountryStatus::getCountryCode, statusQueryDto.getCountryCode())
                .notEmptyIn(StyleCountryStatus::getStatus, Arrays.stream(
                        Opt.ofNullable(statusQueryDto.getStatus()).orElse("").split(COMMA)
                ).map(StyleCountryStatusEnum::findByCode).filter(Objects::nonNull).collect(Collectors.toList()))
                .between(StyleCountryStatus::getUpdateDate, statusQueryDto.getConfirmTime())
                .notEmptyIn(bulkStyleNoFunc, statusQueryDto.getBulkStyleNo())
                .select(bulkStyleNoFunc).orderByDesc(StyleCountryStatus::getUpdateDate).orderByAsc(StyleCountryStatus::getId).groupBy(bulkStyleNoFunc);
    }

    @Override
    public PageInfo<MoreLanguageStatusDto> listQuery(MoreLanguageStatusQueryDto statusQueryDto) {
        Page<StyleCountryStatus> page = statusQueryDto.startPage();
        List<StyleCountryStatus> list = this.list(buildGroupQueryWrapper(statusQueryDto));

        List<MoreLanguageStatusDto> moreLanguageStatusList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            List<StyleCountryStatus> allList = this.list(new BaseLambdaQueryWrapper<StyleCountryStatus>()
                    .notEmptyIn(StyleCountryStatus::getCountryCode, statusQueryDto.getCountryCode())
                    .in(bulkStyleNoFunc, list.stream().map(bulkStyleNoFunc).collect(Collectors.toList()))
            );
           list.forEach(styleCountryStatus -> {
                String bulkStyleNo = bulkStyleNoFunc.apply(styleCountryStatus);
                List<StyleCountryStatus> statusList = allList.stream()
                        .filter(it -> bulkStyleNo.equals(bulkStyleNoFunc.apply(it)))
                        .collect(Collectors.toList());
               moreLanguageStatusList.add(new MoreLanguageStatusDto(bulkStyleNo, MORE_LANGUAGE_CV.copyList2CountryDTO(statusList)));
            });
        }

        return BASE_CV.copy(page.toPageInfo(), moreLanguageStatusList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(List<StyleCountryStatus> updateStatusList) {
        List<String> bulkStyleNoList = updateStatusList.stream().map(bulkStyleNoFunc).distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(bulkStyleNoList)) {
            return false;
        }
        List<String> countryCodeList = updateStatusList.stream().map(StyleCountryStatus::getCountryCode)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<StyleCountryStatus> ew = new BaseLambdaQueryWrapper<StyleCountryStatus>()
                .notEmptyIn(StyleCountryStatus::getCountryCode, countryCodeList)
                .in(bulkStyleNoFunc, bulkStyleNoList)
                ;
        List<StyleCountryStatus> styleCountryStatusList = this.list(ew);

        // 获取吊牌状态
        HangTagSearchDTO searchDTO = new HangTagSearchDTO();
        searchDTO.setBulkStyleNos(bulkStyleNoList.toArray(new String[]{}));
        List<HangTagListVO> hangTagList = findHangTagList(searchDTO);

        // check
        bulkStyleNoList.forEach(bulkStyleNo-> {
            HangTagListVO hangTagListVO = hangTagList.stream().filter(it -> bulkStyleNo.equals(it.getBulkStyleNo()))
                    .findFirst().orElseThrow(() -> new OtherException("bulkStyleNo未找到有效吊牌数据"));
            if (!Arrays.asList(HangTagStatusEnum.TRANSLATE_CHECK, HangTagStatusEnum.FINISH).contains(hangTagListVO.getStatus())) {
                throw new OtherException("吊牌状态必须为待翻译确认或已审核");
            }
        });

        // 获取当前国家的语言
        List<CountryDTO> countryDTOList = countryLanguageService.getAllCountry(String.join(COMMA,countryCodeList));

        Map<String, Map<CountryLanguageType, List<String>>> map = new HashMap<>(countryDTOList.size());

        countryDTOList.forEach(countryDTO -> {
            String code = countryDTO.getCode();
            Map<CountryLanguageType, List<String>> typeMap = map.getOrDefault(code, new HashMap<>());
            Arrays.stream(CountryLanguageType.values()).forEach(type-> {
                List<String> standardColumnCodeList = typeMap.getOrDefault(type, new ArrayList<>());
                standardColumnCodeList.addAll(countryLanguageService.findStandardColumnCodeList(code, type, false));
                typeMap.put(type,standardColumnCodeList);
            });
            map.put(code,typeMap);
        });

        List<StyleCountryStatus> statusList = updateStatusList.stream().flatMap(updateStatus -> {
            String countryCode = updateStatus.getCountryCode();
            String bulkStyleNo = updateStatus.getBulkStyleNo();
            return countryDTOList.stream()
                    .filter(it-> StrUtil.isBlank(countryCode) || it.getCode().equals(countryCode))
                    .map(countryDTO -> {
                        String code = countryDTO.getCode();
                        StyleCountryStatus status = styleCountryStatusList.stream().filter(it ->
                                        it.getBulkStyleNo().equals(bulkStyleNo)
                                                && it.getCountryCode().equals(code)
                                ).findFirst().map(MORE_LANGUAGE_CV::copyMyself).orElse(updateStatus);
                        status = MORE_LANGUAGE_CV.copyMyself(status);
                        status.setCountryCode(code);
                        status.setStatus(updateStatus.getStatus());

                        Map<CountryLanguageType, List<String>> typeMap = map.getOrDefault(code, new HashMap<>());
                        List<MoreLanguageStatusCheckDetailDTO> checkDetailList = new ArrayList<>();
                        countryDTO.getLanguageCodeTypeMap().forEach((type, languageCodeList)-> {
                            checkDetailList.addAll(languageCodeList.stream().map(languageCode->
                                    new MoreLanguageStatusCheckDetailDTO(languageCode, type.getCode(), typeMap.getOrDefault(type,new ArrayList<>()))
                            ).collect(Collectors.toList()));
                        });
                        status.setCheckDetailJson(JSONUtil.toJsonStr(checkDetailList));
                        status.updateClear();
                        return status;
                    });
        }).collect(Collectors.toList());

        boolean updateBatch = this.saveOrUpdateBatch(statusList);

        List<String> hangTagIdList = hangTagList.stream().map(HangTagListVO::getId).distinct().collect(Collectors.toList());
        HangTagUpdateStatusDTO statusDTO = new HangTagUpdateStatusDTO();
        statusDTO.setIds(hangTagIdList);
        statusDTO.setStatus(HangTagStatusEnum.FINISH);
        statusDTO.setUserCompany(getCompanyCode());
        hangTagService.updateStatus(statusDTO,true);

        return updateBatch;
    }

}
