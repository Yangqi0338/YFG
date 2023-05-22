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
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.planning.entity.*;
import com.base.sbc.module.planning.service.*;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSaveDto;
import com.base.sbc.module.sample.dto.SendSampleMakingDto;
import com.base.sbc.module.sample.dto.TechnologySaveDto;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.entity.Technology;
import com.base.sbc.module.sample.mapper.SampleMapper;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.service.TechnologyService;
import com.base.sbc.module.sample.vo.DesignDocTreeVo;
import com.base.sbc.module.sample.vo.MaterialVo;
import com.base.sbc.module.sample.vo.SamplePageVo;
import com.base.sbc.module.sample.vo.SampleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：样衣 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.service.SampleService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-9 11:16:15
 */
@Service
public class SampleServiceImpl extends ServicePlusImpl<SampleMapper, Sample> implements SampleService {


    @Autowired
    private TechnologyService technologyService;

    @Autowired
    private FlowableService flowableService;
    @Autowired
    private AttachmentService attachmentService;
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
    CcmFeignService ccmFeignService;
    @Autowired
    AmcFeignService amcFeignService;
    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public Sample saveSample(SampleSaveDto dto) {
        Sample sample = null;
        if (StrUtil.isNotBlank(dto.getId())) {
            sample = getById(dto.getId());
            BeanUtil.copyProperties(dto, sample);
            updateById(sample);
            planningCategoryItemMaterialService.saveMaterialList(dto);
        } else {
            sample =saveNewSample(dto);
        }
        //保存工艺信息
        if (ObjectUtil.isNotEmpty(dto.getTechnology())) {
            TechnologySaveDto technologyDto = dto.getTechnology();
            Technology technology = technologyService.getOne(new QueryWrapper<Technology>().eq("f_id", sample.getId()));
            technology = Optional.ofNullable(technology).orElse(new Technology());
            BeanUtil.copyProperties(technologyDto, technology,"id");
            technology.setFId(sample.getId());
            technology.setDelFlag(BaseGlobal.DEL_FLAG_NORMAL);
            technology.setStatus(BaseGlobal.STATUS_NORMAL);
            technology.copyFrom(sample);
            technologyService.saveOrUpdate(technology);

        }
        // 附件信息
        QueryWrapper<Attachment> aqw = new QueryWrapper<>();
        aqw.eq("f_id", dto.getId());
        List<Attachment> attachments = new ArrayList<>(12);
        if (CollUtil.isNotEmpty(dto.getAttachmentList())) {
            attachments = BeanUtil.copyToList(dto.getAttachmentList(), Attachment.class);
            for (Attachment attachment : attachments) {
                attachment.setFId(sample.getId());
                attachment.setType("sample");
                attachment.setStatus(BaseGlobal.STATUS_NORMAL);
            }
        }
        attachmentService.addAndUpdateAndDelList(attachments, aqw);
        return sample;
    }

    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public Sample saveNewSample(SampleSaveDto dto) {

        // 判断当前用户是否有编码
        UserCompany userInfo = amcFeignService.getUserInfo(getUserId());
        if(userInfo==null||StrUtil.isBlank(userInfo.getUserCode())){
            throw  new OtherException("您未设置用户编码");
        }
        if(StrUtil.isBlank(dto.getPlanningSeasonId())){
            throw  new OtherException("未选择产品季");
        }
        PlanningSeason planningSeason=null;
        PlanningBand planningBand;
        //1 查询产品季 年份 + 季节
        QueryWrapper<PlanningSeason> seasonQw=new QueryWrapper<>();
        seasonQw.eq(COMPANY_CODE,getCompanyCode());
        seasonQw.eq("id",dto.getPlanningSeasonId());
        List<PlanningSeason> seasonList = planningSeasonService.list(seasonQw);
        if(CollUtil.isEmpty(seasonList)){
            throw  new OtherException("产品季为空");
        }

        planningSeason=CollUtil.getFirst(seasonList);
        //2 查询波段企划 产品季 + 波段
        QueryWrapper<PlanningBand> bandQw=new QueryWrapper<>();
        bandQw.eq("planning_season_id",planningSeason.getId());
        bandQw.eq("band_code",dto.getBandCode());
        bandQw.eq("del_flag",BaseGlobal.DEL_FLAG_NORMAL);
        List<PlanningBand> bandList = planningBandService.list(bandQw);
        if(CollUtil.isEmpty(bandList)){
            throw  new OtherException("波段企划为空");
        }
        planningBand=CollUtil.getFirst(bandList);

        //3 查询波段下的品类信息 波段 + 品类
        //品类
        List<String> categoryIds = StrUtil.split(dto.getCategoryIds(), StrUtil.COMMA);

        QueryWrapper<PlanningCategory> categoryQw=new QueryWrapper<>();
        categoryQw.eq("planning_band_id",planningBand.getId());
        bandQw.eq("del_flag",BaseGlobal.DEL_FLAG_NORMAL);
        bandQw.eq("category_ids",CollUtil.join(CollUtil.sub(categoryIds,0,2),StrUtil.COMMA));
        List<PlanningCategory> categoryList = planningCategoryService.list(categoryQw);
        if(CollUtil.isEmpty(categoryList)){
            throw  new OtherException("该波段下无此品类信息");
        }
        PlanningCategory planningCategory=CollUtil.getFirst(categoryList);
        // 品类信息的需求数+1
        planningCategory.setPlanRequirementNum(planningCategory.getPlanRequirementNum().add(BigDecimal.ONE));
        planningCategoryService.updateById(planningCategory);
        // 新增坑位信息
        PlanningCategoryItem categoryItem=new PlanningCategoryItem();
        categoryItem.setPlanningSeasonId(planningSeason.getId());
        categoryItem.setPlanningBandId(planningBand.getId());
        categoryItem.setPlanningCategoryId(planningCategory.getId());
        categoryItem.setStylePic(dto.getStylePic());
        categoryItem.setCategoryId(CollUtil.getLast(categoryIds));
        categoryItem.setStatus("1");
        categoryItem.setCategoryIds(dto.getCategoryIds());
        categoryItem.setCategoryName(dto.getCategoryName());
        categoryItem.setDesigner(userInfo.getAliasUserName()+StrUtil.COMMA+userInfo.getUserCode());
        categoryItem.setDesignerId(userInfo.getUserId());
        categoryItem.setMaterialCount(new BigDecimal(String.valueOf(CollUtil.size(dto.getMaterialList()))));
        categoryItem.setHisDesignNo(dto.getHisDesignNo());
        // 设置款号
        String designNo=planningCategoryItemService.getNextCode(dto.getBrand(),dto.getYear(),dto.getSeason(),dto.getCategoryName());
        if(StrUtil.isBlank(designNo)){
            throw  new OtherException("款号生成失败");
        }
        designNo=designNo+userInfo.getUserCode();
        categoryItem.setDesignNo(designNo);
        planningCategoryItemService.save(categoryItem);
        // 新增坑位 信息对应的关联素材
        if(CollUtil.isNotEmpty(dto.getMaterialList())){
            List<PlanningCategoryItemMaterial> cims = dto.getMaterialList().stream().map(item -> {
                PlanningCategoryItemMaterial p = new PlanningCategoryItemMaterial();
                BeanUtil.copyProperties(categoryItem, p, "id");
                p.setDelFlag(BaseGlobal.DEL_FLAG_NORMAL);
                p.setMaterialCategoryId(categoryItem.getId());
                p.setMaterialId(item.getMaterialId());
                return p;
            }).collect(Collectors.toList());
            planningCategoryItemMaterialService.saveBatch(cims);
        }
        // 新增样衣设计
        Sample sample =BeanUtil.copyProperties(dto,Sample.class);
        PlanningUtils.toSample(sample,planningSeason, planningBand, categoryItem);
        save(sample);
        return sample;
    }

