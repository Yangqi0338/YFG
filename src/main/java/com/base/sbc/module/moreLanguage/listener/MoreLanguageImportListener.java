package com.base.sbc.module.moreLanguage.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.DataVerifyResultVO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExcelQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExportBaseDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageMapExportMapping;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailDTO;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService;
import com.base.sbc.module.moreLanguage.service.StyleCountryStatusService;
import com.base.sbc.module.operalog.dto.OperaLogJsonDto;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.standard.dto.StandardColumnDto;
import com.base.sbc.module.standard.dto.StandardColumnQueryDto;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.codeFunc;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.enableFlagFunc;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.idFunc;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.languageCodeFunc;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.singleLanguageFlagFunc;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.typeFunc;
import static com.base.sbc.module.moreLanguage.service.impl.StandardColumnCountryTranslateServiceImpl.countryLanguageIdFunc;
import static com.base.sbc.module.moreLanguage.service.impl.StandardColumnCountryTranslateServiceImpl.propertiesCodeFunc;
import static com.base.sbc.module.moreLanguage.service.impl.StandardColumnCountryTranslateServiceImpl.titleCodeFunc;
import static com.base.sbc.module.moreLanguage.service.impl.StyleCountryStatusServiceImpl.statusFunc;

/**
 * @author KC
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MoreLanguageImportListener extends AnalysisEventListener<Map<Integer, String>> {
    /**
     * 数据合法性校验结果
     */
    private static final ThreadLocal<List<DataVerifyResultVO>> dataVerifyResults = new TransmittableThreadLocal<>();
    public static ThreadLocal<Map<String, MoreLanguageMapExportMapping>> sheetExportMapping = new TransmittableThreadLocal<>();

    private final StandardColumnCountryTranslateService standardColumnCountryTranslateService;

    private final CountryLanguageService countryLanguageService;

    private final StandardColumnService standardColumnService;
    private final StyleCountryStatusService styleCountryStatusService;
    private final CcmFeignService ccmFeignService;

    @Setter
    private MoreLanguageExcelQueryDto excelQueryDto;

    private ThreadPoolExecutor threadPoolExecutor;
    SFunction<DataVerifyResultVO, String> sheetNameFunc = DataVerifyResultVO::getSheetName;
    SFunction<DataVerifyResultVO, Integer> rowIndexFunc = DataVerifyResultVO::getRowIndex;

    @PostConstruct
    public void initThread(){
        threadPoolExecutor = new ThreadPoolExecutor(8, 8,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(16), r -> {
                    Thread thread = new Thread(r,"多语言导入");
                    thread.setUncaughtExceptionHandler((Thread t,Throwable e) -> {
                        if(e != null){
                            e.printStackTrace();
                            throw new OtherException(e.getMessage());
                        }
                    });
                    return thread;
                });
    }

    /**
     * headRowNumber() 通过这个方法来解析对应的行, 不指定为1, 即第一行数据会进到invokeHeadMap方法
     * */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        getMapping(context,(mapExportMapping)-> {
            mapExportMapping.setHeadMap(headMap);
            return true;
        });

        // 添加检查点, 使用工厂模式
    }

    private <T> T getMapping(AnalysisContext context, Function<MoreLanguageMapExportMapping, T> function){
        Map<String, MoreLanguageMapExportMapping> exportMapping = sheetExportMapping.get();
        if (exportMapping == null) {
            exportMapping = new HashMap<>(16);
        }
        String sheetName = context.readSheetHolder().getSheetName();
        MoreLanguageMapExportMapping mapExportMapping = exportMapping.getOrDefault(sheetName, new MoreLanguageMapExportMapping(sheetName));
        T result = function.apply(mapExportMapping);
        exportMapping.put(sheetName,mapExportMapping);
        sheetExportMapping.set(exportMapping);;
        return result;
    }

    private void removeMapping(AnalysisContext context){
        String sheetName = context.readSheetHolder().getSheetName();
        Map<String, MoreLanguageMapExportMapping> exportMapping = sheetExportMapping.get();
        if (exportMapping == null || !exportMapping.containsKey(sheetName)) {
            return;
        }
        exportMapping.remove(sheetName);
        if (exportMapping.isEmpty()) {
            sheetExportMapping.remove();
        }
    }

    /**
     * 调用完invokeHeadMap后会调用
     * */
    @Override
    public void invoke(Map<Integer, String> value, AnalysisContext context) {
        YesOrNoEnum singleLanguageFlag = excelQueryDto.getSingleLanguageFlag();
        Pair<MoreLanguageMapExportMapping, MoreLanguageExportBaseDTO> exportPair = getMapping(context, (mapExportMapping)-> {
            MoreLanguageExportBaseDTO tempBaseDTO = mapExportMapping.initByFirstRow(value, MoreLanguageExportBaseDTO.class);
            return Pair.of(mapExportMapping, tempBaseDTO);
        });
        Integer rowIndex = context.readRowHolder().getRowIndex();
        MoreLanguageMapExportMapping exportMapping = exportPair.getKey();
        MoreLanguageExportBaseDTO exportBaseDTO = exportPair.getValue();
        String sheetName = exportMapping.getSheetName();

        Map<String, String> map = exportMapping.buildMap(value);

        StandardColumnCountryTranslate baseSource;
        // 设置值
        if (exportBaseDTO != null) {
            boolean isSingleLanguageFlag = singleLanguageFlag == YesOrNoEnum.YES;
            if (StrUtil.isBlank(exportBaseDTO.getExcelCode()) ||
                    !exportBaseDTO.getExcelCode().equals(isSingleLanguageFlag ? excelQueryDto.getLanguageCode() : excelQueryDto.getCode())) {
                throw new OtherException("导入的国家或语言与该导出模版的国家或语言不对应\n请选择正确的导入国家");
            }
            // 初始化
            String key = exportBaseDTO.getKey();
            String keyName = exportBaseDTO.getKeyName();
            String uniqueCode = exportBaseDTO.getStandardColumnCode();

            String[] uniqueCodeArray = uniqueCode.split(RedisKeyBuilder.COMMA);
            if (uniqueCodeArray.length <= 1) {
                throw new OtherException(String.format("%s第%s行隐藏的关键唯一标识值不正确,请重新导入", sheetName, rowIndex));
            }
            CountryLanguageType type = CountryLanguageType.findByCode(uniqueCodeArray[0]);
            exportMapping.setType(type);

            CountryQueryDto countryQueryDto = BeanUtil.copyProperties(excelQueryDto, CountryQueryDto.class);
            List<CountryLanguageDto> countryLanguageList = countryLanguageService.listQuery(countryQueryDto);
            if (CollectionUtil.isEmpty(countryLanguageList)) {
                throw new OtherException("未找到对应的国家语言数据");
            }

            // 装饰名字
            List<BasicBaseDict> dictList = ccmFeignService.getDictInfoToList(MoreLanguageProperties.languageDictCode);
            countryLanguageList.forEach(countryLanguageDto -> {
                countryLanguageDto.buildLanguageName(dictList);
            });

            exportMapping.setCountryLanguageList(countryLanguageList);

            baseSource = new StandardColumnCountryTranslate();
            baseSource.setTitleCode(uniqueCodeArray[1]);
            baseSource.setPropertiesCode(key);
            baseSource.setPropertiesName(keyName);

            exportMapping.setBaseSourceData(baseSource);
        }else {
            baseSource = exportMapping.getBaseSourceData();
        }

        List<CountryLanguageDto> countryLanguageList = exportMapping.getCountryLanguageList();

        StringJoiner codeJoiner = new StringJoiner("-");
        for (String code : baseSource.getPropertiesCode().split("-")) {
            if (!map.containsKey(code)) throw new OtherException(String.format("%s第%s行未获取到编码", sheetName, rowIndex));
            codeJoiner.add(map.remove(code));
        }
        StringJoiner nameJoiner = new StringJoiner("-");
        for (String name : baseSource.getPropertiesName().split("-")) {
            if (!map.containsKey(name)) throw new OtherException(String.format("%s第%s行未获取到字段名字", sheetName, rowIndex));
            nameJoiner.add(map.remove(name));
        }
        String code = codeJoiner.toString();
        String name = nameJoiner.toString();

        StandardColumnCountryTranslate baseCountryTranslate = BeanUtil.copyProperties(baseSource, StandardColumnCountryTranslate.class);
        baseCountryTranslate.setPropertiesCode(code);
        baseCountryTranslate.setPropertiesName(name);

        List<StandardColumnCountryTranslate> translateList = exportMapping.getSourceData();

        countryLanguageList.forEach(countryLanguage -> {
            StandardColumnCountryTranslate countryTranslate = BeanUtil.copyProperties(baseCountryTranslate, StandardColumnCountryTranslate.class);
            countryTranslate.setCountryLanguageId(countryLanguage.getId());
            String contentKey = countryLanguage.getLanguageCode() + "-" + "content";
            countryTranslate.setContent("");
            map.keySet().stream().filter(it-> it.contains(contentKey)).findFirst().ifPresent(key-> {
                List<String> keyList = Arrays.asList(key.split(","));
                String valueStr = map.get(key);
                if (StrUtil.isNotBlank(valueStr)) {
                    if (keyList.size() > 1) {
                        int i = keyList.indexOf(contentKey);
                        String[] valueList = valueStr.split(",");
                        countryTranslate.setContent(valueList[Math.min(valueList.length - 1, i)]);
                    }else {
                        countryTranslate.setContent(valueStr);
                    }
                }
            });
            if (StrUtil.isNotBlank(countryTranslate.getContent())) {
                translateList.add(countryTranslate);
            }
        });

        exportMapping.setSourceData(translateList);
    }

    /**
     * 解析完所有行后会调用
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doAfterAllAnalysed(AnalysisContext context) {
        List<Runnable> taskList = new ArrayList<>();
//        Runnable task = ()-> {
            MoreLanguageMapExportMapping exportMapping = getMapping(context, (mapExportMapping) -> mapExportMapping);
            List<StandardColumnCountryTranslate> translateList = exportMapping.getSourceData();
            if (CollectionUtil.isEmpty(translateList)) {
                addDataVerifyResult(context, null,"成功0条","未解析到数据");
                return;
            }
            CountryLanguageType type = exportMapping.getType();
            StandardColumnCountryTranslate baseTranslate = exportMapping.getBaseSourceData();

            StringJoiner uniqueCodeJoiner = new StringJoiner(RedisKeyBuilder.COMMA);
            uniqueCodeJoiner.add(type.getCode());
            uniqueCodeJoiner.add(baseTranslate.getTitleCode());

            // 保存起来 减少查询? TODO
            List<CountryLanguageDto> countryLanguageList = exportMapping.getCountryLanguageList();

            Object titleObject = standardColumnService.findByCode(uniqueCodeJoiner.toString());
            if (titleObject == null) {
                addDataVerifyResult(context, null,"未找到对应的吊牌字段","检查是否修改了隐藏列或者该吊牌字段已被删除");
                return;
            }
            StandardColumn standardColumn = (StandardColumn) titleObject;
            String standardColumnName = standardColumn.getName();
            String standardColumnCode = standardColumn.getCode();
            StandardColumnType standardColumnType = standardColumn.getType();

            OperaLogEntity baseEntity = new OperaLogEntity();
            baseEntity.setDocumentName(standardColumnName);
            baseEntity.setDocumentCode(standardColumnCode);
            baseEntity.setName("多语言翻译");
            baseEntity.setPath(excelQueryDto.getCode() + ":" + type.getCode());
            baseEntity.setContent(countryLanguageList.get(0).getName() + ":" + type.getText());
            if (excelQueryDto.getSingleLanguageFlag() == YesOrNoEnum.YES) {
                baseEntity.setName("单语言翻译");
                baseEntity.setPath(excelQueryDto.getLanguageCode() + ":" + type.getCode());
                baseEntity.setContent(countryLanguageList.get(0).getLanguageName() + ":" + type.getText());
            };

            List<String> countryLanguageIdList = countryLanguageList.stream().map(idFunc).collect(Collectors.toList());
            List<StandardColumnCountryTranslate> updateOldTranslateList = standardColumnCountryTranslateService.list(
                    new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                            .in(countryLanguageIdFunc, countryLanguageIdList)
                            .eq(titleCodeFunc, standardColumnCode)
            );
            Pair<List<StandardColumnCountryTranslate>, List<StandardColumnCountryTranslate>> listPair =
                    diffTranslateList(standardColumnName, context, translateList, countryLanguageList, updateOldTranslateList, true);

            List<StandardColumnCountryTranslate> updateNewTranslateList = listPair.getKey();
            List<StandardColumnCountryTranslate> addTranslateList = listPair.getValue();

            // 日志处理
            taskList.add(()-> {
                if (CollectionUtil.isNotEmpty(addTranslateList)) {
                    OperaLogEntity addLogEntity = BeanUtil.copyProperties(baseEntity, OperaLogEntity.class);
                    addLogEntity.setType("新增");
                    List<OperaLogJsonDto> operaLogJsonList = new ArrayList<>();
                    addTranslateList.stream().sorted(Comparator.comparing(propertiesCodeFunc)).forEach(translate-> {
                        operaLogJsonList.add(new OperaLogJsonDto(translate.getPropertiesName(), "" , translate.getContent()));
                    });
                    addLogEntity.setJsonContent(JSONUtil.toJsonStr(operaLogJsonList));
                    standardColumnCountryTranslateService.saveOperaLog(addLogEntity);
                }
                if (CollectionUtil.isNotEmpty(updateNewTranslateList)) {
                    OperaLogEntity updateLogEntity = BeanUtil.copyProperties(baseEntity, OperaLogEntity.class);
                    updateLogEntity.setType("修改");
                    List<OperaLogJsonDto> operaLogJsonList = new ArrayList<>();
                    updateNewTranslateList.stream().sorted(Comparator.comparing(propertiesCodeFunc)).forEach(translate-> {
                        updateOldTranslateList.stream().filter(it-> it.getId().equals(translate.getId())).findFirst().ifPresent(oldTranslate-> {
                            operaLogJsonList.add(new OperaLogJsonDto(translate.getPropertiesName(), oldTranslate.getContent() , translate.getContent()));
                        });
                    });
                    updateLogEntity.setJsonContent(JSONUtil.toJsonStr(operaLogJsonList));
                    standardColumnCountryTranslateService.saveOperaLog(updateLogEntity);
                }
            });
            // 仅更新做操作
            updateNewTranslateList.addAll(addTranslateList);
            if (CollectionUtil.isNotEmpty(updateNewTranslateList)) {
                standardColumnCountryTranslateService.saveOrUpdateBatch(updateNewTranslateList);
                // 同步审核状态
                taskList.add(()-> {
                    List<StyleCountryStatus> changeStatusList = new ArrayList<>();
                    countryLanguageList.forEach(countryLanguage -> {
                        String languageCode = countryLanguage.getLanguageCode();
                        List<String> sameLanguageCodeList = countryLanguageService.listOneField(new LambdaQueryWrapper<CountryLanguage>()
                                .in(languageCodeFunc, languageCode)
                                .eq(enableFlagFunc, YesOrNoEnum.YES)
                                .eq(typeFunc, type)
                                .eq(singleLanguageFlagFunc, YesOrNoEnum.NO), codeFunc
                        );
                        if (CollUtil.isNotEmpty(sameLanguageCodeList)) {
                            List<StandardColumnCountryTranslate> countryTranslateList = updateNewTranslateList.stream()
                                    .filter(it -> it.getCountryLanguageId().equals(countryLanguage.getId())).collect(Collectors.toList());

                            // 获取已审核的当前国家中,存在对应编码的数据
                            List<StyleCountryStatus> statusList = styleCountryStatusService.list(new LambdaQueryWrapper<StyleCountryStatus>()
                                    .eq(statusFunc, StyleCountryStatusEnum.CHECK)
                                    .in(StyleCountryStatus::getCode, sameLanguageCodeList)
                                    .eq(StyleCountryStatus::getType, type)
                                    .like(StyleCountryStatus::getStandardColumnCode, standardColumnCode)
                            );
                            statusList.forEach(status-> {
                                List<MoreLanguageStatusCheckDetailDTO> languageDetailList = JSONUtil.toList(status.getCheckDetailJson(), MoreLanguageStatusCheckDetailDTO.class);
                                languageDetailList.stream().filter(it-> it.getLanguageCode().equals(languageCode)).forEach(languageDetail-> {
                                    countryTranslateList.forEach(translate -> {
                                        languageDetail.getAuditList().stream()
                                                .filter(audit -> audit.getStandardColumnCode().equals(standardColumnCode))
                                                .filter(audit -> audit.getSource().equals(translate.getPropertiesCode()))
                                                .forEach(audit-> audit.setStatus(YesOrNoEnum.NO.getValueStr()));
                                    });
                                });
                                String checkDetailJson = JSONUtil.toJsonStr(languageDetailList.stream().filter(it -> CollUtil.isNotEmpty(it.getAuditList())).collect(Collectors.toList()));
                                if (!status.getCheckDetailJson().equals(checkDetailJson)) {
                                    status.setCheckDetailJson(checkDetailJson);
                                    changeStatusList.add(status);
                                }
                            });
                        }
                    });
                    if (CollUtil.isNotEmpty(changeStatusList)) {
                        styleCountryStatusService.saveOrUpdateBatch(changeStatusList);
                    }
                });

                // 同步其他type但隐藏的数据
                taskList.add(() -> {
                    List<CountryLanguageDto> typeList = exportMapping.getTotalCountryLanguageList().stream().filter(it -> it.getType() != type).collect(Collectors.toList());
                    List<String> propertiesCodeList = updateNewTranslateList.stream().map(propertiesCodeFunc).collect(Collectors.toList());

                    boolean isRoot = StandardColumnType.findRootList().contains(standardColumnType);
                    List<StandardColumnType> standardColumnTypeList = typeList.stream().flatMap(it ->
                            it.getType().getStandardColumnType().getChildrenTypeList().stream()
                    ).distinct().collect(Collectors.toList());
                    boolean needSync = standardColumnService.exists(new BaseLambdaQueryWrapper<StandardColumn>()
                            .in(StandardColumn::getCode, isRoot ? propertiesCodeList : Collections.singletonList(standardColumnCode))
                            .eq(StandardColumn::getShowFlag, YesOrNoEnum.NO)
                            .in(StandardColumn::getType, standardColumnTypeList)
                    );

                    if (needSync) {
                        Set<String> titleCodeList = new HashSet<>();
                        if (isRoot) {
                            titleCodeList.addAll(standardColumnService.listOneField(new BaseLambdaQueryWrapper<StandardColumn>()
                                    .in(StandardColumn::getType,StandardColumnType.findRootList())
                                    .ne(StandardColumn::getType, standardColumnType), StandardColumn::getCode
                            ));
                        }else titleCodeList.add(standardColumnCode);

                        List<StandardColumnCountryTranslate> syncTranslateList = new ArrayList<>();
                        titleCodeList.forEach(titleCode-> {
                            List<StandardColumnCountryTranslate> list = standardColumnCountryTranslateService.list(new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                                    .in(countryLanguageIdFunc, typeList.stream().map(idFunc).collect(Collectors.toList()))
                                    .in(propertiesCodeFunc, propertiesCodeList)
                                    .eq(titleCodeFunc, titleCode)
                            );

                            syncTranslateList.addAll(typeList.stream().flatMap(countryLanguage ->
                                    BeanUtil.copyToList(updateNewTranslateList, StandardColumnCountryTranslate.class).stream().peek(translate -> {
                                        translate.setId(null);
                                        translate.setCreateDate(null);
                                        translate.updateClear();
                                        translate.setTitleCode(titleCode);
                                        list.stream().filter(it-> translate.getTitleCode().equals(it.getTitleCode()) && translate.getPropertiesCode().equals(it.getPropertiesCode()))
                                                .findFirst().ifPresent(oldTranslate-> {
                                                    translate.setId(oldTranslate.getId());
                                                    translate.setCreateDate(oldTranslate.getCreateDate());
                                                });
                                        translate.setCountryLanguageId(countryLanguage.getId());
                                    })
                            ).collect(Collectors.toList()));
                        });

                        standardColumnCountryTranslateService.saveOrUpdateBatch(syncTranslateList);
                    }
                });
            }
            removeMapping(context);
//        };
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(servletRequestAttributes,true);
        taskList.forEach(task-> {
            threadPoolExecutor.execute(TtlRunnable.get(task));
        });
    }

    private Pair<List<StandardColumnCountryTranslate>, List<StandardColumnCountryTranslate>> diffTranslateList(
            String standardColumnName,
            AnalysisContext context,
            List<StandardColumnCountryTranslate> translateList,
            List<CountryLanguageDto> countryLanguageList,
            List<StandardColumnCountryTranslate> updateOldTranslateList,
            Boolean addDataVerifyResult
    ){
        List<StandardColumnCountryTranslate> updateNewTranslateList = new ArrayList<>();
        List<StandardColumnCountryTranslate> addTranslateList = new ArrayList<>();
        for (int i = 0; i < translateList.size(); i++) {
            StandardColumnCountryTranslate translate = translateList.get(i);
            CountryLanguageDto countryLanguageDto = countryLanguageList.stream().filter(it -> it.getId().equals(translate.getCountryLanguageId())).findFirst().orElse(null);
            if (countryLanguageDto == null) {
                addDataVerifyResult(addDataVerifyResult, context, i,"国家语言","未找到");
                continue;
            }
            translate.setTitleName(standardColumnName);

            Optional<StandardColumnCountryTranslate> countryTranslateOpt = updateOldTranslateList.stream()
                    .filter(it ->
                            it.getPropertiesCode().equals(translate.getPropertiesCode())
                                    &&
                            it.getCountryLanguageId().equals(countryLanguageDto.getId())
                    ).findFirst();
            if (countryTranslateOpt.isPresent()) {
                StandardColumnCountryTranslate countryTranslate = countryTranslateOpt.get();
                translate.setId(countryTranslate.getId());
                translate.setPropertiesName(countryTranslate.getPropertiesName());
                translate.setTitleName(countryTranslate.getTitleName());
                if (!StrUtil.equals(Opt.ofNullable(translate.getContent()).map(String::trim).orElse(null),
                        Opt.ofNullable(countryTranslate.getContent()).map(String::trim).orElse(null)
                )) {
                    updateNewTranslateList.add(translate);
                }
            }else {
                translate.setId(null);
                addTranslateList.add(translate);
            }

            // 检查数据其他数据正确性(重新查询一遍数据) TODO
            if (StrUtil.isBlank(translate.getContent())) {
                addDataVerifyResult(addDataVerifyResult, context, i,translate.getPropertiesName() + "的" + countryLanguageDto.getLanguageName()+"翻译内容","未输入");
            }
        }
        return Pair.of(updateNewTranslateList, addTranslateList);
    }

    public void addDataVerifyResult(AnalysisContext context, Integer rowIndex, String key, String value) {
        addDataVerifyResult(true, context, rowIndex, key, value);
    }

    /**
     * 展示验证信息
     * */
    public void addDataVerifyResult(Boolean addDataVerifyResult, AnalysisContext context, Integer rowIndex, String key, String value) {
        if (!addDataVerifyResult) return;
        List<DataVerifyResultVO> dataVerifyResultVOS = dataVerifyResults.get();
        if (dataVerifyResultVOS == null) {
            dataVerifyResultVOS = new ArrayList<>();
        }
        String sheetName = context.readSheetHolder().getSheetName();

        DataVerifyResultVO dataVerifyResultVO = dataVerifyResultVOS.stream()
                .filter(it -> sheetName.equals(it.getSheetName()) && rowIndex.equals(it.getRowIndex()))
                .findFirst().orElse(new DataVerifyResultVO(sheetName, rowIndex, new HashMap<>()));

        dataVerifyResultVO.getErrorMessageList().put(key,value);
        dataVerifyResultVOS.add(dataVerifyResultVO);

        dataVerifyResults.set(dataVerifyResultVOS);
    }

    /**
     * 展示验证信息
     * */
    public String dataVerifyHandler() {
        List<DataVerifyResultVO> dataVerifyResultVOS = dataVerifyResults.get();
        StringJoiner joiner = new StringJoiner("、");
        if (CollectionUtil.isNotEmpty(dataVerifyResultVOS)) {
            dataVerifyResultVOS.stream().collect(Collectors.groupingBy(sheetNameFunc)).forEach((sheetName, sameSheetList)-> {
                sameSheetList.stream().min(Comparator.comparing(rowIndexFunc)).ifPresent(dataVerifyResultVO -> {
                    List<String> keyList = new ArrayList<>(dataVerifyResultVO.getErrorMessageList().keySet());
                    if (CollectionUtil.isNotEmpty(keyList)) {
                        String key = keyList.get(0);
                        String value = dataVerifyResultVO.getErrorMessageList().get(key);
                        String msg = sheetName + "有" + key + value;
                        joiner.add(msg);
                    }

                });
            });
        }
        dataVerifyResults.remove();
        return joiner.toString();
    }

}

