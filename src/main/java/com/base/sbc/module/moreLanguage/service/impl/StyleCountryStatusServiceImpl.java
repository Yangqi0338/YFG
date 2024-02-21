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
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum;
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
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.*;
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
        // 获取全部数据并转换为excel实体类
        return MORE_LANGUAGE_CV.copyList2ExcelDTO(listQuery(queryDto).getList());
    }

    private List<HangTagListVO> findHangTagList(HangTagSearchDTO searchDTO){
        // 获取带数据权限的所有吊牌
        String authSql = dataPermissionsService
                .getDataPermissionsSql(DataPermissionsBusinessTypeEnum.hangTagList.getK(), MoreLanguageProperties.hangTagMainDbAlias, null, false);

        searchDTO.setCompanyCode(userUtils.getCompanyCode());
        return hangTagMapper.queryList(searchDTO, authSql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MoreLanguageStatusExcelResultDTO> importExcel(List<MoreLanguageStatusExcelDTO> dataList) {
        List<MoreLanguageStatusExcelResultDTO> result = new ArrayList<>();

        // 导入空数据判断
        List<String> bulkStyleNoList = dataList.stream().map(MoreLanguageStatusExcelDTO::getBulkStyleNo).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(bulkStyleNoList)) return result;

        // 检查数据库条数是否一样,如果一致不做处理
        long count = getBaseMapper().countByBulkStyleNo(bulkStyleNoList);
        if (count == bulkStyleNoList.size()) return result;
        else {
            /* ----------------------------过滤操作---------------------------- */
            // 获取DB的款号数据
            List<String> dbBulkStyleNoList = this.listOneField(new LambdaQueryWrapper<StyleCountryStatus>()
                    .in(bulkStyleNoFunc,bulkStyleNoList)
                    .groupBy(bulkStyleNoFunc), bulkStyleNoFunc);
            // DB存在就返回成功
            result.addAll(MORE_LANGUAGE_CV.copyList2ResultDTO(dbBulkStyleNoList));
            // 去掉数据库存在的
            bulkStyleNoList.removeIf(dbBulkStyleNoList::contains);
            if (CollectionUtil.isEmpty(bulkStyleNoList)) return result;

            // 查询款号对应的吊牌数据
            HangTagSearchDTO searchDTO = new HangTagSearchDTO();
            searchDTO.setBulkStyleNos(bulkStyleNoList.toArray(new String[]{}));
            List<HangTagListVO> voList = findHangTagList(searchDTO);

            // 款号是模糊查询, 查询出来的要去掉包含,但是不相等
            // 例: 款号 A1, 返回了 A11,A1. 去掉A11, 否则会当做数据不存在而进行导入
            voList.removeIf(hangTagListVO-> bulkStyleNoList.stream().anyMatch(bulkStyleNo-> hangTagListVO.getBulkStyleNo().contains(bulkStyleNo) && !hangTagListVO.getBulkStyleNo().equals(bulkStyleNo)));

            // 去掉存在吊牌的款号,若还有剩余,则这些款号是没有吊牌的,需要报失败提示
            bulkStyleNoList.removeIf(bulkStyleNo-> voList.stream().anyMatch(it-> it.getBulkStyleNo().equals(bulkStyleNo)));
            if (CollectionUtil.isNotEmpty(bulkStyleNoList)) {
                result.addAll(bulkStyleNoList.stream()
                        .map(bulkStyleNo-> new MoreLanguageStatusExcelResultDTO(bulkStyleNo, 2, MoreLanguageProperties.getMsg(NOT_EXIST_BULK_STATUS)))
                        .collect(Collectors.toList()));
            }

            // 检查状态是否是翻译确认,非翻译确认进行过滤并报成功但异常的提示
            List<HangTagListVO> notRightHangTagList = voList.stream()
                    .filter(it -> !Arrays.asList(HangTagStatusEnum.TRANSLATE_CHECK, HangTagStatusEnum.FINISH).contains(it.getStatus()))
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(notRightHangTagList)) {
                result.addAll(notRightHangTagList.stream()
                        .map(hangTag-> new MoreLanguageStatusExcelResultDTO(hangTag.getBulkStyleNo(), 1,
                                MoreLanguageProperties.getMsg(ERROR_STATUS, Opt.ofNullable(hangTag.getStatus()).orElse(HangTagStatusEnum.NOT_INPUT).getText()))
                        ).collect(Collectors.toList()));
                voList.removeAll(notRightHangTagList);
            }

            /* ----------------------------数据库操作---------------------------- */
            if (CollectionUtil.isEmpty(voList)) return result;

            // 获取需要处理的款号,并报成功提示
            List<String> handlerBulkStyleNoList = voList.stream().map(HangTagListVO::getBulkStyleNo).collect(Collectors.toList());
            result.addAll(MORE_LANGUAGE_CV.copyList2ResultDTO(handlerBulkStyleNoList));
            // 获取所有国家,导入期间大概率固定,所以使用ThreadLocal, 也为了适配多线程 TODO
            if (CollectionUtil.isEmpty(countryList.get())) {
                countryList.set(countryLanguageService.getAllCountry(null));
            }
            List<CountryDTO> allCountry = countryList.get();

            // 进行插入操作
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
        // 根据更新时间和id进行排序,根据款号进行分组,根据条件进行筛选,仅返回款号
        return new BaseLambdaQueryWrapper<StyleCountryStatus>()
                .notEmptyIn(StyleCountryStatus::getCountryCode, statusQueryDto.getCountryCode())
                .notEmptyIn(StyleCountryStatus::getStatus, statusQueryDto.getStatus())
                .between(StyleCountryStatus::getUpdateDate, statusQueryDto.getConfirmTime())
                .notEmptyIn(bulkStyleNoFunc, statusQueryDto.getBulkStyleNo())
                .select(bulkStyleNoFunc).orderByDesc(StyleCountryStatus::getUpdateDate).orderByAsc(StyleCountryStatus::getId).groupBy(bulkStyleNoFunc);
    }

    @Override
    public PageInfo<MoreLanguageStatusDto> listQuery(MoreLanguageStatusQueryDto statusQueryDto) {
        // 查询分组后的状态款号列表
        Page<StyleCountryStatus> page = statusQueryDto.startPage();
        List<StyleCountryStatus> list = this.list(buildGroupQueryWrapper(statusQueryDto));

        List<MoreLanguageStatusDto> moreLanguageStatusList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            // 根据款号查出对应的子数据
            List<StyleCountryStatus> allList = this.list(new BaseLambdaQueryWrapper<StyleCountryStatus>()
                    .notEmptyIn(StyleCountryStatus::getCountryCode, statusQueryDto.getCountryCode())
                    .in(bulkStyleNoFunc, list.stream().map(bulkStyleNoFunc).collect(Collectors.toList()))
            );
            // 根据分页的款号,将子数据封装成通用数据,让前端通过预先返回的结构,自行映射
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
        // 空值判断
        List<String> bulkStyleNoList = updateStatusList.stream().map(bulkStyleNoFunc).distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(bulkStyleNoList)) {
            return false;
        }
        // 获取需要更新的国家编码列表, 查询DB已存在的数据
        List<String> countryCodeList = updateStatusList.stream().map(StyleCountryStatus::getCountryCode)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<StyleCountryStatus> ew = new BaseLambdaQueryWrapper<StyleCountryStatus>()
                .notEmptyIn(StyleCountryStatus::getCountryCode, countryCodeList)
                .in(bulkStyleNoFunc, bulkStyleNoList)
                ;
        // 有可能比传入的更新款号少,因为可以不导入直接在吊牌列表进行审核更改状态,这时是新增一条状态数据
        List<StyleCountryStatus> styleCountryStatusList = this.list(ew);

        // 获取吊牌状态
        HangTagSearchDTO searchDTO = new HangTagSearchDTO();
        searchDTO.setBulkStyleNos(bulkStyleNoList.toArray(new String[]{}));
        List<HangTagListVO> hangTagList = findHangTagList(searchDTO);

        // 都为正确状态才能更新
        bulkStyleNoList.forEach(bulkStyleNo-> {
            HangTagListVO hangTagListVO = hangTagList.stream().filter(it -> bulkStyleNo.equals(it.getBulkStyleNo()))
                    .findFirst().orElseThrow(() -> new OtherException(MoreLanguageProperties.getMsg(HAVEN_T_TAG,bulkStyleNo)));
            if (!Arrays.asList(HangTagStatusEnum.TRANSLATE_CHECK, HangTagStatusEnum.FINISH).contains(hangTagListVO.getStatus())) {
                throw new OtherException(MoreLanguageProperties.getMsg(WARN_EXAMINE_STATUS));
            }
        });

        // 获取当前国家的语言
        List<CountryDTO> countryDTOList = countryLanguageService.getAllCountry(String.join(COMMA,countryCodeList));

        // 转化为 国家码-(国家类型-关联的标准列编码列表)
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

        /* ----------------------------更新操作---------------------------- */

        // 封装转化为实体类列表
        List<StyleCountryStatus> statusList = updateStatusList.stream().flatMap(updateStatus -> {
            String countryCode = updateStatus.getCountryCode();
            String bulkStyleNo = updateStatus.getBulkStyleNo();
            // 若没有指定更新某个国家,就获取所有国家
            return countryDTOList.stream()
                    .filter(it-> StrUtil.isBlank(countryCode) || it.getCode().equals(countryCode))
                    .map(countryDTO -> {
                        String code = countryDTO.getCode();
                        // 获取已存在的数据或直接使用空数据
                        StyleCountryStatus status = styleCountryStatusList.stream().filter(it ->
                                        it.getBulkStyleNo().equals(bulkStyleNo)
                                                && it.getCountryCode().equals(code)
                                ).findFirst().orElse(updateStatus);
                        // 深拷贝
                        status = MORE_LANGUAGE_CV.copyMyself(status);
                        // 设置国家编码和状态
                        status.setCountryCode(code);
                        status.setStatus(updateStatus.getStatus());

                        // 获取对应的标准列编码列表,并封装检查专用的详情json (用作审核之后,翻译新增了一个标准列关联,可以做对应的标记以及反审)
                        Map<CountryLanguageType, List<String>> typeMap = map.getOrDefault(code, new HashMap<>());
                        List<MoreLanguageStatusCheckDetailDTO> checkDetailList = new ArrayList<>();
                        countryDTO.getLanguageCodeTypeMap().forEach((type, languageCodeList)-> {
                            checkDetailList.addAll(languageCodeList.stream().map(languageCode->
                                    new MoreLanguageStatusCheckDetailDTO(languageCode, type.getCode(), typeMap.getOrDefault(type,new ArrayList<>()))
                            ).collect(Collectors.toList()));
                        });
                        status.setCheckDetailJson(JSONUtil.toJsonStr(checkDetailList));
                        // 清除更新标志
                        status.updateClear();
                        return status;
                    });
        }).collect(Collectors.toList());

        boolean updateBatch = this.saveOrUpdateBatch(statusList);

        // 直接调用吊牌更新接口, 将吊牌状态修改为完成
        List<String> hangTagIdList = hangTagList.stream().map(HangTagListVO::getId).distinct().collect(Collectors.toList());
        HangTagUpdateStatusDTO statusDTO = new HangTagUpdateStatusDTO();
        statusDTO.setIds(hangTagIdList);
        statusDTO.setStatus(HangTagStatusEnum.FINISH);
        statusDTO.setUserCompany(getCompanyCode());
        hangTagService.updateStatus(statusDTO,true);

        return updateBatch;
    }

}
