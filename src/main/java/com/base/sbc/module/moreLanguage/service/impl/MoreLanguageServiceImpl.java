/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.config.enums.business.SystemSource;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBCSVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageWebBaseVO;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.EasyPoiMapExportParam;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExcelQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExportBaseDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelTemplateDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageTableTitle;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.mapper.StandardColumnCountryTranslateMapper;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableContext;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableContext.MoreLanguageTableParamEnum;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableContext.MoreLanguageTableTitleHandlerEnum;
import com.base.sbc.module.standard.dto.StandardColumnDto;
import com.base.sbc.module.standard.dto.StandardColumnQueryDto;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import com.github.pagehelper.PageInfo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.formula.functions.Count;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.INCORRECT_STANDARD_CODE;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.NOT_EXIST_STANDARD_CODE;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.NOT_FOUND_COUNTRY_LANGUAGE;
import static com.base.sbc.module.common.convert.ConvertContext.MORE_LANGUAGE_CV;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.idFunc;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.languageCodeFunc;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.singleLanguageFlagFunc;
import static com.base.sbc.module.moreLanguage.service.impl.CountryLanguageServiceImpl.typeFunc;
import static com.base.sbc.module.moreLanguage.service.impl.StandardColumnCountryTranslateServiceImpl.contentFunc;
import static com.base.sbc.module.moreLanguage.service.impl.StandardColumnCountryTranslateServiceImpl.countryLanguageIdFunc;
import static com.base.sbc.module.moreLanguage.service.impl.StandardColumnCountryTranslateServiceImpl.propertiesCodeFunc;
import static com.base.sbc.module.moreLanguage.service.impl.StandardColumnCountryTranslateServiceImpl.titleCodeFunc;
import static com.base.sbc.module.moreLanguage.service.impl.StandardColumnCountryTranslateServiceImpl.updateDateFunc;

/**
 * 类描述：吊牌列头翻译表 service类
 * @address com.base.sbc.module.moreLanguage.service.TagTranslateService
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:58
 * @version 1.0  
 */
@Service
public class MoreLanguageServiceImpl implements MoreLanguageService {

    @Autowired
    private CountryLanguageService countryLanguageService;

    @Autowired
    private StandardColumnCountryTranslateService standardColumnCountryTranslateService;

    @Autowired
    private StandardColumnService standardColumnService;

    @Autowired
    private BasicsdatumSizeService basicsdatumSizeService;

    @Autowired
    private StandardColumnCountryTranslateMapper standardColumnCountryTranslateMapper;

    @Autowired
    private HangTagService hangTagService;

    @Autowired
    private CcmFeignService ccmFeignService;

    public static final SFunction<MoreLanguageTableTitle, Integer> keyFunc = MoreLanguageTableTitle::getKey;
    public static final SFunction<MoreLanguageTableTitle, Integer> keyNameFunc = MoreLanguageTableTitle::getKeyName;

// 自定义方法区 不替换的区域【other_start】

