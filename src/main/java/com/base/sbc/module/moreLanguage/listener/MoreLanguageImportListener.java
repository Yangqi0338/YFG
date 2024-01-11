package com.base.sbc.module.moreLanguage.listener;

import cn.hutool.core.bean.BeanUtil;
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
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.DataVerifyResultVO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExcelQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExportBaseDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageMapExportMapping;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.CountryModel;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.entity.StandardColumnTranslate;
import com.base.sbc.module.moreLanguage.service.CountryModelService;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService;
import com.base.sbc.module.moreLanguage.service.StandardColumnTranslateService;
import com.base.sbc.module.operalog.dto.OperaLogJsonDto;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;


/**
 * @author KC
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MoreLanguageImportListener extends AnalysisEventListener<Map<Integer, String>> {
    /**
     * 批次号前缀
     */
    public static final String importBatchSerialNumberPrefix = "MLI";
    /**
     * 日志输出前缀
     */
    public static final String logPrefix = "【多语言-数据导入】:";
    /**
     * 数据合法性校验结果
     */
    private static ThreadLocal<List<DataVerifyResultVO>> dataVerifyResults = new TransmittableThreadLocal<>();
    /**
     * 导入数据批次编号
     */
    public static ThreadLocal<String> importBatchNo = new TransmittableThreadLocal<>();
    public static ThreadLocal<Map<String, MoreLanguageMapExportMapping>> sheetExportMapping = new TransmittableThreadLocal<>();

    private final StandardColumnTranslateService standardColumnTranslateService;

    private final CountryModelService countryModelService;

    private final StandardColumnCountryTranslateService standardColumnCountryTranslateService;

    private final CountryLanguageService countryLanguageService;

    private final StandardColumnService standardColumnService;

    @Setter
    private MoreLanguageExcelQueryDto excelQueryDto;

    private ThreadPoolExecutor threadPoolExecutor;

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
            countryQueryDto.setType(exportMapping.getType());
            List<CountryLanguageDto> countryLanguageList = countryLanguageService.listQuery(countryQueryDto);
            if (CollectionUtil.isEmpty(countryLanguageList)) {
                throw new OtherException("未找到对应的国家语言数据");
            }
            exportMapping.setCountryLanguageList(countryLanguageList);

            List<CountryLanguageDto> singleLanguageList = new ArrayList<>();
            if (!isSingleLanguageFlag) {
                CountryQueryDto queryDto = new CountryQueryDto();
                queryDto.setLanguageCode(countryLanguageList.stream().map(CountryLanguage::getLanguageCode).collect(Collectors.joining(",")));
                countryQueryDto.setType(exportMapping.getType());
                queryDto.setSingleLanguageFlag(YesOrNoEnum.YES);
                singleLanguageList.addAll(countryLanguageService.listQuery(queryDto));
                if (CollectionUtil.isEmpty(singleLanguageList)) {
                    throw new OtherException("未找到对应的语言数据");
                }
            }
            exportMapping.setSingleLanguageList(singleLanguageList);

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
            if (!map.containsKey(name)) throw new OtherException(String.format("%s第%s行未获取到编码", sheetName, rowIndex));
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
                int i = keyList.indexOf(contentKey);
                String valueStr = map.get(key);
                if (StrUtil.isNotBlank(valueStr)) {
                    String[] valueList = valueStr.split(",");
                    countryTranslate.setContent(valueList[Math.min(valueList.length - 1, i)]);
                }
            });
            translateList.add(countryTranslate);
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

            OperaLogEntity baseEntity = new OperaLogEntity();
            baseEntity.setDocumentName(standardColumnName);
            baseEntity.setDocumentCode(standardColumnCode);
            String code = excelQueryDto.getCode();
            String codeName = countryLanguageList.get(0).getCountryName();
            String name = "多语言翻译";
            if (excelQueryDto.getSingleLanguageFlag() == YesOrNoEnum.YES) {
                name = "单语言翻译";
                code = excelQueryDto.getLanguageCode();
                codeName = countryLanguageList.get(0).getLanguageName();
            };
            baseEntity.setName(name);
            baseEntity.setPath(code + ":" + type.getCode());
            baseEntity.setContent(codeName + ":" + type.getText());
            List<StandardColumnCountryTranslate> updateNewTranslateList = new ArrayList<>();
            List<StandardColumnCountryTranslate> addTranslateList = new ArrayList<>();

            List<String> countryLanguageIdList = countryLanguageList.stream().map(CountryLanguage::getId).collect(Collectors.toList());
            List<StandardColumnCountryTranslate> updateOldTranslateList = standardColumnCountryTranslateService.list(
                    new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                            .in(StandardColumnCountryTranslate::getCountryLanguageId, countryLanguageIdList)
                            .eq(StandardColumnCountryTranslate::getTitleCode, standardColumnCode)
            );

            List<CountryLanguageDto> singleLanguageList = exportMapping.getSingleLanguageList();
            if (CollectionUtil.isNotEmpty(singleLanguageList)) {
                List<String> singleLanguageIdList = singleLanguageList.stream().map(CountryLanguage::getId).collect(Collectors.toList());
                updateOldTranslateList.addAll(standardColumnCountryTranslateService.list(
                        new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                                .in(StandardColumnCountryTranslate::getCountryLanguageId, singleLanguageIdList)
                                .eq(StandardColumnCountryTranslate::getTitleCode, standardColumnCode)
                ));
            }

            for (int i = 0; i < translateList.size(); i++) {
                StandardColumnCountryTranslate translate = translateList.get(i);
                CountryLanguageDto countryLanguageDto = countryLanguageList.stream().filter(it -> it.getId().equals(translate.getCountryLanguageId())).findFirst().orElse(null);
                if (countryLanguageDto == null) {
                    addDataVerifyResult(context, i,"国家语言","未找到");
                    return;
                }
                translate.setTitleName(standardColumnName);

                CountryLanguageDto languageDto = singleLanguageList.stream().filter(it -> it.getLanguageCode().equals(countryLanguageDto.getLanguageCode())).findFirst().orElse(new CountryLanguageDto());

                Optional<StandardColumnCountryTranslate> countryTranslateOpt = updateOldTranslateList.stream()
                        .filter(it ->
                                (it.getCountryLanguageId().equals(countryLanguageDto.getId())
                                        ||
                                it.getCountryLanguageId().equals(languageDto.getId())
                                ) && it.getPropertiesCode().equals(translate.getPropertiesCode()))
                        .findFirst();
                if (countryTranslateOpt.isPresent()) {
                    StandardColumnCountryTranslate countryTranslate = countryTranslateOpt.get();
                    translate.setId(countryTranslate.getId());
                    translate.setPropertiesName(countryTranslate.getPropertiesName());
                    translate.setTitleName(countryTranslate.getTitleName());
                    if (!StrUtil.equals(Opt.ofNullable(translate.getContent()).map(String::trim).orElse(null),
                            Opt.ofNullable(countryTranslate.getContent()).map(String::trim).orElse(null)
                    )) { updateNewTranslateList.add(translate);}
                }else {
                    addTranslateList.add(translate);
                }

                // 检查数据其他数据正确性(重新查询一遍数据) TODO
                if (StrUtil.isBlank(translate.getContent())) {
                    addDataVerifyResult(context, i,translate.getPropertiesName() + "的" + countryLanguageDto.getLanguageName()+"翻译内容","未输入");
                }
            }
            taskList.add(()-> {
                if (CollectionUtil.isNotEmpty(addTranslateList)) {
                    OperaLogEntity addLogEntity = BeanUtil.copyProperties(baseEntity, OperaLogEntity.class);
                    addLogEntity.setType("新增");
                    List<OperaLogJsonDto> operaLogJsonList = new ArrayList<>();
                    addTranslateList.stream().sorted(Comparator.comparing(StandardColumnCountryTranslate::getPropertiesCode)).forEach(translate-> {
                        operaLogJsonList.add(new OperaLogJsonDto(translate.getPropertiesName(), "" , translate.getContent()));
                    });
                    addLogEntity.setJsonContent(JSONUtil.toJsonStr(operaLogJsonList));
                    standardColumnCountryTranslateService.saveOperaLog(addLogEntity);
                }
                if (CollectionUtil.isNotEmpty(updateNewTranslateList)) {
                    OperaLogEntity updateLogEntity = BeanUtil.copyProperties(baseEntity, OperaLogEntity.class);
                    updateLogEntity.setType("修改");
                    List<OperaLogJsonDto> operaLogJsonList = new ArrayList<>();
                    updateNewTranslateList.stream().sorted(Comparator.comparing(StandardColumnCountryTranslate::getPropertiesCode)).forEach(translate-> {
                        updateOldTranslateList.stream().filter(it-> it.getId().equals(translate.getId())).findFirst().ifPresent(oldTranslate-> {
                            operaLogJsonList.add(new OperaLogJsonDto(translate.getPropertiesName(), oldTranslate.getContent() , translate.getContent()));
                        });
                    });
                    updateLogEntity.setJsonContent(JSONUtil.toJsonStr(operaLogJsonList));
                    standardColumnCountryTranslateService.saveOperaLog(updateLogEntity);
                }
            });
            updateNewTranslateList.addAll(addTranslateList);
            if (CollectionUtil.isNotEmpty(updateNewTranslateList)) {
                standardColumnCountryTranslateService.saveOrUpdateBatch(updateNewTranslateList);
                // 修改其他同语种的翻译,多线程 TODO
                taskList.add(()-> {
                    List<CountryLanguageDto> finalCountryLanguageList = new ArrayList<>(countryLanguageList);
                    List<StandardColumnCountryTranslate> finalUpdateNewTranslateList = new ArrayList<>(updateNewTranslateList);

                    List<StandardColumnCountryTranslate> linkNewTranslateList = new ArrayList<>();

                    List<String> languageCodeList = finalCountryLanguageList.stream().map(CountryLanguage::getLanguageCode).distinct().collect(Collectors.toList());
                    List<CountryLanguage> sameLanguageCodeList = countryLanguageService.list(new LambdaQueryWrapper<CountryLanguage>()
                            .select(CountryLanguage::getId, CountryLanguage::getLanguageCode)
                            .in(CountryLanguage::getLanguageCode, languageCodeList)
                            .eq(CountryLanguage::getEnableFlag, YesOrNoEnum.YES)
                            .eq(CountryLanguage::getType, type)
                    );
                    List<String> languageIdList = sameLanguageCodeList.stream().map(CountryLanguage::getLanguageCode).distinct().collect(Collectors.toList());
                    List<String> propertiesCodeList = finalUpdateNewTranslateList.stream().map(StandardColumnCountryTranslate::getPropertiesCode).distinct().collect(Collectors.toList());
                    List<StandardColumnCountryTranslate> sameLanguageCodeTransalteList = standardColumnCountryTranslateService.list(
                            new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                                    .in(StandardColumnCountryTranslate::getCountryLanguageId, languageIdList)
                                    .eq(StandardColumnCountryTranslate::getTitleCode, standardColumnCode)
                                    .in(StandardColumnCountryTranslate::getPropertiesCode, propertiesCodeList)
                    );
                    for (int i = 0; i < finalUpdateNewTranslateList.size(); i++) {
                        StandardColumnCountryTranslate translate = finalUpdateNewTranslateList.get(i);

                        CountryLanguageDto countryLanguageDto = finalCountryLanguageList.stream()
                                .filter(it -> it.getId().equals(translate.getCountryLanguageId()))
                                .findFirst().orElse(null);
                        if (countryLanguageDto == null) {
                            addDataVerifyResult(context, i,"国家语言","未找到");
                            return;
                        }

                        // 找到相关联的languageCode
                        sameLanguageCodeList.stream().filter(it -> it.getLanguageCode().equals(countryLanguageDto.getLanguageCode())).forEach(linkCountryLanguage-> {
                            StandardColumnCountryTranslate linkBaseTranslate = BeanUtil.copyProperties(translate, StandardColumnCountryTranslate.class);
                            String countryLanguageId = linkCountryLanguage.getId();
                            linkBaseTranslate.setCountryLanguageId(countryLanguageId);
                            Optional<StandardColumnCountryTranslate> countryTranslateOpt = sameLanguageCodeTransalteList.stream()
                                    .filter(it ->
                                            it.getCountryLanguageId().equals(countryLanguageId)
                                                    &&
                                            it.getPropertiesCode().equals(linkBaseTranslate.getPropertiesCode())
                                    ).findFirst();
                            if (countryTranslateOpt.isPresent()) {
                                StandardColumnCountryTranslate countryTranslate = countryTranslateOpt.get();
                                linkBaseTranslate.setId(countryTranslate.getId());
                                linkNewTranslateList.add(linkBaseTranslate);
                            }else {
                                linkNewTranslateList.add(linkBaseTranslate);
                            }
                        });

                        standardColumnCountryTranslateService.saveOrUpdateBatch(linkNewTranslateList);
                    }
                });
                // 号型和表头特殊 设置专门的表存储,数据较少,直接删除新增.
                countryModelService.remove(new BaseLambdaQueryWrapper<CountryModel>()
                        .in(CountryModel::getCountryLanguageId, countryLanguageIdList)
                        .eq(CountryModel::getType, type)
                );
                List<String> modelStandardCodeList = Arrays.asList("DP06", "XM08");
                List<CountryModel> translateModelList = updateNewTranslateList.stream().filter(it -> modelStandardCodeList.contains(it.getTitleCode())).map(it -> {
                    CountryModel countryModel = new CountryModel();
                    countryModel.setCountryLanguageId(it.getCountryLanguageId());
                    String propertiesCode = it.getPropertiesCode();
                    String propertiesName = it.getPropertiesName();
                    countryModel.setModelCode(propertiesCode.split("-")[1]);
                    countryModel.setModelName(propertiesName.split("-")[1]);
                    countryModel.setBasicSizeCode(propertiesCode.split("-")[0]);
                    countryModel.setBasicSizeName(propertiesName.split("-")[0]);
                    countryModel.setContent(it.getContent());
                    countryModel.setType(type);
                    return countryModel;
                }).collect(Collectors.toList());
                countryModelService.saveOrUpdateBatch(translateModelList);

                List<String> titleStandardCodeList = Arrays.asList("DP00", "XM00");
                standardColumnTranslateService.remove(new BaseLambdaQueryWrapper<StandardColumnTranslate>()
                        .eq(StandardColumnTranslate::getCountryLanguageId, countryLanguageIdList)
                        .eq(StandardColumnTranslate::getType, type)
                );
                List<StandardColumnTranslate> translateTitleList = updateNewTranslateList.stream().filter(it -> titleStandardCodeList.contains(it.getTitleCode())).map(it -> {
                    StandardColumnTranslate standardColumnTranslate = new StandardColumnTranslate();
                    standardColumnTranslate.setCountryLanguageId(it.getCountryLanguageId());
                    standardColumnTranslate.setStandardColumnCode(it.getPropertiesCode());
                    standardColumnTranslate.setStandardColumnName(it.getPropertiesName());
                    standardColumnTranslate.setContent(it.getContent());
                    standardColumnTranslate.setType(type);
                    return standardColumnTranslate;
                }).collect(Collectors.toList());
                standardColumnTranslateService.saveOrUpdateBatch(translateTitleList);
            }

            removeMapping(context);
//        };
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(servletRequestAttributes,true);
        taskList.forEach(task-> {
            threadPoolExecutor.execute(TtlRunnable.get(task));
        });
    }

    /**
     * 展示验证信息
     * */
    public void addDataVerifyResult(AnalysisContext context, Integer rowIndex, String key, String value) {
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
            dataVerifyResultVOS.stream().collect(Collectors.groupingBy(DataVerifyResultVO::getSheetName)).forEach((sheetName, sameSheetList)-> {
                sameSheetList.stream().min(Comparator.comparing(DataVerifyResultVO::getRowIndex)).ifPresent(dataVerifyResultVO -> {
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
        return joiner + ", 请问是否需要导入?";
    }

}

