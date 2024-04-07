package com.base.sbc.config.utils;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.exception.excel.ExcelExportException;
import cn.afterturn.easypoi.exception.excel.enums.ExcelExportEnum;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import cn.afterturn.easypoi.util.PoiReflectorUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.dto.QueryFieldDto;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.column.entity.ColumnDefine;
import com.base.sbc.module.column.service.ColumnUserDefineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Excel导入导出工具类
 */
@Slf4j
public class ExcelUtils {

    private static final Log logger = LogFactory.getLog(ExcelUtils.class);



    /**
     * excel 导出
     *
     * @param list     数据列表
     * @param fileName 导出时的excel名称
     * @param response
     */
    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, fileName, response);
    }

    /**
     * 默认的 excel 导出
     *
     * @param list     数据列表
     * @param fileName 导出时的excel名称
     * @param response
     */
    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        //把数据添加到excel表格中
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * excel 导出
     *
     * @param list         数据列表
     * @param pojoClass    pojo类型
     * @param fileName     导出时的excel名称
     * @param response
     * @param exportParams 导出参数（标题、sheet名称、是否创建表头，表格类型）
     */
    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) throws IOException {
        //把数据添加到excel表格中
        exportParams.setStyle(ExcelExportTitleStyle.class);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * excel 导出
     *
     * @param list         数据列表
     * @param pojoClass    pojo类型
     * @param fileName     导出时的excel名称
     * @param exportParams 导出参数（标题、sheet名称、是否创建表头，表格类型）
     * @param response
     */
    public static void exportExcel(List<?> list, Class<?> pojoClass, String fileName, ExportParams exportParams, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    public static void exportExcelByTableCode(List<?> list, Class<?> pojoClass, String fileName, ExportParams exportParams, HttpServletResponse response,String tableCode,String imgFlag,Integer maxNumber,String... columns)  throws IOException {
        Assert.notBlank(tableCode,"tableCode不能为空");
        ColumnUserDefineService columnUserDefineService = SpringUtil.getBean(ColumnUserDefineService.class);
        List<ColumnDefine> detail = columnUserDefineService.findDefaultDetail(tableCode);
        Assert.notEmpty(detail,"没有找到对应列配置，请联系管理员维护");
        Map<String, Integer> userColumnMap = new HashMap<>();
        for (ColumnDefine columnDefine : detail) {
            if(BaseGlobal.NO.equals(columnDefine.getHidden())){
                continue;
            }
            Integer sortOrder = columnDefine.getSortOrder();
            if(StrUtil.isNotBlank(columnDefine.getExportAlias())){
                for (String s : columnDefine.getExportAlias().split(",")) {
                    userColumnMap.put(s,sortOrder);
                }
            }
            userColumnMap.put(columnDefine.getColumnCode(),sortOrder);
        }
        //这里是复制出来官方的导出接口
        List<ExcelExportEntity> excelParams = new ArrayList<>();
        Field[] fileds = PoiPublicUtil.getClassFields(pojoClass);
        ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
        String targetId = etarget == null ? null : etarget.value();
        //这里是改造的官方接口
        getAllExcelField(exportParams.getExclusions(), targetId, fileds, excelParams, pojoClass, new ArrayList<>(), null);

        //根据用户配置显示的才导出，导出顺序也按照用户配置
        List<ExcelExportEntity> newExcelParams = new ArrayList<>();
        for (ExcelExportEntity excelParam : excelParams) {
            if(userColumnMap.containsKey(excelParam.getKey())){
                excelParam.setOrderNum(userColumnMap.get(excelParam.getKey()));
                newExcelParams.add(excelParam);
            }
        }

        // defaultExport(list,fileName,response,exportParams,newExcelParams);
        executorExportExcel1(list,fileName,imgFlag,maxNumber,response,exportParams,newExcelParams,columns);
    }

    public static void defaultExport(List<?> list, String fileName, HttpServletResponse response, ExportParams exportParams,List<ExcelExportEntity> entityList) throws IOException {
        //把数据添加到excel表格中
        exportParams.setStyle(ExcelExportTitleStyle.class);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, entityList, list);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * excel 导出
     *
     * @param list      数据列表
     * @param title     表格内数据标题
     * @param sheetName sheet名称
     * @param pojoClass pojo类型
     * @param fileName  导出时的excel名称
     * @param response
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName, ExcelType.XSSF));
    }



    /**
     * excel 导出
     *
     * @param list           数据列表
     * @param title          表格内数据标题
     * @param sheetName      sheet名称
     * @param pojoClass      pojo类型
     * @param fileName       导出时的excel名称
     * @param isCreateHeader 是否创建表头
     * @param response
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    public static void addFixText2LastRow(Sheet sheet, String fixText) {
        int lastRowNum = sheet.getLastRowNum() + 1;
        AtomicInteger cellNumInt = new AtomicInteger();
        sheet.rowIterator().forEachRemaining(row-> {
            cellNumInt.set(row.getLastCellNum());
        });
        int cellNum = cellNumInt.get() - 1;
        Row row = sheet.createRow(lastRowNum);
        Cell rowCell = row.createCell(0);
        rowCell.setCellValue(fixText);
        rowCell.getCellStyle().setWrapText(true);
        row.setHeight((short) -1);
        for (int i = 1; i <= cellNum; i++) {
            row.createCell(i);
        }
        sheet.addMergedRegion(new CellRangeAddress(lastRowNum, lastRowNum, 0, cellNum));
    }


    /**
     * excel下载
     *
     * @param fileName 下载时的文件名称
     * @param response
     * @param workbook excel数据
     */
    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws IOException {
        try (OutputStream out = response.getOutputStream()) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            response.setHeader("Content-Length", String.valueOf(baos.size()));
            out.write( baos.toByteArray() );
        } catch (Exception e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }



    /**
     * excel 导入
     *
     * @param file      excel文件
     * @param pojoClass pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass) throws IOException {
        return importExcel(file, 1, 1, pojoClass);
    }

    /**
     * excel 导入
     *
     * @param filePath   excel文件路径
     * @param titleRows  表格内数据标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedSave(true);
        params.setSaveUrl("/excel/");
        try {
            return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("模板不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }


    /**
     * excel 导入
     *
     * @param file       上传的文件
     * @param titleRows  表格内数据标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (file == null) {
            return null;
        }
        try {
            return importExcel(file.getInputStream(), titleRows, headerRows, pojoClass);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param inputStream 文件输入流
     * @param titleRows   表格内数据标题行
     * @param headerRows  表头行
     * @param pojoClass   pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(InputStream inputStream, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setSaveUrl("/excel/");
        params.setNeedSave(true);
        try {
            return ExcelImportUtil.importExcel(inputStream, pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("excel文件不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 线程导出表格
     * @param list 导出的数据
     * @param pojoClass class
     * @param name 文件名称
     * @param imgFlag 是否导出图片
     * @param maxNumber 最大导出量
     * @param response
     * @param columns 图片列名
     */
    public static void executorExportExcel(List<?> list, Class<?> pojoClass, String name, String imgFlag, Integer maxNumber, HttpServletResponse response, String... columns){
        long t1 = System.currentTimeMillis();

        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(10)
                .build();
        try {
            if (StrUtil.equals(imgFlag, BaseGlobal.YES)) {
                /*导出图片*/
                if (CollUtil.isNotEmpty(list) && list.size() > maxNumber) {
                    throw new OtherException("带图片导出最多只能导出" + maxNumber + "条");
                }
                CountDownLatch countDownLatch = new CountDownLatch(list.size());

                for (Object o  : list) {
                    executor.submit(() -> {
                        try {
                            for (String column : columns) {
                                // column =  com.base.sbc.config.utils.StringUtils.toCamelCase(column);
                                final String stylePic = BeanUtil.getProperty(o, column);
                                BeanUtil.setProperty(o,  column+"1", HttpUtil.downloadBytes(stylePic));
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        } finally {
                            //每次减一
                            countDownLatch.countDown();
                            logger.info(String.valueOf(countDownLatch.getCount()));
                        }
                    });
                }
                countDownLatch.await();
            }
            ExcelUtils.exportExcel(list,pojoClass, name+".xlsx", new ExportParams(name, name, ExcelType.HSSF), response);

        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    public static void executorExportExcel1(List<?> list, String name, String imgFlag, Integer maxNumber, HttpServletResponse response, ExportParams exportParams, List<ExcelExportEntity> newExcelParams, String... columns){
        long t1 = System.currentTimeMillis();

        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(10)
                .build();
        try {
            if (StrUtil.equals(imgFlag, BaseGlobal.YES)) {
                /*导出图片*/
                if (CollUtil.isNotEmpty(list) && list.size() > maxNumber) {
                    throw new OtherException("带图片导出最多只能导出" + maxNumber + "条");
                }
                CountDownLatch countDownLatch = new CountDownLatch(list.size());

                for (Object o  : list) {
                    executor.submit(() -> {
                        try {
                            for (String column : columns) {
                                // column =  com.base.sbc.config.utils.StringUtils.toCamelCase(column);
                                final String stylePic = BeanUtil.getProperty(o, column);
                                BeanUtil.setProperty(o,  column+"1", HttpUtil.downloadBytes(stylePic));
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        } finally {
                            //每次减一
                            countDownLatch.countDown();
                            logger.info(String.valueOf(countDownLatch.getCount()));
                        }
                    });
                }
                countDownLatch.await();
            }
            // ExcelUtils.exportExcel(list,pojoClass, name+".xlsx", new ExportParams(name, name, ExcelType.HSSF), response);
            defaultExport(list,name+".xlsx",response,exportParams,newExcelParams);
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }


    public static void getAllExcelField(String[] exclusions, String targetId, Field[] fields, List<ExcelExportEntity> excelParams, Class<?> pojoClass, List<Method> getMethods, ExcelEntity excelGroup) {
        List<String> exclusionsList = exclusions != null ? Arrays.asList(exclusions) : null;

        for (Field field : fields) {
            if (!PoiPublicUtil.isNotUserExcelUserThis(exclusionsList, field, targetId)) {
                if (field.getAnnotation(Excel.class) != null) {
                    Excel excel = field.getAnnotation(Excel.class);
                    String name = PoiPublicUtil.getValueByTargetId(excel.name(), targetId, null);
                    if (StringUtils.isNotBlank(name)) {
                        excelParams.add(createExcelExportEntity(field, targetId, pojoClass, getMethods, excelGroup));
                    }
                } else if (PoiPublicUtil.isCollection(field.getType())) {
                    ExcelCollection excel = field.getAnnotation(ExcelCollection.class);
                    ParameterizedType pt = (ParameterizedType) field.getGenericType();
                    Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];
                    List<ExcelExportEntity> list = new ArrayList<>();
                    getAllExcelField(exclusions, StringUtils.isNotEmpty(excel.id()) ? excel.id() : targetId, PoiPublicUtil.getClassFields(clz), list, clz, null, null);
                    ExcelExportEntity excelEntity = new ExcelExportEntity();
                    excelEntity.setName(PoiPublicUtil.getValueByTargetId(excel.name(), targetId, null));

                    excelEntity.setOrderNum(Integer.parseInt(PoiPublicUtil.getValueByTargetId(excel.orderNum(), targetId, "0")));
                    excelEntity.setMethod(PoiReflectorUtil.fromCache(pojoClass).getGetMethod(field.getName()));
                    excelEntity.setList(list);
                    excelParams.add(excelEntity);
                } else {
                    List<Method> newMethods = new ArrayList<>();
                    if (getMethods != null) {
                        newMethods.addAll(getMethods);
                    }

                    newMethods.add(PoiReflectorUtil.fromCache(pojoClass).getGetMethod(field.getName()));
                    ExcelEntity excel = field.getAnnotation(ExcelEntity.class);
                    if (excel.show() && StringUtils.isEmpty(excel.name())) {
                        throw new ExcelExportException("if use ExcelEntity ,name mus has value ,data: " + ReflectionToStringBuilder.toString(excel), ExcelExportEnum.PARAMETER_ERROR);
                    }

                    getAllExcelField(exclusions, StringUtils.isNotEmpty(excel.id()) ? excel.id() : targetId, PoiPublicUtil.getClassFields(field.getType()), excelParams, field.getType(), newMethods, excel.show() ? excel : null);
                }
            }
        }
    }

    private static ExcelExportEntity createExcelExportEntity(Field field, String targetId, Class<?> pojoClass, List<Method> getMethods, ExcelEntity excelGroup) {
        Excel excel = field.getAnnotation(Excel.class);
        ExcelExportEntity excelEntity = new ExcelExportEntity();
        excelEntity.setType(excel.type());
        excelEntity.setKey(field.getName());
        excelEntity.setName(PoiPublicUtil.getValueByTargetId(excel.name(), targetId, null));
        excelEntity.setWidth(excel.width());
        excelEntity.setHeight(excel.height());
        excelEntity.setNeedMerge(excel.needMerge());
        excelEntity.setMergeVertical(excel.mergeVertical());
        excelEntity.setMergeRely(excel.mergeRely());
        excelEntity.setReplace(excel.replace());
        excelEntity.setOrderNum(Integer.parseInt(PoiPublicUtil.getValueByTargetId(excel.orderNum(), targetId, "0")));
        excelEntity.setWrap(excel.isWrap());
        excelEntity.setExportImageType(excel.imageType());
        excelEntity.setSuffix(excel.suffix());
        excelEntity.setDatabaseFormat(excel.databaseFormat());
        excelEntity.setFormat(StringUtils.isNotEmpty(excel.exportFormat()) ? excel.exportFormat() : excel.format());
        excelEntity.setStatistics(excel.isStatistics());
        excelEntity.setHyperlink(excel.isHyperlink());
        excelEntity.setMethod(PoiReflectorUtil.fromCache(pojoClass).getGetMethod(field.getName()));
        excelEntity.setNumFormat(excel.numFormat());
        excelEntity.setColumnHidden(excel.isColumnHidden());
        excelEntity.setDict(excel.dict());
        excelEntity.setEnumExportField(excel.enumExportField());
        excelEntity.setTimezone(excel.timezone());
        excelEntity.setAddressList(excel.addressList());
        excelEntity.setDesensitizationRule(excel.desensitizationRule());
        if (excelGroup != null) {
            excelEntity.setGroupName(PoiPublicUtil.getValueByTargetId(excelGroup.name(), targetId, null));
        } else {
            excelEntity.setGroupName(excel.groupName());
        }

        if (getMethods != null) {
            List<Method> newMethods = new ArrayList<>(getMethods);
            newMethods.add(excelEntity.getMethod());
            excelEntity.setMethods(newMethods);
        }

        return excelEntity;
    }

    public static void exportExcelByTableCode(List<?> list, String name, HttpServletResponse response, QueryFieldDto queryFieldDto) throws IOException {
        exportExcelByTableCode(list, name + "xlsx", new ExportParams(name, name, ExcelType.HSSF), response, queryFieldDto);
    }

    public static void exportExcelByTableCode(List<?> list, String fileName, ExportParams exportParams, HttpServletResponse response, QueryFieldDto queryFieldDto) throws IOException {
        String tableCode = queryFieldDto.getTableCode();
        Assert.notBlank(tableCode, "tableCode不能为空");
        ColumnUserDefineService columnUserDefineService = SpringUtil.getBean(ColumnUserDefineService.class);
        List<ColumnDefine> detail = columnUserDefineService.findDefaultDetail(tableCode);
        Assert.notEmpty(detail, "没有找到对应列配置，请联系管理员维护");
        List<ExcelExportEntity> excelParams = new ArrayList<>();

        List<String> imgColumns = new ArrayList<>();
        for (ColumnDefine columnDefine : detail) {
            if (BaseGlobal.NO.equals(columnDefine.getHidden())) {
                continue;
            }
            ExcelExportEntity excelEntity = new ExcelExportEntity();
            excelEntity.setKey(columnDefine.getColumnCode());
            excelEntity.setName(columnDefine.getColumnName());
            excelEntity.setWidth((double) columnDefine.getColumnWidth() / 6.8);
            //excelEntity.setHeight(excel.height());
            //excelEntity.setNeedMerge(excel.needMerge());
            //excelEntity.setMergeVertical(excel.mergeVertical());
            //excelEntity.setMergeRely(excel.mergeRely());
            if (StrUtil.isNotEmpty(columnDefine.getDictType())) {
                JSONArray jsonArray = JSONArray.parseArray(columnDefine.getDictType());
                String[] replace = new String[jsonArray.size() + 1];
                replace[jsonArray.size() ] = "_null";
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String label = jsonObject.getString("label");
                    String value = jsonObject.getString("value");
                    replace[i] = label + "_" + value;
                }
                excelEntity.setReplace(replace);
            }
            excelEntity.setOrderNum(columnDefine.getSortOrder());
            //excelEntity.setWrap(excel.isWrap());
            if (StrUtil.isNotEmpty(columnDefine.getColumnType()) && "img".equals(columnDefine.getColumnType())) {
                excelEntity.setExportImageType(2);
                excelEntity.setType(2);
                imgColumns.add(columnDefine.getColumnCode());
                //将key拼接1，后续将byte[]处理到对应字段上
                excelEntity.setKey(columnDefine.getColumnCode() + "1");
            } else if (StrUtil.isNotEmpty(columnDefine.getColumnType()) && "date".equals(columnDefine.getColumnType())) {
                excelEntity.setFormat(columnDefine.getDataFormat());
            }

            //excelEntity.setSuffix(excel.suffix());
            //excelEntity.setDatabaseFormat(excel.databaseFormat());
            //excelEntity.setStatistics(excel.isStatistics());
            //excelEntity.setHyperlink(excel.isHyperlink());
            //excelEntity.setMethod(PoiReflectorUtil.fromCache(pojoClass).getGetMethod(field.getName()));
            //excelEntity.setNumFormat(excel.numFormat());
            //excelEntity.setColumnHidden(excel.isColumnHidden());
            //excelEntity.setDict(excel.dict());
            //excelEntity.setEnumExportField(excel.enumExportField());
            //excelEntity.setTimezone(excel.timezone());
            //excelEntity.setAddressList(excel.addressList());
            //excelEntity.setDesensitizationRule(excel.desensitizationRule());
            if (StrUtil.isNotEmpty(columnDefine.getGroupName())) {
                excelEntity.setGroupName(columnDefine.getGroupName());
            }
            excelParams.add(excelEntity);
        }

        JSONArray jsonArray = JSONArray.parseArray(JSONObject.toJSONString(list));

        if (CollUtil.isNotEmpty(imgColumns) && StrUtil.equals(queryFieldDto.getImgFlag(), BaseGlobal.YES)) {
            StylePicUtils stylePicUtils = SpringUtil.getBean(StylePicUtils.class);
            ExecutorService executor = ExecutorBuilder.create()
                    .setCorePoolSize(8)
                    .setMaxPoolSize(10)
                    .setWorkQueue(new LinkedBlockingQueue<>(list.size()))
                    .build();
            try {
                /*导出图片*/
                if (CollUtil.isNotEmpty(list) && list.size() > 1500) {
                    throw new OtherException("带图片导出最多只能导出1500条");
                }
                CountDownLatch countDownLatch = new CountDownLatch(list.size());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    executor.submit(() -> {
                        try {
                            for (String imgColumn : imgColumns) {
                                String imgUrl = stylePicUtils.getStyleColorUrl2(imgColumn, 30);
                                jsonObject.put(imgColumn + "1", HttpUtil.downloadBytes(imgUrl));
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        } finally {
                            //每次减一
                            countDownLatch.countDown();
                            log.info(String.valueOf(countDownLatch.getCount()));
                        }
                    });
                }
                countDownLatch.await();
            } catch (Exception e) {
                throw new OtherException(e.getMessage());
            } finally {
                executor.shutdown();
            }
        }
        defaultExport(jsonArray, fileName, response, exportParams, excelParams);
    }

}
