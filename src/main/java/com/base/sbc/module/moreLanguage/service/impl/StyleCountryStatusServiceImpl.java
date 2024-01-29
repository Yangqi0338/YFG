package com.base.sbc.module.moreLanguage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.enums.business.HangTagStatusEnum;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.hangtag.dto.HangTagSearchDTO;
import com.base.sbc.module.hangtag.mapper.HangTagMapper;
import com.base.sbc.module.hangtag.vo.HangTagListVO;
import com.base.sbc.module.moreLanguage.dto.CountryDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelResultDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusQueryDto;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.base.sbc.module.moreLanguage.mapper.StyleCountryStatusMapper;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StyleCountryStatusService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private UserUtils userUtils;

    @Override
    public List<MoreLanguageStatusExcelDTO> exportExcel() {
        return findHangTagList(new HangTagSearchDTO()).stream()
                .map(it-> new MoreLanguageStatusExcelDTO(it.getBulkStyleNo()))
                .distinct().collect(Collectors.toList());
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
            SFunction<StyleCountryStatus, String> bulkStyleNoFunc = StyleCountryStatus::getBulkStyleNo;
            List<String> dbBulkStyleNoList = this.listOneField(new LambdaQueryWrapper<StyleCountryStatus>()
                    .groupBy(bulkStyleNoFunc), bulkStyleNoFunc);
            result.addAll(MORE_LANGUAGE_CV.copyList2ResultDTO(dbBulkStyleNoList));
            bulkStyleNoList.removeIf(dbBulkStyleNoList::contains);

            // 检查是否存在于吊牌列表
            HangTagSearchDTO searchDTO = new HangTagSearchDTO();
            searchDTO.setBulkStyleNos(bulkStyleNoList.toArray(new String[]{}));
            List<HangTagListVO> voList = findHangTagList(searchDTO);
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
                        .map(hangTag-> new MoreLanguageStatusExcelResultDTO(hangTag.getBulkStyleNo(), 1, "成功,审核流程仅进行到"+hangTag.getStatus().getText()))
                        .collect(Collectors.toList()));
                voList.removeAll(notRightHangTagList);
            }

            // 获取所有国家
            if (CollectionUtil.isEmpty(countryList.get())) {
                countryList.set(countryLanguageService.getAllCountry());
            }
            List<CountryDTO> allCountry = countryList.get();

            List<StyleCountryStatus> styleCountryStatusList = bulkStyleNoList.stream().flatMap(bulkStyleNo ->
                    allCountry.stream().map(countryDTO -> {
                        StyleCountryStatus status = new StyleCountryStatus();
                        status.setBulkStyleNo(bulkStyleNo);
                        status.setStatus(StyleCountryStatusEnum.UNCHECK);
                        status.setCountryCode(countryDTO.getCode());
                        return status;
                    })
            ).collect(Collectors.toList());

            this.saveOrUpdateBatch(styleCountryStatusList);
            return result;
        }
    }

    @Override
    public PageInfo<MoreLanguageStatusDto> listQuery(MoreLanguageStatusQueryDto statusQueryDto) {
        SFunction<StyleCountryStatus, String> bulkStyleNoFunc = StyleCountryStatus::getBulkStyleNo;
        LambdaQueryWrapper<StyleCountryStatus> ew = new BaseLambdaQueryWrapper<StyleCountryStatus>()
                .notEmptyIn(StyleCountryStatus::getCountryCode, statusQueryDto.getCodeList())
                .notEmptyIn(StyleCountryStatus::getStatus, statusQueryDto.getStatusList())
                .between(StyleCountryStatus::getUpdateDate, statusQueryDto.getConfirmTime())
                .in(bulkStyleNoFunc, statusQueryDto.getBulkStyleNoList())
                .select(bulkStyleNoFunc).groupBy(bulkStyleNoFunc);
        Page<StyleCountryStatus> page = statusQueryDto.startPage();
        List<StyleCountryStatus> list = this.list(ew);

        List<StyleCountryStatus> allList = this.list(new LambdaQueryWrapper<StyleCountryStatus>()
                .in(bulkStyleNoFunc, list.stream().map(bulkStyleNoFunc).collect(Collectors.toList())));
        List<MoreLanguageStatusDto> moreLanguageStatusList = list.stream().map(styleCountryStatus -> {
            String bulkStyleNo = bulkStyleNoFunc.apply(styleCountryStatus);
            List<StyleCountryStatus> statusList = allList.stream()
                    .filter(it -> bulkStyleNo.equals(bulkStyleNoFunc.apply(it)))
                    .collect(Collectors.toList());
            return new MoreLanguageStatusDto(bulkStyleNo, MORE_LANGUAGE_CV.copyList2CountryDTO(statusList));
        }).collect(Collectors.toList());

        return BASE_CV.copy(page.toPageInfo(), moreLanguageStatusList);
    }

}
