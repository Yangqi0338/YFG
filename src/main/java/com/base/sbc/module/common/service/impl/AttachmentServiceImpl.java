/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.module.common.dto.AttachmentSaveDto;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.mapper.AttachmentMapper;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackPatternAttachmentSaveDto;
import com.base.sbc.module.pack.dto.PackTechSpecSavePicDto;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.sample.dto.SampleAttachmentDto;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 类描述：附件 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.common.service.AttachmentService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-12 16:05:59
 */
@Service
public class AttachmentServiceImpl extends BaseServiceImpl<AttachmentMapper, Attachment> implements AttachmentService {


    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private MinioUtils minioUtils;
//** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 通过fid 查询
     *
     * @param foreignId
     * @return
     */
    @Override
    public List<AttachmentVo> findByforeignId(String foreignId, String type) {
        return getBaseMapper().findByFId(foreignId, type, null);
    }

    /**
     * 通过fid 查询
     *
     * @param foreignId
     * @return
     */
    @Override
    public List<AttachmentVo> findByforeignIds(List<String> foreignId, String type) {
        return getBaseMapper().findByFIds(foreignId, type, null);
    }

    @Override
    public List<AttachmentVo> findByforeignIdTypeLikeStart(String foreignId, String typeLikeStart) {
        return getBaseMapper().findByFId(foreignId, null, typeLikeStart);
    }

    @Override
    public AttachmentVo getAttachmentById(String id) {
        Attachment attachment = getById(id);
        QueryWrapper<UploadFile> qw = new QueryWrapper<>();
        qw.eq("id", attachment.getFileId());
        List<UploadFile> list = uploadFileService.list(qw);
        if (CollUtil.isNotEmpty(list)) {
            AttachmentVo attachmentVo = BeanUtil.copyProperties(list.get(0), AttachmentVo.class);
            attachmentVo.setId(id);
            attachmentVo.setFileId(attachment.getFileId());
            return attachmentVo;
        }
        return null;
    }

    @Override
    public AttachmentVo getAttachmentByFileId(String fileId) {
        QueryWrapper<UploadFile> qw = new QueryWrapper<>();
        qw.eq("id", fileId);
        List<UploadFile> list = uploadFileService.list(qw);
        if (CollUtil.isNotEmpty(list)) {
            AttachmentVo attachmentVo = BeanUtil.copyProperties(list.get(0), AttachmentVo.class);
            attachmentVo.setFileId(fileId);
            return attachmentVo;
        }
        return null;
    }

    /**
     * 保存附件
     *
     * @param id
     * @param files
     * @param type
     */
    @Override
    public void saveFiles(String id, List<SampleAttachmentDto> files, String type) {
        QueryWrapper<Attachment> aqw = new QueryWrapper<>();
        aqw.eq("foreign_id", id);
        aqw.eq("type", type);
        remove(aqw);
        List<Attachment> attachments = new ArrayList<>(12);
        if (CollUtil.isNotEmpty(files)) {
            attachments = BeanUtil.copyToList(files, Attachment.class);
            for (Attachment attachment : attachments) {
                attachment.setId(null);
                attachment.setForeignId(id);
                attachment.setType(type);
                attachment.setStatus(BaseGlobal.STATUS_NORMAL);
            }
            saveBatch(attachments);
        }
    }

    @Override
    public List<AttachmentVo> findByQw(QueryWrapper queryWrapper) {
        List<AttachmentVo> byQw = getBaseMapper().findByQw(queryWrapper);
        minioUtils.setObjectUrlToList(byQw, "url");
        return byQw;
    }

