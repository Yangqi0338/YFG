/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricsummary.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthQueryDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialWidthPageVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabricsummary.entity.FabricSummary;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryGroup;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryPrintLog;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryStyle;
import com.base.sbc.module.fabricsummary.mapper.FabricSummaryMapper;
import com.base.sbc.module.fabricsummary.service.FabricSummaryGroupService;
import com.base.sbc.module.fabricsummary.service.FabricSummaryPrintLogService;
import com.base.sbc.module.fabricsummary.service.FabricSummaryService;
import com.base.sbc.module.fabricsummary.service.FabricSummaryStyleService;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.sample.dto.FabricSummaryStyleMaterialDto;
import com.base.sbc.module.sample.dto.FabricSummaryV2Dto;
import com.base.sbc.module.sample.dto.PrintFabricSummaryLogDto;
import com.base.sbc.module.sample.vo.FabricStyleGroupVo;
import com.base.sbc.module.sample.vo.FabricSummaryGroupVo;
import com.base.sbc.module.sample.vo.FabricSummaryInfoVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：款式管理-面料汇总 service类
 * @address com.base.sbc.module.fabricsummary.service.FabricSummaryService
 * @author your name
 * @email your email
 * @date 创建时间：2024-3-28 15:25:40
 * @version 1.0  
 */
@Service
public class FabricSummaryServiceImpl extends BaseServiceImpl<FabricSummaryMapper, FabricSummary> implements FabricSummaryService {

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private StylePicUtils stylePicUtils;

    @Autowired
    private FabricSummaryGroupService fabricSummaryGroupService;

    @Autowired
    private FabricSummaryStyleService fabricSummaryStyleService;

    @Autowired
    private FabricSummaryPrintLogService fabricSummaryPrintLogService;

    @Autowired
    private PlanningSeasonService planningSeasonService;

    @Autowired
    private PackInfoService packetInfoService;

    @Autowired
    private BasicsdatumMaterialService basicsdatumMaterialService;

    @Autowired
    @Lazy
    private OrderBookDetailService orderBookDetailService;

    @Autowired
    private BasicsdatumMaterialColorService materialColorService;

