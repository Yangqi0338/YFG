package com.base.sbc.module.moreLanguage.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.annotation.EditPermission;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.HangTagStatusEnum;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import com.base.sbc.config.enums.business.SystemSource;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.exception.RightException;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageSystemDTO;
import com.base.sbc.module.hangtag.dto.HangTagSearchDTO;
import com.base.sbc.module.hangtag.dto.HangTagUpdateStatusDTO;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.hangtag.mapper.HangTagMapper;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.hangtag.vo.HangTagListVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBaseVO;
import com.base.sbc.module.moreLanguage.dto.CountryDTO;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailAuditDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelResultDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusQueryDto;
import com.base.sbc.module.moreLanguage.dto.StyleCountryStatusDto;
import com.base.sbc.module.moreLanguage.dto.TypeLanguageDto;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.base.sbc.module.moreLanguage.mapper.StyleCountryStatusMapper;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StyleCountryStatusService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.ERROR_STATUS;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.FILE_DOWNLOAD_FAILED;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.FILE_EXPORT_FAILED;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.HAVEN_T_TAG;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.NOT_EXIST_BULK_STATUS;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.NOT_FOUND_COUNTRY_LANGUAGE;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.THE_FILE_DOES_NOT_EXIST;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.WARN_EXAMINE_STATUS;
import static com.base.sbc.module.common.convert.ConvertContext.BASE_CV;
import static com.base.sbc.module.common.convert.ConvertContext.HANG_TAG_CV;
import static com.base.sbc.module.common.convert.ConvertContext.MORE_LANGUAGE_CV;
import static com.base.sbc.module.common.convert.ConvertContext.OPEN_CV;

