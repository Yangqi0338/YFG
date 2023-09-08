/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.formType.service.FieldValService;
import com.base.sbc.module.formType.utils.FieldValDataGroupConstant;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.base.sbc.module.sample.dto.SampleAttachmentDto;
import com.base.sbc.module.sample.vo.MaterialVo;
import com.base.sbc.module.smp.DataUpdateScmService;
import com.base.sbc.module.style.dto.StyleMasterDataSaveDto;
import com.base.sbc.module.style.dto.StyleSaveDto;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleInfoColor;
import com.base.sbc.module.style.entity.StyleMasterData;
import com.base.sbc.module.style.mapper.StyleMapper;
import com.base.sbc.module.style.mapper.StyleMasterDataMapper;
import com.base.sbc.module.style.service.StyleInfoColorService;
import com.base.sbc.module.style.service.StyleMasterDataService;
import com.base.sbc.module.style.vo.StyleInfoColorVo;
import com.base.sbc.module.style.vo.StyleMasterDataVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 类描述：款式主数据 service类
 * @address com.base.sbc.module.style.service.StyleMasterDataService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-9-7 13:57:43
 * @version 1.0  
 */
@Service
@RequiredArgsConstructor
public class StyleMasterDataServiceImpl extends BaseServiceImpl<StyleMasterDataMapper, StyleMasterData> implements StyleMasterDataService {


    private final StyleMapper styleMapper;


    private final AttachmentService attachmentService;

    private final PlanningCategoryItemMaterialService planningCategoryItemMaterialService;

    private final UploadFileService uploadFileService;

    private final PlanningCategoryItemService planningCategoryItemService;

    private final StyleInfoColorService styleInfoColorService;

    private final FieldValService fieldValService;

    private final DataUpdateScmService dataUpdateScmService;

