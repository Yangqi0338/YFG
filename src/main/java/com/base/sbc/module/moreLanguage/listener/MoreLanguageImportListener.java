package com.base.sbc.module.moreLanguage.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.moreLanguage.dto.DataVerifyResultVO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExportBaseDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageMapExportMapping;
import com.base.sbc.module.moreLanguage.entity.Country;
import com.base.sbc.module.moreLanguage.entity.CountryModel;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.entity.StandardColumnTranslate;
import com.base.sbc.module.moreLanguage.service.CountryModelService;
import com.base.sbc.module.moreLanguage.service.CountryService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService;
import com.base.sbc.module.moreLanguage.service.StandardColumnTranslateService;
import com.base.sbc.module.moreLanguage.service.impl.CountryModelServiceImpl;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.base.sbc.config.constant.Constants.COMMA;


@Slf4j
@Component
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
     * 导入数据批次编号
     */
    public static ThreadLocal<String> importBatchNo = new ThreadLocal<>();
    public static ThreadLocal<Map<String, MoreLanguageMapExportMapping>> sheetExportMapping = new ThreadLocal<>();

    @Autowired
    private StandardColumnTranslateService standardColumnTranslateService;

    @Autowired
    private CountryModelService countryModelService;

    @Autowired
    private StandardColumnCountryTranslateService standardColumnCountryTranslateService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private StandardColumnService standardColumnService;

    @Setter
    private String countryLanguageId;


    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        getMapping(context,(mapExportMapping)-> {
            mapExportMapping.setHeadMap(headMap);
            return true;
        });
    }

    private <T> T getMapping(AnalysisContext context, Function<MoreLanguageMapExportMapping, T> function){
        Map<String, MoreLanguageMapExportMapping> exportMapping = sheetExportMapping.get();
        if (exportMapping == null) {
            exportMapping = new HashMap<>();
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

    // 解析

    @Override
    public void invoke(Map<Integer, String> value, AnalysisContext context) {
        Pair<MoreLanguageMapExportMapping, MoreLanguageExportBaseDTO> exportPair = getMapping(context, (mapExportMapping)-> {
            MoreLanguageExportBaseDTO tempBaseDTO = mapExportMapping.initByFirstRow(value, MoreLanguageExportBaseDTO.class);
            return Pair.of(mapExportMapping, tempBaseDTO);
        });
        MoreLanguageMapExportMapping exportMapping = exportPair.getKey();
        MoreLanguageExportBaseDTO exportBaseDTO = exportPair.getValue();
        Map<String, String> map = exportMapping.buildMap(value);

        StandardColumnCountryTranslate baseSource;
        // 设置值
        if (exportBaseDTO != null) {
            // 初始化
            String key = exportBaseDTO.getKey();
            String keyName = exportBaseDTO.getKeyName();
            String standardColumnCode = exportBaseDTO.getStandardColumnCode();

            baseSource = new StandardColumnCountryTranslate();
            baseSource.setCountryLanguageId(countryLanguageId);
            baseSource.setTitleCode(standardColumnCode);
            baseSource.setPropertiesCode(key);
            baseSource.setPropertiesName(keyName);

            exportMapping.setBaseSourceData(baseSource);
        }else {
            baseSource = exportMapping.getBaseSourceData();
        }
        String content = map.get("content");
        String code = Arrays.stream(baseSource.getPropertiesCode().split("-"))
                .map(map::get).collect(Collectors.joining("-"));
        String name = Arrays.stream(baseSource.getPropertiesName().split("-"))
                .map(map::get).collect(Collectors.joining("-"));

        // 获取TableData,查看数据是否修改
        // 也可以使用隐藏列,缺点是会被修改移除

        if (StrUtil.isBlank(content) || StrUtil.isBlank(code)) return;

        StandardColumnCountryTranslate countryTranslate = BeanUtil.copyProperties(baseSource, StandardColumnCountryTranslate.class);
        countryTranslate.setPropertiesCode(code);
        countryTranslate.setPropertiesName(name);
        countryTranslate.setContent(content);

        List<StandardColumnCountryTranslate> translateList = exportMapping.getSourceData();
        if (CollectionUtil.isEmpty(translateList)) {
            translateList = new ArrayList<>();
        }
        translateList.add(countryTranslate);
        exportMapping.setSourceData(translateList);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doAfterAllAnalysed(AnalysisContext context) {
        long timeMillis = System.currentTimeMillis();
        MoreLanguageMapExportMapping exportMapping = getMapping(context, (mapExportMapping) -> mapExportMapping);
        List<StandardColumnCountryTranslate> translateList = exportMapping.getSourceData();
        if (CollectionUtil.isEmpty(translateList)) return;
        StandardColumnCountryTranslate baseTranslate = exportMapping.getBaseSourceData();
        String countryLanguageId = baseTranslate.getCountryLanguageId();
        String titleCode = baseTranslate.getTitleCode();

        Country country = countryService.getById(countryLanguageId);
        if (country == null) throw new OtherException("未找到国家");

        System.out.println("asdasda----a-" + (System.currentTimeMillis() - timeMillis));
        Object titleObject = standardColumnService.findByCode(titleCode);
        if (titleObject == null) throw new OtherException("未找到标准列");
        StandardColumn standardColumn = (StandardColumn) titleObject;
        System.out.println("asdasda----b-" + (System.currentTimeMillis() - timeMillis));

        for (StandardColumnCountryTranslate it : translateList) {
            it.setTitleName(standardColumn.getName());
            String code = String.join(COMMA, it.getCountryLanguageId(), it.getTitleCode(), Opt.ofNullable(it.getPropertiesCode()).orElse(" "));
            StandardColumnCountryTranslate countryTranslate = standardColumnCountryTranslateService.findByCode(code);
            if (countryTranslate != null) {
                it.setId(countryTranslate.getId());
                it.setPropertiesName(countryTranslate.getPropertiesName());
                it.setTitleName(countryTranslate.getTitleName());
            }
        }
        System.out.println("asdasda----c-" + (System.currentTimeMillis() - timeMillis));

        standardColumnCountryTranslateService.saveOrUpdateBatch(translateList);
        System.out.println("asdasda----c1-" + translateList.size() +"-" + (System.currentTimeMillis() - timeMillis));
        // 号型和表头特殊 设置专门的表存储,数据较少,直接删除新增.
        countryModelService.remove(new BaseLambdaQueryWrapper<CountryModel>().eq(CountryModel::getCountryLanguageId, countryLanguageId));
        System.out.println("asdasda----d-" + (System.currentTimeMillis() - timeMillis));
        List<CountryModel> translateModelList = translateList.stream().filter(it -> "DP06".equals(it.getTitleCode())).map(it -> {
            CountryModel countryModel = new CountryModel();
            countryModel.setCountryLanguageId(countryLanguageId);
            String propertiesCode = it.getPropertiesCode();
            String propertiesName = it.getPropertiesName();
            countryModel.setModelCode(propertiesCode.split("-")[1]);
            countryModel.setModelName(propertiesName.split("-")[1]);
            countryModel.setBasicSizeCode(propertiesCode.split("-")[0]);
            countryModel.setBasicSizeName(propertiesName.split("-")[0]);
            countryModel.setContent(it.getContent());
            return countryModel;
        }).collect(Collectors.toList());
        countryModelService.saveBatch(translateModelList);

        standardColumnTranslateService.remove(new BaseLambdaQueryWrapper<StandardColumnTranslate>().eq(StandardColumnTranslate::getCountryLanguageId, countryLanguageId));
        System.out.println("asdasda----e-" + (System.currentTimeMillis() - timeMillis));
        List<StandardColumnTranslate> translateTitleList = translateList.stream().filter(it -> "DP00".equals(it.getTitleCode())).map(it -> {
            StandardColumnTranslate standardColumnTranslate = new StandardColumnTranslate();
            standardColumnTranslate.setCountryLanguageId(countryLanguageId);
            standardColumnTranslate.setStandardColumnCode(it.getPropertiesCode());
            standardColumnTranslate.setStandardColumnName(it.getPropertiesName());
            standardColumnTranslate.setContent(it.getContent());
            return standardColumnTranslate;
        }).collect(Collectors.toList());
        standardColumnTranslateService.saveBatch(translateTitleList);

        removeMapping(context);

        System.out.println("asdasda----f" + (System.currentTimeMillis() - timeMillis));
    }

    /**
     * 销毁资源
     */
    public static void destroyResource() {
        importBatchNo.remove();
        sheetExportMapping.remove();
        log.info(MoreLanguageImportListener.logPrefix + "线程{}资源销毁...", Thread.currentThread().getName());
    }

}