    @Override
    public  PageInfo<String> fabricSummaryIdList(FabricSummaryV2Dto dto) {
        Page<String> page  = PageHelper.startPage(dto);
        BaseQueryWrapper<String> qw = new BaseQueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getId())){
            qw.eq("tfs.id",dto.getId());
        }
        qw.eq("tfs.company_code",dto.getCompanyCode());
        QueryGenerator.initQueryWrapperByMap(qw, dto);
        baseMapper.fabricSummaryIdList(qw);
        return page.toPageInfo();
    }

    @Override
    public PageInfo<FabricSummaryInfoVo> fabricSummaryInfoVoList(FabricSummaryV2Dto dto) {
        if (StringUtils.isEmpty(dto.getGroupId())){
            throw new OtherException("分组id不可为空");
        }
        BaseQueryWrapper<FabricSummaryInfoVo> qw = new BaseQueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getId())){
            qw.eq("tfs.id",dto.getId());
        }
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        qw.eq("tfs.company_code", super.getCompanyCode());
        qw.in("tfs.group_id",StringUtils.convertList(dto.getGroupId()));
        qw.in(StringUtils.isNotEmpty(dto.getMaterialCode()),"tfs.material_code",StringUtils.convertList(dto.getMaterialCode()));
        qw.in(StringUtils.isNotEmpty(dto.getStyleNo()),"tfss.style_no",StringUtils.convertList(dto.getStyleNo()));
        qw.orderByDesc("tfs.create_date");
        Page<FabricSummaryInfoVo> page = PageHelper.startPage(dto);
        List<FabricSummaryInfoVo> list = getBaseMapper().fabricSummaryInfoVoList(qw);
        if (CollectionUtils.isNotEmpty(list)){
            list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        }
        PageInfo<FabricSummaryInfoVo> pageInfo = page.toPageInfo();
        if (isColumnHeard){
            pageInfo.setList(list);
            return pageInfo;
        }
        minioUtils.setObjectUrlToList(list, "imageUrl");
        stylePicUtils.setStylePic(list, "stylePic");

        Map<String, List<FabricSummaryInfoVo>> map = list.stream().collect(Collectors.groupingBy(FabricSummaryInfoVo::getId));

        List<FabricSummaryInfoVo> result = new ArrayList<>();
        for (String s : map.keySet()) {
            List<FabricSummaryInfoVo> fabricSummaryInfoVoList = map.get(s);
            if (fabricSummaryInfoVoList.size() > 1){
                fabricSummaryInfoVoList =  fabricSummaryInfoVoList.stream().sorted(Comparator.comparing(FabricSummaryInfoVo::getSort)).collect(Collectors.toList());
            }
            result.addAll(fabricSummaryInfoVoList);
        }
        pageInfo.setList(result.stream().sorted(Comparator.comparing(FabricSummaryInfoVo::getCreateDate).reversed()).collect(Collectors.toList()));
        return pageInfo;


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean fabricSummaryGroupSaveOrUpdate(FabricStyleGroupVo fabricStyleGroupVo) {
        if (StringUtils.isBlank(fabricStyleGroupVo.getGroupName())){
            throw new OtherException("名称不能为空");
        }
        QueryWrapper<FabricSummaryGroup> qw = new QueryWrapper<>();
        qw.lambda().eq(FabricSummaryGroup::getGroupName,fabricStyleGroupVo.getGroupName());
        qw.lambda().eq(FabricSummaryGroup::getDelFlag,"0");
        if (StringUtils.isNotBlank(fabricStyleGroupVo.getId())){
            qw.lambda().ne(FabricSummaryGroup::getId,fabricStyleGroupVo.getId());
            fabricStyleGroupVo.updateInit();
        }else {
            PlanningSeason planningSeason = planningSeasonService.getById(fabricStyleGroupVo.getPlanningSeasonId());
            if (planningSeason == null) {
                throw new OtherException("产品季信息为空");
            }
            BeanUtils.copyProperties(planningSeason,fabricStyleGroupVo);
            fabricStyleGroupVo.setId(new IdGen().nextIdStr());
            fabricStyleGroupVo.insertInit();
            fabricStyleGroupVo.setPlanningSeasonName(planningSeason.getName());
        }
        List<FabricSummaryGroup> list = fabricSummaryGroupService.list(qw);
        if (CollectionUtils.isNotEmpty(list)){
            throw new OtherException("名称不能重复，请检查名称");
        }
        return fabricSummaryGroupService.saveOrUpdate(fabricStyleGroupVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFabricSummaryGroup(FabricStyleGroupVo fabricStyleGroupVo) {
        if (StringUtils.isBlank(fabricStyleGroupVo.getId())){
            return true;
        }
        QueryWrapper<FabricSummary> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FabricSummary::getGroupId,fabricStyleGroupVo.getId());
        queryWrapper.lambda().eq(FabricSummary::getDelFlag,"0");
        if (count(queryWrapper) > 0){
            throw new OtherException("分组中存在数据，不允许删除！");
        }
        UpdateWrapper<FabricSummaryGroup> uw = new UpdateWrapper<>();
        uw.lambda().eq(FabricSummaryGroup::getId,fabricStyleGroupVo.getId());
        uw.lambda().set(FabricSummaryGroup::getDelFlag,"1");
        return fabricSummaryGroupService.update(uw);
    }

    @Override
    public PageInfo<FabricSummaryGroupVo> fabricSummaryGroup(FabricSummaryStyleMaterialDto dto) {
        return fabricSummaryGroupService.getGroupList(dto);
    }

    @Override
    public PageInfo<FabricSummaryInfoVo> selectFabricSummaryStyle(FabricSummaryV2Dto dto) {
        FabricSummaryGroup groupServiceById = fabricSummaryGroupService.getById(dto.getGroupId());
        if (Objects.isNull(groupServiceById)){
            throw new OtherException("上级盒子不存在！");
        }
        dto.setPlanningSeasonId(groupServiceById.getPlanningSeasonId());
        PageInfo<FabricSummaryInfoVo> pageInfo = packetInfoService.selectFabricSummaryStyle(dto);

        //是否引用关联
        pageInfo.getList().forEach(item ->{
            item.setGroupId(groupServiceById.getId());
            List<FabricSummaryStyle> list = fabricSummaryStyleService.getByGroupStyle(groupServiceById.getId(),item.getBomId(),item.getStyleNo());
            if (CollectionUtils.isNotEmpty(list)){
                item.setCiteStatus("1");
                item.setFabricSummaryStyleId(list.get(0).getId());
            }
        });
        stylePicUtils.setStylePic(pageInfo.getList(), "stylePicUrl");
        stylePicUtils.setStylePic(pageInfo.getList(), "styleColorPicUrl");
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveFabricSummary(List<FabricSummaryInfoVo> dto) {
        if (CollectionUtils.isEmpty(dto)){
            return true;
        }
        FabricSummaryGroup groupServiceById = fabricSummaryGroupService.getById(dto.get(0).getGroupId());
        if (Objects.isNull(groupServiceById)){
            throw new OtherException("上级盒子不存在！");
        }
        List<FabricSummaryInfoVo> infoVos = dto.stream().filter(item -> StringUtils.isNotBlank(item.getFabricSummaryStyleId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(infoVos)){
            throw new OtherException(infoVos.get(0).getDesignNo() + "已添加，请勿重复添加");
        }
        dto.forEach(item ->{
            List<FabricSummaryStyle> list = fabricSummaryStyleService.getByGroupStyle(groupServiceById.getId(),item.getBomId(),item.getStyleNo());
            if (CollectionUtils.isNotEmpty(list)){
                throw new OtherException(item.getDesignNo() + "已添加，请勿重复添加");
            }
        });
        List<FabricSummary> fabricSummaries = Lists.newArrayList();
        Map<String, FabricSummary> infoVoMap = Maps.newHashMap();
        List<FabricSummaryStyle> fabricSummaryStyles= Lists.newArrayList();
        for (FabricSummaryInfoVo fabricSummaryInfoVo : dto) {
            FabricSummaryStyle fabricSummaryStyle = new FabricSummaryStyle();
            BeanUtil.copyProperties(fabricSummaryInfoVo, fabricSummaryStyle);
            fabricSummaryStyle.insertInit();
            fabricSummaryStyle.setId(new IdGen().nextIdStr());
            //补充款式信息
            fullFabricSummaryStyle(fabricSummaryInfoVo);

            FabricSummary byGroupIdAndMaterialCode = getByGroupIdAndMaterialCode(groupServiceById.getId(), fabricSummaryInfoVo.getMaterialCode());
            if (!Objects.isNull(byGroupIdAndMaterialCode)){
                fabricSummaryStyle.setFabricSummaryId(byGroupIdAndMaterialCode.getId());
                fabricSummaryStyles.add(fabricSummaryStyle);
                continue;
            }
            FabricSummary fabricSummary = infoVoMap.get(fabricSummaryInfoVo.getMaterialCode());
            if (null == fabricSummary){
                fabricSummary =  basicsdatumMaterialService.getMaterialSummaryInfo(fabricSummaryInfoVo.getMaterialCode());
                fabricSummary.setGroupId(groupServiceById.getId());
                infoVoMap.put(fabricSummaryInfoVo.getMaterialCode(),fabricSummary);
            }
            //补充信息
            fullFabricSummary(fabricSummary);
            fabricSummaries.add(fabricSummary);

            fabricSummaryStyle.setFabricSummaryId(fabricSummary.getId());
            fabricSummaryStyles.add(fabricSummaryStyle);

        }
        if (CollectionUtils.isNotEmpty(fabricSummaries)){
            saveBatch(fabricSummaries);
        }

        return fabricSummaryStyleService.saveBatch(fabricSummaryStyles);
    }

    @Override
    @Transactional(rollbackFor =Exception.class)
    public boolean fabricSummarySync(List<FabricSummaryV2Dto> dto) {
        if (CollectionUtils.isEmpty(dto)){
            return true;
        }

        for (FabricSummaryV2Dto fabricSummaryV2Dto : dto) {
            FabricSummary fabricSummary = getById(fabricSummaryV2Dto.getId());
            if (Objects.isNull(fabricSummary)){
                throw new OtherException("数据不存在！");
            }
            FabricSummaryGroup groupServiceById = fabricSummaryGroupService.getById(fabricSummary.getGroupId());
            if (Objects.isNull(groupServiceById)){
                throw new OtherException("上级盒子不存在！");
            }
            fabricSummaryV2Dto.setPlanningSeasonId(groupServiceById.getPlanningSeasonId());
            //检查更新相关
            checkSynFabricSummary(fabricSummary,fabricSummaryV2Dto);
        }
        //打印日志
        FabricSummaryPrintLog fabricSummaryPrintLog = new FabricSummaryPrintLog();
        fabricSummaryPrintLog.insertInit();
        fabricSummaryPrintLog.setFabricSummaryId(JSON.toJSONString(dto.get(0).getId()));
        fabricSummaryPrintLogService.save(fabricSummaryPrintLog);
        return true;
    }

    @Override
    public PageInfo<FabricSummaryPrintLog> printFabricSummaryLog(PrintFabricSummaryLogDto dto) {
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("del_flag","0");
        qw.eq(org.apache.commons.lang3.StringUtils.isNotBlank(dto.getCreateName()),"create_name",dto.getCreateName());
        qw.eq(org.apache.commons.lang3.StringUtils.isNotBlank(dto.getFabricSummaryId()),"fabric_summary_id",dto.getFabricSummaryId());
        Page<FabricSummaryPrintLog> page = PageHelper.startPage(dto);
        fabricSummaryPrintLogService.list(qw);
        return page.toPageInfo();
    }

    private void fullFabricSummary(FabricSummary fabricSummaryInfoVo) {
        fabricSummaryInfoVo.setFabricSummaryCode("ML"+System.currentTimeMillis());
        fabricSummaryInfoVo.insertInit();
        fabricSummaryInfoVo.setId(new IdGen().nextIdStr());
        //规格
        BasicsdatumMaterialWidthQueryDto basicsdatumMaterialWidthQueryDto = new BasicsdatumMaterialWidthQueryDto();
        basicsdatumMaterialWidthQueryDto.setMaterialCode(fabricSummaryInfoVo.getMaterialCode());
        PageInfo<BasicsdatumMaterialWidthPageVo> basicsdatumMaterialWidthList = basicsdatumMaterialService.getBasicsdatumMaterialWidthList(basicsdatumMaterialWidthQueryDto);
        if (CollectionUtils.isNotEmpty(basicsdatumMaterialWidthList.getList()) && basicsdatumMaterialWidthList.getList().size() == 1){
            fabricSummaryInfoVo.setWidthName(basicsdatumMaterialWidthList.getList().get(0).getName());
        }
    }

    private void fullFabricSummaryStyle(FabricSummaryInfoVo fabricSummaryInfoVo) {
        // 总投产
        OrderBookDetailQueryDto orderBookDetailQueryDto = new OrderBookDetailQueryDto();
        orderBookDetailQueryDto.setBulkStyleNoFull(fabricSummaryInfoVo.getStyleNo());
        List<OrderBookDetailVo> orderBookDetailVoPageInfo = orderBookDetailService.queryList(orderBookDetailQueryDto);
        if(CollectionUtils.isNotEmpty(orderBookDetailVoPageInfo)){
            List<String> totalProductions = orderBookDetailVoPageInfo.stream().map(OrderBookDetailVo::getTotalProduction).collect(Collectors.toList());
            fabricSummaryInfoVo.setTotalProduction(String.valueOf(totalProductions.stream().filter(org.apache.commons.lang3.StringUtils::isNotBlank).mapToDouble(Double::parseDouble).sum()));
        }
        //色号
        List<BasicsdatumMaterialColor> list  = materialColorService.getBasicsdatumMaterialColorCodeList(fabricSummaryInfoVo.getMaterialCode(), fabricSummaryInfoVo.getColorCode());
        if (CollectionUtils.isNotEmpty(list)){
            fabricSummaryInfoVo.setSupplierColorNo(list.get(0).getSupplierColorCode());
        }
    }

    /**
     * 检查款式相关
     * @param fabricSummary
     */
    private void checkSynFabricSummary(FabricSummary fabricSummary, FabricSummaryV2Dto fabricSummaryV2Dto) {
        QueryWrapper<FabricSummaryStyle> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(FabricSummaryStyle::getFabricSummaryId,fabricSummary.getId())
                .eq(FabricSummaryStyle::getDelFlag,'0')
                .eq(FabricSummaryStyle::getGroupId,fabricSummary.getGroupId());
        List<FabricSummaryStyle> list = fabricSummaryStyleService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        fabricSummaryV2Dto.setBomList(list.stream().map(FabricSummaryStyle::getBomId).collect(Collectors.toList()));
        fabricSummaryV2Dto.setStyleNos(list.stream().map(FabricSummaryStyle::getStyleNo).collect(Collectors.toList()));
        PageInfo<FabricSummaryInfoVo> pageInfo = packetInfoService.selectFabricSummaryStyle(fabricSummaryV2Dto);
//        if (CollectionUtils.isEmpty(pageInfo.getList())){
//            throw new OtherException(JSON.toJSONString(fabricSummaryV2Dto.getStyleNos())+"等款式，已经不在引用该物料："+fabricSummary.getMaterialCode());
//        }
        if (CollectionUtils.isEmpty(pageInfo.getList())){
            list.forEach(item ->item.setDelFlag("1")) ;
            fabricSummaryStyleService.deleteByIds(list.stream().map(FabricSummaryStyle::getId).collect(Collectors.toList()));
            return;
        }
        //不存在的引用关系需要删除
        List<String> deletes = Lists.newArrayList();
        Map<String, List<FabricSummaryInfoVo>> map = pageInfo.getList().stream().collect(Collectors.groupingBy(FabricSummaryInfoVo::getBomId));
        Set<String> poms = map.keySet();
        String materialCode = pageInfo.getList().get(0).getMaterialCode();
        for (FabricSummaryStyle fabricSummaryStyle : list) {
            //不存在引用，或者对应的引用被替换
            if (!poms.contains(fabricSummaryStyle.getBomId()) || !fabricSummary.getMaterialCode().equals(materialCode)){
                fabricSummaryStyle.setDelFlag("1");
                deletes.add(fabricSummaryStyle.getId());
                continue;
            }
            //检查更新款式相关
            checkUpdateFabricSummary(fabricSummary,fabricSummaryStyle,map.get(fabricSummaryStyle.getBomId()));
        }
        if (CollectionUtils.isNotEmpty(deletes)){
            fabricSummaryStyleService.deleteByIds(deletes);
        }
    }

    private void checkUpdateFabricSummary(FabricSummary fabricSummary, FabricSummaryStyle fabricSummaryStyle, List<FabricSummaryInfoVo> fabricSummaryInfoVos) {
        FabricSummaryInfoVo infoVo = fabricSummaryInfoVos.get(0);
        boolean isUpdate = false;

        //数据更新
        fabricSummary.setGramWeight(infoVo.getGramWeight());
        fabricSummary.setDensity(infoVo.getDensity());
        fabricSummary.setMinimumOrderQuantity(infoVo.getMinimumOrderQuantity());
        fabricSummary.setSpecification(infoVo.getSpecification());
        updateById(fabricSummary);

        if (StringUtils.isNotEmpty(infoVo.getMaterialColor()) && !infoVo.getMaterialColor().equals(fabricSummaryStyle.getMaterialColor())){
            fabricSummaryStyle.setMaterialColor(infoVo.getMaterialColor());
            fabricSummaryStyle.setMaterialColorCode(infoVo.getMaterialColorCode());
            //色号
            List<BasicsdatumMaterialColor> list  = materialColorService.getBasicsdatumMaterialColorCodeList(infoVo.getMaterialCode(), infoVo.getColorCode());
            if (CollectionUtils.isNotEmpty(list)){
                fabricSummaryStyle.setSupplierColorNo(list.get(0).getSupplierColorCode());
            }
            isUpdate= true;
        }

        if (StringUtils.isNotEmpty(infoVo.getCollocationName()) && !infoVo.getCollocationName().equals(fabricSummaryStyle.getCollocationName())){
            fabricSummaryStyle.setCollocationName(infoVo.getCollocationName());
            fabricSummaryStyle.setCollocationCode(infoVo.getCollocationCode());
            isUpdate= true;
        }

        if (StringUtils.isNotEmpty(infoVo.getPartName()) && !infoVo.getPartName().equals(fabricSummaryStyle.getPartName())){
            fabricSummaryStyle.setPartName(infoVo.getPartName());
            fabricSummaryStyle.setPartCode(infoVo.getPartCode());
            isUpdate= true;
        }

        if (isUpdate){
            fabricSummaryStyleService.updateById(fabricSummaryStyle);
        }

    }

    private FabricSummary getByGroupIdAndMaterialCode(String groupId, String materialCode){
        QueryWrapper<FabricSummary> qw = new QueryWrapper<>();
        qw.lambda().eq(FabricSummary::getMaterialCode, materialCode);
        qw.lambda().eq(FabricSummary::getDelFlag, "0");
        qw.lambda().eq(FabricSummary::getGroupId, groupId);
        List<FabricSummary> list = list(qw);
        return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
    }


// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