    @Override
    public  List<StandardColumnDto> queryCountryTitle(MoreLanguageQueryDto moreLanguageQueryDto) {
//        ForkJoinPool workPool = new ForkJoinPool(2, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);

        // 查询根标准列
//        ForkJoinTask<List<StandardColumnDto>> rootStandardColumnFuture = workPool.submit(() -> {
//            StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
//            queryDto.setTypeList(StandardColumnType.findRootList());
//            return standardColumnService.listQuery(queryDto);
//        }).fork();

        // 设置国家
        CountryQueryDto countryQueryDto = BeanUtil.copyProperties(moreLanguageQueryDto, CountryQueryDto.class);
        List<CountryLanguageDto> countryLanguageList = new ArrayList<>(countryLanguageService.listQuery(countryQueryDto));
        if (CollectionUtil.isEmpty(countryLanguageList)) {
            throw new OtherException(MoreLanguageProperties.getMsg(NOT_FOUND_COUNTRY_LANGUAGE));
        }

        CountryLanguageDto baseCountryLanguage = countryLanguageList.get(0);
        String code = baseCountryLanguage.getCode();
        CountryLanguageType type = baseCountryLanguage.getType();

        // 获取缓存中的标准列
        List<StandardColumn> standardColumnList = countryLanguageService.findStandardColumnList(code, type ,moreLanguageQueryDto.isCache());

        // 做语言判断
        MoreLanguageTableParamEnum.IN_CACHE.setParam(moreLanguageQueryDto.getCache());
        MoreLanguageTableTitleHandlerEnum.COPY.setParam(JSONUtil.toJsonStr(countryLanguageList));

        // 只查询可配置的
        standardColumnList.removeIf(it-> it.getModel().equals(StandardColumnModel.TEXT));
        standardColumnList.removeIf(it-> it.getShowFlag() != YesOrNoEnum.YES);

        // 根据id排序,并逐一装饰对应的title
        return BeanUtil.copyToList(standardColumnList, StandardColumnDto.class).stream()
                .sorted(Comparator.comparing(StandardColumnDto::getId))
                .peek(MoreLanguageTableContext::decorateTitleJson)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportExcel(MoreLanguageExcelQueryDto excelQueryDto) {
        CopyOnWriteArrayList<EasyPoiMapExportParam> exportParams = new CopyOnWriteArrayList<>();
        ExcelExportService excelExportService = new ExcelExportService();

        // 封装数据查询的QueryDTO
        MoreLanguageQueryDto languageQueryDto = BeanUtil.copyProperties(excelQueryDto,MoreLanguageQueryDto.class);
        languageQueryDto.setCache(YesOrNoEnum.NO.getValueStr());
        languageQueryDto.reset2QueryList();

        /* ----------------------------数据库查询---------------------------- */

        // 获取导出拥有的国家语言
        CountryQueryDto countryQueryDto = BeanUtil.copyProperties(excelQueryDto,CountryQueryDto.class);
        List<CountryLanguageDto> countryLanguageDtoList = countryLanguageService.listQuery(countryQueryDto);
        if (CollectionUtil.isEmpty(countryLanguageDtoList)) {
            throw new OtherException(MoreLanguageProperties.getMsg(NOT_FOUND_COUNTRY_LANGUAGE));
        }
        CountryLanguageDto baseCountryLanguage = countryLanguageDtoList.get(0);
        YesOrNoEnum singleLanguageFlag = baseCountryLanguage.getSingleLanguageFlag();
        String code = baseCountryLanguage.getCode();

        // 提取拥有的类型(有可能新增时没有选择洗唛?)
        // 多线程导出(和redis有冲突TODO)
//        AtomicInteger threadNum = new AtomicInteger();
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 8,
//                0L, TimeUnit.MILLISECONDS,
//                new ArrayBlockingQueue<>(16), r -> {
//                    Thread thread = new Thread(r,"多语言导出-"+threadNum.incrementAndGet());
//                    thread.setUncaughtExceptionHandler((Thread t,Throwable e) -> {
//                        if(e != null){
//                            throw new OtherException(e.getMessage());
//                        }
//                    });
//                    return thread;
//                });
        List<CountryLanguageType> typeList = countryLanguageDtoList.stream().map(typeFunc).distinct().collect(Collectors.toList());
        // 标识哪一个sheet的哪一行需要做空值判断
        Map<String, List<Integer>> sheetBlankCellNumMap = new HashMap<>();
        boolean lackHandler = YesOrNoEnum.YES == excelQueryDto.getShowLack();

        /* ----------------------------处理数据---------------------------- */

        typeList.forEach(type-> {
            // 获取当前国家当前类型拥有的吊牌字段列表
            languageQueryDto.setType(type);
            languageQueryDto.setStandardColumnCode(null);
            MoreLanguageTableParamEnum.IN_EXCEL.setBooleanParam(YesOrNoEnum.YES);
            List<StandardColumnDto> standardColumnDtoList = this.queryCountryTitle(languageQueryDto);

            for (StandardColumnDto standardColumnDto : standardColumnDtoList) {
//                threadPoolExecutor.execute(()-> {
                    String standardColumnCode = standardColumnDto.getCode();
                    String tableTitleJson = standardColumnDto.getTableTitleJson();
                    if (StrUtil.isBlank(tableTitleJson)) {
                        return;
                    }

                    String sheetName = type.getText() + "-" + standardColumnDto.getName();
                    ExportParams params = new ExportParams(MoreLanguageProperties.excelTitle, sheetName, ExcelType.XSSF);
                    List<ExcelExportEntity> beanList = new ArrayList<>();

                    // 设置ExcelPoi的导出参数
                    EasyPoiMapExportParam exportParam = new EasyPoiMapExportParam();
                    exportParam.setTitle(params);
                    exportParam.setEntity(beanList);

                    // json转表头配置列表并遍历 (!!)
                    List<MoreLanguageTableTitle> tableTitleList = JSONUtil.toList(tableTitleJson, MoreLanguageTableTitle.class);
                    for (int i = 0; i < tableTitleList.size(); i++) {
                        MoreLanguageTableTitle moreLanguageTableTitle = tableTitleList.get(i);
                        // 封装ExcelPoi的导出实体
                        ExcelExportEntity exportEntity = new ExcelExportEntity(moreLanguageTableTitle.getText(), moreLanguageTableTitle.getCode());
                        // 设置其他参数,是否隐藏,宽度
                        exportEntity.setColumnHidden(moreLanguageTableTitle.isExcelHidden());
                        if (moreLanguageTableTitle.getExcelWidth() != null) {
                            exportEntity.setWidth(moreLanguageTableTitle.getExcelWidth());
                        }
                        // 若需要做空值处理,并获取多语翻译字段的row的列数
                        if (lackHandler && moreLanguageTableTitle.getCode().contains(MoreLanguageProperties.excelTranslateContentField)) {
                            List<Integer> lackCheckCellNumList = sheetBlankCellNumMap.getOrDefault(sheetName, new ArrayList<>());
                            lackCheckCellNumList.add(i);
                            sheetBlankCellNumMap.put(sheetName, lackCheckCellNumList);
                        }
                        beanList.add(exportEntity);
                    }

                    // 拼装多个key,若没有取第一个
                    String key = findUniqueCode(tableTitleList, keyFunc, 0);
                    // 拼装多个keyName,若没有取第二个
                    String keyName = findUniqueCode(tableTitleList, keyNameFunc, 1);

                    // 封装导出的基础类
                    MoreLanguageExportBaseDTO exportBaseDTO = new MoreLanguageExportBaseDTO();
                    // 类转map,并和表头配置列表去重
                    Map<String, Object> baseTitleMap = BeanUtil.beanToMap(exportBaseDTO);
                    beanList.removeIf(it-> baseTitleMap.containsKey(it.getKey().toString()));

                    // 设置基础数据
                    exportBaseDTO.setStandardColumnCode(type.getCode() + RedisKeyBuilder.COMMA + standardColumnCode);
                    exportBaseDTO.setKey(key);
                    exportBaseDTO.setKeyName(keyName);
                    exportBaseDTO.setExcelCode(singleLanguageFlag == YesOrNoEnum.YES ? baseCountryLanguage.getLanguageCode() : code);

                    // 添加map默认表头,否则无法支持导入
                    beanList.addAll(buildExportEntityListByClass(MoreLanguageExportBaseDTO.class));

                    // 获取列表数据
                    MoreLanguageQueryDto moreLanguageQueryDto = BeanUtil.copyProperties(languageQueryDto, MoreLanguageQueryDto.class);
                    moreLanguageQueryDto.setStandardColumnCode(standardColumnCode);
                    MoreLanguageTableParamEnum.IN_EXCEL.setBooleanParam(YesOrNoEnum.YES);
                    List<Map<String, Object>> mapList = listQuery(moreLanguageQueryDto).getList();

                    if (CollectionUtil.isNotEmpty(mapList)) {
                        Map<String, Object> firstRow = mapList.get(0);
                        // 封装当前sheet的每一列的数据映射排布设置到第一行 (!!)
                        exportBaseDTO.setMapping(JSONUtil.toJsonStr(beanList.stream().collect(Collectors.toMap(ExcelExportEntity::getName, ExcelExportEntity::getKey))));
                        firstRow.putAll(BeanUtil.beanToMap(exportBaseDTO, false, true));
                    }

                    // 设置数据
                    exportParam.setData(mapList);
                    exportParams.add(exportParam);
//                });
            }
        });

//        threadPoolExecutor.shutdown();
//        threadPoolExecutor.awaitTermination(30,TimeUnit.MINUTES);

        /* ----------------------------处理导出以及样式调整---------------------------- */

        buildMapSheet(String.format("(%s)吊牌&洗唛多语言-%s.xlsx", baseCountryLanguage.getName(), System.currentTimeMillis()),
                exportParams, sheetBlankCellNumMap
        );
    }

    @Override
    public PageInfo<Map<String,Object>> listQuery(MoreLanguageQueryDto moreLanguageQueryDto) {
        boolean cache = moreLanguageQueryDto.isCache();
        /* ----------------------------检查+基础数据查询---------------------------- */

        String standardColumnCode = moreLanguageQueryDto.getStandardColumnCode();
        if (StrUtil.isBlank(standardColumnCode)) {
            throw new OtherException(MoreLanguageProperties.getMsg(INCORRECT_STANDARD_CODE));
        }

        // 先查询国家存在什么语言
        CountryQueryDto countryQueryDto = BeanUtil.copyProperties(moreLanguageQueryDto, CountryQueryDto.class);
        List<CountryLanguageDto> countryLanguageList = countryLanguageService.listQuery(countryQueryDto);
        if (CollectionUtil.isEmpty(countryLanguageList)) {
            throw new OtherException(MoreLanguageProperties.getMsg(NOT_FOUND_COUNTRY_LANGUAGE));
        }

        CountryLanguageDto baseCountryLanguage = countryLanguageList.get(0);
        String code = baseCountryLanguage.getCode();
        CountryLanguageType type = baseCountryLanguage.getType();

        // 查redis
        StandardColumn standardColumn = MoreLanguageProperties.getStandardColumn(standardColumnService, type, standardColumnCode);
        List<String> standardColumnCodeList = countryLanguageService.findStandardColumnCodeList(code, type, cache);

        if (!standardColumnCodeList.contains(standardColumnCode)) {
            throw new OtherException(MoreLanguageProperties.getMsg(INCORRECT_STANDARD_CODE));
        }

        // 获取单语言
        CountryQueryDto languageDto = new CountryQueryDto();
        languageDto.setType(type);
        languageDto.setLanguageCode(countryLanguageList.stream()
                .map(it-> it.getLanguageCodeByColumnCode(standardColumnCode))
                .distinct().collect(Collectors.joining(COMMA)));
        languageDto.setSingleLanguageFlag(YesOrNoEnum.YES);
        List<CountryLanguageDto> languageDtoList = countryLanguageService.listQuery(languageDto);

        /* ----------------------------处理表头+翻译数据查询---------------------------- */

        // 获取翻译的表头 找差集
        String tableTitleJson = standardColumn.getTableTitleJson();
        if (StrUtil.isBlank(tableTitleJson)) throw new OtherException(MoreLanguageProperties.getMsg(NOT_EXIST_STANDARD_CODE));

        // 解析当前标准列的表头列表
        List<MoreLanguageTableTitle> tableTitleList = JSONUtil.toList(tableTitleJson, MoreLanguageTableTitle.class);

        // 获取翻译这个数据库表的所有在表头列表的字段
        List<String> translateFieldList = Arrays.stream(StandardColumnCountryTranslate.class.getDeclaredFields())
                .map(Field::getName)
                .filter(fieldName-> tableTitleList.stream().anyMatch(it-> fieldName.equals(it.getCode())))
                .collect(Collectors.toList());

        // 剪裁掉翻译的字段
        tableTitleList.removeIf(tableTitle -> translateFieldList.contains(tableTitle.getCode()));

        // 装饰输出表头 (!!)
        MoreLanguageTableParamEnum.IN_CACHE.setParam(moreLanguageQueryDto.getCache());
        MoreLanguageTableTitleHandlerEnum.COPY.setParam(JSONUtil.toJsonStr(countryLanguageList));
        List<String> showFieldList = JSONUtil.toList(MoreLanguageTableContext.decorateTitleJson(standardColumn), MoreLanguageTableTitle.class).stream()
                .map(MoreLanguageTableTitle::getCode).collect(Collectors.toList());

        standardColumn.setTableTitleJson(JSONUtil.toJsonStr(tableTitleList));

        // 封装已存在的全部关联标准列编码
        MoreLanguageTableParamEnum.OWNER_TAG_CODE.setParam(type + "————" + String.join(COMMA, standardColumnCodeList));
        // 获取源数据,map (!!)
        PageInfo<Map<String, Object>> mapList = MoreLanguageTableContext.getTableData(moreLanguageQueryDto, standardColumn);

        // 获取所有关键编码的翻译key列表
        List<Map<String, Object>> resultList = mapList.getList();

        // 获取多键集合,没有就取第一个
        List<String> codeList = Opt.ofEmptyAble(tableTitleList.stream()
                .filter(it -> ObjectUtil.isNotEmpty(keyFunc.apply(it))).sorted(Comparator.comparing(keyFunc))
                .collect(Collectors.toList())
        ).orElse(Collections.singletonList(tableTitleList.get(0))).stream().map(it-> StrUtil.toUnderlineCase(it.getCode())).collect(Collectors.toList());
        List<String> propertiesCodeList = new ArrayList<>();
        resultList.forEach(resultMap-> {
            String realPropertiesCode = resultMap.entrySet().stream()
                    .filter(it -> codeList.contains(it.getKey()))
                    .map(it -> it.getValue().toString())
                    .collect(Collectors.joining("-"));
            resultMap.put("realPropertiesCode", realPropertiesCode);
            propertiesCodeList.add(realPropertiesCode);
        });

        // 查询翻译 也可以找redis,不常修改
        List<StandardColumnCountryTranslate> translateList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(propertiesCodeList)) {
            // 查询翻译
            translateList.addAll(standardColumnCountryTranslateService.list( new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                    .eq(titleCodeFunc, standardColumnCode)
                    .in(propertiesCodeFunc, propertiesCodeList)
                    .in(countryLanguageIdFunc, languageDtoList.stream().map(idFunc).collect(Collectors.toList()))
                    .and(qw-> qw.isNotNull(contentFunc).ne(contentFunc,""))));
            // 将单语言的翻译修正为模板翻译id
            translateList.forEach(translate-> {
                languageDtoList.stream().filter(it -> translate.getCountryLanguageId().equals(it.getId())).findFirst().flatMap(countryLanguageDto ->
                        countryLanguageList.stream().filter(it ->
                                countryLanguageDto.getLanguageCode().equals(it.getLanguageCodeByColumnCode(standardColumnCode))
                                        && countryLanguageDto.getType() == it.getType()
                        ).findFirst()
                ).ifPresent(countryLanguage -> {
                    translate.setCountryLanguageId(countryLanguage.getId());
                });
            });
        }

        /* ----------------------------处理数据---------------------------- */

        // 时间的处理,若表头有基础信息，通过这个设置
        List<String> baseEntityFieldNameList = Arrays.stream(BaseDataEntity.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        // 设置数据
        mapList.setList(resultList.stream().map(resultMap-> {
            // 获取当前数据的关键key
            String propertiesKey = resultMap.get("realPropertiesCode").toString();
            Map<String, Object> map = new HashMap<>(showFieldList.size());
            // 更新时间倒序获取标题翻译 (DP00的)
            List<StandardColumnCountryTranslate> translatePropertiesList = translateList.stream()
                    .filter(it -> it.getPropertiesCode().equals(propertiesKey))
                    .sorted(Comparator.comparing(updateDateFunc).reversed()).collect(Collectors.toList());
            // 遍历需要展示的每个表头
            showFieldList.forEach(field->{
                Object fieldValue = null;
                // 设置中断回滚点
                breakPoint:
                for (StandardColumnCountryTranslate translate : translatePropertiesList) {
                    // 获取当前存在的每个翻译的国家
                    CountryLanguageDto countryLanguage = countryLanguageList.stream()
                            .filter(it -> it.getId().equals(translate.getCountryLanguageId())).findFirst()
                            .orElse(new CountryLanguageDto());
                    // 将翻译实体类转为map
                    Map<String, Object> translateResultMap = BeanUtil.beanToMap(translate);
                    for (Map.Entry<String, Object> entry : translateResultMap.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        // 如果在翻译中找不到,有可能是洗唛多语言,添加一个语言编码尝试匹配
                        if (!field.equals(key)) {
                            key = (countryLanguage.getLanguageCode() + "-" + key);
                        }
                        // 如果匹配到,直接回到中断点,否则没有翻译值
                        if (field.contains(key)) {
                            fieldValue = value;
                            break breakPoint;
                        }
                    }
                }
                // 找除了翻译的其他值,并一一修改
                // 先转为下划线
                String fieldKey = StrUtil.toUnderlineCase(field);
                if (fieldValue == null && !baseEntityFieldNameList.contains(field) && resultMap.containsKey(fieldKey)) {
                    fieldValue = resultMap.get(fieldKey);
                }
                // 对日期进行处理, 封装字符串列表
                if (fieldValue instanceof Date) {
                    fieldValue = DateUtil.format((Date) fieldValue, "yyyy-MM-dd HH:mm:ss");
                }
                map.put(field, Opt.ofNullable(fieldValue).orElse(""));
            });
            return map;
        }).collect(Collectors.toList()));

        return mapList;
    }

    @Override
    public List<Map<String, Object>> listAllByTable(String fields, String tableName, String where) {
        // 为了动态sql查询
        return standardColumnCountryTranslateMapper.listAllByTable(fields, tableName, where);
    }

    @Override
    public List<StandardColumnDto> findStandardColumn(String code) {
        StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
        String[] split = code.split("————");
        if (StrUtil.isNotBlank(split[0])) {
            queryDto.setTypeList(Collections.singletonList(StandardColumnType.valueOf(split[0])));
        }
        queryDto.setCodeList(Arrays.asList(split[1].split(COMMA)));

        return standardColumnService.listQuery(queryDto);
    }

    @Override
    public List<BasicsdatumSize> findSize(String code) {
        // 为号型特殊写的反射方法
        List<BasicsdatumSize> result = new ArrayList<>();
        // 获取所有基础尺码
        List<BasicsdatumSize> list = basicsdatumSizeService.list();
        // 根据号型分组
        list.stream().collect(Collectors.groupingBy(BasicsdatumSize::getModelTypeCode, LinkedHashMap::new, Collectors.toList()))
                .forEach((modelTypeCode, sameCodeList)-> {
                    // 切分编码和名字
                    String[] modelTypeCodeArray = modelTypeCode.split(COMMA);
                    String[] modelTypeArray = sameCodeList.get(0).getModelType().split(COMMA);
                    // 封装对应的size
                    for (int i = 0; i < modelTypeCodeArray.length; i++) {
                        for (BasicsdatumSize size : sameCodeList) {
                            BasicsdatumSize newSize = MORE_LANGUAGE_CV.copyMyself(size);
                            newSize.setModelTypeCode(modelTypeCodeArray[i]);
                            newSize.setModelType(modelTypeArray[i]);
                            result.add(newSize);
                        }
                    }
                });
        return result;
    }

    @SneakyThrows
    @Override
    public void exportMergeExcel(HangTagMoreLanguageDTO moreLanguageDTO) {
        moreLanguageDTO.setSource(SystemSource.PDM);
        moreLanguageDTO.setSingleLanguageFlag(YesOrNoEnum.YES);

        List<BasicBaseDict> dictList = ccmFeignService.getDictInfoToList(MoreLanguageProperties.languageDictCode);
        // 中文排第一个
        dictList.sort((a,b)-> {
            if (MoreLanguageProperties.checkInternal(a.getValue())) return -1;
            if (MoreLanguageProperties.checkInternal(b.getValue())) return 1;
            return a.getValue().compareTo(b.getValue());
        });

        // 获取每个对应语言和号型的国家
        List<CountryLanguageDto> list = MORE_LANGUAGE_CV.copyList2Dto(countryLanguageService.list(new BaseLambdaQueryWrapper<CountryLanguage>()
                .in(languageCodeFunc, dictList.stream().map(BasicBaseDict::getValue).collect(Collectors.toList()))
                .eq(singleLanguageFlagFunc, YesOrNoEnum.YES)
        ));

        List<String> codeList = new ArrayList<>();
        dictList.forEach(dict -> {
            CountryLanguage countryLanguage = list.stream().filter(it -> it.getLanguageCode().equals(dict.getValue()))
                    .findFirst().orElseThrow(() -> new OtherException("请维护" + dict.getName() + "的单语言翻译"));
            codeList.add(countryLanguage.getCode());
        });
        moreLanguageDTO.setCode(String.join(COMMA,codeList));
        Map<StandardColumnType, List<HangTagMoreLanguageWebBaseVO>> resultMap = (Map<StandardColumnType, List<HangTagMoreLanguageWebBaseVO>>)
                hangTagService.getMoreLanguageDetailsByBulkStyleNo(moreLanguageDTO);

        EasyPoiMapExportParam exportParam = new EasyPoiMapExportParam();
        ExportParams params = new ExportParams("全语言翻译", "全语言翻译", ExcelType.XSSF);
        exportParam.setTitle(params);

        // 封装excel的字段
        List<ExcelExportEntity> beanList = new ArrayList<>(buildExportEntityListByClass(MoreLanguageStatusExcelTemplateDTO.class));
        exportParam.setEntity(beanList);
        int dynamicExportEntityStartIndex = beanList.size();
        dictList.forEach(languageDict -> {
            ExcelExportEntity exportEntity = new ExcelExportEntity(languageDict.getName(), languageDict.getValue());
            exportEntity.setWidth(100);
            beanList.add(exportEntity);
        });

        List<Map<String, Object>> mapList = new ArrayList<>();
        exportParam.setData(mapList);
        StrUtil.split(moreLanguageDTO.getBulkStyleNo(), COMMA).forEach(bulkStyleNo-> {
            MoreLanguageStatusExcelTemplateDTO excelTemplateDTO = new MoreLanguageStatusExcelTemplateDTO();
            excelTemplateDTO.setBulkStyleNo(bulkStyleNo);
            resultMap.forEach((countryLanguageType, sameTypeList) -> {
                sameTypeList.stream().filter(it-> it.getBulkStyleNo().equals(bulkStyleNo))
                        .collect(Collectors.groupingBy(HangTagMoreLanguageWebBaseVO::getStandardColumnCode)).forEach((standardColumnCode, sameStandardList)-> {
                            Map<String, Object> map = BeanUtil.beanToMap(excelTemplateDTO);
                            mapList.add(map);
                            dictList.forEach(languageDict -> {
                                String languageCode = languageDict.getValue();
                                String content = "";
                                if (MoreLanguageProperties.checkInternal(languageCode)) {
                                    content = sameStandardList.get(0).getSourceContent();
                                } else {
                                    Optional<HangTagMoreLanguageWebBaseVO> webBaseVOOpt = sameStandardList.stream()
                                            .filter(webBaseVO -> webBaseVO.getLanguageList().stream().anyMatch(it -> it.getLanguageCode().equals(languageCode)))
                                            .findFirst();
                                    if (webBaseVOOpt.isPresent()) {
                                        content = webBaseVOOpt.get().getContent().getOrDefault(languageCode, "");
                                    }
                                }
                                map.put(languageCode, content);
                            });
                        });
            });
        });

        /* ----------------------------处理导出以及样式调整---------------------------- */

        buildMapSheet(String.format("吊牌&洗唛多语言翻译导出-%s.xlsx", System.currentTimeMillis()), Collections.singletonList(exportParam),
                MapUtil.of(params.getSheetName(),IntStream.range(dynamicExportEntityStartIndex, beanList.size()).boxed().collect(Collectors.toList())));
    }

    private List<ExcelExportEntity> buildExportEntityListByClass(Class<?> clazz){
        List<ExcelExportEntity> beanList = new ArrayList<>();
        Field[] fileds = PoiPublicUtil.getClassFields(clazz);
        ExcelTarget etarget = clazz.getAnnotation(ExcelTarget.class);
        String targetId = etarget == null ? null : etarget.value();

        // 直接在ExcelExportService拷出来的
        // poi源代码,将基础类各个属性转为ExcelPoi的导出实体并添加到处理列表 (!!)
        ExcelUtils.getAllExcelField(new String[] {}, targetId, fileds, beanList, clazz, null, null);
        return beanList;
    }

    private <T extends Comparable<? super T>> String findUniqueCode(List<MoreLanguageTableTitle> tableTitleList, SFunction<MoreLanguageTableTitle, T> keyFunc, Integer defaultIndex){
        return Opt.ofBlankAble(
                tableTitleList.stream().filter(it -> ObjectUtil.isNotEmpty(keyFunc.apply(it))).sorted(Comparator.comparing(keyFunc))
                        .map(MoreLanguageTableTitle::getCode)
                        .collect(Collectors.joining("-"))
        ).orElse(tableTitleList.get(defaultIndex).getCode());
    }

    @SneakyThrows
    private void buildMapSheet(String fileName, List<EasyPoiMapExportParam> exportParams, Map<String, List<Integer>> sheetBlankCellNumMap){
        if (CollectionUtil.isEmpty(exportParams)) return;
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 这里URLEncoder.encode可以防止中文乱码
        fileName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");

        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        Workbook workbook = null;
        ExcelExportService excelExportService = new ExcelExportService();
        try (OutputStream out = response.getOutputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ) {
            // 遍历每个sheet的导出实体
            for (EasyPoiMapExportParam exportParam : exportParams) {
                ExportParams title = exportParam.getTitle();
                if (workbook == null) {
                    // 初始化并创建第一个自定义表头的sheet
                    workbook = ExcelExportUtil.exportExcel(title, exportParam.getEntity(), exportParam.getData());
                } else {
                    // 创建自定义表头的sheet
                    excelExportService.createSheetForMap(workbook, title, exportParam.getEntity(), exportParam.getData());
                }

                Sheet sheet = workbook.getSheet(title.getSheetName());

                // 修正表头高度
                Row titleRow = sheet.getRow(0);
                titleRow.setHeightInPoints(80);
                // 修正表头样式
                CellStyle titleStyle = workbook.createCellStyle();
                titleStyle.setAlignment(HorizontalAlignment.LEFT);
                titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                titleStyle.setWrapText(false);
                titleRow.setRowStyle(titleStyle);

                // 获取需要处理的列
                List<Integer> lackCheckCellNumList = sheetBlankCellNumMap.getOrDefault(sheet.getSheetName(), new ArrayList<>());
                int rowCount = sheet.getLastRowNum();
                // 从第三行开始遍历，跳过表头
                for (int i = MoreLanguageProperties.excelDataRowNum; i <= rowCount; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        int cellCount = row.getLastCellNum();
                        // 判断翻译是否为空
                        boolean needHandlerLack = lackCheckCellNumList.stream()
                                .anyMatch(cellNum -> StrUtil.isBlank(row.getCell(cellNum).getStringCellValue()));
                        for (int j = 0; j < cellCount; j++) {
                            Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            CellStyle cellStyle = workbook.createCellStyle();
                            cellStyle.cloneStyleFrom(cell.getCellStyle());
                            Font font = workbook.createFont();
                            if (needHandlerLack) {
                                // 为空,将整段样式改为红色字体
                                font.setColor(IndexedColors.RED.getIndex());
                            }
                            String stringCellValue = cell.getStringCellValue();
                            if (StrUtil.isNotBlank(stringCellValue) && stringCellValue.contains("\n")) {
                                cell.setCellValue(new XSSFRichTextString(stringCellValue));
                                cellStyle.setWrapText(true);
                                row.setHeight((short) -1);
                            }
                            cellStyle.setFont(font);
                            cell.setCellStyle(cellStyle);
                        }
                    }
                }

//                    CellStyle cellStyle = workbook.createCellStyle();
//                    cellStyle.setAlignment(HorizontalAlignment.LEFT);
//                    sheet.setDefaultColumnStyle(0, cellStyle);
            }

            workbook.write(baos);
            response.setHeader("Content-Length", String.valueOf(baos.size()));
            out.write( baos.toByteArray() );

        }finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

// 自定义方法区 不替换的区域【other_end】
	
}
