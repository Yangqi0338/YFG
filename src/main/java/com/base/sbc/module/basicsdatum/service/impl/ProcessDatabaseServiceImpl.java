package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.minio.MinioUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.ProcessDatabaseExcelDto;
import com.base.sbc.module.basicsdatum.dto.ProcessDatabasePageDto;
import com.base.sbc.module.basicsdatum.entity.ProcessDatabase;
import com.base.sbc.module.basicsdatum.mapper.ProcessDatabaseMapper;
import com.base.sbc.module.basicsdatum.service.ProcessDatabaseService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
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
        if(name.indexOf("部件库")!= -1){
            type="1";
        }else if(name.indexOf("基础工艺")!= -1){
            type="2";
        }else if(name.indexOf("外辅工艺")!= -1){
            type="3";
        }else if(name.indexOf("裁剪工艺")!= -1){
            type="4";
        }else if(name.indexOf("注意事项")!= -1){
            type="5";
        }else if(name.indexOf("整烫包装")!= -1){
            type="6";
        }else if(name.indexOf("模板部件")!= -1){
            type="7";
        }
        /*switch (split[0]){
            case "部件库":
                type="1";
                break;
            case "基础工艺":
                type="2";
                break;
            case "外辅工艺":
                type="3";
                break;
            case "裁剪工艺":
                type="4";
                break;
            case "注意事项":
                type="5";
                break;
            case "整烫包装":
                type="6";
                break;
            case "模板部件":
                type="7";
                break;
            default:
                break;
        }*/
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<ProcessDatabaseExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), ProcessDatabaseExcelDto.class, params);
        list = list.stream().filter(s -> StringUtils.isNotBlank(s.getCode())).collect(Collectors.toList());
        for (ProcessDatabaseExcelDto processDatabaseExcelDto : list) {
            processDatabaseExcelDto.setType(type);
            if (!StringUtils.isEmpty(processDatabaseExcelDto.getPicture())) {
                if (StringUtils.isNotEmpty(processDatabaseExcelDto.getPicture())) {
                    File file1 = new File(processDatabaseExcelDto.getPicture());
                    /*上传图*/
                    AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1));
                    processDatabaseExcelDto.setPicture(attachmentVo.getUrl());
                }
            }
            if ("1".equals(type) || "7".equals(type)){
                processDatabaseExcelDto.setProcessType(processDatabaseExcelDto.getComponentCategory());
            }

            if ("3".equals(type)){
                processDatabaseExcelDto.setCategoryName(processDatabaseExcelDto.getMajorCategories()+"-"+processDatabaseExcelDto.getMiddleClass()+"-"+processDatabaseExcelDto.getSubclass());
            }
        }

        List<ProcessDatabase> processDatabaseList = BeanUtil.copyToList(list, ProcessDatabase.class);

        for (ProcessDatabase processDatabase : processDatabaseList) {
            QueryWrapper<ProcessDatabase> queryWrapper =new QueryWrapper<>();
            queryWrapper.eq("code",processDatabase.getCode());
            queryWrapper.eq("type",type);
            this.saveOrUpdate(processDatabase,queryWrapper);
        }
        return true;
    }

    /**
     * 分页查询
     *
     * @param pageDto 查询条件对象
     * @return 分页对象
     */
    @Override
    public PageInfo<ProcessDatabase> listPage(ProcessDatabasePageDto pageDto) {
        QueryWrapper<ProcessDatabase> queryWrapper =new QueryWrapper<>();

        queryWrapper.eq(StringUtils.isNotEmpty(pageDto.getType()),"type",pageDto.getType());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getProcessType()),"process_type",pageDto.getProcessType());
        queryWrapper.eq(StringUtils.isNotEmpty(pageDto.getStatus()),"status",pageDto.getStatus());

        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getDescription()),"description",pageDto.getDescription());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getProcessName()),"process_name",pageDto.getProcessName());
        queryWrapper.like(StringUtils.isNotEmpty(pageDto.getCode()),"code",pageDto.getCode());
        queryWrapper.orderByDesc("create_date");
        if (pageDto.getTime() != null && pageDto.getTime().length > 0) {
            queryWrapper.ge(StringUtils.isNotEmpty(pageDto.getTime()[0]), "create_date", pageDto.getTime()[0]);
            if (pageDto.getTime().length > 1) {
                queryWrapper.and(i -> i.le(StringUtils.isNotEmpty(pageDto.getTime()[1]), "create_date", pageDto.getTime()[1]));
            }
        }

        PageHelper.startPage(pageDto);
        List<ProcessDatabase> list = this.list(queryWrapper);
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


}
