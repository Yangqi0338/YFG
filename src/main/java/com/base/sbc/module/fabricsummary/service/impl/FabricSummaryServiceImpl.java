/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricsummary.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.constant.Constants;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.DateUtils;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthQueryDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPricePageVo;
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
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.sample.dto.FabricSummaryStyleMaterialDto;
import com.base.sbc.module.sample.dto.FabricSummaryV2Dto;
import com.base.sbc.module.sample.dto.PrintFabricSummaryLogDto;
import com.base.sbc.module.sample.vo.FabricStyleGroupVo;
import com.base.sbc.module.sample.vo.FabricSummaryGroupVo;
import com.base.sbc.module.sample.vo.FabricSummaryInfoVo;
import com.base.sbc.module.sample.vo.PrintCheckVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
        if (CollUtil.isNotEmpty(list)){
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
        Map<String, Long> printLogCountMap = new HashMap<>();
        //打印次数
        pageInfo.getList().forEach(item ->{
            if (null != printLogCountMap.get(item.getFabricSummaryId())){
                item.setPrintCount(printLogCountMap.get(item.getFabricSummaryId()));
            }else {
                Long printLogCount = fabricSummaryPrintLogService.getPrintLogCount(item.getFabricSummaryId());
                item.setPrintCount(printLogCount);
                printLogCountMap.put(item.getFabricSummaryId(), printLogCount);
            }
        });

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
        if (CollUtil.isNotEmpty(list)){
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
        if (StringUtils.isNotEmpty(dto.getId())){
            FabricSummary fabricSummary = getById(dto.getId());
            if (!Objects.isNull(fabricSummary)){
                dto.setSupplierFabricCode(fabricSummary.getSupplierFabricCode());
            }
        }
        dto.setPlanningSeasonId(groupServiceById.getPlanningSeasonId());
        PageInfo<FabricSummaryInfoVo> pageInfo = packetInfoService.selectFabricSummaryStyle(dto);

        //是否引用关联
        pageInfo.getList().forEach(item ->{
            item.setGroupId(groupServiceById.getId());
            item.setFabricSummaryId(dto.getId());
            List<FabricSummaryStyle> list = fabricSummaryStyleService.getByGroupStyle(dto.getId(), groupServiceById.getId(),item.getBomId(),item.getStyleNo());
            if (CollUtil.isNotEmpty(list)){
                item.setCiteStatus("1");
                item.setAddStatus(StringUtils.isBlank(dto.getId()) ? "0":"1");
            }
        });
        stylePicUtils.setStylePic(pageInfo.getList(), "stylePicUrl");
        stylePicUtils.setStylePic(pageInfo.getList(), "styleColorPicUrl");
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveFabricSummary(List<FabricSummaryInfoVo> dto) {
        if (CollUtil.isEmpty(dto)){
            return true;
        }
        FabricSummaryGroup groupServiceById = fabricSummaryGroupService.getById(dto.get(0).getGroupId());
        if (Objects.isNull(groupServiceById)){
            throw new OtherException("上级盒子不存在！");
        }
        List<FabricSummaryInfoVo> infoVos = dto.stream().filter(item -> StringUtils.isNotBlank(item.getFabricSummaryStyleId())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(infoVos)){
            throw new OtherException(infoVos.get(0).getDesignNo() + "已添加，请勿重复添加");
        }
        for (FabricSummaryInfoVo item : dto) {
            if (StringUtils.isBlank(item.getFabricSummaryId())){
                continue;
            }
            List<FabricSummaryStyle> list = fabricSummaryStyleService.getByGroupStyle(item.getFabricSummaryId(),groupServiceById.getId(),item.getBomId(),item.getStyleNo());
            if (CollUtil.isNotEmpty(list)){
                throw new OtherException(item.getDesignNo() + "已添加，请勿重复添加");
            }
        }
        List<FabricSummary> fabricSummaries = Lists.newArrayList();
        Map<String, FabricSummary> infoVoMap = Maps.newHashMap();
        List<FabricSummaryStyle> fabricSummaryStyles= Lists.newArrayList();
        int index = 0;
        for (FabricSummaryInfoVo fabricSummaryInfoVo : dto) {
            if (!Constants.ONE_STR.equals(fabricSummaryInfoVo.getDesignVerify())){
                throw new OtherException("设计师未确认的详单不允许添加！"+"未确认的款号："+fabricSummaryInfoVo.getStyleNo());
            }
            FabricSummaryStyle fabricSummaryStyle = new FabricSummaryStyle();
            BeanUtil.copyProperties(fabricSummaryInfoVo, fabricSummaryStyle);
            fabricSummaryStyle.insertInit();
            fabricSummaryStyle.setId(new IdGen().nextIdStr());
            //补充款式信息
            fullFabricSummaryStyle(fabricSummaryInfoVo);
            FabricSummary fabricSummary = infoVoMap.get(fabricSummaryInfoVo.getMaterialCode());
            if (null == fabricSummary){
                fabricSummary =  basicsdatumMaterialService.getMaterialSummaryInfo(fabricSummaryInfoVo.getMaterialCode());
                fabricSummary.setGroupId(groupServiceById.getId());
                infoVoMap.put(fabricSummaryInfoVo.getMaterialCode(),fabricSummary);
            }
            //补充信息
            fullFabricSummary(fabricSummary,fabricSummaryInfoVo, ++index);
            fabricSummaries.add(fabricSummary);

            fabricSummaryStyle.setFabricSummaryId(fabricSummary.getId());
            fabricSummaryStyles.add(fabricSummaryStyle);

        }
        if (CollUtil.isNotEmpty(fabricSummaries)){
            saveOrUpdateBatch(fabricSummaries);
        }

        return fabricSummaryStyleService.saveOrUpdateBatch(fabricSummaryStyles);
    }

    @Override
    @Transactional(rollbackFor =Exception.class)
    public ApiResult<PrintCheckVo> fabricSummarySync(List<FabricSummaryV2Dto> dto) {
        if (CollUtil.isEmpty(dto)){
            throw new OtherException("参数不能为空");
        }
        FabricSummaryV2Dto fabricSummaryV2Dto = dto.get(0);
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
        return checkSynFabricSummary(fabricSummary,fabricSummaryV2Dto);
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

    @Override
    public boolean printLogRecord(FabricSummaryV2Dto dto) {
        //打印日志
        FabricSummaryPrintLog fabricSummaryPrintLog = new FabricSummaryPrintLog();
        fabricSummaryPrintLog.insertInit();
        fabricSummaryPrintLog.setFabricSummaryId(dto.getId());
        return fabricSummaryPrintLogService.save(fabricSummaryPrintLog);
    }

    private void fullFabricSummary(FabricSummary fabricSummaryInfo, FabricSummaryInfoVo fabricSummaryInfoVo,int index) {
        getFabricSummaryCode(fabricSummaryInfo, index);
        fabricSummaryInfo.insertInit();
        fabricSummaryInfo.setId(new IdGen().nextIdStr());
        if (StringUtils.isEmpty(fabricSummaryInfo.getSupplierFabricCode()) && StringUtils.isEmpty(fabricSummaryInfoVo.getSupplierFabricCode())){
            return;
        }
        if (StringUtils.isEmpty(fabricSummaryInfo.getSupplierFabricCode()) && StringUtils.isNotEmpty(fabricSummaryInfoVo.getSupplierFabricCode())){
            fabricSummaryInfo.setSupplierFabricCode(fabricSummaryInfoVo.getSupplierFabricCode());
            fabricSummaryInfo.setSupplierAbbreviation(fabricSummaryInfoVo.getSupplierAbbreviation());
            fabricSummaryInfo.setSupplierName(fabricSummaryInfoVo.getSupplierName());
            fabricSummaryInfo.setSupplierId(fabricSummaryInfo.getSupplierId());
            return;
        }


        //规格
        BasicsdatumMaterialWidthQueryDto basicsdatumMaterialWidthQueryDto = new BasicsdatumMaterialWidthQueryDto();
        basicsdatumMaterialWidthQueryDto.setMaterialCode(fabricSummaryInfo.getMaterialCode());
        PageInfo<BasicsdatumMaterialWidthPageVo> basicsdatumMaterialWidthList = basicsdatumMaterialService.getBasicsdatumMaterialWidthList(basicsdatumMaterialWidthQueryDto);
        if (CollUtil.isNotEmpty(basicsdatumMaterialWidthList.getList()) && basicsdatumMaterialWidthList.getList().size() == 1){
            fabricSummaryInfo.setWidthName(basicsdatumMaterialWidthList.getList().get(0).getName());
        }

        //期货
        BasicsdatumMaterialPriceQueryDto basicsdatumMaterialPriceQueryDto = new BasicsdatumMaterialPriceQueryDto();
        basicsdatumMaterialPriceQueryDto.setMaterialCode(fabricSummaryInfo.getMaterialCode());
        PageInfo<BasicsdatumMaterialPricePageVo> basicsdatumMaterialPriceList = basicsdatumMaterialService.getBasicsdatumMaterialPriceList(basicsdatumMaterialPriceQueryDto);
        if (CollUtil.isNotEmpty(basicsdatumMaterialPriceList.getList())){
            fabricSummaryInfo.setProductionDay(basicsdatumMaterialPriceList.getList().get(0).getProductionDay());
        }

    }

    private void fullFabricSummaryStyle(FabricSummaryInfoVo fabricSummaryInfoVo) {
        // 总投产
        fabricSummaryInfoVo.setTotalProduction(orderBookDetailService.getByStyleNoTotalProduction(fabricSummaryInfoVo.getStyleNo()));
        //色号
        List<BasicsdatumMaterialColor> list  = materialColorService.getBasicsdatumMaterialColorCodeList(fabricSummaryInfoVo.getMaterialCode(), fabricSummaryInfoVo.getMaterialColorCode());
        if (CollUtil.isNotEmpty(list)){
            fabricSummaryInfoVo.setSupplierColorNo(list.get(0).getSupplierColorCode());
        }
    }

    /**
     * 检查款式相关
     * @param fabricSummary
     */
    private ApiResult<PrintCheckVo> checkSynFabricSummary(FabricSummary fabricSummary, FabricSummaryV2Dto fabricSummaryV2Dto) {
        QueryWrapper<FabricSummaryStyle> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(FabricSummaryStyle::getFabricSummaryId,fabricSummary.getId())
                .eq(FabricSummaryStyle::getDelFlag,'0')
                .eq(FabricSummaryStyle::getGroupId,fabricSummary.getGroupId());
        List<FabricSummaryStyle> list = fabricSummaryStyleService.list(queryWrapper);
        if (CollUtil.isEmpty(list)){
            return ApiResult.success("成功",new PrintCheckVo());
        }
        fabricSummaryV2Dto.setBomList(list.stream().map(FabricSummaryStyle::getBomId).collect(Collectors.toList()));
        fabricSummaryV2Dto.setStyleNos(list.stream().map(FabricSummaryStyle::getStyleNo).collect(Collectors.toList()));
        PageInfo<FabricSummaryInfoVo> pageInfo = packetInfoService.selectFabricSummaryStyle(fabricSummaryV2Dto);
        if (CollUtil.isEmpty(pageInfo.getList())){
            return ApiResult.error("该物料下已无引用的款式，请确认！",200);
        }

        List<String> bomIds = pageInfo.getList().stream().map(FabricSummaryInfoVo::getBomId).collect(Collectors.toList());
        //已经被删除引用的款式
        List<FabricSummaryStyle> delFabricSummaryStyle = list.stream().filter(item -> !bomIds.contains(item.getBomId())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(delFabricSummaryStyle)){
            return ApiResult.error(delFabricSummaryStyle.get(0).getStyleNo()+"款已不引用该物料，请删除后再打印！",200);
        }

        Map<String, List<FabricSummaryInfoVo>> map = pageInfo.getList().stream().collect(Collectors.groupingBy(FabricSummaryInfoVo::getBomId));
        List<FabricSummaryInfoVo> designerNotlist = Lists.newArrayList();
        for (FabricSummaryStyle fabricSummaryStyle : list) {
            FabricSummaryInfoVo fabricSummaryInfoVo =  map.get(fabricSummaryStyle.getBomId()).get(0);
            if (!Constants.ONE_STR.equals(fabricSummaryInfoVo.getDesignVerify())){
                designerNotlist.add(fabricSummaryInfoVo);
            }
            if (StringUtils.isBlank(fabricSummaryInfoVo.getSupplierFabricCode()) && StringUtils.isBlank(fabricSummary.getSupplierFabricCode())){
                continue;
            }
            //物料号改变
            if (!Optional.ofNullable(fabricSummaryInfoVo.getSupplierFabricCode()).orElse("").
                    equals(Optional.ofNullable(fabricSummary.getSupplierFabricCode()).orElse(""))){
                return ApiResult.error(fabricSummaryStyle.getStyleNo()+"款：物料号已经改变，请删除重新引用后再打印！",200);
            }
        }
        return ApiResult.success("成功",new PrintCheckVo(CollUtil.isNotEmpty(designerNotlist) ? designerNotlist : null));
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
            List<BasicsdatumMaterialColor> list  = materialColorService.getBasicsdatumMaterialColorCodeList(infoVo.getMaterialCode(), infoVo.getMaterialColorCode());
            if (CollUtil.isNotEmpty(list)){
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
        return CollUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 获取面料编码
     * @param fabricSummary
     * @return
     */
    private void getFabricSummaryCode(FabricSummary fabricSummary, int index){
        int serialNumber = this.getBaseMapper().getSerialNumberMax(fabricSummary.getGroupId()) + index;
        //XD+品牌代码+日期+4位流水
        String fabricSummaryCode = "XD" + fabricSummary.getBrand() + DateUtils.formatDate(new Date(), "yyyyMMdd") +String.format("%04d", serialNumber);
        fabricSummary.setFabricSummaryCode(fabricSummaryCode);

    }


// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
