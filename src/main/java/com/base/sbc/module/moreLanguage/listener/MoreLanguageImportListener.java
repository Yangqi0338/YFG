package com.base.sbc.module.moreLanguage.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
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
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
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
    private static ThreadLocal<List<DataVerifyResultVO>> dataVerifyResults = new ThreadLocal<>();
    /**
     * 导入数据批次编号
     */
    public static ThreadLocal<String> importBatchNo = new ThreadLocal<>();
    public static ThreadLocal<Map<String, MoreLanguageMapExportMapping>> sheetExportMapping = new ThreadLocal<>();

    private final StandardColumnTranslateService standardColumnTranslateService;

    private final CountryModelService countryModelService;

    private final StandardColumnCountryTranslateService standardColumnCountryTranslateService;

    private final CountryLanguageService countryLanguageService;

    private final StandardColumnService standardColumnService;

    @Setter
    private MoreLanguageExcelQueryDto excelQueryDto;

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
        Pair<MoreLanguageMapExportMapping, MoreLanguageExportBaseDTO> exportPair = getMapping(context, (mapExportMapping)-> {
            MoreLanguageExportBaseDTO tempBaseDTO = mapExportMapping.initByFirstRow(value, MoreLanguageExportBaseDTO.class);
            return Pair.of(mapExportMapping, tempBaseDTO);
        });
        MoreLanguageMapExportMapping exportMapping = exportPair.getKey();
        MoreLanguageExportBaseDTO exportBaseDTO = exportPair.getValue();

        Map<String, String> map = exportMapping.buildMap(value);
        List<CountryLanguageDto> countryLanguageList = exportMapping.getCountryLanguageList();

        StandardColumnCountryTranslate baseSource;
        // 设置值
        if (exportBaseDTO != null) {
            // 初始化
            String key = exportBaseDTO.getKey();
            String keyName = exportBaseDTO.getKeyName();
            String uniqueCode = exportBaseDTO.getStandardColumnCode();

            String[] uniqueCodeArray = uniqueCode.split(RedisKeyBuilder.COMMA);
            if (uniqueCodeArray.length <= 1) {
                throw new OtherException("隐藏的关键唯一标识值不正确,请重新导入");
            }
            CountryLanguageType type = CountryLanguageType.findByCode(uniqueCodeArray[0]);
            exportMapping.setType(type);

            baseSource = new StandardColumnCountryTranslate();
            baseSource.setTitleCode(uniqueCodeArray[1]);
            baseSource.setPropertiesCode(key);
            baseSource.setPropertiesName(keyName);

            exportMapping.setBaseSourceData(baseSource);
        }else {
            baseSource = exportMapping.getBaseSourceData();
        }

        if (CollectionUtil.isEmpty(countryLanguageList)) {
            CountryQueryDto countryQueryDto = BeanUtil.copyProperties(excelQueryDto, CountryQueryDto.class);
            countryQueryDto.setType(exportMapping.getType());
            countryLanguageList = new ArrayList<>(countryLanguageService.listQuery(countryQueryDto));
            if (CollectionUtil.isEmpty(countryLanguageList)) {
                throw new OtherException("未找到对应的国家语言数据");
            }
            exportMapping.setCountryLanguageList(countryLanguageList);
        }

        String code = Arrays.stream(baseSource.getPropertiesCode().split("-"))
                .map(map::remove).collect(Collectors.joining("-"));
        String name = Arrays.stream(baseSource.getPropertiesName().split("-"))
                .map(map::get).collect(Collectors.joining("-"));

        // 获取TableData,查看数据是否修改
        // 也可以使用隐藏列,缺点是会被修改移除

        if (StrUtil.isBlank(code)) return;

        StandardColumnCountryTranslate baseCountryTranslate = BeanUtil.copyProperties(baseSource, StandardColumnCountryTranslate.class);
        baseCountryTranslate.setPropertiesCode(code);
        baseCountryTranslate.setPropertiesName(name);

        List<StandardColumnCountryTranslate> translateList = exportMapping.getSourceData();

        countryLanguageList.forEach(countryLanguage -> {
            StandardColumnCountryTranslate countryTranslate = BeanUtil.copyProperties(baseCountryTranslate, StandardColumnCountryTranslate.class);
            countryTranslate.setCountryLanguageId(countryLanguage.getId());
            StringJoiner joiner = new StringJoiner("-");
            if (excelQueryDto.getSingleLanguageFlag() != YesOrNoEnum.YES) {
                joiner.add(countryLanguage.getLanguageCode());
            }
            joiner.add("content");
            String content = map.getOrDefault(joiner.toString(), "");
            countryTranslate.setContent(content);
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
        String codeName = countryLanguageList.get(0).getName();
        String name = "多语言翻译";
        if (excelQueryDto.getSingleLanguageFlag() == YesOrNoEnum.YES) {
            name = "单语言翻译";
            code = excelQueryDto.getLanguageCode();
            codeName = countryLanguageList.get(0).getLanguageName();
        };
        baseEntity.setName(name);
        baseEntity.setPath(code + "-" + type.getCode());
        baseEntity.setContent(codeName + "-" + type.getText());
        List<StandardColumnCountryTranslate> updateNewTranslateList = new ArrayList<>();
        List<StandardColumnCountryTranslate> addTranslateList = new ArrayList<>();

        List<String> countryLanguageIdList = countryLanguageList.stream().map(CountryLanguage::getId).collect(Collectors.toList());
        List<StandardColumnCountryTranslate> updateOldTranslateList = standardColumnCountryTranslateService.list(
                new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                        .in(StandardColumnCountryTranslate::getCountryLanguageId, countryLanguageIdList)
                        .eq(StandardColumnCountryTranslate::getTitleCode, standardColumnCode)
        );

        for (int i = 0; i < translateList.size(); i++) {
            StandardColumnCountryTranslate translate = translateList.get(i);
            CountryLanguageDto countryLanguageDto = countryLanguageList.stream().filter(it -> it.getId().equals(translate.getCountryLanguageId())).findFirst().orElse(null);
            if (countryLanguageDto == null) {
                addDataVerifyResult(context, i,"国家语言","未找到");
                return;
            }
            translate.setTitleName(standardColumnName);

            Optional<StandardColumnCountryTranslate> countryTranslateOpt = updateOldTranslateList.stream()
                    .filter(it -> it.getCountryLanguageId().equals(countryLanguageDto.getId()) && it.getPropertiesCode().equals(translate.getPropertiesCode()))
                    .findFirst();
            if (countryTranslateOpt.isPresent()) {
                StandardColumnCountryTranslate countryTranslate = countryTranslateOpt.get();
                translate.setId(countryTranslate.getId());
                translate.setPropertiesName(countryTranslate.getPropertiesName());
                translate.setTitleName(countryTranslate.getTitleName());
                updateNewTranslateList.add(translate);
            }else {
                addTranslateList.add(translate);
            }

            // 检查数据其他数据正确性(重新查询一遍数据) TODO
            if (StrUtil.isBlank(translate.getContent())) {
                addDataVerifyResult(context, i,translate.getPropertiesName() + "的" + countryLanguageDto.getLanguageName()+"翻译内容","未输入");
            }
        }
        baseEntity.setType("新增");
        standardColumnCountryTranslateService.saveBatchOperaLog(addTranslateList, baseEntity);
        baseEntity.setType("修改");
        standardColumnCountryTranslateService.updateBatchOperaLog(updateOldTranslateList, updateNewTranslateList, baseEntity);
        standardColumnCountryTranslateService.saveOrUpdateBatch(translateList);

        // 号型和表头特殊 设置专门的表存储,数据较少,直接删除新增.
        countryModelService.remove(new BaseLambdaQueryWrapper<CountryModel>().in(CountryModel::getCountryLanguageId, countryLanguageIdList));
        List<CountryModel> translateModelList = translateList.stream().filter(it -> "DP06".equals(it.getTitleCode())).map(it -> {
            CountryModel countryModel = new CountryModel();
            countryModel.setCountryLanguageId(it.getCountryLanguageId());
            String propertiesCode = it.getPropertiesCode();
            String propertiesName = it.getPropertiesName();
            countryModel.setModelCode(propertiesCode.split("-")[1]);
            countryModel.setModelName(propertiesName.split("-")[1]);
            countryModel.setBasicSizeCode(propertiesCode.split("-")[0]);
            countryModel.setBasicSizeName(propertiesName.split("-")[0]);
            countryModel.setContent(it.getContent());
            return countryModel;
        }).collect(Collectors.toList());
        countryModelService.saveOrUpdateBatch(translateModelList);

        standardColumnTranslateService.remove(new BaseLambdaQueryWrapper<StandardColumnTranslate>().eq(StandardColumnTranslate::getCountryLanguageId, countryLanguageIdList));
        List<StandardColumnTranslate> translateTitleList = translateList.stream().filter(it -> "DP00".equals(it.getTitleCode())).map(it -> {
            StandardColumnTranslate standardColumnTranslate = new StandardColumnTranslate();
            standardColumnTranslate.setCountryLanguageId(it.getCountryLanguageId());
            standardColumnTranslate.setStandardColumnCode(it.getPropertiesCode());
            standardColumnTranslate.setStandardColumnName(it.getPropertiesName());
            standardColumnTranslate.setContent(it.getContent());
            return standardColumnTranslate;
        }).collect(Collectors.toList());
        standardColumnTranslateService.saveOrUpdateBatch(translateTitleList);

        removeMapping(context);
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