    @Override
    public PageInfo queryPageInfo(SamplePageDto dto) {
        String companyCode = getCompanyCode();
        String userId = getUserId();
        QueryWrapper<Sample> qw = new QueryWrapper<>();
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "design_no", dto.getSearch()).or().like(StrUtil.isNotBlank(dto.getSearch()), "his_design_no", dto.getSearch());
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getDesignerId()), "designer_id", dto.getDesignerId());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "season", dto.getSeason());
        qw.in(StrUtil.isNotBlank(dto.getStatus()), "status", StrUtil.split(dto.getStatus(), CharUtil.COMMA));
        qw.in(StrUtil.isNotBlank(dto.getKitting()), "kitting", StrUtil.split(dto.getKitting(), CharUtil.COMMA));
        qw.likeRight(StrUtil.isNotBlank(dto.getCategoryIds()), "category_ids", dto.getCategoryIds());
        qw.eq(BaseConstant.COMPANY_CODE, companyCode);
        //1我下发的
        if (StrUtil.equals(dto.getUserType(), SamplePageDto.userType1)) {
            qw.eq("sender", userId);
        }
        //2我创建的
        else if (StrUtil.equals(dto.getUserType(), SamplePageDto.userType2)) {
            qw.isNull("sender");
            qw.eq("create_id", userId);
        }
        //3我负责的
        else if (StrUtil.equals(dto.getUserType(), SamplePageDto.userType3)) {
            qw.eq("designer_id", userId);
        }
        Page<SamplePageVo> objects = PageHelper.startPage(dto);
        getBaseMapper().selectByQw(qw);
        amcFeignService.addUserAvatarToList(objects.getResult(),"designerId","aliasUserAvatar");
        return objects.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean startApproval(String id) {
        Sample sample = getById(id);
        if (sample == null) {
            throw new OtherException("样衣数据不存在,请先保存");
        }
        boolean flg = flowableService.start(FlowableService.sample_pdn, id, "/pdm/api/saas/sample/approval", "/pdm/api/saas/sample/approval","/sampleClothesDesign/sampleDesign/" + id, BeanUtil.beanToMap(sample));
        if (flg) {
            sample.setConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
            updateById(sample);
        }
        return flg;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean approval(AnswerDto dto) {
        Sample sample = getById(dto.getBusinessKey());
        if (sample != null) {
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                //设置样衣状态为 已开款
                sample.setStatus("1");
                sample.setConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                sample.setStatus("0");
                sample.setConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
            }
            updateById(sample);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean sendSampleMaking(SendSampleMakingDto dto) {
        Sample sample = checkedSampleExists(dto.getId());
        sample.setStatus("2");
        sample.setKitting(dto.getKitting());
        updateById(sample);
        return true;
    }

    @Override
    public Sample checkedSampleExists(String id) {
        Sample sample = getById(id);
        if (sample == null) {
            throw new OtherException("样衣数据不存在");
        }
        return sample;
    }

    @Override
    public SampleVo getDetail(String id) {
        Sample sample = getById(id);
        if (sample == null) {
            return null;
        }
        SampleVo sampleVo = BeanUtil.copyProperties(sample, SampleVo.class);
        //查询工艺信息
        Technology technology = technologyService.getOne(new QueryWrapper<Technology>().eq("f_id", id));
        sampleVo.setTechnology(Optional.ofNullable(technology).orElse(new Technology()));

        //查询附件
        List<AttachmentVo> attachmentVoList = attachmentService.findByFId(id);
        sampleVo.setAttachmentList(attachmentVoList);

        // 关联的素材库
        QueryWrapper mqw=new QueryWrapper<PlanningCategoryItemMaterial>();
        mqw.eq("planning_category_item_id", sample.getPlanningCategoryItemId());
        mqw.eq("del_flag", BaseGlobal.DEL_FLAG_NORMAL);
        List<PlanningCategoryItemMaterial> list = planningCategoryItemMaterialService.list(mqw);
        List<MaterialVo> materialList = BeanUtil.copyToList(list,MaterialVo.class);
        sampleVo.setMaterialList(materialList);


        return sampleVo;
    }

    @Override
    public List<DesignDocTreeVo> queryDesignDocTree(DesignDocTreeVo designDocTreeVo) {
        // 查询第0级 年份季节
        if(designDocTreeVo.getLevel()==null){
            return getAllYearSeason();
        }
        // 查询波段
        else if(designDocTreeVo.getLevel()==0){
            return queryBand(designDocTreeVo);
        }
        // 查询大类
        else if(designDocTreeVo.getLevel()==1){
            return queryCategory(designDocTreeVo,0);
        }
        // 查询品类
        else if(designDocTreeVo.getLevel()==2){
            return queryCategory(designDocTreeVo,1);
        }

        return null;
    }

    private List<DesignDocTreeVo> queryCategory(DesignDocTreeVo designDocTreeVo,int categoryIdx) {
        List<DesignDocTreeVo> result=new ArrayList<>(16);
        if(!StrUtil.isAllNotEmpty(designDocTreeVo.getYear(),designDocTreeVo.getSeason(),designDocTreeVo.getBandCode())){
            return result;
        }
        if(categoryIdx==1&&StrUtil.isBlank(designDocTreeVo.getCategoryIds())){
            return result;
        }
        QueryWrapper<Sample> qw=new QueryWrapper<>();
        qw.eq(COMPANY_CODE,getCompanyCode());
        qw.eq(DEL_FLAG,BaseGlobal.DEL_FLAG_NORMAL);
        qw.eq("year",designDocTreeVo.getYear());
        qw.eq("season",designDocTreeVo.getSeason());
        qw.eq("band_code",designDocTreeVo.getBandCode());
        qw.likeRight(StrUtil.isNotBlank(designDocTreeVo.getCategoryIds()),"category_ids",designDocTreeVo.getCategoryIds());
        qw.select("DISTINCT category_ids");
        List<Sample> list = list(qw);
        if(CollUtil.isNotEmpty(list)){
            String categoryIds= list.stream().map(item->{
                return CollUtil.get(StrUtil.split(item.getCategoryIds(), CharUtil.COMMA), categoryIdx);
            }).collect(Collectors.joining(","));
            Map<String, String> nameMaps = ccmFeignService.findStructureTreeNameByCategoryIds(categoryIds);
            Set<String> categoryIdsSet=new HashSet<>(16);
            for (Sample sample : list) {
                List<String> split = StrUtil.split(sample.getCategoryIds(), CharUtil.COMMA);
                String categoryId = CollUtil.get(split, categoryIdx);
                System.out.println(categoryId);
                if(categoryIdsSet.contains(categoryId)){
                    continue;
                }
                categoryIdsSet.add(categoryId);
                DesignDocTreeVo vo =new DesignDocTreeVo();
                BeanUtil.copyProperties(designDocTreeVo,vo);
                vo.setLevel(2+categoryIdx);
                vo.setChildren(categoryIdx==0);

                List<String> sub = CollUtil.sub(split, 0, categoryIdx+1);
                vo.setCategoryIds(CollUtil.join(sub,StrUtil.COMMA));
                vo.setLabel(MapUtil.getStr(nameMaps,categoryId,categoryId));
                result.add(vo);
            }
        }
        return result;
    }


    private List<DesignDocTreeVo> queryBand(DesignDocTreeVo designDocTreeVo){
        List<DesignDocTreeVo> result=new ArrayList<>(16);
        if(StrUtil.isBlank(designDocTreeVo.getYear())||StrUtil.isBlank(designDocTreeVo.getSeason())){
            return result;
        }
        QueryWrapper<Sample> qw=new QueryWrapper<>();
        qw.eq(COMPANY_CODE,getCompanyCode());
        qw.eq(DEL_FLAG,BaseGlobal.DEL_FLAG_NORMAL);
        qw.eq("year",designDocTreeVo.getYear());
        qw.eq("season",designDocTreeVo.getSeason());
        qw.select("DISTINCT band_code");
        qw.orderByAsc("band_code");
        List<Sample> list = list(qw);
        if(CollUtil.isNotEmpty(list)){
            for (Sample sample : list) {
                DesignDocTreeVo vo =new DesignDocTreeVo();
                BeanUtil.copyProperties(designDocTreeVo,vo);
                vo.setBandCode(sample.getBandCode());
                vo.setLevel(1);
                vo.setLabel(sample.getBandCode());
                vo.setChildren(true);
                result.add(vo);
            }
        }
        return result;
    }

    private List<DesignDocTreeVo> getAllYearSeason(){
      QueryWrapper<Sample> qw=new QueryWrapper<>();
      qw.eq(COMPANY_CODE,getCompanyCode());
      qw.eq(DEL_FLAG,BaseGlobal.DEL_FLAG_NORMAL);
      qw.select("DISTINCT year,season");
      List<Sample> list = list(qw);
      List<DesignDocTreeVo> result=new ArrayList<>(16);
      if(CollUtil.isNotEmpty(list)){
          //根据年份季节排序
          Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_Year,C8_Quarter");
          Map<String, String> c8Quarter = dictInfoToMap.get("C8_Quarter");
          Map<String, String> c8Year = dictInfoToMap.get("C8_Year");
          list.sort((a,b)->{
              int aIdx=CollUtil.indexOf(c8Quarter.keySet(),t->t.equals(a.getSeason()));
              int bIdx=CollUtil.indexOf(c8Quarter.keySet(),t->t.equals(b.getSeason()));
              String aLabel=a.getYear()+ StrUtil.padPre(aIdx==-1?"9999":String.valueOf(aIdx),3,"0");
              String bLabel=b.getYear()+ StrUtil.padPre(bIdx==-1?"9999":String.valueOf(bIdx),3,"0");
              return bLabel.compareTo(aLabel);
          });

          for (Sample sample : list) {
              DesignDocTreeVo vo =new DesignDocTreeVo();
              BeanUtil.copyProperties(sample,vo);
              vo.setLevel(0);
              vo.setLabel(MapUtil.getStr(c8Year,vo.getYear(),vo.getYear())+MapUtil.getStr(c8Quarter,vo.getSeason(),vo.getSeason()));
              vo.setChildren(true);
              result.add(vo);
          }
      }
      return result;
    }


}

