/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.band.service.BandService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.formType.entity.FieldVal;
import com.base.sbc.module.formType.service.FieldManagementService;
import com.base.sbc.module.formType.service.FieldValService;
import com.base.sbc.module.formType.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formType.utils.FormTypeCodes;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.entity.*;
import com.base.sbc.module.planning.service.*;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.base.sbc.module.planning.vo.DimensionTotalVo;
import com.base.sbc.module.planning.vo.PlanningSummaryDetailVo;
import com.base.sbc.module.planning.vo.PlanningSummaryVo;
import com.base.sbc.module.sample.dto.*;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.entity.SampleStyleColor;
import com.base.sbc.module.sample.mapper.SampleDesignMapper;
import com.base.sbc.module.sample.mapper.SampleStyleColorMapper;
import com.base.sbc.module.sample.service.SampleDesignService;
import com.base.sbc.module.sample.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：样衣设计 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.service.SampleService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-9 11:16:15
 */
@Service
public class SampleDesignServiceImpl extends BaseServiceImpl<SampleDesignMapper, SampleDesign> implements SampleDesignService {


    @Autowired
    private FlowableService flowableService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private PlanningCategoryItemMaterialService planningCategoryItemMaterialService;
    @Autowired
    private PlanningSeasonService planningSeasonService;
    @Autowired
    private PlanningBandService planningBandService;
    @Autowired
    private PlanningCategoryService planningCategoryService;
    @Autowired
    private PlanningCategoryItemService planningCategoryItemService;

    @Autowired
    private SampleStyleColorMapper sampleStyleColorMapper;

    @Autowired
    CcmFeignService ccmFeignService;
    @Autowired
    AmcFeignService amcFeignService;
    @Autowired
    private BasicsdatumModelTypeService basicsdatumModelTypeService;
    @Autowired
    private FieldManagementService fieldManagementService;
    @Autowired
    private FieldValService fieldValService;
    @Autowired
    private BandService bandService;
    private IdGen idGen = new IdGen();

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public SampleDesign saveSampleDesign(SampleDesignSaveDto dto) {
        SampleDesign sampleDesign = null;
        if (StrUtil.isNotBlank(dto.getId())) {
            sampleDesign = getById(dto.getId());
            BeanUtil.copyProperties(dto, sampleDesign);
            setMainStylePic(sampleDesign, dto.getStylePicList());
            PlanningUtils.setCategory(sampleDesign);
            this.updateById(sampleDesign);
        } else {
            sampleDesign = saveNewSampleDesign(dto);
        }
        // 保存工艺信息
        fieldValService.save(sampleDesign.getId(), FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY, dto.getTechnologyInfo());
        // 附件信息
        saveFiles(sampleDesign.getId(), dto.getAttachmentList(), AttachmentTypeConstant.SAMPLE_DESIGN_FILE_ATTACHMENT);
        // 图片信息
        saveFiles(sampleDesign.getId(), dto.getStylePicList(), AttachmentTypeConstant.SAMPLE_DESIGN_FILE_STYLE_PIC);

        //保存关联的素材库
        planningCategoryItemMaterialService.saveMaterialList(dto);


        return sampleDesign;
    }


