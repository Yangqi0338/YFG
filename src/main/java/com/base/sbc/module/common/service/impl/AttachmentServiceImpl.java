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

//** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 通过fid 查询
     *
     * @param foreignId
     * @return
     */
    @Override
    public List<AttachmentVo> findByforeignId(String foreignId, String type) {
        return getBaseMapper().findByFId(foreignId, type);
    }

    @Override
    public AttachmentVo getAttachmentById(String id) {
        Attachment attachment = getById(id);
        QueryWrapper<UploadFile> qw =new QueryWrapper<>();
        qw.eq("id",attachment.getFileId());
        List<UploadFile> list = uploadFileService.list(qw);
        if(CollUtil.isNotEmpty(list)){
            AttachmentVo attachmentVo = BeanUtil.copyProperties(list.get(0), AttachmentVo.class);
            attachmentVo.setId(id);
            attachmentVo.setFileId(attachment.getFileId());
            return attachmentVo;
        }
        return null;
    }

    @Override
    public List<AttachmentVo> findByQw(QueryWrapper queryWrapper) {
        List<AttachmentVo> byQw = getBaseMapper().findByQw(queryWrapper);
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
        Map<String, AttachmentVo> collect = byQw.stream().collect(Collectors.toMap(k -> k.getFileId(), v -> v, (a, b) -> a));
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
        removeBatchByIds(StrUtil.split(id, CharUtil.COMMA));
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
    public AttachmentVo savePackTechSpecPic(PackTechSpecSavePicDto dto) {
        Attachment attachment = BeanUtil.copyProperties(dto, Attachment.class);
        attachment.setType(dto.getPackType() + StrUtil.DASHED + dto.getSpecType());
        attachment.setId(null);
        save(attachment);
        return getAttachmentById(attachment.getId());
    }


//** 自定义方法区 不替换的区域【other_end】 **/

}
