package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.ExecutorContext;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.business.ProcessDatabaseType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampProcessDatabaseDto;
import com.base.sbc.module.basicsdatum.dto.BasicCategoryDot;
import com.base.sbc.module.basicsdatum.dto.BasicsCraftExcelDto;
import com.base.sbc.module.basicsdatum.dto.ComponentLibraryExcelDto;
import com.base.sbc.module.basicsdatum.dto.CraftMaterialExcelDto;
import com.base.sbc.module.basicsdatum.dto.ExternalCraftExcelDto;
import com.base.sbc.module.basicsdatum.dto.FormworkComponentExcelDto;
import com.base.sbc.module.basicsdatum.dto.ProcessDatabaseExcelDto;
import com.base.sbc.module.basicsdatum.dto.ProcessDatabasePageDto;
import com.base.sbc.module.basicsdatum.entity.ProcessDatabase;
import com.base.sbc.module.basicsdatum.mapper.ProcessDatabaseMapper;
import com.base.sbc.module.basicsdatum.service.ProcessDatabaseService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private DataPermissionsService dataPermissionsService;

    /**
     * @param file 文件
     * @return 成功或者失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importExcel(MultipartFile file) throws Exception {

        String originalFilename = file.getOriginalFilename();
        String[] split = originalFilename.split("\\.");
        /*类别 1：部件库，2：基础工艺，3：外辅工艺，4：裁剪工艺，5：注意事项，6：整烫包装，7：模板部件*/
        ProcessDatabaseType type = ProcessDatabaseType.findByText(split[0]);
        if (type == null) {
            throw new OtherException("文件名称错误");
        }
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<ProcessDatabaseExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), ProcessDatabaseExcelDto.class, params);
        list = list.stream().filter(s -> StringUtils.isNotBlank(s.getCode())).collect(Collectors.toList());

        //获取字典值
        String dict = type.getDict();
        Map<String, Map<String, String>> dictInfoToMap = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        if (StringUtils.isNotBlank(dict)) {
            dictInfoToMap = ccmFeignService.getDictInfoToMap(dict);
            map = dictInfoToMap.get(dict.split(",")[0]);
        }
        List<BasicCategoryDot> basicCategoryDotList = new ArrayList<>();
        if (type == ProcessDatabaseType.mbbj) {
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

            if (type == ProcessDatabaseType.bjk || type == ProcessDatabaseType.mbbj) {
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
            } else if (type == ProcessDatabaseType.jcgy || type == ProcessDatabaseType.ztbz) {

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

            if (type == ProcessDatabaseType.mbbj) {
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
            LambdaQueryWrapper<ProcessDatabase> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProcessDatabase::getCode, processDatabase.getCode());
            queryWrapper.eq(ProcessDatabase::getType, type);
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
    public void deriveExcel(HttpServletResponse response, ProcessDatabaseType type) throws IOException {
        if (type == null) {
            throw new OtherException(BaseErrorEnum.ERR_MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION);
        }
        ProcessDatabasePageDto pageDto = new ProcessDatabasePageDto();
        pageDto.setType(type);
        List<ProcessDatabase> dataList = list(pageDto);
        switch (type) {
            case bjk: {
                /*部件库*/
                List<ComponentLibraryExcelDto> list = BeanUtil.copyToList(dataList, ComponentLibraryExcelDto.class);
                ExcelUtils.exportExcel(list, ComponentLibraryExcelDto.class, "基础资料.xlsx", new ExportParams(), response);
                break;
            }
            case jcgy:
            case ztbz: {
                /*基础工艺*/
                List<BasicsCraftExcelDto> list = BeanUtil.copyToList(dataList, BasicsCraftExcelDto.class);
                ExcelUtils.exportExcel(list, BasicsCraftExcelDto.class, "基础资料.xlsx", new ExportParams(), response);
                break;
            }
            case wfgy: {
                /*外辅工艺*/
                List<ExternalCraftExcelDto> list = BeanUtil.copyToList(dataList, ExternalCraftExcelDto.class);
                ExcelUtils.exportExcel(list, ExternalCraftExcelDto.class, "基础资料.xlsx", new ExportParams(), response);
                break;
            }
            case mbbj: {
                /*模板部件*/
                List<FormworkComponentExcelDto> list = BeanUtil.copyToList(dataList, FormworkComponentExcelDto.class);
                minioUtils.setObjectUrlToList(list, "picture");
                try {
                    /*导出图片*/
                    CountDownLatch countDownLatch = new CountDownLatch(list.size());
                    for (FormworkComponentExcelDto dto : list) {
                        ExecutorContext.imageExecutor.submit(() -> {
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
                }
                break;
            }
            default: {
                List<CraftMaterialExcelDto> list = BeanUtil.copyToList(dataList, CraftMaterialExcelDto.class);
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
            LambdaQueryWrapper<ProcessDatabase> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProcessDatabase::getCode, addRevampProcessDatabaseDto.getCode());
            queryWrapper.eq(ProcessDatabase::getType, addRevampProcessDatabaseDto.getType());
            List<ProcessDatabase> processDatabaseList = baseMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(processDatabaseList)) {
                throw new OtherException("同一个工艺类型下，不允许编码重复！");
            }
        }
        ProcessDatabase processDatabase = new ProcessDatabase();
        BeanUtils.copyProperties(addRevampProcessDatabaseDto, processDatabase);
        CommonUtils.removeQuery(processDatabase, "picture");
        return saveOrUpdate(processDatabase, processDatabase.getType().getText(), addRevampProcessDatabaseDto.getProcessName(), addRevampProcessDatabaseDto.getCode());
    }

    /**
     * 分页查询
     *
     * @param pageDto 查询条件对象
     * @return 分页对象
     */
    @Override
    public PageInfo<ProcessDatabase> listPage(ProcessDatabasePageDto pageDto) {
        Page<ProcessDatabase> page = pageDto.startPage();
        List<ProcessDatabase> list = list(pageDto);
        minioUtils.setObjectUrlToList(list, "picture");
        return page.toPageInfo();
    }

    @Override
    public List<ProcessDatabase> list(ProcessDatabasePageDto pageDto) {
        BaseQueryWrapper<ProcessDatabase> queryWrapper = buildQueryWrapper(pageDto);
        return this.list(queryWrapper);
    }

    private BaseQueryWrapper<ProcessDatabase> buildQueryWrapper(ProcessDatabasePageDto pageDto) {
        BaseQueryWrapper<ProcessDatabase> queryWrapper = new BaseQueryWrapper<>();

        queryWrapper.notNullEq("type", pageDto.getType());
        queryWrapper.notEmptyLike("process_type", pageDto.getProcessType());
        queryWrapper.notEmptyEq("status", pageDto.getStatus());
        queryWrapper.notEmptyLike("description", pageDto.getDescription());
        queryWrapper.notEmptyLike("create_name", pageDto.getCreateName());
        queryWrapper.notEmptyLike("process_name", pageDto.getProcessName());
        queryWrapper.likeList("process_name", pageDto.getProcessNameList());
        queryWrapper.notEmptyLike("code", pageDto.getCode());
        queryWrapper.notEmptyLike("brand_id", pageDto.getBrandCode());
        queryWrapper.notEmptyLike("component", pageDto.getComponent());
        queryWrapper.notEmptyLike("category_id", pageDto.getCategoryCode());
        queryWrapper.notEmptyLike("category_id", pageDto.getCategoryId());
        queryWrapper.notEmptyIn("component", pageDto.getComponent());
        queryWrapper.notEmptyLike("category_name", pageDto.getCategoryName());
        queryWrapper.notEmptyLike("process_require", pageDto.getProcessRequire());
        queryWrapper.andLike(pageDto.getSearch(), "code", "process_name");
        queryWrapper.between("create_date", pageDto.getCreateDate());
        queryWrapper.orderByDesc(Opt.ofBlankAble(pageDto.getOrderBy()).orElse("create_date"));

        if (pageDto.getType() == ProcessDatabaseType.bjk) {
            dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.componentLibrary.getK());
        }
        return queryWrapper;
    }

    /**
     * 获取到部件中部件类别可查询的数据
     * @return
     */
    @Override
    public List<ProcessDatabase> getQueryList(ProcessDatabasePageDto pageDto, String field) {
        pageDto.setStatus(BaseGlobal.NO);
        BaseQueryWrapper<ProcessDatabase> queryWrapper = buildQueryWrapper(pageDto);

        queryWrapper.groupBy(StringUtils.toUnderScoreCase(field));
        List<ProcessDatabase> processDatabaseList = this.list(queryWrapper);
        if (CollUtil.isEmpty(processDatabaseList)) {
            return processDatabaseList;
        }

        if (StrUtil.equals(field, "categoryId")) {
            return processDatabaseList.stream().map(ProcessDatabase::getCategoryName).distinct().map(it -> {
                ProcessDatabase p = new ProcessDatabase();
                p.setCategoryName(it);
                return p;
            }).collect(Collectors.toList());
        }

        /*去掉空数据*/
        return processDatabaseList.stream().filter(p -> StrUtil.isNotBlank(BeanUtil.getProperty(p, field))).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> listAllDistinct(ProcessDatabasePageDto pageDto) {
        BaseQueryWrapper<ProcessDatabase> queryWrapper = buildQueryWrapper(pageDto);
        queryWrapper.select("component","component_name");

        List<ProcessDatabase> list = this.list(queryWrapper);
        return list.stream().collect(Collectors.toMap(ProcessDatabase::getComponent, ProcessDatabase::getComponentName, (v1, v2) -> v1));
    }
}