    public void setMainStylePic(SampleDesign sampleDesign, List<SampleAttachmentDto> stylePicList) {
        if (CollUtil.isNotEmpty(stylePicList)) {
            sampleDesign.setStylePic(stylePicList.get(0).getFileId());
        } else {
            sampleDesign.setStylePic("");
        }
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

    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public SampleDesign saveNewSampleDesign(SampleDesignSaveDto dto) {

        if (StrUtil.isBlank(dto.getDesignerId())) {
            throw new OtherException("请选择设计师");
        }
        // 判断当前用户是否有编码
        UserCompany userInfo = amcFeignService.getUserInfo(dto.getDesignerId());
        if (userInfo == null || StrUtil.isBlank(userInfo.getUserCode())) {
            throw new OtherException("您未设置用户编码");
        }
        if (StrUtil.isBlank(dto.getPlanningSeasonId())) {
            throw new OtherException("未选择产品季");
        }
        //查询产品季
        PlanningSeason planningSeason = planningSeasonService.getById(dto.getPlanningSeasonId());
        PlanningBand planningBand;
        if (ObjectUtil.isEmpty(planningSeason)) {
            throw new OtherException("产品季为空");
        }
        //2 查询波段企划
        planningBand = planningBandService.getById(dto.getPlanningBandId());
        if (ObjectUtil.isEmpty(planningBand)) {
            throw new OtherException("波段企划为空");
        }

        //3 查询波段下的品类信息

        PlanningCategory planningCategory = planningCategoryService.getById(dto.getPlanningCategoryId());
        if (ObjectUtil.isEmpty(planningCategory)) {
            throw new OtherException("无此品类信息");
        }
        // 品类信息的需求数+1
        planningCategory.setPlanRequirementNum(planningCategory.getPlanRequirementNum().add(BigDecimal.ONE));
        planningCategoryService.updateById(planningCategory);
        // 新增坑位信息
        PlanningCategoryItem categoryItem = new PlanningCategoryItem();
        categoryItem.setPlanningSeasonId(planningSeason.getId());
        categoryItem.setPlanningBandId(planningBand.getId());
        categoryItem.setPlanningCategoryId(planningCategory.getId());
        categoryItem.setStylePic(dto.getStylePic());
        categoryItem.setStatus("1");
        categoryItem.setCategoryIds(dto.getCategoryIds());
        PlanningUtils.setCategory(categoryItem);
        categoryItem.setCategoryName(dto.getCategoryName());
        categoryItem.setDesigner(userInfo.getAliasUserName() + StrUtil.COMMA + userInfo.getUserCode());
        categoryItem.setDesignerId(userInfo.getUserId());
        categoryItem.setMaterialCount(new BigDecimal(String.valueOf(CollUtil.size(dto.getMaterialList()))));
        categoryItem.setHisDesignNo(dto.getHisDesignNo());
        // 设置款号
        String designNo = planningCategoryItemService.getNextCode(dto.getBrand(), dto.getYear(), dto.getSeason(), dto.getCategoryName());
        if (StrUtil.isBlank(designNo)) {
            throw new OtherException("款号生成失败");
        }
        designNo = designNo + userInfo.getUserCode();
        categoryItem.setDesignNo(designNo);
        planningCategoryItemService.save(categoryItem);

        // 新增样衣设计
        SampleDesign sampleDesign = BeanUtil.copyProperties(dto, SampleDesign.class);
        PlanningUtils.toSampleDesign(sampleDesign, planningSeason, planningBand, categoryItem);
        setMainStylePic(sampleDesign, dto.getStylePicList());
        PlanningUtils.setCategory(sampleDesign);
        save(sampleDesign);
        dto.setPlanningCategoryItemId(categoryItem.getId());
        dto.setPlanningBandId(planningBand.getId());
        dto.setPlanningSeasonId(planningSeason.getId());
        return sampleDesign;
    }


    @Override
    public PageInfo queryPageInfo(SampleDesignPageDto dto) {
        String companyCode = getCompanyCode();
        String userId = getUserId();
        QueryWrapper<SampleDesign> qw = new QueryWrapper<>();
        qw.and(StrUtil.isNotEmpty(dto.getSearch()), i -> i.like("design_no", dto.getSearch()).or().like("his_design_no", dto.getSearch()));
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getDesignerId()), "designer_id", dto.getDesignerId());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "season", dto.getSeason());
        qw.in(StrUtil.isNotBlank(dto.getStatus()), "status", StrUtil.split(dto.getStatus(), CharUtil.COMMA));
        qw.in(StrUtil.isNotBlank(dto.getKitting()), "kitting", StrUtil.split(dto.getKitting(), CharUtil.COMMA));
        qw.eq(StrUtil.isNotBlank(dto.getProdCategory3rd()), "prod_category3rd", dto.getProdCategory3rd());
        qw.like(StrUtil.isNotBlank(dto.getDesignNo()), "design_no", dto.getDesignNo());
        qw.eq(StrUtil.isNotBlank(dto.getDevtType()), "devt_type", dto.getDevtType());
        qw.eq(StrUtil.isNotBlank(dto.getPlanningSeasonId()), "planning_season_id", dto.getPlanningSeasonId());

        if(!StringUtils.isEmpty(dto.getIsTrim())){
            if(dto.getIsTrim().equals(BaseGlobal.STATUS_NORMAL)){
                /*查询配饰*/
                qw.like ("category_name","配饰");
            }else {
                /*查询主款*/
                qw.notLike("category_name","配饰");
            }
        }
        qw.likeRight(StrUtil.isNotBlank(dto.getCategoryIds()), "category_ids", dto.getCategoryIds());
        qw.eq(BaseConstant.COMPANY_CODE, companyCode);

        //1我下发的
        if (StrUtil.equals(dto.getUserType(), SampleDesignPageDto.userType1)) {
            qw.eq("sender", userId);
        }
        //2我创建的
        else if (StrUtil.equals(dto.getUserType(), SampleDesignPageDto.userType2)) {
            qw.isNull("sender");
            qw.eq("create_id", userId);
        }
        //3我负责的
        else if (StrUtil.equals(dto.getUserType(), SampleDesignPageDto.userType3)) {
            qw.eq("designer_id", userId);
        }
        // 所有
        else if (StrUtil.equals(dto.getUserType(), SampleDesignPageDto.userType0)) {
            amcFeignService.getDataPermissionsForQw(DataPermissionsBusinessTypeEnum.SAMPLE_DESIGN.getK(), qw);
        } else {
//            amcFeignService.teamAuth(qw, "planning_season_id", getUserId());
            // amcFeignService.getDataPermissionsForQw(DataPermissionsBusinessTypeEnum.SAMPLE_DESIGN.getK(), qw);
        }
        Page<SampleDesignPageVo> objects = PageHelper.startPage(dto);
        getBaseMapper().selectByQw(qw);
        List<SampleDesignPageVo> result = objects.getResult();
        // 设置图片
        attachmentService.setListStylePic(result, "stylePic");
        amcFeignService.addUserAvatarToList(result, "designerId", "aliasUserAvatar");
        return objects.toPageInfo();
    }

    /**
     * 查询样衣设计及款式配色
     *
     * @param dto
     * @return
     */
    @Override
    public PageInfo sampleSampleStyle(SampleDesignPageDto dto) {
        PageInfo pageInfo = queryPageInfo(dto);
        List<SampleDesignPageVo> list = pageInfo.getList();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(sampleDesignPageVo -> {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("ssc.sample_design_id", sampleDesignPageVo.getId());
                queryWrapper.eq("ssc.del_flag", "0");
                queryWrapper.eq(StrUtil.isNotBlank(dto.getStyleStatus()), "ssc.status", dto.getStyleStatus());
                if(StrUtil.isNotBlank(dto.getMeetFlag()) ){
                    if (dto.getMeetFlag().equals(BaseGlobal.STATUS_NORMAL)) {
                        queryWrapper.ne( "sob.meet_flag", BaseGlobal.STATUS_CLOSE);
                    }
                }
                List<SampleStyleColorVo> sampleStyleColorVoList = sampleStyleColorMapper.getSampleStyleColorList(queryWrapper);
                sampleDesignPageVo.setSampleStyleColorVoList(sampleStyleColorVoList);
            });
        }
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean startApproval(String id) {
        SampleDesign sampleDesign = getById(id);
        if (sampleDesign == null) {
            throw new OtherException("样衣数据不存在,请先保存");
        }
        sampleDesign.setConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
        updateById(sampleDesign);
        Map<String, Object> variables = BeanUtil.beanToMap(sampleDesign);
        boolean flg = flowableService.start(FlowableService.sample_design_pdn + "[" + sampleDesign.getDesignNo() + "]", FlowableService.sample_design_pdn, id, "/pdm/api/saas/sampleDesign/approval", "/pdm/api/saas/sampleDesign/approval", "/sampleClothesDesign/sampleDesign/" + id, variables);
        return flg;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean approval(AnswerDto dto) {
        SampleDesign sampleDesign = getById(dto.getBusinessKey());
        if (sampleDesign != null) {
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                //设置样衣状态为 已开款
                sampleDesign.setStatus("1");
                sampleDesign.setConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                sampleDesign.setStatus("0");
                sampleDesign.setConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
            }
            updateById(sampleDesign);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean sendMaking(SendSampleMakingDto dto) {
        SampleDesign sampleDesign = checkedSampleDesignExists(dto.getId());
        sampleDesign.setStatus("2");
        sampleDesign.setKitting(dto.getKitting());
        updateById(sampleDesign);
        return true;
    }

    @Override
    public SampleDesign checkedSampleDesignExists(String id) {
        SampleDesign sampleDesign = getById(id);
        if (sampleDesign == null) {
            throw new OtherException("样衣数据不存在");
        }
        return sampleDesign;
    }

    @Override
    public SampleDesignVo getDetail(String id) {
        SampleDesign sampleDesign = getById(id);
        if (sampleDesign == null) {
            return null;
        }
        SampleDesignVo sampleVo = BeanUtil.copyProperties(sampleDesign, SampleDesignVo.class);
        //查询附件
        List<AttachmentVo> attachmentVoList = attachmentService.findByforeignId(id, AttachmentTypeConstant.SAMPLE_DESIGN_FILE_ATTACHMENT);
        sampleVo.setAttachmentList(attachmentVoList);

        // 关联的素材库
        QueryWrapper mqw = new QueryWrapper<PlanningCategoryItemMaterial>();
        mqw.eq("planning_category_item_id", sampleDesign.getPlanningCategoryItemId());
        mqw.eq("del_flag", BaseGlobal.DEL_FLAG_NORMAL);
        List<PlanningCategoryItemMaterial> list = planningCategoryItemMaterialService.list(mqw);
        List<MaterialVo> materialList = BeanUtil.copyToList(list, MaterialVo.class);
        sampleVo.setMaterialList(materialList);
        //号型类型
        sampleVo.setSizeRangeName(basicsdatumModelTypeService.getNameById(sampleVo.getSizeRange()));
        //波段
        sampleVo.setBandName(bandService.getNameByCode(sampleVo.getBandCode()));
        // 款式图片
        List<AttachmentVo> stylePicList = attachmentService.findByforeignId(id, AttachmentTypeConstant.SAMPLE_DESIGN_FILE_STYLE_PIC);
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

        //维度标签
        sampleVo.setDimensionLabels(queryDimensionLabelsBySdId(id));

        return sampleVo;
    }

    @Override
    public List<DesignDocTreeVo> queryDesignDocTree(DesignDocTreeVo designDocTreeVo) {
        // 查询第0级 年份季节
        if (designDocTreeVo.getLevel() == null) {
            return getAllYearSeason();
        }
        // 查询波段
        else if (designDocTreeVo.getLevel() == 0) {
            return queryBand(designDocTreeVo);
        }
        // 查询大类
        else if (designDocTreeVo.getLevel() == 1) {
            return queryCategory(designDocTreeVo, 0);
        }
        // 查询品类
        else if (designDocTreeVo.getLevel() == 2) {
            return queryCategory(designDocTreeVo, 1);
        }

        return null;
    }

    @Override
    public List<FieldManagementVo> queryDimensionLabels(DimensionLabelsSearchDto dto) {

        // 2.查询字段配置信息
        List<FieldManagementVo> fieldManagementListByIds = fieldManagementService.list(FormTypeCodes.DIMENSION_LABELS, dto.getCategoryId(), dto.getSeason());
        if (CollUtil.isEmpty(fieldManagementListByIds)) {
            return null;
        }
        // [3].查询字段值
        if (StrUtil.isNotBlank(dto.getSampleDesignId())) {
            List<FieldVal> fvList = fieldValService.list(dto.getSampleDesignId(), FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
            fieldManagementService.conversion(fieldManagementListByIds, fvList);
        }
        if (CollUtil.isNotEmpty(fieldManagementListByIds)) {
            return fieldManagementListByIds.stream().filter(FieldManagementVo::isSelected).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<FieldManagementVo> queryDimensionLabelsBySdId(String id) {
        SampleDesign sampleDesign = getById(id);
        if (sampleDesign == null) {
            return null;
        }
        DimensionLabelsSearchDto dto = new DimensionLabelsSearchDto();
        dto.setSampleDesignId(id);
        dto.setSeason(sampleDesign.getSeason());
        dto.setCategoryId(CollUtil.get(StrUtil.split(sampleDesign.getCategoryIds(), StrUtil.COMMA), 1));
        return queryDimensionLabels(dto);
    }

    @Override
    public List<SampleUserVo> getDesignerList(String companyCode) {
        List<SampleUserVo> list = getBaseMapper().getDesignerList(companyCode);
        amcFeignService.setUserAvatarToList(list);
        return list;
    }

    @Override
    public List getBandChart(String month) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.in(StrUtil.isNotBlank(month), "month", StrUtil.split(month, CharUtil.COMMA));
        amcFeignService.teamAuth(qw, "planning_season_id", getUserId());
        List<ChartBarVo> chartBarVos = getBaseMapper().getBandChart(qw);
        return getChartList(chartBarVos);
    }

    @Override
    public List getCategoryChart(String category) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.in(StrUtil.isNotBlank(category), "prod_category", StrUtil.split(category, CharUtil.COMMA));
        amcFeignService.teamAuth(qw, "planning_season_id", getUserId());
        List<ChartBarVo> chartBarVos = getBaseMapper().getCategoryChart(qw);
        ccmFeignService.setCategoryName(chartBarVos, "product", "product");
        return getChartList(chartBarVos);
    }

    @Override
    public Map getDesignDataOverview(String time) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<String> timeRange = StrUtil.split(time, CharUtil.COMMA);
        //企划下发需求总数 (统计从坑位下发的数据)
        QueryWrapper qhxfxqzsQw = new QueryWrapper();
        qhxfxqzsQw.isNotNull("sender");
        getDesignDataOverviewCommonQw(qhxfxqzsQw, timeRange);
        long qhxfxqzs = this.count(qhxfxqzsQw);
        result.put("企划下发需求总数", qhxfxqzs);
        // 设计需求总数(统计从坑位下发的数据 + 新建的数据)
        QueryWrapper sjxqzsQw = new QueryWrapper();
        sjxqzsQw.isNull("sender");
        getDesignDataOverviewCommonQw(sjxqzsQw, timeRange);
        long sjxqzs = this.count(sjxqzsQw);
        result.put("设计需求总数", sjxqzs);
        //未开款 状态为0
        QueryWrapper wkkQw = new QueryWrapper();
        wkkQw.eq("status", BasicNumber.ZERO.getNumber());
        getDesignDataOverviewCommonQw(wkkQw, timeRange);
        long wkks = this.count(wkkQw);
        result.put("未开款", wkks);

        //已开款数 状态为1
        QueryWrapper ykkQw = new QueryWrapper();
        ykkQw.eq("status", BasicNumber.ONE.getNumber());
        getDesignDataOverviewCommonQw(ykkQw, timeRange);
        long ykk = this.count(ykkQw);
        result.put("已开款", ykk);

        //已下发数 状态为2
        QueryWrapper yxfsQw = new QueryWrapper();
        yxfsQw.eq("status", BasicNumber.TWO.getNumber());
        getDesignDataOverviewCommonQw(yxfsQw, timeRange);
        long yxfs = this.count(yxfsQw);
        result.put("已下发打版", yxfs);
        return result;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean handleOldCategory() {
        //波段品类信息
        List<PlanningCategory> o1List = planningCategoryService.list();
        List<PlanningCategory> n1List = new ArrayList<>();
        for (PlanningCategory o : o1List) {
            PlanningCategory n = new PlanningCategory();
            n.setId(o.getId());
            n.setCategoryIds(o.getCategoryIds());
            PlanningUtils.setCategory(n);
            n1List.add(n);
        }
        //坑位信息
        List<PlanningCategoryItem> o2List = planningCategoryItemService.list();
        List<PlanningCategoryItem> n2List = new ArrayList<>();
        for (PlanningCategoryItem o : o2List) {
            PlanningCategoryItem n = new PlanningCategoryItem();
            n.setId(o.getId());
            n.setCategoryIds(o.getCategoryIds());
            PlanningUtils.setCategory(n);
            n2List.add(n);
        }
        //样衣信息
        List<SampleDesign> o3List = list();
        List<SampleDesign> n3List = new ArrayList<>();
        for (SampleDesign o : o3List) {
            SampleDesign n = new SampleDesign();
            n.setId(o.getId());
            n.setCategoryIds(o.getCategoryIds());
            PlanningUtils.setCategory(n);
            n3List.add(n);
        }
        planningCategoryService.updateBatchById(n1List);
        planningCategoryItemService.updateBatchById(n2List);
        updateBatchById(n3List);
        return true;
    }

    @Override
    public PlanningSummaryVo categoryBandSummary(PlanningBoardSearchDto dto) {
        PlanningSummaryVo vo = new PlanningSummaryVo();
        //查询波段统计
        QueryWrapper brandTotalQw = new QueryWrapper();
        brandTotalQw.select("sd.band_code as name,count(1) as total");
        brandTotalQw.groupBy("sd.band_code");
        stylePlanningCommonQw(brandTotalQw, dto);
        List<DimensionTotalVo> bandTotal = getBaseMapper().dimensionTotal(brandTotalQw);
        vo.setBandTotal(PlanningUtils.removeEmptyAndSort(bandTotal));
        Map<String, String> bandNames = new HashMap<>(4);
        if (CollUtil.isNotEmpty(bandTotal)) {
            bandNames = bandService.getNamesByCodes(bandTotal.stream().map(DimensionTotalVo::getName).collect(Collectors.joining(StrUtil.COMMA)));
            for (DimensionTotalVo dimensionTotalVo : bandTotal) {
                dimensionTotalVo.setName(bandNames.getOrDefault(dimensionTotalVo.getName(), dimensionTotalVo.getName()));
            }
        }
        //查询品类统计
        QueryWrapper categoryQw = new QueryWrapper();
        categoryQw.select("prod_category as name,count(1) as total");
        categoryQw.groupBy("prod_category");
        stylePlanningCommonQw(categoryQw, dto);
        List<DimensionTotalVo> categoryTotal = getBaseMapper().dimensionTotal(categoryQw);
        ccmFeignService.setCategoryName(categoryTotal, "name", "name");
        vo.setCategoryTotal(PlanningUtils.removeEmptyAndSort(categoryTotal));
        //查询明细
        QueryWrapper detailQw = new QueryWrapper();
        stylePlanningCommonQw(detailQw, dto);
        List<PlanningSummaryDetailVo> detailVoList = getBaseMapper().categoryBandSummary(detailQw);
        if (CollUtil.isNotEmpty(detailVoList)) {
            for (PlanningSummaryDetailVo planningSummaryDetailVo : detailVoList) {
                planningSummaryDetailVo.setBandCode(bandNames.getOrDefault(planningSummaryDetailVo.getBandCode(), planningSummaryDetailVo.getBandCode()));
            }
            amcFeignService.setUserAvatarToList(detailVoList);
            attachmentService.setListStylePic(detailVoList, "stylePic");
            ccmFeignService.setCategoryName(detailVoList, "prodCategory", "prodCategory");
            Map<String, List<PlanningSummaryDetailVo>> seatData = detailVoList.stream().collect(Collectors.groupingBy(k -> k.getProdCategory() + StrUtil.DASHED + k.getBandCode()));
            vo.setSeatData(seatData);
        }
        return vo;
    }

    @Override
    public List<StyleBoardCategorySummaryVo> categorySummary(PlanningBoardSearchDto dto) {
        QueryWrapper<SampleDesign> qw = new QueryWrapper();
        qw.eq(StrUtil.isNotEmpty(dto.getPlanningSeasonId()), "sd.planning_season_id", dto.getPlanningSeasonId());
        qw.and(StrUtil.isNotEmpty(dto.getSearch()), i -> i.like("sd.design_no", dto.getSearch()).or().like("sd.style_no", dto.getSearch()));
        qw.in(StrUtil.isNotEmpty(dto.getBandCode()), "sd.band_code", StrUtil.split(dto.getBandCode(), CharUtil.COMMA));
        qw.in(StrUtil.isNotEmpty(dto.getMonth()), "sd.month", StrUtil.split(dto.getMonth(), CharUtil.COMMA));
        qw.in(StrUtil.isNotEmpty(dto.getProdCategoryId()), "sd.prod_category", StrUtil.split(dto.getProdCategoryId(), CharUtil.COMMA));
        List<StyleBoardCategorySummaryVo> styleBoardCategorySummaryVos = getBaseMapper().categorySummary(qw);
        // 统计大类数量
        if (CollUtil.isNotEmpty(styleBoardCategorySummaryVos)) {
            Map<String, Long> category1stTotal = styleBoardCategorySummaryVos.stream().collect(Collectors.groupingBy(StyleBoardCategorySummaryVo::getProdCategory1st))
                    .entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> {
                        return v.getValue().stream().map(StyleBoardCategorySummaryVo::getSkc).reduce((a, b) -> a + b).orElse(0L);
                    }));
            //反写品类名称
            Set<String> categoryIds = new HashSet<>(16);
            for (StyleBoardCategorySummaryVo vo : styleBoardCategorySummaryVos) {
                categoryIds.add(vo.getProdCategory());
                categoryIds.add(vo.getProdCategory1st());
                categoryIds.add(vo.getProdCategory2nd());
            }
            Map<String, String> categoryNames = ccmFeignService.findStructureTreeNameByCategoryIds(CollUtil.join(categoryIds, StrUtil.COMMA));
            for (StyleBoardCategorySummaryVo vo : styleBoardCategorySummaryVos) {
                vo.setTotal(category1stTotal.getOrDefault(vo.getProdCategory1st(), 0L));
                vo.setProdCategory1st(categoryNames.getOrDefault(vo.getProdCategory1st(), vo.getProdCategory1st()));
                vo.setProdCategory2nd(categoryNames.getOrDefault(vo.getProdCategory2nd(), vo.getProdCategory2nd()));
                vo.setProdCategory(categoryNames.getOrDefault(vo.getProdCategory(), vo.getProdCategory()));
            }
        }
        return styleBoardCategorySummaryVos;
    }

    @Override
    public CategoryStylePlanningVo categoryStylePlanning(PlanningBoardSearchDto dto) {
        CategoryStylePlanningVo vo = new CategoryStylePlanningVo();
        // 企划需求数
        QueryWrapper prsQw = new QueryWrapper();
        stylePlanningCommonQw(prsQw, dto);
        prsQw.isNotNull("sender");
        Long planRequirementSkc = getBaseMapper().colorCount(prsQw);
        vo.setPlanRequirementSkc(planRequirementSkc);
        //设计需求数
        QueryWrapper<CategoryStylePlanningVo> drsQw = new QueryWrapper();
        stylePlanningCommonQw(drsQw, dto);
        Long designRequirementSkc = getBaseMapper().colorCount(drsQw);
        vo.setDesignRequirementSkc(designRequirementSkc);
        vo.setBandCode(dto.getBandCode());
        vo.setBandName(bandService.getNameByCode(dto.getBandCode()));
        if (StrUtil.isNotBlank(dto.getProdCategoryId())) {
            Map<String, String> prodCategoryNames = ccmFeignService.findStructureTreeNameByCategoryIds(dto.getProdCategoryId());
            vo.setProdCategory(CollUtil.join(prodCategoryNames.values(), StrUtil.COMMA));
        }
        return vo;
    }

    private void stylePlanningCommonQw(QueryWrapper<?> qw, PlanningBoardSearchDto dto) {
        qw.eq("sd." + COMPANY_CODE, getCompanyCode());
        qw.and(StrUtil.isNotEmpty(dto.getSearch()), i -> i.like("sd.design_no", dto.getSearch()).or().like("sd.style_no", dto.getSearch()));
        qw.eq(StrUtil.isNotEmpty(dto.getPlanningSeasonId()), "sd.planning_season_id", dto.getPlanningSeasonId());
        qw.in(StrUtil.isNotEmpty(dto.getBandCode()), "sd.band_code", StrUtil.split(dto.getBandCode(), CharUtil.COMMA));
        qw.in(StrUtil.isNotEmpty(dto.getMonth()), "sd.month", StrUtil.split(dto.getMonth(), CharUtil.COMMA));
        qw.in(StrUtil.isNotEmpty(dto.getProdCategoryId()), "sd.prod_category", StrUtil.split(dto.getProdCategoryId(), CharUtil.COMMA));
    }

    private void getDesignDataOverviewCommonQw(QueryWrapper qw, List timeRange) {
        qw.ne("del_flag", BaseGlobal.YES);
        qw.eq(COMPANY_CODE, getCompanyCode());
        amcFeignService.teamAuth(qw, "planning_season_id", getUserId());
        qw.between(CollUtil.isNotEmpty(timeRange), "create_date", CollUtil.getFirst(timeRange), CollUtil.getLast(timeRange));
    }

    private List getChartList(List<ChartBarVo> chartBarVos) {
        List first = CollUtil.newArrayList("product", "总数", "未开款数", "已开款数", "已下发打版");
        List result = new ArrayList(16);
        result.add(first);
        if (CollUtil.isNotEmpty(chartBarVos)) {
            Map<String, List<ChartBarVo>> productMap = chartBarVos.stream().collect(Collectors.groupingBy(ChartBarVo::getProduct));
            for (Map.Entry<String, List<ChartBarVo>> kv : productMap.entrySet()) {
                List productData = new ArrayList();
                productData.add(kv.getKey());
                List<ChartBarVo> value = kv.getValue();
                Map<String, BigDecimal> dm = Optional.ofNullable(value).map(cbs -> {
                    return cbs.stream().collect(Collectors.toMap(k -> k.getDimension(), v -> v.getTotal()));
                }).orElse(new HashMap<>(2));
                //总数
                productData.add(NumberUtil.add(dm.values().toArray(new BigDecimal[dm.size()])));
                //未开款
                productData.add(Optional.ofNullable(dm.get(BasicNumber.ZERO.getNumber())).orElse(BigDecimal.ZERO));
                //已开款数
                productData.add(Optional.ofNullable(dm.get(BasicNumber.ONE.getNumber())).orElse(BigDecimal.ZERO));
                //已下发打版
                productData.add(Optional.ofNullable(dm.get(BasicNumber.TWO.getNumber())).orElse(BigDecimal.ZERO));
                result.add(productData);
            }

        }
        return result;
    }

    private List<DesignDocTreeVo> queryCategory(DesignDocTreeVo designDocTreeVo, int categoryIdx) {
        List<DesignDocTreeVo> result = new ArrayList<>(16);
        if (!StrUtil.isAllNotEmpty(designDocTreeVo.getYear(), designDocTreeVo.getSeason(), designDocTreeVo.getBandCode())) {
            return result;
        }
        if (categoryIdx == 1 && StrUtil.isBlank(designDocTreeVo.getCategoryIds())) {
            return result;
        }
        QueryWrapper<SampleDesign> qw = new QueryWrapper<>();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.eq(DEL_FLAG, BaseGlobal.DEL_FLAG_NORMAL);
        qw.eq("year", designDocTreeVo.getYear());
        qw.eq("season", designDocTreeVo.getSeason());
        qw.eq("band_code", designDocTreeVo.getBandCode());
        qw.likeRight(StrUtil.isNotBlank(designDocTreeVo.getCategoryIds()), "category_ids", designDocTreeVo.getCategoryIds());
        qw.select("DISTINCT category_ids");
        List<SampleDesign> list = list(qw);
        if (CollUtil.isNotEmpty(list)) {
            String categoryIds = list.stream().map(item -> {
                return CollUtil.get(StrUtil.split(item.getCategoryIds(), CharUtil.COMMA), categoryIdx);
            }).collect(Collectors.joining(","));
            Map<String, String> nameMaps = ccmFeignService.findStructureTreeNameByCategoryIds(categoryIds);
            Set<String> categoryIdsSet = new HashSet<>(16);
            for (SampleDesign sampleDesign : list) {
                List<String> split = StrUtil.split(sampleDesign.getCategoryIds(), CharUtil.COMMA);
                String categoryId = CollUtil.get(split, categoryIdx);
                System.out.println(categoryId);
                if (categoryIdsSet.contains(categoryId)) {
                    continue;
                }
                categoryIdsSet.add(categoryId);
                DesignDocTreeVo vo = new DesignDocTreeVo();
                BeanUtil.copyProperties(designDocTreeVo, vo);
                vo.setLevel(2 + categoryIdx);
                vo.setChildren(categoryIdx == 0);

                List<String> sub = CollUtil.sub(split, 0, categoryIdx + 1);
                vo.setCategoryIds(CollUtil.join(sub, StrUtil.COMMA));
                vo.setLabel(MapUtil.getStr(nameMaps, categoryId, categoryId));
                result.add(vo);
            }
        }
        return result;
    }


    private List<DesignDocTreeVo> queryBand(DesignDocTreeVo designDocTreeVo) {
        List<DesignDocTreeVo> result = new ArrayList<>(16);
        if (StrUtil.isBlank(designDocTreeVo.getYear()) || StrUtil.isBlank(designDocTreeVo.getSeason())) {
            return result;
        }
        QueryWrapper<SampleDesign> qw = new QueryWrapper<>();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.eq(DEL_FLAG, BaseGlobal.DEL_FLAG_NORMAL);
        qw.eq("year", designDocTreeVo.getYear());
        qw.eq("season", designDocTreeVo.getSeason());
        qw.select("DISTINCT band_code");
        qw.orderByAsc("band_code");
        List<SampleDesign> list = list(qw);
        if (CollUtil.isNotEmpty(list)) {
            for (SampleDesign sampleDesign : list) {
                DesignDocTreeVo vo = new DesignDocTreeVo();
                BeanUtil.copyProperties(designDocTreeVo, vo);
                vo.setBandCode(sampleDesign.getBandCode());
                vo.setLevel(1);
                vo.setLabel(sampleDesign.getBandCode());
                vo.setChildren(true);
                result.add(vo);
            }
        }
        return result;
    }

    private List<DesignDocTreeVo> getAllYearSeason() {
        QueryWrapper<SampleDesign> qw = new QueryWrapper<>();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.eq(DEL_FLAG, BaseGlobal.DEL_FLAG_NORMAL);
        qw.select("DISTINCT year,season");
        List<SampleDesign> list = list(qw);
        List<DesignDocTreeVo> result = new ArrayList<>(16);
        if (CollUtil.isNotEmpty(list)) {
            //根据年份季节排序
            Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_Year,C8_Quarter");
            Map<String, String> c8Quarter = dictInfoToMap.get("C8_Quarter");
            Map<String, String> c8Year = dictInfoToMap.get("C8_Year");
            list.sort((a, b) -> {
                int aIdx = CollUtil.indexOf(c8Quarter.keySet(), t -> t.equals(a.getSeason()));
                int bIdx = CollUtil.indexOf(c8Quarter.keySet(), t -> t.equals(b.getSeason()));
                String aLabel = a.getYear() + StrUtil.padPre(aIdx == -1 ? "9999" : String.valueOf(aIdx), 3, "0");
                String bLabel = b.getYear() + StrUtil.padPre(bIdx == -1 ? "9999" : String.valueOf(bIdx), 3, "0");
                return bLabel.compareTo(aLabel);
            });

            for (SampleDesign sampleDesign : list) {
                DesignDocTreeVo vo = new DesignDocTreeVo();
                BeanUtil.copyProperties(sampleDesign, vo);
                vo.setLevel(0);
                vo.setLabel(MapUtil.getStr(c8Year, vo.getYear(), vo.getYear()) + MapUtil.getStr(c8Quarter, vo.getSeason(), vo.getSeason()));
                vo.setChildren(true);
                result.add(vo);
            }
        }
        return result;
    }


}