/**
 * {@code 描述：}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/12/28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StyleCountryStatusServiceImpl extends BaseServiceImpl<StyleCountryStatusMapper, StyleCountryStatus> implements StyleCountryStatusService {

    @Autowired
    private CountryLanguageService countryLanguageService;

    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Autowired
    private HangTagMapper hangTagMapper;

    @Resource
    @Lazy
    private HangTagService hangTagService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private RedisUtils redisUtils;

    private final CcmFeignService ccmFeignService;

    public static final SFunction<StyleCountryStatus, String> bulkStyleNoFunc = StyleCountryStatus::getBulkStyleNo;
    public static final SFunction<StyleCountryStatus, StyleCountryStatusEnum> statusFunc = StyleCountryStatus::getStatus;
    public static final SFunction<StyleCountryStatus, String> codeFunc = StyleCountryStatus::getCode;
    public static final SFunction<StyleCountryStatus, CountryLanguageType> typeFunc = StyleCountryStatus::getType;
    public static final SFunction<StyleCountryStatus, Date> printTimeFunc = StyleCountryStatus::getPrintTime;
    public static final SFunction<StyleCountryStatus, String> standardColumnFunc = StyleCountryStatus::getStandardColumnCode;
    public static final SFunction<StyleCountryStatus, Date> updateDateFunc = StyleCountryStatus::getUpdateDate;
    public static final SFunction<StyleCountryStatus, String> idFunc = StyleCountryStatus::getId;

    @Override
    public List<MoreLanguageStatusExcelDTO> exportExcel() {
        MoreLanguageStatusQueryDto queryDto = new MoreLanguageStatusQueryDto();
        queryDto.reset2QueryList();
        // 获取全部数据并转换为excel实体类
        return MORE_LANGUAGE_CV.copyList2ExcelDTO(listQuery(queryDto).getList());
    }

    private List<HangTagListVO> findHangTagList(HangTagSearchDTO searchDTO){
        // 获取带数据权限的所有吊牌
        BaseQueryWrapper<HangTagListVO> qw = new BaseQueryWrapper<>();
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.hangTagList.getK(), MoreLanguageProperties.hangTagMainDbAlias, null, false);

        searchDTO.setCompanyCode(userUtils.getCompanyCode());
        return hangTagMapper.queryList(searchDTO, qw);
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
            voList.removeIf(hangTagListVO-> bulkStyleNoList.stream().anyMatch(bulkStyleNo->
                    hangTagListVO.getBulkStyleNo().contains(bulkStyleNo) && !hangTagListVO.getBulkStyleNo().equals(bulkStyleNo)
            ));

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
                    allCountry.stream().flatMap(countryDTO -> {
                        return Arrays.stream(CountryLanguageType.values()).map(it-> {
                            StyleCountryStatus status = new StyleCountryStatus();
                            status.setBulkStyleNo(bulkStyleNo);
                            status.setStatus(StyleCountryStatusEnum.UNCHECK);
                            status.setType(it);
                            status.setCode(countryDTO.getCode());
                            status.setName(countryDTO.getName());
                            return status;
                        });
                    })
            ).collect(Collectors.toList());

            this.saveOrUpdateBatch(styleCountryStatusList);
            return result;
        }
    }

    /**
     * 导出导入吊牌款号失败的数据
     * @param uniqueValue 唯一标识 用作 Redis 查询
     */
    @Override
    public void exportImportExcelFailData(String uniqueValue, HttpServletResponse response) {
        if (ObjectUtil.isEmpty(uniqueValue)) {
            log.warn("*************** uniqueValues 传参为空 ***************");
            throw new RightException(MoreLanguageProperties.getMsg(FILE_DOWNLOAD_FAILED));
        }
        // 根据唯一值从Redis 中读取要下载的文件信息
        Object excelInfo = redisUtils.get("uniqueValue:" + uniqueValue);
        if (ObjectUtil.isEmpty(excelInfo)) {
            log.warn("*************** 未找到当前查询的导出信息 ***************");
            throw new RightException(MoreLanguageProperties.getMsg(THE_FILE_DOES_NOT_EXIST));
        }
        List<MoreLanguageStatusExcelResultDTO> moreLanguageStatusExcelResultDTO =
                JSONUtil.toList(String.valueOf(excelInfo), MoreLanguageStatusExcelResultDTO.class);

        try {
            ExcelUtils.exportExcel(
                    moreLanguageStatusExcelResultDTO,
                    MoreLanguageStatusExcelResultDTO.class,
                    "导入吊牌款号失败数据.xlsx",
                    new ExportParams("导入吊牌款号失败数据", "导入吊牌款号失败数据", ExcelType.HSSF),
                    response
            );
        } catch (IOException e) {
            log.error("数据「{}」导出失败，失败原因：「{}」", uniqueValue, e.getMessage(), e);
            throw new RightException(MoreLanguageProperties.getMsg(FILE_EXPORT_FAILED));
        }
    }

    private BaseQueryWrapper<StyleCountryStatus> buildGroupQueryWrapper(MoreLanguageStatusQueryDto statusQueryDto) {
        // 根据更新时间和id进行排序,根据款号进行分组,根据条件进行筛选,仅返回款号
        BaseLambdaQueryWrapper<StyleCountryStatus> queryWrapper = new BaseLambdaQueryWrapper<StyleCountryStatus>()
                .notEmptyIn(codeFunc, statusQueryDto.getCountryCode())
                .between(updateDateFunc, statusQueryDto.getConfirmTime())
                .notEmptyIn(bulkStyleNoFunc, statusQueryDto.getBulkStyleNo());
        queryWrapper.select(bulkStyleNoFunc).orderByDesc(updateDateFunc).orderByAsc(idFunc).groupBy(bulkStyleNoFunc);
        BaseQueryWrapper<StyleCountryStatus> unwrapWrapper = queryWrapper.unwrap();
        unwrapWrapper.notEmptyIn("tscs.status", statusQueryDto.getStatus());
        return unwrapWrapper;

    }

    @Override
    @EditPermission(type = DataPermissionsBusinessTypeEnum.styleCountryStatus)
    public PageInfo<MoreLanguageStatusDto> listQuery(MoreLanguageStatusQueryDto statusQueryDto) {
        // 查询分组后的状态款号列表
        BaseQueryWrapper<StyleCountryStatus> qw = buildGroupQueryWrapper(statusQueryDto);
        QueryGenerator.initQueryWrapperByMapNoDataPermission(qw,statusQueryDto);
        Page<StyleCountryStatus> page = statusQueryDto.startPage();
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.styleCountryStatus.getK(), "ts.");
        List<StyleCountryStatus> list = baseMapper.queryList(qw);

        List<MoreLanguageStatusDto> moreLanguageStatusList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            // 根据款号查出对应的子数据
            List<StyleCountryStatus> allList = this.list(new BaseLambdaQueryWrapper<StyleCountryStatus>()
                    .notEmptyIn(codeFunc, statusQueryDto.getCountryCode())
                    .in(bulkStyleNoFunc, list.stream().map(bulkStyleNoFunc).collect(Collectors.toList()))
            );
            // 根据分页的款号,将子数据封装成通用数据,让前端通过预先返回的结构,自行映射
            list.forEach(styleCountryStatus -> {
                String bulkStyleNo = styleCountryStatus.getBulkStyleNo();
                List<StyleCountryStatus> statusList = allList.stream()
                        .filter(it -> bulkStyleNo.equals(it.getBulkStyleNo()))
                        .collect(Collectors.toList());
                moreLanguageStatusList.add(new MoreLanguageStatusDto(bulkStyleNo, styleCountryStatus.getBrand(), styleCountryStatus.getReceiveDeptId(), MORE_LANGUAGE_CV.copyList2CountryDTO(statusList)));
            });
        }

        return BASE_CV.copy(page.toPageInfo(), moreLanguageStatusList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(List<StyleCountryStatus> updateStatusList, List<HangTag> hangTagList, boolean needUpdateHangTag) {
        // 空值判断
        List<String> bulkStyleNoList = updateStatusList.stream().map(bulkStyleNoFunc).distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(bulkStyleNoList)) {
            return false;
        }
        // 获取需要更新的国家编码列表, 查询DB已存在的数据
        String codeList = updateStatusList.stream().map(codeFunc)
                .filter(Objects::nonNull).distinct().collect(Collectors.joining(COMMA));
        List<CountryLanguageType> typeList = updateStatusList.stream().map(typeFunc)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // 有可能比传入的更新款号少,因为可以不导入直接在吊牌列表进行审核更改状态,这时是新增一条状态数据
        // 只查吊牌
        List<StyleCountryStatus> styleCountryStatusList = this.list(new BaseLambdaQueryWrapper<StyleCountryStatus>()
                .notEmptyIn(typeFunc,typeList)
                .in(bulkStyleNoFunc, bulkStyleNoList)
                .and(StrUtil.isNotBlank(codeList), andOperation->
                        andOperation.eq(typeFunc, CountryLanguageType.WASHING).or(it-> it.in(codeFunc, codeList))
                )
        );

        // 获取吊牌状态
        if (CollectionUtil.isEmpty(hangTagList)) {
            HangTagSearchDTO searchDTO = new HangTagSearchDTO();
            searchDTO.setBulkStyleNos(bulkStyleNoList.toArray(new String[]{}));
            hangTagList.addAll(HANG_TAG_CV.copyList2Entity(findHangTagList(searchDTO)));
        }

        // 都为正确状态才能更新
        bulkStyleNoList.forEach(bulkStyleNo-> {
            HangTag hangTag = hangTagList.stream().filter(it -> bulkStyleNo.equals(it.getBulkStyleNo()))
                    .findFirst().orElseThrow(() -> new OtherException(MoreLanguageProperties.getMsg(HAVEN_T_TAG,bulkStyleNo)));
            if (!Arrays.asList(HangTagStatusEnum.TRANSLATE_CHECK,HangTagStatusEnum.PART_TRANSLATE_CHECK, HangTagStatusEnum.FINISH).contains(hangTag.getStatus())) {
                throw new OtherException(MoreLanguageProperties.getMsg(WARN_EXAMINE_STATUS));
            }
        });

        // 获取当前国家的语言
        List<CountryDTO> countryDTOList = countryLanguageService.getAllCountry("");
        long size = countryLanguageService.getAllCountrySize();

        // 转化为 国家码-(国家类型-关联的标准列编码列表)
        Map<String, Map<CountryLanguageType, List<String>>> map = new HashMap<>(countryDTOList.size());
        countryDTOList.forEach(countryDTO -> {
            String code = countryDTO.getCode();
            Map<CountryLanguageType, List<String>> typeMap = map.getOrDefault(code, new HashMap<>());
            Arrays.stream(CountryLanguageType.values()).filter(it-> CollUtil.isEmpty(typeList) || typeList.contains(it)).forEach(type-> {
                List<String> standardColumnCodeList = typeMap.getOrDefault(type, new ArrayList<>());
                standardColumnCodeList.addAll(countryLanguageService.findStandardColumnCodeList(code, type, false));
                typeMap.put(type,standardColumnCodeList);
            });
            map.put(code,typeMap);
        });

        /* ----------------------------更新操作---------------------------- */
        // 先查询存在的缓存,并移除
        // 剩下的再走普通查询
        List<HangTagMoreLanguageBaseVO> webBaseVOList = new ArrayList<>();
        Map<String,List<String>> needSearchMap = new HashMap<>();
        map.keySet().forEach(code-> {
            List<String> needSearchList = new ArrayList<>();
            bulkStyleNoList.forEach(bulkStyleNo-> {
                Object cache = RedisStaticFunUtils.sPop(RedisKeyConstant.HANG_TAG_COUNTRY.addEnd(true, code, bulkStyleNo));
                if (ObjectUtil.isNotNull(cache)) {
                    List<HangTagMoreLanguageBaseVO> cacheList = (List<HangTagMoreLanguageBaseVO>) cache;
                    webBaseVOList.addAll(cacheList);
                }else {
                    needSearchList.add(bulkStyleNo);
                }
            });
            if (CollectionUtil.isNotEmpty(needSearchList)) {
                needSearchMap.put(code,needSearchList);
            }
        });
        if (!needSearchMap.isEmpty()) {
            HangTagMoreLanguageDTO languageDTO = new HangTagMoreLanguageDTO();
            languageDTO.setBulkStyleNo(needSearchMap.values().stream().flatMap(Collection::stream).distinct().collect(Collectors.joining(COMMA)));
            languageDTO.setSource(SystemSource.SYSTEM);
            languageDTO.setCode(String.join(COMMA,needSearchMap.keySet()));
            languageDTO.setUserCompany(userUtils.getCompanyCode());
            webBaseVOList.addAll((List<HangTagMoreLanguageBaseVO>) hangTagService.getMoreLanguageDetailsByBulkStyleNo(languageDTO));
        }
        Map<CountryLanguageType, List<HangTagMoreLanguageBaseVO>> listMap = webBaseVOList.stream().collect(Collectors.groupingBy(HangTagMoreLanguageBaseVO::getCountryLanguageType));

        // 封装转化为实体类列表
        List<StyleCountryStatus> statusList = updateStatusList.stream().flatMap(updateStatus -> {
            String countryCode = updateStatus.getCode();
            String bulkStyleNo = updateStatus.getBulkStyleNo();
            List<StyleCountryStatus> bulkCountryStatusList = styleCountryStatusList.stream().filter(it -> bulkStyleNo.equals(it.getBulkStyleNo())).collect(Collectors.toList());
            StyleCountryStatus baseStatus = MORE_LANGUAGE_CV.copyMyself(updateStatus);
            baseStatus.setStatus(StyleCountryStatusEnum.UNCHECK);
            return countryDTOList.stream()
                    .flatMap(countryDTO -> {
                        String code = countryDTO.getCode();
                        List<StyleCountryStatus> sameCodeStatusList = new ArrayList<>();

                        countryDTO.getLanguageCodeTypeMap().forEach((type,languageCodeList)-> {
                            if (CollUtil.isEmpty(typeList) || typeList.contains(type)) {
                                // 获取对应的标准列编码列表,并封装检查专用的详情json (用作审核之后,翻译新增了一个标准列关联,可以做对应的标记以及反审)
                                List<String> standardColumnCodeList = map.getOrDefault(code, new HashMap<>(1)).getOrDefault(type, new ArrayList<>());
                                // 获取组装的审核列信息
                                Map<String, List<Map<String, List<MoreLanguageStatusCheckDetailAuditDTO>>>> standardColumnCodeMap =
                                        listMap.getOrDefault(type, new ArrayList<>()).stream().filter(it ->
                                                code.equals(it.getCode()) && type.equals(it.getCountryLanguageType()) && bulkStyleNo.equals(it.getBulkStyleNo())
                                        ).collect(CommonUtils.groupingBy(HangTagMoreLanguageBaseVO::getStandardColumnCode, HangTagMoreLanguageBaseVO::buildAuditMap));

                                StyleCountryStatus status = bulkCountryStatusList.stream().filter(it ->
                                        it.getType().equals(type) && it.getCode().equals(code)
                                ).findFirst().orElseGet(()-> {
                                    // 深拷贝
                                    if (type == CountryLanguageType.WASHING) {
                                        StyleCountryStatus styleCountryStatus = bulkCountryStatusList.stream().filter(it -> it.getType().equals(type)).findFirst().orElse(baseStatus);
                                        styleCountryStatus = MORE_LANGUAGE_CV.copyMyself(styleCountryStatus);
                                        styleCountryStatus.setId(null);
                                        return styleCountryStatus;
                                    }else if (StrUtil.equals(code, countryCode)){
                                        return MORE_LANGUAGE_CV.copyMyself(baseStatus);
                                    }
                                    return null;
                                });
                                if (status != null) {
                                    status.setCode(code);
                                    status.setName(countryDTO.getName());
                                    status.setType(type);
                                    status.setStatus(updateStatus.getStatus());
                                    List<MoreLanguageStatusCheckDetailDTO> checkDetailList = languageCodeList.stream().map(languageCode-> {
                                        List<MoreLanguageStatusCheckDetailAuditDTO> languageAuditList = standardColumnCodeList.stream().flatMap(standardColumnCode ->
                                                // 获取当前标准列当前语言的审核列表
                                                standardColumnCodeMap.getOrDefault(standardColumnCode, new ArrayList<>()).stream().flatMap(mapList -> mapList.get(languageCode).stream())
                                        ).filter(it -> StrUtil.isNotBlank(it.getSource())).collect(Collectors.toList());
                                        return new MoreLanguageStatusCheckDetailDTO(languageCode, CollUtil.distinct(languageAuditList, (it)-> it.getStandardColumnCode() + it.getSource(), true));
                                    }).collect(Collectors.toList());

                                    status.setStandardColumnCode(checkDetailList.stream().flatMap(checkDetailDTO-> checkDetailDTO.getAuditList()
                                            .stream().map(MoreLanguageStatusCheckDetailAuditDTO::getStandardColumnCode)
                                    ).distinct().collect(Collectors.joining(COMMA)));
                                    status.setCheckDetailJson(JSONUtil.toJsonStr(checkDetailList));
                                    // 清除更新标志
                                    status.updateClear();
                                    sameCodeStatusList.add(status);
                                }
                            }
                        });

                        return sameCodeStatusList.stream();
                    });
        }).collect(Collectors.toList());

        boolean updateBatch = this.saveOrUpdateBatch(statusList);

        if (needUpdateHangTag) {
            statusList.stream().collect(Collectors.groupingBy((it)-> Pair.of(it.getStatus(), it.getType()))).forEach((keyPair,sameKeyList)-> {
                // 可能存在未处理的状态,需要筛选
                List<String> rightBulkStyleNoList = statusList.stream().map(bulkStyleNoFunc).collect(Collectors.toList());
                hangTagList.stream()
                        .filter(it-> rightBulkStyleNoList.contains(it.getBulkStyleNo()))
                        .map(hangTag-> {
                            HangTagStatusEnum status = HangTagStatusEnum.PART_TRANSLATE_CHECK;
                            if (hangTag.getStatus() != HangTagStatusEnum.TRANSLATE_CHECK) {
                                if (this.count(new BaseLambdaQueryWrapper<StyleCountryStatus>()
                                        .eq(bulkStyleNoFunc, hangTag.getBulkStyleNo())
                                        .eq(statusFunc, StyleCountryStatusEnum.CHECK)) >= (size * CountryLanguageType.values().length)
                                ) {
                                    status = HangTagStatusEnum.FINISH;
                                }
                            }
                            if (hangTag.getStatus() == status) return null;
                            return Pair.of(status, hangTag);
                        }).filter(Objects::nonNull)
                        .collect(CommonUtils.groupingBy(Pair::getKey, Pair::getValue))
                        .forEach((status,sameStatusHangTagList)-> {
                            // 直接调用吊牌更新接口, 将吊牌状态修改为完成
                            HangTagUpdateStatusDTO statusDTO = new HangTagUpdateStatusDTO();
                            statusDTO.setIds(sameStatusHangTagList.stream().map(HangTag::getId).distinct().collect(Collectors.toList()));
                            statusDTO.setStatus(status);
                            statusDTO.setCountryCode(codeList);
                            statusDTO.setCountryStatus(keyPair.getKey());
                            statusDTO.setType(keyPair.getValue());
                            // 设置为translate_check 打破循环
                            if (status == HangTagStatusEnum.PART_TRANSLATE_CHECK) {
                                sameStatusHangTagList.forEach(it-> it.setStatus(HangTagStatusEnum.TRANSLATE_CHECK));
                            }
                            hangTagService.updateStatus(statusDTO,true, sameStatusHangTagList);
                        });
            });
        }

        return updateBatch;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePrintRecord(HangTagMoreLanguageSystemDTO languageDTO) {
        String bulkStyleNo = languageDTO.getBulkStyleNo();
        // 查询国家
        CountryQueryDto countryQueryDto = OPEN_CV.copy2CountryQuery(languageDTO);
        List<CountryLanguageDto> countryLanguageDtoList = countryLanguageService.listQuery(countryQueryDto);
        if (CollectionUtil.isEmpty(countryLanguageDtoList)) {
            log.error(MoreLanguageProperties.getMsg(NOT_FOUND_COUNTRY_LANGUAGE));
            return;
        }

        countryLanguageDtoList.stream().collect(Collectors.groupingBy(CountryLanguageDto::getCode)).forEach((code,sameCodeList)-> {
            this.update(new LambdaUpdateWrapper<StyleCountryStatus>()
                    .set(printTimeFunc,new Date())
                    .eq(bulkStyleNoFunc, bulkStyleNo)
                    .eq(typeFunc, countryQueryDto.getType())
                    .eq(codeFunc, code)
            );
        });
    }

    @Override
    public StyleCountryStatusDto findPrintRecordByStyleNo(HangTagMoreLanguageDTO languageDTO) {
        String bulkStyleNo = languageDTO.getBulkStyleNo();

        // 查询国家
        CountryQueryDto countryQueryDto = MORE_LANGUAGE_CV.copy2QueryDto(languageDTO);
        List<CountryLanguageDto> countryLanguageDtoList = countryLanguageService.listQuery(countryQueryDto);
        if (CollectionUtil.isEmpty(countryLanguageDtoList)) {
            throw new OtherException(MoreLanguageProperties.getMsg(NOT_FOUND_COUNTRY_LANGUAGE));
        }

        // 装饰名字
        List<BasicBaseDict> dictList = ccmFeignService.getDictInfoToList(MoreLanguageProperties.languageDictCode);
        countryLanguageDtoList.forEach(countryLanguageDto -> {
            countryLanguageDto.buildLanguageName(dictList);
        });

        // 封装基础数据
        StyleCountryStatusDto recordDto = MORE_LANGUAGE_CV.copy2StatusDto(countryLanguageDtoList.get(0));

        List<TypeLanguageDto> typeLanguageDtoList = new ArrayList<>();

        // 根据类型排序分组, 封装该款号拥有的LanguageList
        countryLanguageDtoList.stream().sorted(CommonUtils.comparing(CountryLanguage::getType))
                .collect(CommonUtils.groupingBy(CountryLanguageDto::getType)).forEach((type, sameTypeList)-> {

                    TypeLanguageDto typeLanguageDto = new TypeLanguageDto();
                    typeLanguageDto.setType(type);
                    typeLanguageDto.setLanguageList(sameTypeList.stream().map(MORE_LANGUAGE_CV::copy2Save).collect(Collectors.toList()));
                    typeLanguageDtoList.add(typeLanguageDto);

                    // 获取审核状态
                    Opt.ofNullable(this.findOne(new LambdaQueryWrapper<StyleCountryStatus>()
                            .select(statusFunc, printTimeFunc)
                            .eq(bulkStyleNoFunc, bulkStyleNo)
                            .eq(typeFunc, type)
                            .eq(codeFunc, recordDto.getCode())
                    )).ifPresent(styleCountryStatus-> {
                        typeLanguageDto.getLanguageList().forEach(it-> it.setPrintTime(styleCountryStatus.getPrintTime()));
                        recordDto.getStatusMap().put(type, styleCountryStatus.getStatus());
                    });
                });
        recordDto.setTypeLanguageDtoList(typeLanguageDtoList);

        return recordDto;
    }

}
