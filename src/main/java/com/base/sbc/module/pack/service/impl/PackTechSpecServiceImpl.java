/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.MdUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.operalog.service.OperaLogService;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackTechSpec;
import com.base.sbc.module.pack.mapper.PackInfoMapper;
import com.base.sbc.module.pack.mapper.PackTechSpecMapper;
import com.base.sbc.module.pack.service.PackTechPackagingService;
import com.base.sbc.module.pack.service.PackTechSpecService;
import com.base.sbc.module.pack.utils.CharUtils;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.pack.vo.PackTechSpecVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 类描述：资料包-工艺说明 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackTechSpecService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 15:41:45
 */
@Service
public class PackTechSpecServiceImpl extends AbstractPackBaseServiceImpl<PackTechSpecMapper, PackTechSpec> implements PackTechSpecService {


    // 自定义方法区 不替换的区域【other_start】
    @Resource
    private AttachmentService attachmentService;
    @Resource
    private PackTechPackagingService packTechPackagingService;
    @Resource
    private OperaLogService operaLogService;

    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private PackInfoMapper packInfoMapper;

    @Override
    public List<PackTechSpecVo> list(PackTechSpecSearchDto dto) {
        QueryWrapper<PackTechSpec> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        qw.eq(StrUtil.isNotBlank(dto.getSpecType()), "spec_type", dto.getSpecType());
        qw.orderByAsc("sort", "id");

        List<PackTechSpec> list = list(qw);
        return BeanUtil.copyToList(list, PackTechSpecVo.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackTechSpecVo saveByDto(PackTechSpecDto dto) {
        //新增
        if (CommonUtils.isInitId(dto.getId())) {
            PackTechSpec pageData = BeanUtil.copyProperties(dto, PackTechSpec.class);
            pageData.setId(null);
            QueryWrapper<PackTechSpec> countQw = new QueryWrapper<>();
            PackUtils.commonQw(countQw, dto);
            countQw.eq(StrUtil.isNotBlank(dto.getSpecType()), "spec_type", dto.getSpecType());
            if (dto.getSort() == null) {
                //查询排序
                List<PackTechSpec> pts = list(countQw);
                long count = pts.size();
                pageData.setSort(new BigDecimal(String.valueOf(count + 1)));
            }
            if ("裁剪工艺".equals(dto.getSpecType())) {
                List<PackTechSpec> pts = list(countQw);
                pts.add(dto);
                int totalRows = CharUtils.getRows(pts);
                if(totalRows > 10) {
                    throw new OtherException("裁剪工艺最多只能定义10条数据！(当前超出"+(totalRows - 10)+")");
                }
            }
            genContentImgUrl(dto.getContent(), null, pageData);
            save(pageData);
            return BeanUtil.copyProperties(pageData, PackTechSpecVo.class);
        }
        //修改
        else {
            PackTechSpec dbData = getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            QueryWrapper<PackTechSpec> countQw = new QueryWrapper<>();
            PackUtils.commonQw(countQw, dto);
            countQw.eq(StrUtil.isNotBlank(dto.getSpecType()), "spec_type", dto.getSpecType());
            countQw.ne("id", dto.getId());
            if ("裁剪工艺".equals(dto.getSpecType())) {
                List<PackTechSpec> pts = list(countQw);
                pts.add(dto);
                int totalRows = CharUtils.getRows(pts);
                if(totalRows > 10) {
                    throw new OtherException("裁剪工艺最多只能定义10条数据！(当前超出"+(totalRows - 10)+")");
                }
            }
            this.saveOperaLog("修改",dto.getPackType()+StrUtil.DASHED+dto.getSpecType(),dto.getForeignId(),dto.getItem(),dto.getItemCode(),dto, dbData);
            BeanUtil.copyProperties(dto, dbData);
//            String oldContent = dbData.getContent();
//            genContentImgUrl(dto.getContent(), oldContent, dbData);
            updateById(dbData);
            return BeanUtil.copyProperties(dbData, PackTechSpecVo.class);
        }
    }

    @Override
    public OperaLogEntity genOperaLogEntity(Object bean, String type) {
        PackTechSpec bean1 = (PackTechSpec) bean;
        OperaLogEntity operaLogEntity = super.genOperaLogEntity(bean, type);
        operaLogEntity.setName(getModeName() + StrUtil.DASHED + bean1.getSpecType());
        return operaLogEntity;
    }


    @Override
    public void genContentImgUrl(String newContent, String oldContent, PackTechSpec bean) {

        if (StrUtil.equals(newContent, oldContent) && StrUtil.isNotBlank(bean.getContentImgUrl())) {
            return;
        }
//        newContent=HtmlUtil.filter(newContent);
        if (StrUtil.isBlank(newContent)) {
            bean.setContentImgUrl("");
            return;
        }
        try {
            BufferedImage bufferedImage = MdUtils.htmlToImage(newContent);
            String fileName = bean.getForeignId() + StrUtil.DASHED + bean.getPackType() + bean.getSpecType() + ".png";
            AttachmentVo attachmentVo = uploadFileService.uploadToMinio(bufferedImage, fileName);
            uploadFileService.delByUrl(bean.getContentImgUrl());
            bean.setContentImgUrl(Optional.ofNullable(attachmentVo).map(AttachmentVo::getUrl).orElse(""));
            bean.setContent(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PackTechSpecVo> copyOther(List<PackTechSpecDto> list) {
        PackTechSpecDto dto = list.get(0);
        QueryWrapper<PackTechSpec> countQw = new QueryWrapper<>();
        PackUtils.commonQw(countQw, dto);
        countQw.eq(StrUtil.isNotBlank(dto.getSpecType()), "spec_type", dto.getSpecType());
        long count = count(countQw);
        List<PackTechSpec> packTechSpecs = BeanUtil.copyToList(list, PackTechSpec.class);
        for (PackTechSpec packTechSpec : packTechSpecs) {
            packTechSpec.setId(null);
            CommonUtils.resetCreateUpdate(packTechSpec);
            packTechSpec.setSort(new BigDecimal(++count));
        }
        saveBatch(packTechSpecs);
        for (PackTechSpec packTechSpec : packTechSpecs) {
            this.saveOperaLog("复制",packTechSpec.getPackType()+StrUtil.DASHED+packTechSpec.getSpecType(),packTechSpec.getForeignId(),packTechSpec.getItem(),packTechSpec.getItemCode(),packTechSpec, null);
        }
        return BeanUtil.copyToList(packTechSpecs, PackTechSpecVo.class);
    }

    @Override
    public List<PackTechSpecVo> batchSave(PackTechSpecBatchSaveDto dto) {
        List<PackTechSpecDto> list = dto.getList();

        if (StrUtil.isNotBlank(dto.getOverlayFlg())) {
            //删除
            QueryWrapper<PackTechSpec> delQw = new QueryWrapper<>();
            delQw.eq("foreign_id", dto.getForeignId());
            delQw.eq("pack_type", dto.getPackType());
            delQw.eq("spec_type", dto.getSpecType());
            remove(delQw);
        }
        QueryWrapper<PackTechSpec> countQw = new QueryWrapper<>();
        PackUtils.commonQw(countQw, dto);
        countQw.eq(StrUtil.isNotBlank(dto.getSpecType()), "spec_type", dto.getSpecType());
        List<PackTechSpec> pts = list(countQw);
        long count = pts.size();
        if ("裁剪工艺".equals(dto.getSpecType())) {
            int totalRows = 0;
            pts.addAll(list);
            for (int i = 0; i < pts.size(); i++) {
                PackTechSpec packTechSpec = pts.get(i);
                Integer itemRowCount = CharUtils.contentRows(132f, packTechSpec.getItem(), false);
                Integer contentRowCount = CharUtils.contentRows(912f, packTechSpec.getContent(), false);
                totalRows += itemRowCount > contentRowCount ? itemRowCount : contentRowCount;
            }
            if(totalRows > 10) {
                throw new OtherException("裁剪工艺最多只能定义10条数据！(当前超出"+(totalRows - 10)+")");
            }
        }
        List<PackTechSpec> packTechSpecs = BeanUtil.copyToList(list, PackTechSpec.class);
        for (PackTechSpec packTechSpec : packTechSpecs) {
            packTechSpec.setForeignId(dto.getForeignId());
            packTechSpec.setPackType(dto.getPackType());
            packTechSpec.setSpecType(dto.getSpecType());
            packTechSpec.setSort(new BigDecimal(++count));
            packTechSpec.setId(null);
        }
        saveBatch(packTechSpecs);
        /*日志记录*/
        for (PackTechSpec packTechSpec : packTechSpecs) {
            /*新增操作记录*/
            this.saveOperaLog("新增",dto.getPackType()+StrUtil.DASHED+dto.getSpecType(),dto.getForeignId(),packTechSpec.getItem(),packTechSpec.getItemCode(),packTechSpec,null);
        }
        return BeanUtil.copyToList(packTechSpecs, PackTechSpecVo.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean references(PackTechSpecReferencesDto dto) {
        //查询资料包信息
        QueryWrapper<PackInfo> packQw = new QueryWrapper();
        packQw.eq("style_color_id", dto.getStyleColorId());
        packQw.eq("pack_type", PackUtils.PACK_TYPE_DESIGN);
        List<PackInfoListVo> packInfoListVos = packInfoMapper.queryByQw(packQw);
        if (CollUtil.isEmpty(packInfoListVos)) {
            throw new OtherException("找不到款式配色的BOM信息");
        }

        PackInfoListVo packInfoListVo = packInfoListVos.get(0);
        //复制文件
        attachmentService.copyTechFile(packInfoListVo.getId(), packInfoListVo.getPackType(), dto.getTargetForeignId(), dto.getTargetPackType(), dto.getItem());
        //工艺说明
        copyItem(packInfoListVo.getId(), packInfoListVo.getPackType(), dto.getTargetForeignId(), dto.getTargetPackType(), dto.getItem());
        if (StrUtil.contains(dto.getItem(), "包装方式和体积重量")) {
            // 工艺说明包装方式
            packTechPackagingService.copy(packInfoListVo.getId(), packInfoListVo.getPackType(), dto.getTargetForeignId(), dto.getTargetPackType(), BaseGlobal.NO);
        }


        return true;
    }

    /**
     * 删除工艺说明
     *
     * @param dto
     * @return
     */
    @Override
    public boolean removeById(IdsDto dto) {
        List<PackTechSpec> packTechSpecList = baseMapper.selectBatchIds(StringUtils.convertList(dto.getId()));
        List<String> collect = packTechSpecList.stream().map(PackTechSpec::getId).collect(Collectors.toList());
        removeByIds(collect);
        /*操作记录*/
        for (PackTechSpec packTechSpec : packTechSpecList) {
            this.saveOperaLog("删除",packTechSpec.getPackType()+StrUtil.DASHED+packTechSpec.getSpecType(),packTechSpec.getForeignId(),packTechSpec.getItem(),packTechSpec.getItemCode(),packTechSpec,null);

        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void copyItem(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String item) {
        if (StrUtil.equals(sourceForeignId, targetForeignId) && StrUtil.equals(sourcePackType, targetPackType)) {
            return;
        }
        List<String> items = StrUtil.split(item, CharUtil.COMMA);
        //删除目标的项目
//        QueryWrapper<PackTechSpec> delQw = new QueryWrapper<PackTechSpec>();
//        delQw.eq("foreign_id", targetForeignId);
//        delQw.eq("pack_type", targetPackType);
//        delQw.in("spec_type",items);
//        remove(delQw);
        //复制
        QueryWrapper<PackTechSpec> query = new QueryWrapper<>();
        query.eq("foreign_id", sourceForeignId);
        query.eq("pack_type", sourcePackType);
        query.in("spec_type", items);
        List<PackTechSpec> list = list(query);
        if (CollUtil.isNotEmpty(list)) {
            for (PackTechSpec t : list) {
                t.setId(null);
                BeanUtil.setProperty(t, "foreignId", targetForeignId);
                BeanUtil.setProperty(t, "packType", targetPackType);
            }
            saveBatch(list);
        }

    }


    @Override
    public List<PackTechAttachmentVo> picList(PackTechSpecSearchDto dto) {
        List<AttachmentVo> attachmentVos = null;
        if (StrUtil.isAllNotBlank(dto.getPackType(), dto.getSpecType())) {
            attachmentVos = attachmentService.findByforeignId(dto.getForeignId(), dto.getPackType() + StrUtil.DASHED + dto.getSpecType());
        } else {
            attachmentVos = attachmentService.findByforeignIdTypeLikeStart(dto.getForeignId(), dto.getPackType() + StrUtil.DASHED);
        }
        List<PackTechAttachmentVo> packTechAttachmentVos = BeanUtil.copyToList(attachmentVos, PackTechAttachmentVo.class);
        return packTechAttachmentVos;
    }

    /**
     * @param attachmentList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    public void picListSort(List<Attachment> attachmentList) {
        if (ObjectUtil.isNotEmpty(attachmentList)) {
            attachmentService.updateBatchById(attachmentList);
        }
    }

    @Override
    public AttachmentVo savePic(PackTechSpecSavePicDto dto) {
        return attachmentService.savePackTechSpecPic(dto);
    }

    @Override
    public PageInfo<OperaLogEntity> operationLog(PackTechSpecPageDto pageDto) {
        QueryWrapper<OperaLogEntity> qw = new QueryWrapper<>();
//        qw.eq("path", pageDto.getPackType());
        qw.eq("name", pageDto.getPackType() + StrUtil.DASHED + pageDto.getSpecType());
        qw.eq("parent_id", pageDto.getForeignId());

        qw.orderByDesc("id");
        Page<OperaLogEntity> objects = PageHelper.startPage(pageDto);
        operaLogService.list(qw);
        return objects.toPageInfo();
    }


    @Override
    String getModeName() {
        return "工艺说明";
    }

// 自定义方法区 不替换的区域【other_end】

}
