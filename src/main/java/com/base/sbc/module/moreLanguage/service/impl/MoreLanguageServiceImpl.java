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
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.Pinyin4jUtil;
import com.base.sbc.module.moreLanguage.dto.CountrySaveDto;
import com.base.sbc.module.moreLanguage.dto.EasyPoiMapExportParam;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExcelQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExportBaseDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageTableTitle;
import com.base.sbc.module.moreLanguage.entity.Country;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryRelation;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.mapper.StandardColumnCountryTranslateMapper;
import com.base.sbc.module.moreLanguage.service.CountryModelService;
import com.base.sbc.module.moreLanguage.service.CountryService;
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
import com.google.common.base.Function;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.base.sbc.config.redis.RedisKeyConstant.COMMA;

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
    private CountryService countryService;

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
    public List<StandardColumnDto> queryCountryTitle(MoreLanguageQueryDto moreLanguageQueryDto) {
        String countryLanguageId = moreLanguageQueryDto.getCountryLanguageId();

        ForkJoinPool workPool = new ForkJoinPool(2, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);

        /* ----------------------------可以分成两个线程进行操作---------------------------- */

        // 查询根标准列
        ForkJoinTask<List<StandardColumnDto>> rootStandardColumnFuture = workPool.submit(() -> {
            StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
            queryDto.setTypeList(CollUtil.toList(StandardColumnType.TAG_ROOT));
            return standardColumnService.listQuery(queryDto);
        }).fork();

        ForkJoinTask<List<StandardColumnDto>> standardColumnFuture = workPool.submit(() -> {
            // 查询 关联表
            // 可以从redis拿,只有countryAdd和导入会更新,频率低
            List<String> standardColumnCodeList = findStandardColumnCodeList(countryLanguageId);

            // 只查询可配置的
            StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
            queryDto.setNoModel(StandardColumnModel.TEXT);
            queryDto.setCodeList(standardColumnCodeList);
            return standardColumnService.listQuery(queryDto);
        }).fork();

        return Stream.of(rootStandardColumnFuture.join(), standardColumnFuture.join()).flatMap(Collection::stream)
                .peek(it-> {
                    it.setTableTitleJson(JSONUtil.toJsonStr(JSONUtil.toList(it.getTableTitleJson(), MoreLanguageTableTitle.class).stream()
                            .filter(tableTitle-> !tableTitle.isHidden()).collect(Collectors.toList())));
                })
                .sorted(Comparator.comparing(StandardColumnDto::getId)).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String countrySave(CountrySaveDto countrySaveDto) {
        String countryName = countrySaveDto.getCountryName();
        String languageName = countrySaveDto.getLanguageName();
        String countryCode = Pinyin4jUtil.converterToFirstSpell(countryName);
        String languageCode = countrySaveDto.getLanguageCode();
        String countryLanguageId = countrySaveDto.getCountryLanguageId();

        // 检查是否已有国家表
        if (countryService.exists(new BaseLambdaQueryWrapper<Country>()
                .notNullNe(Country::getId, countryLanguageId)
                .eq(Country::getCountryCode, countryCode)
                .eq(Country::getLanguageCode, languageCode))
        ) {
            throw new OtherException("已存在对应国家-语言, 修改数据请使用对应的Excel导入");
        }

        // 插入国家表
        Country country = new Country();
        country.setId(countryLanguageId);
        country.setCountryCode(countryCode);
        country.setCountryName(countryName);
        country.setLanguageCode(languageCode);
        country.setLanguageName(languageName);

        countryService.saveOrUpdate(country);

        String countryId = country.getId();

        removeRelation(countryId);

        // 查找根
        StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
        queryDto.setTypeList(CollUtil.toList(StandardColumnType.TAG_ROOT));
        countrySaveDto.getStandardColumnCodeList().addAll(standardColumnService.listQuery(queryDto).stream().map(StandardColumnDto::getCode).collect(Collectors.toList()));

        List<StandardColumnCountryRelation> countryRelationList = countrySaveDto.getStandardColumnCodeList().stream().map(standardColumnCode -> {
            // 从redis标准列数据
            RedisStaticFunUtils.setBusinessService(standardColumnService).setMessage("非法标准列code");
            StandardColumn standardColumn = (StandardColumn)
                    RedisStaticFunUtils.hget(RedisKeyConstant.STANDARD_COLUMN_LIST, standardColumnCode);
            return new StandardColumnCountryRelation(
                    countryId,
                    standardColumn.getCode(),
                    standardColumn.getName(),
                    ""
            );
        }).collect(Collectors.toList());
        relationService.saveBatch(countryRelationList);

        // 存到redis
        RedisStaticFunUtils.lSet(RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION + countryId,
                countryRelationList.stream().map(StandardColumnCountryRelation::getStandardColumnCode).collect(Collectors.toList()));
        RedisStaticFunUtils.clear();

//        // 使用redis作为中间通信,standardColumnCodeList参数暂时没用了
//        exportExcel(new MoreLanguageExcelQueryDto(countryId, new ArrayList<>()));

        return countryId;
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void removeRelation(String countryId){
        // 直接删除关联
        relationService.physicalDeleteQWrap(new BaseQueryWrapper<StandardColumnCountryRelation>().eq("country_language_id", countryId));
    }

    private List<String> findStandardColumnCodeList(String countryLanguageId) {
        // 查询关联关系
        String redisKey = RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION + countryLanguageId;
        List<Object> tempStandardColumnCodeList = RedisStaticFunUtils.lGet(redisKey);
        if (CollectionUtil.isEmpty(tempStandardColumnCodeList)) {
            List<StandardColumnCountryRelation> relationList = relationService.list(new BaseLambdaQueryWrapper<StandardColumnCountryRelation>()
                    .eq(StandardColumnCountryRelation::getCountryLanguageId, countryLanguageId)
            );
            List<String> standardColumnCodeList = relationList.stream().map(StandardColumnCountryRelation::getStandardColumnCode).collect(Collectors.toList());
            RedisStaticFunUtils.lSet(redisKey, standardColumnCodeList.stream().collect(Collectors.toList()));
            return standardColumnCodeList;
        } else {
            return tempStandardColumnCodeList.stream().map(Object::toString).collect(Collectors.toList());
        }
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportExcel(MoreLanguageExcelQueryDto excelQueryDto) {
        String countryLanguageId = excelQueryDto.getCountryLanguageId();
        Country country = countryService.getById(countryLanguageId);
        if (country == null) {
            throw new OtherException("无效的国家语言");
        }
        String languageName = country.getLanguageName();

        List<String> standardColumnCodeList = findStandardColumnCodeList(countryLanguageId);

        StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
        queryDto.setNoModel(StandardColumnModel.TEXT);
        queryDto.setTypeList(Arrays.asList(StandardColumnType.TAG, StandardColumnType.TAG_ROOT));
        queryDto.setCodeList(standardColumnCodeList);
        List<StandardColumnDto> standardColumnDtoList = standardColumnService.listQuery(queryDto);

        List<EasyPoiMapExportParam> exportParams = new ArrayList<>();
        ExcelExportService excelExportService = new ExcelExportService();

        MoreLanguageExportBaseDTO exportBaseDTO = new MoreLanguageExportBaseDTO();
        exportBaseDTO.setLanguageName(languageName);

        for (StandardColumnDto standardColumnDto : standardColumnDtoList) {
            String standardColumnCode = standardColumnDto.getCode();
            String standardColumnName = standardColumnDto.getName();

            EasyPoiMapExportParam exportParam = new EasyPoiMapExportParam();

            exportBaseDTO.setStandardColumnCode(standardColumnCode);

            ExportParams params = new ExportParams("温馨提示：请您按照导入规测进行导入，否测可能影响到导入成功，请注意。\n" +
                    "1、如若需要删除内容信息，请删除整行信息。\n" +
                    "2、请不要删除表头信息。\n" +
                    "3、请不要删除翻译语言内自带信息。", standardColumnName, ExcelType.XSSF);
//            params.setStyle(ExcelStyleUtil.class);
            exportParam.setTitle(params);

            String tableTitleJson = standardColumnDto.getTableTitleJson();
            List<MoreLanguageTableTitle> tableTitleList = JSONUtil.toList(tableTitleJson, MoreLanguageTableTitle.class);
            List<ExcelExportEntity> beanList = tableTitleList.stream().sorted(Comparator.comparing(MoreLanguageTableTitle::isHidden)).map(it -> {
                ExcelExportEntity exportEntity = new ExcelExportEntity(it.getText(), it.getCode());
                exportEntity.setColumnHidden(it.isHidden());
                if (it.getExcelWidth() != null) {
                    exportEntity.setWidth(it.getExcelWidth());
                }
                return exportEntity;
            }).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(beanList)) {
                continue;
            }

            // 可以修改MoreLanguageTableTitle这个类来决定get的位置
            List<MoreLanguageTableTitle> keyList = tableTitleList.stream().filter(it -> it.getKey() != null)
                    .sorted(Comparator.comparing(MoreLanguageTableTitle::getKey)).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(keyList)) {
                keyList = Collections.singletonList(tableTitleList.get(0));
            }
            tableTitleList.removeAll(keyList);
            String key = keyList.stream().map(MoreLanguageTableTitle::getCode).collect(Collectors.joining("-"));

            List<MoreLanguageTableTitle> keyNameList = tableTitleList.stream().filter(it -> it.getKeyName() != null)
                    .sorted(Comparator.comparing(MoreLanguageTableTitle::getKeyName)).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(keyNameList)) {
                keyNameList = Collections.singletonList(tableTitleList.get(0));
            }
            tableTitleList.removeAll(keyNameList);
            String keyName = keyNameList.stream().map(MoreLanguageTableTitle::getCode).collect(Collectors.joining("-"));

            exportBaseDTO.setKey(key);
            exportBaseDTO.setKeyName(keyName);

            // 封装exportBaseDTO数据为map
            Map<String, Object> baseTitleMap = BeanUtil.beanToMap(exportBaseDTO);
            Map<String, Object> baseMap = BeanUtil.beanToMap(exportBaseDTO, false, true);
            beanList.removeIf(it-> baseTitleMap.containsKey(it.getKey().toString()));

            // 添加map默认表头,否则无法支持导入
            // 直接在ExcelExportService拷出来的
            Class<MoreLanguageExportBaseDTO> pojoClass = MoreLanguageExportBaseDTO.class;
            Field[] fileds = PoiPublicUtil.getClassFields(pojoClass);
            ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
            String targetId = etarget == null ? null : etarget.value();

            ExcelUtils.getAllExcelField(params.getExclusions(), targetId, fileds, beanList, pojoClass, null, null);

            exportParam.setEntity(beanList);

            MoreLanguageQueryDto languageQueryDto = new MoreLanguageQueryDto(countryLanguageId, standardColumnCode);
            languageQueryDto.setPageNum(1);
            languageQueryDto.setPageSize(Integer.MAX_VALUE);
            List<Map<String, Object>> mapList = listQuery(languageQueryDto).getList();

            if (CollectionUtil.isNotEmpty(mapList)) {
                Map<String, Object> firstRow = mapList.get(0);
                firstRow.putAll(baseMap);
                firstRow.put("mapping", JSONUtil.toJsonStr(beanList
                        .stream().collect(Collectors.toMap(ExcelExportEntity::getName, ExcelExportEntity::getKey))));
                mapList.forEach(it-> {
                    String mergeVerticalField = "languageName";
                    it.put(mergeVerticalField, baseMap.get(mergeVerticalField));
                });
            }

            exportParam.setData(mapList);

            exportParams.add(exportParam);
        }

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode(
                String.format("(%s)吊牌&洗唛多语言-%s.xlsx", country.getUniqueName(), System.currentTimeMillis()
                ),"UTF-8").replaceAll("\\+", "%20");

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
                    CellStyle rowStyle = workbook.createCellStyle();
//                    rowStyle.setBorderTop(BorderStyle.THIN);
//                    rowStyle.setBorderBottom(BorderStyle.THIN);
//                    rowStyle.setBorderLeft(BorderStyle.THIN);
//                    rowStyle.setBorderRight(BorderStyle.THIN);
                    rowStyle.setAlignment(HorizontalAlignment.LEFT);
                    rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    rowStyle.setWrapText(false);
//                    Font font = workbook.createFont();
//                    font.setColor(IndexedColors.RED.index);
//                    font.setFontName("宋体");
//                    font.setBold(true);
//                    font.setFontHeightInPoints((short) 10);
//                    rowStyle.setFont(font);
                    titleRow.setRowStyle(rowStyle);

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
        String countryLanguageId = moreLanguageQueryDto.getCountryLanguageId();
        String standardColumnCode = moreLanguageQueryDto.getStandardColumnCode();
        HashMap<String, String> reflectMap = new HashMap<>();
        if (StrUtil.isBlank(standardColumnCode)) {
            throw new OtherException("未选择标准列");
        }

        // 除了字典都使用redis获取

        // 检查是否有对应关系
        if (StrUtil.isNotBlank(countryLanguageId) && !relationService.exists(new BaseLambdaQueryWrapper<StandardColumnCountryRelation>()
                .eq(StandardColumnCountryRelation::getCountryLanguageId, countryLanguageId)
                .eq(StandardColumnCountryRelation::getStandardColumnCode, standardColumnCode)
        )) {
            throw new OtherException("该国家不存在所查询的标准列");
        }

        // 查redis
        StandardColumn standardColumn = standardColumnService.findOne(new BaseLambdaQueryWrapper<StandardColumn>().eq(StandardColumn::getCode, standardColumnCode));
        if (standardColumn == null) {
            throw new OtherException("无效的标准列");
        }

        List<String> standardColumnCodeList = findStandardColumnCodeList(countryLanguageId);
        reflectMap.put("ownerTagCode", String.join(COMMA, standardColumnCodeList));

        // 获取翻译的表头 找差集
        String tableTitleJson = standardColumn.getTableTitleJson();
        if (StrUtil.isBlank(tableTitleJson)) throw new OtherException("未设置表头,请找开发协助");

        List<MoreLanguageTableTitle> tableTitleList = JSONUtil.toList(tableTitleJson, MoreLanguageTableTitle.class);
        List<String> translateFieldList = Arrays.stream(StandardColumnCountryTranslate.class.getDeclaredFields())
                .map(Field::getName).collect(Collectors.toList());
        
        List<String> showFieldList = tableTitleList.stream().filter(it-> !it.isHidden())
                .map(MoreLanguageTableTitle::getCode).collect(Collectors.toList());

        List<MoreLanguageTableTitle> one2ManyKeyList = tableTitleList.stream().filter(it -> it.getKey() != null)
                .sorted(Comparator.comparing(MoreLanguageTableTitle::getKey))
                .collect(Collectors.toList());
        List<String> keyList;
        if (CollectionUtil.isNotEmpty(one2ManyKeyList)) {
            keyList = one2ManyKeyList.stream().map(it-> StrUtil.toUnderlineCase(it.getCode())).collect(Collectors.toList());
        }else {
            keyList = Collections.singletonList(StrUtil.toUnderlineCase(showFieldList.get(0)));
        }

        // 找差集
        translateFieldList.removeAll(CollUtil.disjunction(translateFieldList, showFieldList));

        tableTitleList.removeIf(it->  translateFieldList.contains(it.getCode()));

        // 剪裁掉翻译的字段
        standardColumn.setTableTitleJson(JSONUtil.toJsonStr(tableTitleList));

        PageInfo<Map<String, Object>> mapList = MoreLanguageTableContext.getTableData(moreLanguageQueryDto, standardColumn, reflectMap);

        List<Map<String, Object>> resultList = mapList.getList();
        List<String> propertiesCodeList = resultList.stream()
                .map(it -> keyList.stream().map(key-> it.get(key).toString()).collect(Collectors.joining("-"))).collect(Collectors.toList());
        // 分页.. 有顺序问题
        // 查询翻译 也可以找redis,不常修改
        translateFieldList.add("properties_code");
        List<Map<String, Object>> translateList = standardColumnCountryTranslateService.pageMaps(moreLanguageQueryDto.toMPPageMap(),
                new BaseQueryWrapper<StandardColumnCountryTranslate>().select(translateFieldList)
                        .lambda()
                        .eq(StandardColumnCountryTranslate::getCountryLanguageId, countryLanguageId)
                        .eq(StandardColumnCountryTranslate::getTitleCode, standardColumnCode)
                        .in(StandardColumnCountryTranslate::getPropertiesCode, propertiesCodeList)
        ).getRecords();

        mapList.setList(resultList.stream().map(resultMap-> {
            String propertiesKey = keyList.stream().map(key -> resultMap.getOrDefault(key, "").toString()).collect(Collectors.joining("-"));
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> translateResultMap = translateList.stream().filter(it -> it.get("properties_code").equals(propertiesKey)).findFirst().orElse(new HashMap<>());
            showFieldList.forEach(field->{
                String fieldKey = StrUtil.toUnderlineCase(field);
                map.put(field, resultMap.getOrDefault(fieldKey, translateResultMap.getOrDefault(fieldKey, "")));
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
        queryDto.setTypeList(CollUtil.toList(StandardColumnType.TAG));
        if (StrUtil.isNotBlank(code)) {
            queryDto.setCodeList(Arrays.asList(code.split(COMMA)));
        }
        return standardColumnService.listQuery(queryDto);
    }

    @Override
    public CountrySaveDto countryDetail(String countryLanguageId) {
        Country country = countryService.getById(countryLanguageId);
        if (country == null) throw new OtherException("未查询到国家语言");

        CountrySaveDto countrySaveDto = BeanUtil.copyProperties(country, CountrySaveDto.class);
        countrySaveDto.setCountryLanguageId(country.getId());

//        SFunction<StandardColumnCountryRelation, String> getStandardColumnCode = StandardColumnCountryRelation::getStandardColumnCode;
        List<String> standardColumnCodeList = relationService.listOneField(new LambdaQueryWrapper<StandardColumnCountryRelation>()
                .eq(StandardColumnCountryRelation::getCountryLanguageId, countryLanguageId),
                StandardColumnCountryRelation::getStandardColumnCode
        );
        countrySaveDto.setStandardColumnCodeList(standardColumnCodeList);
        return countrySaveDto;
    }

// 自定义方法区 不替换的区域【other_end】
	
}
