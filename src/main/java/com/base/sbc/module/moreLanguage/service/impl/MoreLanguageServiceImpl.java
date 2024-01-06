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
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.EasyPoiMapExportParam;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExcelQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExportBaseDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageTableTitle;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryRelation;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.mapper.StandardColumnCountryTranslateMapper;
import com.base.sbc.module.moreLanguage.service.CountryModelService;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService;
import com.base.sbc.module.moreLanguage.service.StandardColumnTranslateService;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableContext;
import com.base.sbc.module.standard.dto.StandardColumnDto;
import com.base.sbc.module.standard.dto.StandardColumnQueryDto;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import com.github.pagehelper.PageInfo;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.jetbrains.annotations.NotNull;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

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
    private CountryModelService countryModelService;

    @Autowired
    private StandardColumnCountryTranslateService standardColumnCountryTranslateService;

    @Autowired
    private StandardColumnTranslateService standardColumnTranslateService;

    @Autowired
    private StandardColumnCountryRelationService relationService;

    @Autowired
    private StandardColumnService standardColumnService;

    @Autowired
    private StandardColumnCountryTranslateMapper standardColumnCountryTranslateMapper;

// 自定义方法区 不替换的区域【other_start】

    @Override
    public  List<StandardColumnDto> queryCountryTitle(MoreLanguageQueryDto moreLanguageQueryDto) {
//        ForkJoinPool workPool = new ForkJoinPool(2, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);

        /* ----------------------------可以分成两个线程进行操作---------------------------- */

        // 查询根标准列
//        ForkJoinTask<List<StandardColumnDto>> rootStandardColumnFuture = workPool.submit(() -> {
//            StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
//            queryDto.setTypeList(StandardColumnType.findRootList());
//            return standardColumnService.listQuery(queryDto);
//        }).fork();

//        ForkJoinTask<List<StandardColumnDto>> standardColumnFuture = workPool.submit(() -> {
            // 查询 关联表
            // 可以从redis拿,只有countryAdd和导入会更新,频率低
//            List<String> standardColumnCodeList = findStandardColumnCodeList(countryLanguageId);
//
//            // 只查询可配置的
//            StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
//            queryDto.setNoModel(StandardColumnModel.TEXT);
//            queryDto.setCodeList(standardColumnCodeList);
//            return standardColumnService.listQuery(queryDto);
//        }).fork();

        // 设置国家
        CountryQueryDto countryQueryDto = BeanUtil.copyProperties(moreLanguageQueryDto, CountryQueryDto.class);
        List<CountryLanguageDto> countryLanguageList = new ArrayList<>(countryLanguageService.listQuery(countryQueryDto));
        if (CollectionUtil.isEmpty(countryLanguageList)) {
            throw new OtherException("未找到对应的国家语言数据");
        }

        CountryLanguageDto baseCountryLanguage = countryLanguageList.get(0);
        String code = baseCountryLanguage.getCode();
        CountryLanguageType type = baseCountryLanguage.getType();
        YesOrNoEnum singleLanguageFlag = baseCountryLanguage.getSingleLanguageFlag();

        List<StandardColumn> standardColumnList = countryLanguageService.findStandardColumnList(code, type ,moreLanguageQueryDto.isCache());

        // 做语言判断
        MoreLanguageTableContext.MoreLanguageTableParamEnum.NO_DECORATE.setBooleanParam(singleLanguageFlag);
        MoreLanguageTableContext.MoreLanguageTableParamEnum.IN_CACHE.setParam(moreLanguageQueryDto.getCache());
        MoreLanguageTableContext.setParam(MapUtil.of(
                MoreLanguageTableContext.MoreLanguageTableTitleHandlerEnum.COPY.getHandlerKey(), JSONUtil.toJsonStr(countryLanguageList)
        ));

        // 只查询可配置的
        standardColumnList.removeIf(it-> it.getModel().equals(StandardColumnModel.TEXT));

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

        // 获取导出拥有的国家语言
        CountryQueryDto countryQueryDto = BeanUtil.copyProperties(excelQueryDto,CountryQueryDto.class);
        List<CountryLanguageDto> countryLanguageDtoList = countryLanguageService.listQuery(countryQueryDto);
        if (CollectionUtil.isEmpty(countryLanguageDtoList)) {
            throw new OtherException("无效的国家或语言");
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
        List<CountryLanguageType> typeList = countryLanguageDtoList.stream().map(CountryLanguageDto::getType).distinct().collect(Collectors.toList());
        // 标识哪一个sheet的哪一行需要做空值判断
        Map<String, List<Integer>> sheetBlankCellNumMap = new HashMap<>();
        boolean lackHandler = YesOrNoEnum.YES == excelQueryDto.getShowLack();

        typeList.forEach(type-> {
            // 获取当前国家当前类型拥有的吊牌字段列表
            languageQueryDto.setType(type);
            languageQueryDto.setStandardColumnCode(null);
            MoreLanguageTableContext.MoreLanguageTableParamEnum.IN_EXCEL.setBooleanParam(YesOrNoEnum.YES);
            List<StandardColumnDto> standardColumnDtoList = this.queryCountryTitle(languageQueryDto);

            for (StandardColumnDto standardColumnDto : standardColumnDtoList) {
//                threadPoolExecutor.execute(()-> {
                    String standardColumnCode = standardColumnDto.getCode();
                    String sheetName = type.getText() + "-" + standardColumnDto.getName();

                    String tableTitleJson = standardColumnDto.getTableTitleJson();
                    if (StrUtil.isBlank(tableTitleJson)) {
                        return;
                    }
                    List<MoreLanguageTableTitle> tableTitleList = JSONUtil.toList(tableTitleJson, MoreLanguageTableTitle.class);
                    List<ExcelExportEntity> beanList = new ArrayList<>();
                    for (int i = 0; i < tableTitleList.size(); i++) {
                        MoreLanguageTableTitle moreLanguageTableTitle = tableTitleList.get(i);
                        ExcelExportEntity exportEntity = new ExcelExportEntity(moreLanguageTableTitle.getText(), moreLanguageTableTitle.getCode());
                        exportEntity.setColumnHidden(moreLanguageTableTitle.isExcelHidden());
                        if (moreLanguageTableTitle.getExcelWidth() != null) {
                            exportEntity.setWidth(moreLanguageTableTitle.getExcelWidth());
                        }
                        if (lackHandler && moreLanguageTableTitle.getCode().contains("content")) {
                            List<Integer> lackCheckCellNumList = sheetBlankCellNumMap.getOrDefault(sheetName, new ArrayList<>());
                            lackCheckCellNumList.add(i);
                            sheetBlankCellNumMap.put(sheetName, lackCheckCellNumList);
                        }
                        beanList.add(exportEntity);
                    }

                // 拼装多个key,若没有取第一个
                    List<MoreLanguageTableTitle> keyList = tableTitleList.stream().filter(it -> it.getKey() != null)
                            .sorted(Comparator.comparing(MoreLanguageTableTitle::getKey)).collect(Collectors.toList());
                    if (CollectionUtil.isEmpty(keyList)) {
                        keyList = Collections.singletonList(tableTitleList.get(0));
                    }
                    String key = keyList.stream().map(MoreLanguageTableTitle::getCode).collect(Collectors.joining("-"));

                    // 拼装多个keyName,若没有取第二个
                    List<MoreLanguageTableTitle> keyNameList = tableTitleList.stream().filter(it -> it.getKeyName() != null)
                            .sorted(Comparator.comparing(MoreLanguageTableTitle::getKeyName)).collect(Collectors.toList());
                    if (CollectionUtil.isEmpty(keyNameList)) {
                        keyNameList = Collections.singletonList(tableTitleList.get(1));
                    }
                    String keyName = keyNameList.stream().map(MoreLanguageTableTitle::getCode).collect(Collectors.joining("-"));

                    MoreLanguageExportBaseDTO exportBaseDTO = new MoreLanguageExportBaseDTO();
                    Map<String, Object> baseTitleMap = BeanUtil.beanToMap(exportBaseDTO);
                    beanList.removeIf(it-> baseTitleMap.containsKey(it.getKey().toString()));

                    exportBaseDTO.setStandardColumnCode(type.getCode() + RedisKeyBuilder.COMMA + standardColumnCode);
                    exportBaseDTO.setKey(key);
                    exportBaseDTO.setKeyName(keyName);

                    // 添加map默认表头,否则无法支持导入
                    // 直接在ExcelExportService拷出来的
                    Class<MoreLanguageExportBaseDTO> pojoClass = MoreLanguageExportBaseDTO.class;
                    Field[] fileds = PoiPublicUtil.getClassFields(pojoClass);
                    ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
                    String targetId = etarget == null ? null : etarget.value();

                    EasyPoiMapExportParam exportParam = new EasyPoiMapExportParam();
                    // 封装导出的参数
                    ExportParams params = new ExportParams("温馨提示：请您按照导入规测进行导入，否测可能影响到导入成功，请注意。\n" +
                            "1、如若需要删除内容信息，请删除整行信息。\n" +
                            "2、请不要删除表头信息。\n" +
                            "3、请不要删除翻译语言内自带信息。\n", sheetName, ExcelType.XSSF);
//            params.setStyle(ExcelStyleUtil.class);

                    ExcelUtils.getAllExcelField(params.getExclusions(), targetId, fileds, beanList, pojoClass, null, null);

                    exportParam.setTitle(params);
                    exportParam.setEntity(beanList);

                    exportBaseDTO.setMapping(JSONUtil.toJsonStr(beanList.stream().collect(Collectors.toMap(ExcelExportEntity::getName, ExcelExportEntity::getKey))));
                    // 封装exportBaseDTO数据为map
                    Map<String, Object> baseMap = BeanUtil.beanToMap(exportBaseDTO, false, true);

                    // 获取列表数据
                    MoreLanguageQueryDto moreLanguageQueryDto = BeanUtil.copyProperties(languageQueryDto, MoreLanguageQueryDto.class);
                    moreLanguageQueryDto.setStandardColumnCode(standardColumnCode);
                    List<Map<String, Object>> mapList = listQuery(moreLanguageQueryDto).getList();

                    if (CollectionUtil.isNotEmpty(mapList)) {
                        Map<String, Object> firstRow = mapList.get(0);
                        firstRow.putAll(baseMap);
//                    mapList.forEach(it-> {
//                        String mergeVerticalField = "languageName";
//                        it.put(mergeVerticalField, baseMap.get(mergeVerticalField));
//                    });
                    }

                    exportParam.setData(mapList);

                    exportParams.add(exportParam);
//                });
            }
        });

//        threadPoolExecutor.shutdown();
//        System.out.println("1231");
//        threadPoolExecutor.awaitTermination(30,TimeUnit.MINUTES);
//        System.out.println("1231");

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode(
                String.format("(%s)吊牌&洗唛多语言-%s.xlsx",
                        (baseCountryLanguage.getCountryName()),
                        System.currentTimeMillis()),"UTF-8").replaceAll("\\+", "%20");

        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);

        Workbook workbook = null;
        try (OutputStream out = response.getOutputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ) {
            if (CollectionUtil.isNotEmpty(exportParams)) {
                for (EasyPoiMapExportParam exportParam : exportParams) {
                    ExportParams title = exportParam.getTitle();
                    if (workbook == null) {
                        workbook = ExcelExportUtil.exportExcel(title, exportParam.getEntity(), exportParam.getData());
                    } else {
                        excelExportService.createSheetForMap(workbook, title, exportParam.getEntity(), exportParam.getData());
                    }

                    Sheet sheet = workbook.getSheet(title.getSheetName());

                    Row titleRow = sheet.getRow(0);
                    titleRow.setHeightInPoints(80);
                    CellStyle titleStyle = workbook.createCellStyle();
//                    rowStyle.setBorderTop(BorderStyle.THIN);
//                    rowStyle.setBorderBottom(BorderStyle.THIN);
//                    rowStyle.setBorderLeft(BorderStyle.THIN);
//                    rowStyle.setBorderRight(BorderStyle.THIN);
                    titleStyle.setAlignment(HorizontalAlignment.LEFT);
                    titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    titleStyle.setWrapText(false);
//                    Font font = workbook.createFont();
//                    font.setColor(IndexedColors.RED.index);
//                    font.setFontName("宋体");
//                    font.setBold(true);
//                    font.setFontHeightInPoints((short) 10);
//                    rowStyle.setFont(font);
                    titleRow.setRowStyle(titleStyle);

                    CellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    // 修正每一行的样式
                    if (lackHandler) {
                        List<Integer> lackCheckCellNumList = sheetBlankCellNumMap.get(sheet.getSheetName());
                        if (CollectionUtil.isNotEmpty(lackCheckCellNumList)) {
                            int rowCount = sheet.getLastRowNum();
                            // 从第三行开始遍历，跳过表头
                            for (int i = 2; i <= rowCount; i++) {
                                Row row = sheet.getRow(i);
                                if (row != null) {
                                    int cellCount = row.getLastCellNum();
                                    boolean needHandlerLack = lackCheckCellNumList.stream()
                                            .anyMatch(cellNum -> StrUtil.isBlank(row.getCell(cellNum).getStringCellValue()));
                                    for (int j = 0; j < cellCount; j++) {
                                        if (needHandlerLack) {
                                            Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                            cell.setCellStyle(cellStyle);
                                        }
                                    }
                                }
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
            }
        }finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    @Override
    public PageInfo<Map<String,Object>> listQuery(MoreLanguageQueryDto moreLanguageQueryDto) {
        boolean cache = moreLanguageQueryDto.isCache();
        String standardColumnCode = moreLanguageQueryDto.getStandardColumnCode();

        if (StrUtil.isBlank(standardColumnCode)) {
            throw new OtherException("未选择标准列");
        }

        // 先查询国家存在什么语言
        CountryQueryDto countryQueryDto = BeanUtil.copyProperties(moreLanguageQueryDto, CountryQueryDto.class);
        List<CountryLanguageDto> countryLanguageList = countryLanguageService.listQuery(countryQueryDto);
        if (CollectionUtil.isEmpty(countryLanguageList)) {
            throw new OtherException("无效的国家语言数据");
        }

        CountryLanguageDto baseCountryLanguage = countryLanguageList.get(0);
        String code = baseCountryLanguage.getCode();
        CountryLanguageType type = baseCountryLanguage.getType();
        YesOrNoEnum singleLanguageFlag = baseCountryLanguage.getSingleLanguageFlag();
        String languageCode = baseCountryLanguage.getLanguageCode();

        // 查redis
        RedisStaticFunUtils.setBusinessService(standardColumnService).setMessage("无效的标准列" + type.getCode() + RedisKeyBuilder.COMMA + standardColumnCode);
        StandardColumn standardColumn = (StandardColumn) RedisStaticFunUtils.hget(
                RedisKeyConstant.STANDARD_COLUMN_LIST.build(), type.getCode() + RedisKeyBuilder.COMMA + standardColumnCode);

        List<String> standardColumnCodeList = countryLanguageService.findStandardColumnCodeList(code, type, cache);

        if (!standardColumnCodeList.contains(standardColumnCode)) {
            throw new OtherException("无效的标准列,请重新刷新页面");
        }

        // 获取翻译的表头 找差集
        String tableTitleJson = standardColumn.getTableTitleJson();
        if (StrUtil.isBlank(tableTitleJson)) throw new OtherException("未设置表头,请找开发协助");

        // 解析当前标准列的表头列表
        List<MoreLanguageTableTitle> tableTitleList = JSONUtil.toList(tableTitleJson, MoreLanguageTableTitle.class);

        // 获取翻译这个数据库表的所有在表头列表的字段
        List<String> translateFieldList = Arrays.stream(StandardColumnCountryTranslate.class.getDeclaredFields())
                .map(Field::getName)
                .filter(fieldName-> tableTitleList.stream().anyMatch(it-> fieldName.equals(it.getCode())))
                .collect(Collectors.toList());

        // 获取多键集合,没有就取第一个
        List<MoreLanguageTableTitle> one2ManyKeyList = tableTitleList.stream()
                .filter(it -> it.getKey() != null)
                .sorted(Comparator.comparing(MoreLanguageTableTitle::getKey))
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(one2ManyKeyList)) {
            one2ManyKeyList = CollUtil.toList(tableTitleList.get(0));
        }
        List<String> keyList = one2ManyKeyList.stream().map(it-> StrUtil.toUnderlineCase(it.getCode())).collect(Collectors.toList());;

        // 剪裁掉翻译的字段
        tableTitleList.removeIf(tableTitle -> translateFieldList.contains(tableTitle.getCode()));

        // 装饰输出表头
        MoreLanguageTableContext.MoreLanguageTableParamEnum.NO_DECORATE.setBooleanParam(singleLanguageFlag);
        MoreLanguageTableContext.MoreLanguageTableParamEnum.IN_CACHE.setParam(moreLanguageQueryDto.getCache());
        MoreLanguageTableContext.setParam(MapUtil.of(
                MoreLanguageTableContext.MoreLanguageTableTitleHandlerEnum.COPY.getHandlerKey(), JSONUtil.toJsonStr(countryLanguageList)
        ));
        List<String> showFieldList = JSONUtil.toList(MoreLanguageTableContext.decorateTitleJson(standardColumn), MoreLanguageTableTitle.class).stream()
                .map(MoreLanguageTableTitle::getCode).collect(Collectors.toList());

        standardColumn.setTableTitleJson(JSONUtil.toJsonStr(tableTitleList));

        MoreLanguageTableContext.MoreLanguageTableParamEnum.OWNER_TAG_CODE.setParam(String.join(COMMA, standardColumnCodeList));
        PageInfo<Map<String, Object>> mapList = MoreLanguageTableContext.getTableData(moreLanguageQueryDto, standardColumn);

        List<Map<String, Object>> resultList = mapList.getList();
        List<String> propertiesCodeList = resultList.stream()
                .map(it -> keyList.stream().map(key-> it.get(key).toString()).collect(Collectors.joining("-"))).collect(Collectors.toList());

        // 分页.. 有顺序问题
        // 查询翻译 也可以找redis,不常修改
        List<StandardColumnCountryTranslate> translateList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(propertiesCodeList)) {
            LambdaQueryWrapper<StandardColumnCountryTranslate> translateQueryWrapper = new LambdaQueryWrapper<StandardColumnCountryTranslate>()
                    .select(StandardColumnCountryTranslate::getCountryLanguageId, StandardColumnCountryTranslate::getPropertiesCode, StandardColumnCountryTranslate::getContent)
                    .eq(StandardColumnCountryTranslate::getTitleCode, standardColumnCode)
                    .in(StandardColumnCountryTranslate::getPropertiesCode, propertiesCodeList);

            translateList.addAll(standardColumnCountryTranslateService.page(moreLanguageQueryDto.toMPPage(StandardColumnCountryTranslate.class),
                    translateQueryWrapper.clone()
                            .in(StandardColumnCountryTranslate::getCountryLanguageId, countryLanguageList.stream().map(CountryLanguage::getId).collect(Collectors.toList()))
            ).getRecords());

            // 如果为空, 查找单语言翻译
            if(CollectionUtil.isEmpty(translateList)) {
                CountryQueryDto languageDto = new CountryQueryDto();
                languageDto.setLanguageCode(languageCode);
                languageDto.setType(type);
                languageDto.setSingleLanguageFlag(YesOrNoEnum.YES);
                List<CountryLanguageDto> countryLanguageDtoList = countryLanguageService.listQuery(languageDto);

                translateList.addAll(standardColumnCountryTranslateService.page(moreLanguageQueryDto.toMPPage(StandardColumnCountryTranslate.class),
                        translateQueryWrapper.clone()
                                .in(StandardColumnCountryTranslate::getCountryLanguageId, countryLanguageDtoList.stream().map(CountryLanguage::getId).collect(Collectors.toList()))
                ).getRecords());
            }
        }

        mapList.setList(resultList.stream().map(resultMap-> {
            String propertiesKey = keyList.stream().map(key -> resultMap.getOrDefault(key, "").toString()).collect(Collectors.joining("-"));
            Map<String, Object> map = new HashMap<>(showFieldList.size());
            List<StandardColumnCountryTranslate> translatePropertiesResultMap = translateList.stream().filter(it -> it.getPropertiesCode().equals(propertiesKey)).collect(Collectors.toList());
            showFieldList.forEach(field->{
                String fieldKey = StrUtil.toUnderlineCase(field);
                Object fieldValue = "";
                if (!resultMap.containsKey(fieldKey)) {
                    for (CountryLanguageDto countryLanguage : countryLanguageList) {
                        Map<String, Object> translateResultMap = translatePropertiesResultMap.stream()
                                .filter(it -> it.getCountryLanguageId().equals(countryLanguage.getId())).findFirst().map(BeanUtil::beanToMap).orElse(new HashMap<>());
                        for (Map.Entry<String, Object> entry : translateResultMap.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (singleLanguageFlag == YesOrNoEnum.NO) {
                                key = (countryLanguage.getLanguageCode() + "-" + key);
                            }
                            if (fieldKey.equals(key)) {
                                fieldValue = value;
                                break;
                            }
                        }
                    }
                }else {
                    fieldValue = resultMap.get(fieldKey);
                }
                if (fieldValue instanceof Date) {
                    fieldValue = DateUtil.format((Date)fieldValue, "yyyy-MM-dd HH:mm:ss");
                }
                map.put(field, fieldValue);
            });
            return map;
        }).collect(Collectors.toList()));

        return mapList;
    }

    @Override
    public List<Map<String, Object>> listAllByTable(String fields, String tableName, String where) {
        return standardColumnCountryTranslateMapper.listAllByTable(fields, tableName, where);
    }

    @Override
    public List<StandardColumnDto> findStandardColumn(String code) {
        StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
        queryDto.setTypeList(CollUtil.toList(StandardColumnType.TAG, StandardColumnType.WASHING));
        queryDto.setCodeList(Arrays.asList(code.split(COMMA)));

        return standardColumnService.listQuery(queryDto);
    }

// 自定义方法区 不替换的区域【other_end】
	
}