    /**
     * 生成款式主数据
     *
     * @param styleId 款式id
     * @return
     */
    @Override
    public Boolean createStyleMasterData(String styleId) {
        if (StringUtils.isBlank(styleId)) {
            throw new OtherException("款式id为空");
        }
        /*款式信息*/
        Style style = styleMapper.selectById(styleId);
        /*创建一个款式主数据*/
        StyleMasterData styleMasterData = new StyleMasterData();
        /*复制*/
        BeanUtils.copyProperties(style, styleMasterData);
        /**/
        styleMasterData.setStyleId(style.getId());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("style_id", style.getId());

        List<StyleMasterData> list = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            styleMasterData.setId(null);
            baseMapper.insert(styleMasterData);
        }
        /*        *//*当数据存在时新增不存在时修改*//*
        QueryWrapper queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.eq("style_id",style.getId());
        this.saveOrUpdate(styleMasterData,queryWrapper);*/
        return true;
    }

    /**查询明细
     * @param id
     * @return
     */
    @Override
    public StyleMasterDataVo getDetail(String id) {
        StyleMasterData style = baseMapper.selectById(id);
        if (style == null) {
            return null;
        }
        StyleMasterDataVo sampleVo = BeanUtil.copyProperties(style, StyleMasterDataVo.class);
        //查询附件
        List<AttachmentVo> attachmentVoList = attachmentService.findByforeignId(sampleVo.getStyleId(), AttachmentTypeConstant.SAMPLE_DESIGN_FILE_ATTACHMENT);
        sampleVo.setAttachmentList(attachmentVoList);

        // 关联的素材库
        QueryWrapper<PlanningCategoryItemMaterial> mqw = new QueryWrapper<PlanningCategoryItemMaterial>();
        mqw.eq("planning_category_item_id", style.getPlanningCategoryItemId());
        mqw.eq("del_flag", BaseGlobal.DEL_FLAG_NORMAL);
        List<PlanningCategoryItemMaterial> list = planningCategoryItemMaterialService.list(mqw);
        List<MaterialVo> materialList = BeanUtil.copyToList(list, MaterialVo.class);
        sampleVo.setMaterialList(materialList);
        //号型类型
//        sampleVo.setSizeRangeName(basicsdatumModelTypeService.getNameById(sampleVo.getSizeRange()));
        //波段
//        sampleVo.setBandName(bandService.getNameByCode(sampleVo.getBandCode()));
        // 款式图片
        List<AttachmentVo> stylePicList = attachmentService.findByforeignId(id, AttachmentTypeConstant.STYLE_MASTER_DATA_PIC);
        sampleVo.setStylePicList(stylePicList);
        if (StrUtil.isNotBlank(sampleVo.getStylePic())) {
            String fileId = sampleVo.getStylePic();
            AttachmentVo one = CollUtil.findOne(stylePicList, (a) -> StrUtil.equals(a.getFileId(), fileId));
            sampleVo.setStylePic(Optional.ofNullable(one).map(AttachmentVo::getUrl).orElse(uploadFileService.getUrlById(sampleVo.getStylePic())));
            //旧数据处理
            if (CollUtil.isEmpty(stylePicList)) {
                AttachmentVo attachmentVo = new AttachmentVo();
                attachmentVo.setFileId(fileId);
                attachmentVo.setUrl(sampleVo.getStylePic());
                attachmentVo.setId(IdUtil.randomUUID());
                sampleVo.setStylePicList(CollUtil.newArrayList(attachmentVo));
            }
        }
        sampleVo.setSeatStylePic(planningCategoryItemService.getStylePicUrlById(sampleVo.getPlanningCategoryItemId()));
//        //维度标签
//        sampleVo.setDimensionLabels(queryDimensionLabelsBySdId(id));

        //如果有设置系列，则同步更新坑位信息表
        if(com.base.sbc.config.utils.StringUtils.isNotBlank(style.getSeriesId()) || com.base.sbc.config.utils.StringUtils.isNotBlank(style.getSeries())){
            UpdateWrapper<PlanningCategoryItem> wrapper = new UpdateWrapper<>();
            wrapper.eq("id",style.getPlanningCategoryItemId());
            wrapper.set("series_id",style.getSeriesId());
            wrapper.set("series",style.getSeries());
            planningCategoryItemService.update(null,wrapper);
        }
        // 查询 款式设计详情颜色列表
        List<StyleInfoColor> styleInfoColorList = styleInfoColorService.list(new QueryWrapper<StyleInfoColor>().eq("foreign_id", id));
        if (CollectionUtil.isNotEmpty(styleInfoColorList)) {
            sampleVo.setStyleInfoColorVoList(BeanUtil.copyToList(styleInfoColorList, StyleInfoColorVo.class));
        }

        return sampleVo;
    }

    /**
     * 保存修改款式主数据
     *
     * @param dto
     * @return
     */
    @Override
    public StyleMasterDataVo saveStyle(StyleMasterDataSaveDto dto) {
        StyleMasterData styleMasterData = baseMapper.selectById(dto.getId());
        resetDesignNo(dto, styleMasterData);

        BeanUtil.copyProperties(dto, styleMasterData);
        setMainStylePic(styleMasterData, dto.getStylePicList());
        this.updateById(styleMasterData);

        // 保存工艺信息
        fieldValService.save(styleMasterData.getId(), FieldValDataGroupConstant.STYLE_MASTER_DATA_DIMENSION, dto.getTechnologyInfo());
        // 图片信息
        saveFiles(styleMasterData.getId(), dto.getStylePicList(), AttachmentTypeConstant.STYLE_MASTER_DATA_PIC);
        //保存关联的素材库
//        planningCategoryItemMaterialService.saveMaterialList(dto);
        return getDetail(styleMasterData.getId());
    }

    public void saveFiles(String id, List<SampleAttachmentDto> files, String type) {
        QueryWrapper<Attachment> aqw = new QueryWrapper<>();
        aqw.eq("foreign_id", id);
        aqw.eq("type", type);
        attachmentService.remove(aqw);
        List<Attachment> attachments = new ArrayList<>(12);
        if (CollUtil.isNotEmpty(files)) {
            attachments = BeanUtil.copyToList(files, Attachment.class);
            for (Attachment attachment : attachments) {
                attachment.setId(null);
                attachment.setForeignId(id);
                attachment.setType(type);
                attachment.setStatus(BaseGlobal.STATUS_NORMAL);
            }
            attachmentService.saveBatch(attachments);
        }
    }

    public void setMainStylePic(StyleMasterData style, List<SampleAttachmentDto> stylePicList) {
        if (CollUtil.isNotEmpty(stylePicList)) {
            style.setStylePic(stylePicList.get(0).getFileId());
        } else {
            style.setStylePic("");
        }
    }


    private void resetDesignNo(StyleMasterDataSaveDto dto, StyleMasterData db) {
        boolean initId = CommonUtils.isInitId(dto.getId());
        if (StrUtil.isBlank(dto.getDesignNo()) && !initId) {
            throw new OtherException("设计款号不能为空");
        }
        if (StrUtil.equals(dto.getOldDesignNo(), dto.getDesignNo())) {
            String newDesignNo = PlanningUtils.getNewDesignNo(dto.getDesignNo(), db.getDesigner(), dto.getDesigner());
            dto.setDesignNo(newDesignNo);
        } else {
            String prefix = PlanningUtils.getDesignNoPrefix(db.getDesignNo(), db.getDesigner());
            if (StrUtil.startWith(dto.getDesignNo(), prefix)) {
                String newDesignNo = PlanningUtils.getNewDesignNo(dto.getDesignNo(), db.getDesigner(), dto.getDesigner());
                dto.setDesignNo(newDesignNo);
            } else {
                QueryWrapper qw = new QueryWrapper();
                qw.eq("design_no", dto.getDesignNo());
                qw.ne(!initId, "id", dto.getId());
                long count = count(qw);
                if (count > 0) {
                    throw new OtherException("设计款号重复");
                }
            }

        }


    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
