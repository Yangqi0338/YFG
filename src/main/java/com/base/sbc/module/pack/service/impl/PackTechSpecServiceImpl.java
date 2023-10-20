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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.MdUtils;
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
public class PackTechSpecServiceImpl extends PackBaseServiceImpl<PackTechSpecMapper, PackTechSpec> implements PackTechSpecService {


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
            //查询排序
            if (dto.getSort() == null) {
                QueryWrapper<PackTechSpec> countQw = new QueryWrapper<>();
                PackUtils.commonQw(countQw, dto);
                countQw.eq(StrUtil.isNotBlank(dto.getSpecType()), "spec_type", dto.getSpecType());
                long count = count(countQw);
                pageData.setSort(new BigDecimal(String.valueOf(count + 1)));
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
            saveOrUpdateOperaLog(dto, dbData, genOperaLogEntity(dbData, "修改"));
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
        return BeanUtil.copyToList(packTechSpecs, PackTechSpecVo.class);
    }

    @Override
    public List<PackTechSpecVo> batchSave(PackTechSpecBatchSaveDto dto) {
        List<PackTechSpecDto> list = dto.getList();

        if (StrUtil.isNotBlank(dto.getOverlayFlg())) {
            //删除
            QueryWrapper<PackTechSpec> delQw = new QueryWrapper<PackTechSpec>();
            delQw.eq("foreign_id", dto.getForeignId());
            delQw.eq("pack_type", dto.getPackType());
            delQw.eq("spec_type", dto.getSpecType());
            remove(delQw);
        }
        List<PackTechSpec> packTechSpecs = BeanUtil.copyToList(list, PackTechSpec.class);
        for (PackTechSpec packTechSpec : packTechSpecs) {
            packTechSpec.setForeignId(dto.getForeignId());
            packTechSpec.setPackType(dto.getPackType());
            packTechSpec.setSpecType(dto.getSpecType());
            packTechSpec.setId(null);
        }
        saveBatch(packTechSpecs);
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
            packTechPackagingService.copy(packInfoListVo.getId(), packInfoListVo.getPackType(), dto.getTargetForeignId(), dto.getTargetPackType());
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
        QueryWrapper<PackTechSpec> query = new QueryWrapper<PackTechSpec>();
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

    @Override
    public AttachmentVo savePic(PackTechSpecSavePicDto dto) {
        return attachmentService.savePackTechSpecPic(dto);
    }

    @Override
    public PageInfo<OperaLogEntity> operationLog(PackTechSpecPageDto pageDto) {
        QueryWrapper<OperaLogEntity> qw = new QueryWrapper<>();
        qw.eq("path", pageDto.getPackType());
        qw.eq("name", getModeName() + StrUtil.DASHED + pageDto.getSpecType());
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