    @Override
    public void setListStylePic(List list, String fileIdKey) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<String> fileId = new ArrayList<>(12);
        for (Object vo : list) {
            String v = BeanUtil.getProperty(vo, fileIdKey);
            if (StrUtil.isNotBlank(v)) {
                fileId.add(v);
            }
        }
        if (CollUtil.isEmpty(fileId)) {
            return;
        }
        QueryWrapper qw = new QueryWrapper();
        qw.in("f.id", fileId);
        List<AttachmentVo> byQw = findByQw(qw);
        if (CollUtil.isEmpty(byQw)) {
            return;
        }
        Map<String, AttachmentVo> collect = byQw.stream().collect(Collectors.toMap(AttachmentVo::getFileId, v -> v, (a, b) -> a));
        for (Object vo : list) {
            String v = BeanUtil.getProperty(vo, fileIdKey);
            if (StrUtil.isBlank(v)) {
                continue;
            }
            BeanUtil.setProperty(vo, fileIdKey, Optional.ofNullable(collect.get(v)).map(AttachmentVo::getUrl).orElse(""));
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Integer saveAttachment(List<AttachmentSaveDto> dtos, String foreignId, String type) {
        // 删除之前的
        QueryWrapper<Attachment> removeQw = new QueryWrapper<>();
        removeQw.eq("foreign_id", foreignId);
        removeQw.eq("type", type);
        remove(removeQw);
        if (CollUtil.isEmpty(dtos)) {
            return 0;
        }
        List<Attachment> attachmentList = new ArrayList<>();
        for (AttachmentSaveDto dto : dtos) {
            Attachment attachment = BeanUtil.copyProperties(dto, Attachment.class);
            attachment.setForeignId(foreignId);
            attachment.setType(type);
            attachment.setStatus(BaseGlobal.STATUS_NORMAL);
            attachmentList.add(attachment);
        }
        saveBatch(attachmentList);
        return attachmentList.size();
    }

    @Override
    public PageInfo<AttachmentVo> patternAttachmentPageInfo(PackCommonPageSearchDto dto) {
        QueryWrapper<Attachment> qw = new QueryWrapper<>();
        qw.eq("foreign_id", dto.getForeignId());
        qw.eq("a.type", dto.getPackType());
        qw.eq("a.del_flag", BaseGlobal.NO);
        Page<AttachmentVo> page = PageHelper.startPage(dto);
        getBaseMapper().findByQw(qw);
        return page.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean del(String id) {
        Attachment attachment = getById(id);
        removeBatchByIds(StrUtil.split(id, CharUtil.COMMA));
        UploadFile uploadFile = uploadFileService.getById(attachment.getFileId());
        if (uploadFile != null) {
            uploadFileService.removeById(attachment.getFileId());
            minioUtils.delFile(uploadFile.getUrl());
            this.saveOperaLog("删除图片",attachment.getType(),attachment.getForeignId(),null,null,new Attachment(),attachment);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public AttachmentVo saveByPA(PackPatternAttachmentSaveDto dto) {
        Attachment attachment = BeanUtil.copyProperties(dto, Attachment.class);
        attachment.setType(dto.getPackType());
        attachment.setId(null);
        save(attachment);
        return getAttachmentById(attachment.getId());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean updateRemarks(String id, String remarks) {
        UpdateWrapper<Attachment> uw = new UpdateWrapper<>();
        uw.eq("id", id);
        uw.set("remarks", remarks);
        setUpdateInfo(uw);
        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public AttachmentVo savePackTechSpecPic(PackTechSpecSavePicDto dto) {
        Attachment attachment = BeanUtil.copyProperties(dto, Attachment.class);
        attachment.setType(dto.getPackType() + StrUtil.DASHED + dto.getSpecType());
        attachment.setId(null);
        save(attachment);
        this.saveOperaLog("上传图片",dto.getPackType()+StrUtil.DASHED+dto.getSpecType(),dto.getForeignId(),null,null,attachment,null);
        return getAttachmentById(attachment.getId());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean delByForeignIdType(String foreignId, String type) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("foreign_id", foreignId);
        qw.likeRight("type", type);
        return remove(qw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String overlayFlag,String specType) {
        if (StrUtil.equals(sourceForeignId, targetForeignId) && StrUtil.equals(sourcePackType, targetPackType)) {
            return true;
        }
        if (StrUtil.equals(overlayFlag, BaseGlobal.YES)) {
            delByForeignIdType(targetForeignId, targetPackType);
        }
        QueryWrapper qw = new QueryWrapper();
        qw.eq("foreign_id", sourceForeignId);
        qw.likeRight("type", sourcePackType + StrUtil.DASHED+specType);
        List<Attachment> list = list(qw);
        if (CollUtil.isNotEmpty(list)) {
            for (Attachment o : list) {
                o.setId(null);
                o.setForeignId(targetForeignId);
                List<String> split = StrUtil.split(o.getType(), CharUtil.DASHED);
                o.setType(targetPackType + StrUtil.DASHED + CollUtil.get(split, 1));
                o.insertInit();
            }
            return saveBatch(list);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean copyTechFile(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String item) {
        if (StrUtil.equals(sourceForeignId, targetForeignId) && StrUtil.equals(sourcePackType, targetPackType)) {
            return true;
        }
        List<String> items = StrUtil.split(item, CharUtil.COMMA);
        List<String> targetAttTypes = PackUtils.getTechSpecAttType(targetPackType, items);
        List<String> sourceAttTypes = PackUtils.getTechSpecAttType(sourcePackType, items);
        //删除目标的
//        QueryWrapper del = new QueryWrapper();
//        del.eq("foreign_id", targetForeignId);
//        del.in("type", targetAttTypes);
//        remove(del);

        QueryWrapper qw = new QueryWrapper();
        qw.eq("foreign_id", sourceForeignId);
        qw.in("type", sourceAttTypes);
        List<Attachment> list = list(qw);
        if (CollUtil.isNotEmpty(list)) {
            for (Attachment o : list) {
                o.setId(null);
                o.setForeignId(targetForeignId);
                List<String> split = StrUtil.split(o.getType(), CharUtil.DASHED);
                o.setType(targetPackType + StrUtil.DASHED + CollUtil.get(split, 1));
            }
            return saveBatch(list);
        }
        return true;
    }


//** 自定义方法区 不替换的区域【other_end】 **/

}
