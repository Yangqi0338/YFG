package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.entity.ProcessDatabase;
import com.base.sbc.module.basicsdatum.mapper.ProcessDatabaseMapper;
import com.base.sbc.module.basicsdatum.service.ProcessDatabaseService;
import com.base.sbc.module.basicsdatum.vo.ProcessDatabaseSelectVO;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/6/5 9:24:57
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class ProcessDatabaseServiceImpl extends BaseServiceImpl<ProcessDatabaseMapper, ProcessDatabase> implements ProcessDatabaseService {

    private final UploadFileService uploadFileService;

    private final MinioUtils minioUtils;

    private final CcmFeignService ccmFeignService;

    /**
     * @param file 文件
     * @return 成功或者失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importExcel(MultipartFile file) throws Exception {

        String originalFilename = file.getOriginalFilename();
        String[] split = originalFilename.split("\\.");
        String type = "";
        /*类别 1：部件库，2：基础工艺，3：外辅工艺，4：裁剪工艺，5：注意事项，6：整烫包装，7：模板部件*/
        String name = split[0];
        String dict = "";
        if (name.contains("部件库")) {
            dict = "C8_SpecCategory";
            type = "1";
        } else if (name.contains("基础工艺")) {
            dict = "C8_SewingType";
            type = "2";
        } else if (name.contains("外辅工艺")) {
            type = "3";
        } else if (name.contains("裁剪工艺")) {
            type = "4";
        } else if (name.contains("注意事项")) {
            type = "5";
        } else if (name.contains("整烫包装")) {
            dict = "C8_SewingType";
            type = "6";
        } else if (name.contains("模板部件")) {
            dict = "C8_SpecCategory,C8_Brand";
            type = "7";
        }

        if (StringUtils.isBlank(type)) {
            throw new OtherException("文件名称错误");
        }
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<ProcessDatabaseExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), ProcessDatabaseExcelDto.class, params);
        list = list.stream().filter(s -> StringUtils.isNotBlank(s.getCode())).collect(Collectors.toList());

        //获取字典值
        Map<String, Map<String, String>> dictInfoToMap = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        if (StringUtils.isNotBlank(dict)) {
            dictInfoToMap = ccmFeignService.getDictInfoToMap(dict);
            map = dictInfoToMap.get(dict.split(",")[0]);
        }
        List<BasicCategoryDot> basicCategoryDotList = new ArrayList<>();
        if ("7".equals(type)) {
            basicCategoryDotList = ccmFeignService.getTreeByNamelList("品类", "1");
            map1 = dictInfoToMap.get(dict.split(",")[1]);
        }
        for (ProcessDatabaseExcelDto processDatabaseExcelDto : list) {
            processDatabaseExcelDto.setType(type);
            if (!StringUtils.isEmpty(processDatabaseExcelDto.getPicture())) {
                if (StringUtils.isNotEmpty(processDatabaseExcelDto.getPicture())) {
                    File file1 = new File(processDatabaseExcelDto.getPicture());
                    /*上传图*/
                    AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1), "MaterialOther/ProcessDatabase/" + processDatabaseExcelDto.getCode() + ".jpg");
                    processDatabaseExcelDto.setPicture(CommonUtils.removeQuery(attachmentVo.getUrl()));
                }
            }

            if (type.equals(BaseGlobal.OUT) || "7".equals(type)) {
                /*部件*/
                if (StringUtils.isNotBlank(processDatabaseExcelDto.getComponentName())) {
                    String[] componentNames = processDatabaseExcelDto.getComponentName().replaceAll(" ", "").split(",");
                    List<String> stringList = new ArrayList<>();
                    List<String> stringList1 = new ArrayList<>();
                    map.forEach((k, v) -> {
                        if (Arrays.asList(componentNames).contains(v)) {
                            stringList.add(k);
                            stringList1.add(v);
                        }
                    });
                    processDatabaseExcelDto.setComponent(StringUtils.join(stringList, ","));
                    processDatabaseExcelDto.setComponentName(StringUtils.join(stringList1, ","));
                }
            } else if (type.equals(BaseGlobal.OUT_READY) || "6".equals(type)) {

                /*工艺类型*/
                if (StringUtils.isNotBlank(processDatabaseExcelDto.getProcessTypeName())) {
                    String[] processTypeNames = processDatabaseExcelDto.getProcessTypeName().replaceAll(" ", "").split(",");
                    List<String> stringList = new ArrayList<>();
                    List<String> stringList1 = new ArrayList<>();
                    map.forEach((k, v) -> {
                        if (Arrays.asList(processTypeNames).contains(v)) {
                            stringList.add(k);
                            stringList1.add(v);
                        }
                    });
                    processDatabaseExcelDto.setProcessType(StringUtils.join(stringList, ","));
                    processDatabaseExcelDto.setProcessTypeName(StringUtils.join(stringList1, ","));
                }

            }

            if ("7".equals(type)) {
//                processDatabaseExcelDto.setProcessType(processDatabaseExcelDto.getComponentCategory());
                /*品类*/
                if (StringUtils.isNotBlank(processDatabaseExcelDto.getCategoryName())) {
                    String[] strings = processDatabaseExcelDto.getCategoryName().replaceAll(" ", "").split(",");
                    List<BasicCategoryDot> list1 = basicCategoryDotList.stream().filter(b -> Arrays.asList(strings).contains(b.getName())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(list1)) {
                        List<String> stringList = list1.stream().map(BasicCategoryDot::getValue).collect(Collectors.toList());
                        List<String> stringList1 = list1.stream().map(BasicCategoryDot::getName).collect(Collectors.toList());
                        processDatabaseExcelDto.setCategoryId(StringUtils.join(stringList, ","));
                        processDatabaseExcelDto.setCategoryName(StringUtils.join(stringList1, ","));
                    }
                }
                /*品牌*/
                if (StringUtils.isNotBlank(processDatabaseExcelDto.getBrandName())) {
                    String[] strings = processDatabaseExcelDto.getBrandName().replaceAll(" ", "").split(",");
                    List<String> stringList = new ArrayList<>();
                    List<String> stringList1 = new ArrayList<>();
                    map1.forEach((k, v) -> {
                        if (Arrays.asList(strings).contains(v)) {
                            stringList.add(k);
                            stringList1.add(v);
                        }
                    });
                    processDatabaseExcelDto.setBrandId(StringUtils.join(stringList, ","));
                    processDatabaseExcelDto.setBrandName(StringUtils.join(stringList1, ","));
                }
            }
        }

        List<ProcessDatabase> processDatabaseList = BeanUtil.copyToList(list, ProcessDatabase.class);

        for (ProcessDatabase processDatabase : processDatabaseList) {
            QueryWrapper<ProcessDatabase> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code", processDatabase.getCode());
            queryWrapper.eq("type", type);
            this.saveOrUpdate(processDatabase, queryWrapper);
        }
        return true;
    }

    /**
     * 导出
     *
     * @param response
     * @param type
     */
    @Override
    public void deriveExcel(HttpServletResponse response, String type) throws IOException {
        if (StringUtils.isBlank(type)) {
            throw new OtherException(BaseErrorEnum.ERR_MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION);
        }
        QueryWrapper<ProcessDatabase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);

        switch (type) {
            case "1": {
                /*部件库*/
                List<ComponentLibraryExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), ComponentLibraryExcelDto.class);
                ExcelUtils.exportExcel(list, ComponentLibraryExcelDto.class, "基础资料.xlsx", new ExportParams(), response);
                break;
            }
            case "2":
            case "6": {
                /*基础工艺*/
                List<BasicsCraftExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), BasicsCraftExcelDto.class);
                ExcelUtils.exportExcel(list, BasicsCraftExcelDto.class, "基础资料.xlsx", new ExportParams(), response);
                break;
            }
            case "3": {
                /*外辅工艺*/
                List<ExternalCraftExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), ExternalCraftExcelDto.class);
                ExcelUtils.exportExcel(list, ExternalCraftExcelDto.class, "基础资料.xlsx", new ExportParams(), response);
                break;
            }
            case "7": {
                /*模板部件*/
                List<FormworkComponentExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), FormworkComponentExcelDto.class);
                minioUtils.setObjectUrlToList(list, "picture");

                ExecutorService executor = ExecutorBuilder.create()
                        .setCorePoolSize(8)
                        .setMaxPoolSize(10)
                        .setWorkQueue(new LinkedBlockingQueue<>(list.size()))
                        .build();

                try {
                    /*导出图片*/
                    CountDownLatch countDownLatch = new CountDownLatch(list.size());
                    for (FormworkComponentExcelDto dto : list) {
                        executor.submit(() -> {
                            try {
                                final String picture = dto.getPicture();
                                dto.setPicture1(HttpUtil.downloadBytes(picture));
                            } catch (Exception e) {
                                log.error(e.getMessage());
                            } finally {
                                //每次减一
                                countDownLatch.countDown();
                            }
                        });
                    }
                    countDownLatch.await();
                    ExportParams exportParams = new ExportParams();
                    exportParams.setType(ExcelType.HSSF);
                    ExcelUtils.exportExcel(list, FormworkComponentExcelDto.class, "基础资料.xlsx",exportParams , response);
                } catch (Exception e) {
                    throw new OtherException(e.getMessage());
                } finally {
                    executor.shutdown();
                }
                break;
            }
            default: {
                List<CraftMaterialExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), CraftMaterialExcelDto.class);
                ExcelUtils.exportExcel(list, CraftMaterialExcelDto.class, "基础资料.xlsx", new ExportParams(), response);
                break;
            }
        }


    }

    /**
     * @param addRevampProcessDatabaseDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean save(AddRevampProcessDatabaseDto addRevampProcessDatabaseDto) {
        /*新增查询编码是否重复*/
        if (StringUtils.isBlank(addRevampProcessDatabaseDto.getId())) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("code", addRevampProcessDatabaseDto.getCode());
            queryWrapper.eq("type", addRevampProcessDatabaseDto.getType());
            List<ProcessDatabase> processDatabaseList = baseMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(processDatabaseList)) {
                throw new OtherException("同一个工艺类型下，不允许编码重复！");
            }
        }
        ProcessDatabase processDatabase = new ProcessDatabase();
        BeanUtils.copyProperties(addRevampProcessDatabaseDto, processDatabase);
        //类别 1：部件库，2：基础工艺，3：外辅工艺，4：裁剪工艺，5：注意事项，6：整烫包装，7：模板部件
        String type="";
        switch (addRevampProcessDatabaseDto.getType()){
            case "1":
                type="部件库";
                break;
            case "2":
                type="基础工艺";
                break;
            case "3":
                type="外辅工艺";
                break;
            case "4":
                type="裁剪工艺";
                break;
            case "5":
                type="注意事项";
                break;
            case "6":
                type="整烫包装";
                break;
            case "7":
                type="模板部件";
                break;
            default:
                break;
        }
        CommonUtils.removeQuery(processDatabase, "picture");
        return saveOrUpdate(processDatabase,type,addRevampProcessDatabaseDto.getProcessName(),addRevampProcessDatabaseDto.getCode());
    }

    /**
     * 分页查询
     *
     * @param pageDto 查询条件对象
     * @return 分页对象
     */
    @Override
    public PageInfo<ProcessDatabase> listPage(ProcessDatabasePageDto pageDto) {
        BaseQueryWrapper<ProcessDatabase> queryWrapper = new BaseQueryWrapper<>();

        queryWrapper.eq(StringUtils.isNotEmpty(pageDto.getType()), "type", pageDto.getType());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getProcessType()), "process_type", pageDto.getProcessType());
        queryWrapper.eq(StringUtils.isNotEmpty(pageDto.getStatus()), "status", pageDto.getStatus());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getBrandName()), "brand_name", pageDto.getBrandName());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getDescription()), "description", pageDto.getDescription());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getCreateName()), "create_name", pageDto.getCreateName());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getProcessName()), "process_name", pageDto.getProcessName());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getCode()), "code", pageDto.getCode());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getBrandCode()), "brand_id", pageDto.getBrandCode());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getComponent()), "component", pageDto.getComponent());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getCategoryCode()), "category_id", pageDto.getCategoryCode());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getCategoryId()), "category_id", pageDto.getCategoryId());
        queryWrapper.in(StringUtils.isNotEmpty(pageDto.getComponent()), "component", StringUtils.convertList(pageDto.getComponent()));
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getCategoryName()), "category_name", pageDto.getCategoryName());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getProcessRequire()), "process_require", pageDto.getProcessRequire());



        queryWrapper.andLike(pageDto.getSearch(), "code", "process_name");
        if (pageDto.getCreateDate() != null) {

            queryWrapper.between("create_date", pageDto.getCreateDate());
        }

        if (StringUtils.isNotEmpty(pageDto.getOrderBy())) {
            queryWrapper.orderByDesc(pageDto.getOrderBy());
        }else {
            queryWrapper.orderByDesc("create_date");
        }

        //if (pageDto.getTime() != null && pageDto.getTime().length > 0) {
        //    queryWrapper.ge(StringUtils.isNotEmpty(pageDto.getTime()[0]), "create_date", pageDto.getTime()[0]);
        //    if (pageDto.getTime().length > 1) {
        //        queryWrapper.and(i -> i.le(StringUtils.isNotEmpty(pageDto.getTime()[1]), "create_date", pageDto.getTime()[1]));
        //    }
        //}

        PageHelper.startPage(pageDto);
        List<ProcessDatabase> list = this.list(queryWrapper);
        minioUtils.setObjectUrlToList(list, "picture");
        return new PageInfo<>(list);
    }

    @Override
    public List<String> getAllPatternPartsCode() {
        QueryWrapper<ProcessDatabase> qw = new QueryWrapper<>();
        qw.select(" ");
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.ne("del_flag", BaseGlobal.YES);
        qw.eq("type", BasicNumber.SEVEN.getNumber());
        return getBaseMapper().getAllPatternPartsCode(qw);

    }

    @Override
    public List<ProcessDatabaseSelectVO> selectProcessDatabase(String type, String categoryName, String companyCode) {
        return super.getBaseMapper().selectProcessDatabase(type, categoryName, companyCode);
    }

    @Override
    public List<ProcessDatabase> getAll() {
        QueryWrapper<ProcessDatabase> qw = new QueryWrapper<>();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.ne("del_flag", BaseGlobal.YES);
        qw.eq("type", BasicNumber.SEVEN.getNumber());
        return list(qw);
    }

    /**
     * 获取到部件中部件类别可查询的数据
     *
     * @param type
     * @param companyCode
     * @return
     */
    @Override
    public List<ProcessDatabase> getQueryList(String type,String field, String brandId,String categoryId,String companyCode) {

        QueryWrapper<ProcessDatabase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(COMPANY_CODE, companyCode);
        queryWrapper.eq("del_flag", BaseGlobal.NO);
        queryWrapper.eq("type", type);
        queryWrapper.eq("status", BaseGlobal.NO);
        queryWrapper.like(StringUtils.isNotBlank(brandId),"brand_id", brandId);
        queryWrapper.like(StringUtils.isNotBlank(categoryId),"category_id", categoryId);
        queryWrapper.groupBy(StringUtils.toUnderScoreCase(field));
        List<ProcessDatabase> processDatabaseList = baseMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(processDatabaseList)) {
            return processDatabaseList;
        }

        if (StrUtil.equals(field, "categoryId")) {
            List<ProcessDatabase> list = new ArrayList<>();
            String collect = processDatabaseList.stream().map(ProcessDatabase::getCategoryName).collect(Collectors.joining(","));

            List<String> stringList = StringUtils.convertList(collect);
            stringList = stringList.stream().distinct().collect(Collectors.toList());
            for (String s : stringList) {
                ProcessDatabase p = new ProcessDatabase();
                p.setCategoryName(s);
                list.add(p);
            }
            return list;
        }

        /*去掉空数据*/
        return processDatabaseList.stream().filter(p -> StrUtil.isNotBlank(BeanUtil.getProperty(p, field))).collect(Collectors.toList());

    }

    @Override
    public Map<String, String> listAllDistinct(ProcessDatabasePageDto pageDto) {
        BaseQueryWrapper<ProcessDatabase> queryWrapper = new BaseQueryWrapper<>();

        queryWrapper.eq(StringUtils.isNotEmpty(pageDto.getType()), "type", pageDto.getType());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getCategoryId()), "category_id", pageDto.getCategoryId());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getCategoryName()), "category_name", pageDto.getCategoryName());

        if (StringUtils.isNotEmpty(pageDto.getOrderBy())) {
            queryWrapper.orderByDesc(pageDto.getOrderBy());
        }else {
            queryWrapper.orderByDesc("create_date");
        }

        queryWrapper.select("component","component_name");

        List<ProcessDatabase> list = this.list(queryWrapper);

        return list.stream().collect(Collectors.toMap(ProcessDatabase::getComponent, ProcessDatabase::getComponentName, (v1, v2) -> v1));
    }
}
