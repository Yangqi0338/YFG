/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioConfig;
import com.base.sbc.config.utils.*;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.eumns.UreportDownEnum;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.UreportService;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.hangTag.entity.HangTag;
import com.base.sbc.module.hangTag.service.HangTagService;
import com.base.sbc.module.hangTag.vo.HangTagVO;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.service.OperaLogService;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.*;
import com.base.sbc.module.pack.mapper.PackInfoMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.GenTechSpecPdfFile;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.*;
import com.base.sbc.module.pricing.vo.PricingVO;
import com.base.sbc.module.smp.DataUpdateScmService;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.entity.StyleInfoColor;
import com.base.sbc.module.style.entity.StyleInfoSku;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.service.StyleInfoColorService;
import com.base.sbc.module.style.service.StyleInfoSkuService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.open.dto.OpenStyleDto;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.*;

/**
 * 类描述：资料包 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackInfoService
 * @email your email
 * @date 创建时间：2023-7-6 17:13:01
 */
@Service
public class PackInfoServiceImpl extends PackBaseServiceImpl<PackInfoMapper, PackInfo> implements PackInfoService {


// 自定义方法区 不替换的区域【other_start】

    @Resource
    private StyleService styleService;

    @Resource
    private AttachmentService attachmentService;
    @Resource
    private UploadFileService uploadFileService;
    @Resource
    private OperaLogService operaLogService;
    @Resource
    private PackBomVersionService packBomVersionService;
    @Resource
    private PackBomService packBomService;
    @Resource
    private PackBomSizeService packBomSizeService;

    @Resource
    private PackSizeConfigService packSizeConfigService;
    @Resource
    private PackSizeService packSizeService;
    @Resource
    private PackProcessPriceService packProcessPriceService;
    @Resource
    private PackPricingService packPricingService;

    @Resource
    private PackPricingProcessCostsService packPricingProcessCostsService;

    @Resource
    private PackPricingCraftCostsService packPricingCraftCostsService;
    @Resource
    private PackPricingOtherCostsService packPricingOtherCostsService;
    @Resource
    private PackTechSpecService packTechSpecService;
    @Resource
    private PackTechPackagingService packTechPackagingService;
    @Resource
    private PackSampleReviewService packSampleReviewService;
    @Resource
    private PackBusinessOpinionService packBusinessOpinionService;

    @Resource
    private PackInfoStatusService packInfoStatusService;
    @Resource
    private FlowableService flowableService;
    @Resource
    private UreportService ureportService;

    @Resource
    private StyleColorMapper styleColorMapper;

    @Resource
    private PackBomColorService packBomColorService;

    @Resource
    private MessageUtils messageUtils;

    @Resource
    @Lazy
    private HangTagService hangTagService;

    @Autowired
    private BaseController baseController;

    @Autowired
    private MinioConfig minioConfig;
    @Value("${baseRequestUrl}")
    private String baseRequestUrl;
    @Value("${baseFrontEndAddress}")
    private String baseFrontEndAddress;

    @Autowired
    private DataUpdateScmService dataUpdateScmService;
    @Autowired
    private CcmFeignService ccmFeignService;
    @Autowired
    private StyleInfoSkuService styleInfoSkuService;
    @Autowired
    private BasicsdatumSizeService basicsdatumSizeService;

    @Resource
    private StyleInfoColorService styleInfoColorService;
    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Override
    public PageInfo<StylePackInfoListVo> pageBySampleDesign(PackInfoSearchPageDto pageDto) {

        // 查询款式设计数据
        BaseQueryWrapper<Style> sdQw = new BaseQueryWrapper<>();
        sdQw.in("status", "1", "2");
        sdQw.notEmptyEq("prod_category1st", pageDto.getProdCategory1st());
        sdQw.notEmptyEq("prod_category", pageDto.getProdCategory());
        sdQw.notEmptyEq("prod_category2nd", pageDto.getProdCategory2nd());
        sdQw.notEmptyEq("prod_category3rd", pageDto.getProdCategory3rd());
        sdQw.notEmptyEq("planning_season_id", pageDto.getPlanningSeasonId());
        sdQw.andLike(pageDto.getSearch(), "design_no", "style_no", "style_name");
        sdQw.notEmptyEq("devt_type", pageDto.getDevtType());
        sdQw.orderByDesc("create_date");

        // 数据权限
        dataPermissionsService.getDataPermissionsForQw(sdQw, DataPermissionsBusinessTypeEnum.packDesign.getK());
        Page<Style> page = PageHelper.startPage(pageDto);
        styleService.list(sdQw);
        PageInfo<StylePackInfoListVo> pageInfo = CopyUtil.copy(page.toPageInfo(), StylePackInfoListVo.class);
        //查询bom列表
        List<StylePackInfoListVo> sdpList = pageInfo.getList();
        if (CollUtil.isNotEmpty(sdpList)) {
            //图片
            attachmentService.setListStylePic(sdpList, "stylePic");
            List<String> sdIds = sdpList.stream().map(StylePackInfoListVo::getId).collect(Collectors.toList());

            Map<String, List<PackInfoListVo>> piMaps = queryListToMapGroupByForeignId(sdIds, PackUtils.PACK_TYPE_DESIGN);
            for (StylePackInfoListVo sd : sdpList) {
                List<PackInfoListVo> packInfoListVos = piMaps.get(sd.getId());
                sd.setPackInfoList(packInfoListVos);
                if (CollUtil.isNotEmpty(packInfoListVos)) {
                    for (PackInfoListVo packInfoListVo : packInfoListVos) {
                        packInfoListVo.setStylePic(sd.getStylePic());
                    }
                }
            }
        }
        return pageInfo;
    }

    @Override
    public PageInfo<PackInfoListVo> pageInfo(PackInfoSearchPageDto pageDto) {
        BaseQueryWrapper<PackInfo> qw = new BaseQueryWrapper<>();
        qw.notEmptyEq("foreign_id", pageDto.getStyleId());
        qw.notEmptyEq("pack_type", PackUtils.PACK_TYPE_DESIGN);
        qw.orderByDesc("id");
        Page<PackInfoListVo> objects = PageHelper.startPage(pageDto);
        List<PackInfoListVo> list = getBaseMapper().queryByQw(qw);
        List<String> stringList = list.stream().map(PackInfoListVo::getId).collect(Collectors.toList());
        Map<String, String> map = packBomService.getPackSendStatus(stringList);
        list.forEach(p -> {
            /*0:全部下发 1:全部未下发 2:部分下发 null:无物料清单*/
            p.setScmSendFlag(map.get(p.getId()));
        });
        attachmentService.setListStylePic(list, "stylePic");
        return objects.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean setPatternNo(PackInfoSetPatternNoDto dto) {
        PackInfo packInfo = new PackInfo();
        packInfo.setPatternNo(dto.getPatternNo());
        packInfo.setPatternMakingId(dto.getPatternMakingId());
        UpdateWrapper<PackInfo> uw = new UpdateWrapper<>();
        uw.lambda().eq(PackInfo::getId, dto.getPackId());
        PackInfo pack =new PackInfo();
        pack.setPatternNo(dto.getPatternNo());
        packInfo.setPatternNo(dto.getPatternNo());
        this.saveOperaLog("关联样板号", dto.getName(),null, dto.getCode(),pack,new PackInfo());
        return update(packInfo, uw);
    }


    @Override
    public PackInfoListVo createByStyle(CreatePackInfoByStyleDto dto) {
        Style style = styleService.getById(dto.getId());
        if (style == null) {
            throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
        }
        PackInfo packInfo = BeanUtil.copyProperties(style, PackInfo.class, "id", "status");
        CommonUtils.resetCreateUpdate(packInfo);
        String newId = IdUtil.getSnowflake().nextIdStr();
        packInfo.setId(newId);
        packInfo.setForeignId(dto.getId());

        //设置编码
        QueryWrapper codeQw = new QueryWrapper();
        codeQw.eq("foreign_id", dto.getId());
        long count = getBaseMapper().countByQw(codeQw);
        packInfo.setCode(style.getDesignNo() + StrUtil.DASHED + (count + 1));
        packInfo.setName(Opt.ofBlankAble(dto.getName()).orElse(packInfo.getCode()));
        packInfo.setPatternNo(dto.getPatternNo());
        packInfo.setPatternMakingId(dto.getPatternMakingId());
        packInfo.setStyleId(style.getId());
        save(packInfo);
        //新建bom版本
        PackBomVersionDto versionDto = BeanUtil.copyProperties(packInfo, PackBomVersionDto.class, "id");
        versionDto.setPackType(PackUtils.PACK_TYPE_DESIGN);
        versionDto.setForeignId(newId);
        // 新建资料包状态表
        packInfoStatusService.newStatus(newId, PackUtils.PACK_TYPE_DESIGN);
        PackBomVersionVo packBomVersionVo = packBomVersionService.saveVersion(versionDto);
        packBomVersionService.enable(BeanUtil.copyProperties(packBomVersionVo, PackBomVersion.class));
        //新建尺码表配置
        packSizeConfigService.createByStyle(newId, PackUtils.PACK_TYPE_DESIGN, style);
        try {
            // 保存款式设计详情颜色
            PackInfoDto packInfoDto = new PackInfoDto();
            packInfoDto.setId(style.getId());
            packInfoDto.setSourcePackType(PackUtils.PACK_TYPE_STYLE);
            packInfoDto.setTargetPackType(PackUtils.PACK_TYPE_DESIGN);
            packInfoDto.setTargetForeignId(packInfo.getId());
            styleInfoColorService.insertStyleInfoColorList(packInfoDto);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("保存款式设计详情颜色错误信息如下：{}" + e);
        }
        if (null == dto.getIsWithBom() || !dto.getIsWithBom()) {
            PackInfoListVo packInfoListVo = BeanUtil.copyProperties(getById(packInfo.getId()), PackInfoListVo.class);
            packInfoListVo.setPackType(PackUtils.PACK_TYPE_DESIGN);
            return packInfoListVo;
        }

        //如果勾选了关联款式BOM物料信息则复制保存款式设计中的bom信息
        List<PackBom> bomList = packBomService.list(style.getId(), PackUtils.PACK_TYPE_STYLE);
        if (CollectionUtil.isNotEmpty(bomList)) {
            //保存bom尺码跟颜色
            List<String> bomIdList = bomList.stream().map(PackBom::getId).collect(Collectors.toList());
            QueryWrapper queryWrapper =  new QueryWrapper<>();
            queryWrapper.in("bom_id",bomIdList);
            List<PackBomSize> bomSizeList = packBomSizeService.list(queryWrapper);
            Map<String, List<PackBomSize>> bomSizeMap = bomSizeList.stream().collect(Collectors.groupingBy(PackBomSize::getBomId));
            List<PackBomColor> bomColorList = packBomColorService.list(style.getId(), PackUtils.PACK_TYPE_STYLE);
            Map<String, List<PackBomColor>> bomColorMap = bomColorList.stream().collect(Collectors.groupingBy(PackBomColor::getBomId));

            // 物料清单转仓库配置项， 开：默认勾选主面料逻辑
            String mainFlag = ccmFeignService.getSwitchByCode(MATERIAL_CREATE_PURCHASEDEMAND.getKeyCode()) ? "1" : "0";
            for (PackBom bom : bomList) {
                String bomId = IdUtil.getSnowflake().nextIdStr();
                List<PackBomSize> bomSizes = bomSizeMap.get(bom.getId());
                if (CollectionUtil.isNotEmpty(bomSizes)) {
                    for (PackBomSize bomSize : bomSizes) {
                        bomSize.setId(null);
                        bomSize.setBomId(bomId);
                        bomSize.setPackType(PackUtils.PACK_TYPE_DESIGN);
                        bomSize.setForeignId(newId);
                        bomSize.setBomVersionId(packBomVersionVo.getId());
                        bomSize.updateInit();
                    }
                }
                List<PackBomColor> bomColors = bomColorMap.get(bom.getId());
                if (CollectionUtil.isNotEmpty(bomColors)) {
                    for (PackBomColor bomColor : bomColors) {
                        bomColor.setId(null);
                        bomColor.setBomId(bomId);
                        bomColor.setPackType(PackUtils.PACK_TYPE_DESIGN);
                        bomColor.setForeignId(newId);
                        bomColor.setBomVersionId(packBomVersionVo.getId());
                        bomColor.updateInit();
                    }
                }
                bom.setId(bomId);
                bom.setBomVersionId(packBomVersionVo.getId());
                bom.setForeignId(packInfo.getId());
                bom.setPackType(PackUtils.PACK_TYPE_DESIGN);
                bom.setStageFlag(PackUtils.PACK_TYPE_DESIGN);
                bom.setMainFlag(mainFlag);
                bom.updateInit();
            }
            packBomService.saveBatch(bomList);
            if (CollectionUtil.isNotEmpty(bomColorList)) {
                packBomColorService.saveBatch(bomColorList);
            }
            if (CollectionUtil.isNotEmpty(bomSizeList)) {
                packBomSizeService.saveBatch(bomSizeList);
            }
        }

        PackInfoListVo packInfoListVo = BeanUtil.copyProperties(getById(packInfo.getId()), PackInfoListVo.class);
        packInfoListVo.setPackType(PackUtils.PACK_TYPE_DESIGN);
        return packInfoListVo;
    }

    @Override
    public Map<String, List<PackInfoListVo>> queryListToMapGroupByForeignId(List<String> foreignIds, String packType) {
        QueryWrapper<PackInfo> qw = new QueryWrapper<>();
        qw.in("foreign_id", foreignIds);
        qw.eq("pack_type", packType);
        List<PackInfoListVo> list = getBaseMapper().queryByQw(qw);
        return Opt.ofNullable(list).map(l -> l.stream().collect(Collectors.groupingBy(PackInfoListVo::getForeignId))).orElse(MapUtil.empty());
    }

    @Override
    public PageInfo<OperaLogEntity> operationLog(PackCommonPageSearchDto pageDto) {
        QueryWrapper<OperaLogEntity> qw = new QueryWrapper<>();
        qw.eq("parent_id", pageDto.getForeignId());
        qw.eq("path", pageDto.getPackType());
        qw.orderByDesc("id");
        Page<OperaLogEntity> objects = PageHelper.startPage(pageDto);
        operaLogService.list(qw);
        return objects.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean toBigGoods(PackCommonSearchDto dto) {
        //查看资料包信息是否存在
        PackInfo packInfo = this.getById(dto.getForeignId());
        if (packInfo == null) {
            throw new OtherException("资料包信息不存在");
        }
        //查看是否在设计阶段
        PackInfoStatus packDesignStatus = packInfoStatusService.get(dto.getForeignId(), PackUtils.PACK_TYPE_DESIGN);
        if (!StrUtil.equals(packDesignStatus.getBomStatus(), BasicNumber.ZERO.getNumber())) {
            throw new OtherException("只有在设计阶段才能转大货");
        }
        //查看版本是否锁定
        PackBomVersion version = packBomVersionService.getEnableVersion(dto);
        if (version == null) {
            throw new OtherException("无物料清单版本");
        }
        if (!StrUtil.equals(version.getLockFlag(), BaseGlobal.YES)) {
            throw new OtherException("物料清单版本未锁定");
        }
        //无配色信息
        if (ccmFeignService.getSwitchByCode(STYLE_MANY_COLOR.getKeyCode()) && ccmFeignService.getSwitchByCode(DESIGN_BOM_TO_BIG_GOODS_CHECK_SWITCH.getKeyCode()) && StringUtils.isAnyBlank(packInfo.getStyleNo(), packInfo.getColor(), packInfo.getStyleColorId())) {
            throw new OtherException("没有配色信息");
        }
        /*判断物料是否全部下发*/
        Boolean issuedToExternalSmpSystemSwitch = ccmFeignService.getSwitchByCode(ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH.getKeyCode());
        if (issuedToExternalSmpSystemSwitch) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("foreign_id", version.getForeignId());
            queryWrapper.eq("pack_type", PackUtils.PACK_TYPE_DESIGN);
            queryWrapper.eq("stage_flag", dto.getPackType());
            queryWrapper.eq("bom_version_id", version.getId());
            queryWrapper.in("scm_send_flag", StringUtils.convertList("0,2,3"));
            List<PackBom> packBomList = packBomService.list(queryWrapper);
            if (CollUtil.isNotEmpty(packBomList)) {
                throw new OtherException("物料清单存在未下发数据");
            }
        }


        if (ccmFeignService.getSwitchByCode(DESIGN_BOM_TO_BIG_GOODS_IS_ONLY_ONCE_SWITCH.getKeyCode()) && YesOrNoEnum.YES.getValueStr().equals(packDesignStatus.getBomStatus())) {
            throw new OtherException("已转大货，不可重复转入");
        }
        Date nowDate = new Date();
        copyPack(dto.getForeignId(), dto.getPackType(), dto.getForeignId(), PackUtils.PACK_TYPE_BIG_GOODS, BasicNumber.ONE.getNumber());
        //设置为已转大货
        packDesignStatus.setBomStatus(BasicNumber.ONE.getNumber());
        packDesignStatus.setToBigGoodsDate(nowDate);
        packInfoStatusService.updateById(packDesignStatus);

        PackInfoStatus packInfoStatus = packInfoStatusService.get(dto.getForeignId(), PackUtils.PACK_TYPE_BIG_GOODS);
        //设置为已转大货
        packInfoStatus.setBomStatus(BasicNumber.ONE.getNumber());
        packInfoStatus.setDesignTechConfirm(BasicNumber.ONE.getNumber());
        packInfoStatus.setToBigGoodsDate(nowDate);
        packInfoStatusService.updateById(packInfoStatus);
        //updateById(packInfo);
        //设置bom 状态
        changeBomStatus(packInfo.getId(), BasicNumber.ONE.getNumber());
        /*配色下发*/
        dataUpdateScmService.updateStyleColorSend(packInfo.getStyleNo());
        /*xiao消息*/
        messageUtils.toBigGoodsSendMessage(packInfo.getPlanningSeasonId(), packInfo.getDesignNo(), baseController.getUser());
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void changeBomStatus(String packInfoId, String bomStatus) {
        PackInfo byId = getById(packInfoId);
        //修改配色的bom 状态
        if (byId == null) {
            return;
        }
        String styleColorId = byId.getStyleColorId();
        if (StrUtil.isBlank(styleColorId)) {
            return;
        }
        StyleColor styleColor = new StyleColor();
        styleColor.setBomStatus(bomStatus);
        UpdateWrapper<StyleColor> uw = new UpdateWrapper<>();
        uw.eq("id", styleColorId);
        styleColorMapper.update(styleColor, uw);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean copyPack(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String flg) {
        //图样附件、物料清单、尺寸表、工序工价、核价信息、工艺说明、样衣评审、业务意见、吊牌洗唛

        //图样附件
        attachmentService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //物料清单
        packBomVersionService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType, flg);
        //尺寸表
        packSizeConfigService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        packSizeService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //工序工价
        packProcessPriceService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //核价信息
        // 基础
        packPricingService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        // 二次加工费
        packPricingCraftCostsService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //加工费
        packPricingProcessCostsService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        // 其他费用
        packPricingOtherCostsService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //工艺说明
        packTechSpecService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        // 工艺说明包装方式
        packTechPackagingService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //样衣评审
        packSampleReviewService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //业务意见
        packBusinessOpinionService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);

        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean startReverseApproval(String id) {
        PackInfo pack = getById(id);
        if (pack == null) {
            throw new OtherException("资料包数据不存在,请先保存");
        }
        PackInfoStatus packInfoStatus = packInfoStatusService.get(id, PackUtils.PACK_TYPE_BIG_GOODS);
        if (StrUtil.equals(packInfoStatus.getReverseConfirmStatus(), BaseGlobal.STOCK_STATUS_WAIT_CHECK)) {
            throw new OtherException("不能重复反审");
        }
        if (!StrUtil.equals(packInfoStatus.getBomStatus(), BasicNumber.ONE.getNumber())) {
            throw new OtherException("只有在大货阶段才能反审");
        }

        packInfoStatus.setReverseConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
        packInfoStatusService.updateById(packInfoStatus);
        Map<String, Object> variables = BeanUtil.beanToMap(pack);
        boolean flg = flowableService.start(FlowableService.big_goods_reverse + "[" + pack.getCode() + "]",
                FlowableService.big_goods_reverse, id,
                "/pdm/api/saas/packInfo/reverseApproval",
                "/pdm/api/saas/packInfo/reverseApproval",
                "/pdm/api/saas/packInfo/reverseApproval",
                StrUtil.format("/styleManagement/dataPackage?id={}&styleId={}&style={}", pack.getId(), pack.getForeignId(), pack.getDesignNo()),
                variables);
        return true;
    }

    @Override
    public boolean startApproval(String id) {
        PackInfo pack = getById(id);
        if (pack == null) {
            throw new OtherException("资料包数据不存在,请先保存");
        }
        PackInfoStatus packInfoStatus = packInfoStatusService.get(id, PackUtils.PACK_TYPE_BIG_GOODS);
        packInfoStatus.setConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
        packInfoStatusService.updateById(packInfoStatus);
        Map<String, Object> variables = BeanUtil.beanToMap(pack);
        boolean flg = flowableService.start(FlowableService.big_goods_reverse + "[" + pack.getCode() + "]",
                FlowableService.big_goods_reverse, id,
                "/pdm/api/saas/packInfo/approval",
                "/pdm/api/saas/packInfo/approval",
                "/pdm/api/saas/packInfo/approval",
                StrUtil.format("/styleManagement/dataPackage?id={}&styleId={}&style={}", pack.getId(), pack.getForeignId(), pack.getDesignNo()),
                variables);

        return true;
    }

    @Override
    public boolean approval(AnswerDto dto) {
        PackInfo packInfo = getById(dto.getBusinessKey());
        if (packInfo != null) {
            PackInfoStatus packInfoStatus = packInfoStatusService.get(dto.getBusinessKey(), PackUtils.PACK_TYPE_BIG_GOODS);
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                packInfoStatus.setConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);
                packInfoStatus.setPostTechConfirm(BaseGlobal.YES);
                packInfoStatus.setPostTechConfirmDate(new Date());
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                packInfoStatus.setConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
                packInfoStatus.setPostTechConfirm(BaseGlobal.NO);
            } else {
                packInfoStatus.setConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);
            }
            packInfoStatusService.updateById(packInfoStatus);
        }
        return true;
    }

    @Override
    public PageInfo<PricingSelectListVO> pricingSelectList(PricingSelectSearchDTO pricingSelectSearchDTO) {
        Page<PricingSelectListVO> voPage = PageHelper.startPage(pricingSelectSearchDTO);
        super.getBaseMapper().pricingSelectList(pricingSelectSearchDTO);
        return CopyUtil.copy(voPage.toPageInfo(), PricingSelectListVO.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public AttachmentVo genTechSpecFile(PackCommonSearchDto dto) {
        //获取款式设计id
        PackInfo byId = this.getById(dto.getForeignId());
        Map<String, String> params = new HashMap<>(12);
        params.put("styleId", byId.getForeignId());
        params.put("foreignId", dto.getForeignId());
        params.put("packType", dto.getPackType());
        // 下载文件并上传到minio
        AttachmentVo attachmentVo = ureportService.downFileAndUploadMinio(UreportDownEnum.PDF, "process", "工艺单", byId.getCode() + ".pdf", params);
        // 将文件id保存到状态表
        PackInfoStatus packInfoStatus = packInfoStatusService.get(dto.getForeignId(), dto.getPackType());
        packInfoStatus.setTechSpecFileId(attachmentVo.getFileId());
        packInfoStatusService.updateById(packInfoStatus);
        return attachmentVo;
    }

    @Override
    public GenTechSpecPdfFile queryGenTechSpecPdfFile(GroupUser groupUser, PackCommonSearchDto dto) {
        //获取资料包信息
        PackInfoListVo detail = getDetail(dto.getForeignId(), dto.getPackType());

        if (detail == null) {
            throw new OtherException("获取资料包数据失败");
        }
        //获取款式信息
        Style style = styleService.getById(detail.getStyleId());

        if (style == null) {
            throw new OtherException("获取款式信息失败");
        }

        if (groupUser == null) {
            groupUser = new GroupUser();
            groupUser.setUsername(style.getCreateId());
            groupUser.setName(style.getCreateName());
        }
        GenTechSpecPdfFile vo = new GenTechSpecPdfFile();
        String objectFileName = minioConfig.getDir() + "/" + DateUtils.getDate() + "/" + System.currentTimeMillis() + ".pdf";
        vo.setObjectFileName(objectFileName);
        //二维码url
        String fileWebUrl = baseFrontEndAddress + "/techSpecView?foreignId=" + dto.getForeignId() + "&packType=" + dto.getPackType() + "&userId=" + groupUser.getId();
        System.out.println(fileWebUrl);
        System.out.println(URLUtil.encode(fileWebUrl));
        String qrCodeUrl = baseRequestUrl + "/pdm/api/open/qrCode?content=" + URLUtil.encodeAll(fileWebUrl);
        vo.setQrCodeUrl(qrCodeUrl);
        System.out.println("qrCodeUrl:" + qrCodeUrl);
        // 获取吊牌信息
        if (StrUtil.isNotBlank(detail.getStyleNo())) {
            HangTagVO tag = hangTagService.getDetailsByBulkStyleNo(detail.getStyleNo(), style.getCompanyCode(), null);
            if (tag != null) {
                BeanUtil.copyProperties(tag, vo);
            }
        }
        if (StrUtil.isNotBlank(vo.getStylePic()) && !StrUtil.contains(vo.getStylePic(), "http")) {
            vo.setStylePic(uploadFileService.getUrlById(vo.getStylePic()));
        }

        //图片
        if (StrUtil.isNotBlank(detail.getStyleColorId())) {
            StyleColor styleColor = styleColorMapper.selectById(detail.getStyleColorId());
            if (styleColor != null && StrUtil.isNotBlank(styleColor.getStyleColorPic())) {
                String styleNoImgUrl = StyleNoImgUtils.getStyleNoImgUrl(groupUser, styleColor.getStyleColorPic());
                vo.setStylePic(styleNoImgUrl);
            }
        }
        PackSizeConfigVo sizeConfig = packSizeConfigService.getConfig(dto.getForeignId(), dto.getPackType());
        vo.setCompanyName("意丰歌集团有限公司");
        vo.setBrandName(style.getBrandName());
        vo.setDesignNo(style.getDesignNo());
        vo.setStyleNo(detail.getStyleNo());
        vo.setDesigner(style.getDesigner());
        vo.setDefaultSize(sizeConfig.getDefaultSize());
        vo.setActiveSizes(sizeConfig.getActiveSizes());
        vo.setPatternDesignName(style.getPatternDesignName());
        vo.setSizeRangeName(style.getSizeRangeName());
        vo.setProdCategoryName(style.getProdCategoryName());
        vo.setWashSkippingFlag(sizeConfig.getWashSkippingFlag());
        PackTechSpecSearchDto techSearch = BeanUtil.copyProperties(dto, PackTechSpecSearchDto.class);
        //工艺信息
        vo.setTechSpecVoList(packTechSpecService.list(techSearch));
        //图片
        vo.setPicList(packTechSpecService.picList(techSearch));
        //尺寸表
        List<PackSize> sizeList = packSizeService.list(dto.getForeignId(), dto.getPackType());
        Date newDate = Opt.ofNullable(detail.getTechSpecVideo()).map(video -> video.getCreateDate()).orElse(new Date());
        vo.setCreateDate(DateUtil.format(newDate, "yy/M/d"));
        vo.setCreateTime(DateUtil.format(newDate, "a HH:mm"));
        vo.setSizeList(BeanUtil.copyToList(sizeList, PackSizeVo.class));
        return vo;
    }

    @Override
    public AttachmentVo genTechSpecFile2(GroupUser groupUser, PackCommonSearchDto dto) {
        GenTechSpecPdfFile vo = queryGenTechSpecPdfFile(groupUser, dto);
        ByteArrayOutputStream gen = vo.gen();
        String fileName = Opt.ofBlankAble(vo.getStyleNo()).orElse(vo.getPackCode()) + ".pdf";
        try {
            MockMultipartFile mockMultipartFile = new MockMultipartFile(fileName, fileName, FileUtil.getMimeType(fileName), new ByteArrayInputStream(gen.toByteArray()));
            AttachmentVo attachmentVo = uploadFileService.uploadToMinio(mockMultipartFile, vo.getObjectFileName());
            // 将文件id保存到状态表
            PackInfoStatus packInfoStatus = packInfoStatusService.get(dto.getForeignId(), dto.getPackType());
            packInfoStatus.setTechSpecFileId(attachmentVo.getFileId());
            packInfoStatusService.updateById(packInfoStatus);
            return attachmentVo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new OtherException("生成工艺文件失败");
        }

    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean copyItems(PackCopyDto dto) {

        if (StrUtil.isNotBlank(dto.getStyleColorId())) {
            //查询资料包信息
            QueryWrapper<PackInfo> packQw = new QueryWrapper();
            packQw.eq("style_color_id", dto.getStyleColorId());
            packQw.eq("pack_type", PackUtils.PACK_TYPE_DESIGN);
            List<PackInfoListVo> packInfoListVos = baseMapper.queryByQw(packQw);
            if (CollUtil.isEmpty(packInfoListVos)) {
                throw new OtherException("找不到款式配色的BOM信息");
            }
            PackInfoListVo packInfoListVo = packInfoListVos.get(0);
            dto.setSourceForeignId(packInfoListVo.getId());
            dto.setSourcePackType(packInfoListVo.getPackType());
        }


        if (StrUtil.contains(dto.getItem(), "物料清单")) {
            packBomVersionService.copy(dto.getSourceForeignId(), dto.getSourcePackType(), dto.getTargetForeignId(), dto.getTargetPackType());
        }
        if (StrUtil.contains(dto.getItem(), "尺寸表")) {
            packSizeService.copy(dto.getSourceForeignId(), dto.getSourcePackType(), dto.getTargetForeignId(), dto.getTargetPackType());
            packSizeConfigService.copy(dto.getSourceForeignId(), dto.getSourcePackType(), dto.getTargetForeignId(), dto.getTargetPackType());
        }

        if (StrUtil.contains(dto.getItem(), "工艺说明")) {
            attachmentService.copy(dto.getSourceForeignId(), dto.getSourcePackType(), dto.getTargetForeignId(), dto.getTargetPackType());
            packTechSpecService.copy(dto.getSourceForeignId(), dto.getSourcePackType(), dto.getTargetForeignId(), dto.getTargetPackType());
            packTechPackagingService.copy(dto.getSourceForeignId(), dto.getSourcePackType(), dto.getTargetForeignId(), dto.getTargetPackType());
        }
        return true;
    }

    @Override
    public BomPrintVo getBomPrint(GroupUser user, PackCommonSearchDto dto) {
        BomPrintVo vo = new BomPrintVo();
        //获取资料包信息
        PackInfoListVo detail = getDetail(dto.getForeignId(), dto.getPackType());

        if (detail == null) {
            throw new OtherException("获取资料包数据失败");
        }
        vo.setApparelLabels(detail.getApparelLabels());
        vo.setSpecNotice(detail.getSpecNotice());
        vo.setSpecialSpecComments(detail.getSpecialSpecComments());
        //获取款式信息
        Style style = styleService.getById(detail.getForeignId());
        if (style == null) {
            throw new OtherException("获取款式信息失败");
        }
        String stylePicId = style.getStylePic();
        // 获取吊牌信息
        if (StrUtil.isNotBlank(detail.getStyleNo())) {
            HangTagVO tag = hangTagService.getDetailsByBulkStyleNo(detail.getStyleNo(), getCompanyCode(), null);
            if (tag != null) {
                BeanUtil.copyProperties(tag, vo);
            }
        }
        if (StrUtil.isNotBlank(stylePicId)) {
            vo.setStylePic(uploadFileService.getUrlById(stylePicId));

        }
        StyleColor styleColor = styleColorMapper.selectById(detail.getStyleColorId());

        if (styleColor != null) {
            //图片
            if (StrUtil.isNotBlank(styleColor.getStyleColorPic())) {
                String styleNoImgUrl = StyleNoImgUtils.getStyleNoImgUrl(user, styleColor.getStyleColorPic());
                vo.setStylePic(styleNoImgUrl);
            }
            vo.setIsMainly(styleColor.getIsMainly());
            vo.setBandName(styleColor.getBandName());
        }
        vo.setDefaultSize(style.getDefaultSize());
        vo.setBrandName(style.getBrandName());
        vo.setStyleNo(detail.getStyleNo());
        vo.setDesigner(CollUtil.getFirst(StrUtil.split(style.getDesigner(), CharUtil.COMMA)));
        vo.setDesignNo(style.getDesignNo());
        //获取物料信息
        List<PackBomVo> enableVersionBomList = packBomVersionService.getEnableVersionBomList(dto.getForeignId(), dto.getPackType());
        vo.setBomList(enableVersionBomList.stream().filter(item -> StrUtil.equals(item.getUnusableFlag(), BaseGlobal.NO)).collect(Collectors.toList()));
        vo.setCreateName(user.getName());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean updatePackInfoStatusField(PackInfoStatusVo dto) {
        PackInfoStatus packInfoStatus = packInfoStatusService.get(dto.getForeignId(), dto.getPackType());
        BeanUtil.copyProperties(dto, packInfoStatus);
        packInfoStatusService.updateById(packInfoStatus);
        return true;
    }

    @Override
    public AttachmentVo saveVideoFile(String foreignId, String packType, String fileId) {
        PackInfoStatus packInfoStatus = packInfoStatusService.get(foreignId, packType);
        UpdateWrapper<PackInfoStatus> uw = new UpdateWrapper<>();
        uw.lambda().eq(PackInfoStatus::getId, packInfoStatus.getId())
                .set(PackInfoStatus::getTechSpecVideoFileId, Opt.ofBlankAble(fileId).orElse(""));
        packInfoStatusService.update(uw);
        if (StrUtil.isBlank(fileId)) {
            return null;
        }
        return attachmentService.getAttachmentByFileId(fileId);
    }



    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean removeByIds(RemoveDto removeDto){
        List<String> stringList = StrUtil.split(removeDto.getIds(), ',');
        List<PackInfo> packInfoList = baseMapper.selectBatchIds(stringList);
        List<PackInfo> packInfoList1 = packInfoList.stream().filter(p -> StrUtil.isNotBlank(p.getStyleNo())).collect(Collectors.toList());
        if (!CollUtil.isEmpty(packInfoList1)) {
            throw new OtherException("资料包存在关联大货数据");
        }
        baseMapper.deleteBatchIds(stringList);
        /*日志记录*/
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        List<String> ids = Arrays.asList(removeDto.getIds().split(","));
        operaLogEntity.setName(removeDto.getName());
        operaLogEntity.setType("删除");
        operaLogEntity.setContent(removeDto.getIds());
        operaLogEntity.setDocumentName(removeDto.getNames());
        operaLogEntity.setParentId(removeDto.getParentId());
        operaLogEntity.setDocumentCode(removeDto.getCodes());
        operaLogService.save(operaLogEntity);
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackInfoListVo copyBom(CopyBomDto dto) {
        // 1 先新建一个资料包
        PackInfoListVo byStyle = createByStyle(dto);
        // 2 拷贝资料包数据
        copyPack(dto.getSourceForeignId(), dto.getSourcePackType(), byStyle.getId(), byStyle.getPackType(), BasicNumber.ZERO.getNumber());

        // 3 将状态表还原
        PackInfoStatus packInfoStatus = packInfoStatusService.get(byStyle.getId(), byStyle.getPackType());
        packInfoStatus.setBomStatus(BasicNumber.ZERO.getNumber());
        packInfoStatus.setScmSendFlag(BasicNumber.ZERO.getNumber());
        packInfoStatusService.updateById(packInfoStatus);
        return byStyle;
    }

    @Override
    public boolean delTechSpecFile(PackCommonSearchDto dto) {
        UpdateWrapper qw = new UpdateWrapper();
        PackUtils.commonQw(qw, dto);
        qw.set("tech_spec_file_id", null);
        packInfoStatusService.update(qw);
        return true;
    }

    @Override
    public PricingVO getPricingVoById(String id) {
        return this.getBaseMapper().getPricingVoById(id);
    }

    /**
     * 样衣id查询bom
     *
     * @param designNo
     * @return
     */
    @Override
    public PageInfo<PackInfoListVo> getInfoListByDesignNo(String designNo) {
        if (StringUtils.isBlank(designNo)) {
            throw new OtherException("缺少设计款号");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("design_no", designNo);
        List<PackInfoListVo> basicsdatumModelTypeList = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), PackInfoListVo.class);
        List<Style> style = styleService.list(queryWrapper);
        if (!CollectionUtils.isEmpty(basicsdatumModelTypeList) && !CollectionUtils.isEmpty(style)) {
            basicsdatumModelTypeList.forEach(b -> {
                b.setStylePic(uploadFileService.getUrlById(style.get(0).getStylePic()));
            });
        }
        PageInfo<PackInfoListVo> pageInfo = new PageInfo<>(basicsdatumModelTypeList);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean association(PackInfoAssociationDto dto) {
        PackInfo packInfo = getById(dto.getPackId());
        if (packInfo == null) {
            throw new OtherException("资料包数据为空");
        }
        StyleColor color = styleColorMapper.selectById(dto.getStyleColorId());
        if (color == null) {
            throw new OtherException("配色数据为空");
        }
        packInfo.setName(color.getStyleNo());
        packInfo.setColor(color.getColorName());
        packInfo.setColorCode(color.getColorCode());
        packInfo.setStyleNo(color.getStyleNo());
        packInfo.setStyleColorId(dto.getStyleColorId());
        updateById(packInfo);
        Map<String,String> map = new HashMap<>();
        map.put("大货款号", "->"+packInfo.getStyleNo());
        this.saveOperaLog("关联大货款号",dto.getName(),packInfo.getName() ,packInfo.getCode(),map);
        color.setBom(packInfo.getCode());
        styleColorMapper.updateById(color);
        return true;
    }

    /**
     * 取消关联配色
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean cancelAssociation(PackInfoAssociationDto dto) {
        PackInfo packInfo = getById(dto.getPackId());
        if (StringUtils.isBlank(packInfo.getStyleNo())) {
            throw new OtherException("未关联款号");
        }
        /*查询bom下的物料是否存在下发*/
        QueryWrapper qw = new QueryWrapper();
        qw.eq("foreign_id", packInfo.getId());
        qw.eq("pack_type", "packDesign");
        qw.in("scm_send_flag", StringUtils.convertList("1,3"));
        List<PackBom> packBomList = packBomService.list(qw);
        if (CollUtil.isNotEmpty(packBomList)) {
            throw new OtherException("物料清单存在已下发数据无法取消");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("style_no", packInfo.getStyleNo());
        StyleColor color = styleColorMapper.selectOne(queryWrapper);
        /*验证是否下发*/
        if (!color.getScmSendFlag().equals(BaseGlobal.NO)) {
            throw new OtherException("数据存在已下发");
        }
        packInfo.setColor("");
        packInfo.setColorCode("");
        packInfo.setStyleNo("");
        packInfo.setStyleColorId("");
        packInfo.setName(packInfo.getDesignNo()+ packInfo.getStyleName()+" BOM");
        updateById(packInfo);
        color.setBom("");
        styleColorMapper.updateById(color);
        Map<String,String> map = new HashMap<>();
        //map.put("大货款号", "->"+packInfo.getStyleNo());
        this.saveOperaLog("取消关联大货款号",dto.getName(),packInfo.getName() ,packInfo.getCode(),map);
        return true;
    }

    @Override

    @Transactional(rollbackFor = {Exception.class})
    public boolean reverseApproval(AnswerDto dto) {
        PackInfo packInfo = getById(dto.getBusinessKey());
        if (packInfo != null) {
            PackInfoStatus bigGoodsPs = packInfoStatusService.get(dto.getBusinessKey(), PackUtils.PACK_TYPE_BIG_GOODS);
            PackInfoStatus designPs = packInfoStatusService.get(dto.getBusinessKey(), PackUtils.PACK_TYPE_DESIGN);
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                Date nowDate = new Date();
                copyPack(dto.getBusinessKey(), PackUtils.PACK_TYPE_BIG_GOODS, dto.getBusinessKey(), PackUtils.PACK_TYPE_DESIGN, BasicNumber.TWO.getNumber());
                bigGoodsPs.setReverseConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);
                bigGoodsPs.setScmSendFlag(BaseGlobal.NO);
                // bom阶段设置为样衣阶段
                bigGoodsPs.setBomStatus(BasicNumber.ZERO.getNumber());
                bigGoodsPs.setDesignTechConfirm(BasicNumber.ZERO.getNumber());
                designPs.setBomStatus(BasicNumber.ZERO.getNumber());
                //设置时间
                bigGoodsPs.setToDesignDate(nowDate);
                designPs.setToDesignDate(nowDate);
                //设置bom 状态
                changeBomStatus(packInfo.getId(), BasicNumber.ZERO.getNumber());
                PackBomVersion enableVersion = packBomVersionService.getEnableVersion(packInfo.getId(), PackUtils.PACK_TYPE_DESIGN);
 /*               *//*反审后物料清单的状态改为可编辑*//*
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.set("scm_send_flag",BaseGlobal.IN_READY);
                updateWrapper.eq("foreign_id",packInfo.getId());
                updateWrapper.eq("pack_type", PackUtils.PACK_TYPE_DESIGN);
                updateWrapper.eq("bom_version_id",enableVersion.getId());
                packBomService.update(updateWrapper);*/

            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                bigGoodsPs.setReverseConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
            } else {
                bigGoodsPs.setConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);
            }
            packInfoStatusService.updateById(bigGoodsPs);
            packInfoStatusService.updateById(designPs);
        }
        return true;
    }

    @Override
    public List<PackInfoListVo> queryByQw(QueryWrapper queryWrapper) {
        return getBaseMapper().queryByQw(queryWrapper);
    }

    @Override
    public PackInfoListVo getByQw(QueryWrapper queryWrapper) {
        List<PackInfoListVo> packInfoListVos = queryByQw(queryWrapper);
        return CollUtil.getFirst(packInfoListVos);
    }

    @Override
    public PageInfo<BigGoodsPackInfoListVo> pageByBigGoods(PackInfoSearchPageDto pageDto) {
        BaseQueryWrapper<PackInfo> sdQw = new BaseQueryWrapper<>();
        sdQw.notEmptyEq("bom_status", pageDto.getBomStatus());
        sdQw.eq("pack_type", PackUtils.PACK_TYPE_BIG_GOODS);
        sdQw.notEmptyEq("prod_category1st", pageDto.getProdCategory1st());
        sdQw.notEmptyEq("prod_category", pageDto.getProdCategory());
        sdQw.notEmptyEq("prod_category2nd", pageDto.getProdCategory2nd());
        sdQw.notEmptyEq("prod_category3rd", pageDto.getProdCategory3rd());
        sdQw.notEmptyEq("planning_season_id", pageDto.getPlanningSeasonId());
        sdQw.andLike(pageDto.getSearch(), "design_no", "style_no", "style_name");
        sdQw.notEmptyEq("devt_type", pageDto.getDevtType());
        sdQw.orderByDesc("create_date");
        // 数据权限
        dataPermissionsService.getDataPermissionsForQw(sdQw, DataPermissionsBusinessTypeEnum.packBigGoods.getK());
        Page<PackInfoListVo> page = PageHelper.startPage(pageDto);
//        list(sdQw);
        List<PackInfoListVo> packInfoListVos = queryByQw(sdQw);
        attachmentService.setListStylePic(packInfoListVos, "stylePic");
        PageInfo<BigGoodsPackInfoListVo> pageInfo = CopyUtil.copy(page.toPageInfo(), BigGoodsPackInfoListVo.class);
        return pageInfo;
    }

    @Override
    public PackInfoListVo getByQw(String foreignId, String packType) {
        QueryWrapper<PackInfo> qw = new QueryWrapper<>();
        qw.eq("foreign_id", foreignId);
        qw.eq("pack_type", packType);
        qw.last("limit 1");
        List<PackInfoListVo> packInfoListVos = baseMapper.queryByQw(qw);
        return CollUtil.getFirst(packInfoListVos);
    }

    @Override
    public PackInfoListVo getDetail(String id, String packType) {
        QueryWrapper<PackInfo> qw = new QueryWrapper<>();
        qw.in("id", id);
        qw.eq("pack_type", packType);
        qw.last("limit 1");
        List<PackInfoListVo> packInfoListVos = getBaseMapper().queryByQw(qw);
        PackInfoListVo packInfoListVo = CollUtil.get(packInfoListVos, 0);
        if (packInfoListVo != null && StrUtil.isNotBlank(packInfoListVo.getTechSpecFileId())) {
            packInfoListVo.setTechSpecFile(attachmentService.getAttachmentByFileId(packInfoListVo.getTechSpecFileId()));
        }
        if (packInfoListVo != null && StrUtil.isNotBlank(packInfoListVo.getTechSpecVideoFileId())) {
            packInfoListVo.setTechSpecVideo(attachmentService.getAttachmentByFileId(packInfoListVo.getTechSpecVideoFileId()));
        }
        return packInfoListVo;
    }

    @Override
    String getModeName() {
        return "资料包明细";
    }


    @Override
    public List<OpenStyleDto> getStyleListForLinkMore(String companyCode) {
        //资料包主表
        QueryWrapper<PackInfo> qwPack = new QueryWrapper<>();
        qwPack.eq("company_code",companyCode);
        //todo 待删除
//        qwPack.eq("id","1698881540366368768");
//        qwPack.eq("id","1701427517884424192");
//        qwPack.eq("id","1701774217148821504");
//        qwPack.eq("id","1703672791017074688");
        //查询前一个小时的数据
        qwPack.last(" AND update_date BETWEEN NOW() - INTERVAL 1 HOUR AND NOW()");
        List<PackInfo> packList = this.list(qwPack);
        if (packList.size() == 0){
            return null;
        }
        List<String> packIds = new ArrayList<>();
        List<String> pIds = new ArrayList<>();
        Map<String, PackInfo> packInfoMap = new HashMap<>();
        for (PackInfo packInfo : packList) {
            packIds.add(packInfo.getForeignId());
            pIds.add(packInfo.getId());
            packInfoMap.put(packInfo.getForeignId(),packInfo);
        }

        //款式主表
        List<Style> styleList = styleService.listByIds(packIds);

        //查询款式主图
        List<String> stylePicIds = new ArrayList<>();
        Map<String, String> fileMap = new HashMap<>();
        for (Style s : styleList) {
            List<String> picList = StringUtils.convertList(s.getStylePic());
            if (picList != null && picList.size() > 0){
                stylePicIds.add(picList.get(0));
            }
        }
        if (stylePicIds.size() > 0){
            List<UploadFile> fileList = uploadFileService.listByIds(stylePicIds);
            fileMap = fileList.stream().collect(Collectors.toMap(f -> f.getId(), f -> f.getUrl(), (f1, f2) -> f2));

        }

        //查询条件
        List<String> codeList = new ArrayList<>();
        List<OpenStyleDto> styleDtoList = new ArrayList<>();
        Map<String, OpenStyleDto> styleDtoMap = Maps.newHashMap();
        List<String> allProductSizeList = new ArrayList<>();
        for (Style style : styleList) {
            OpenStyleDto dto = new OpenStyleDto();
            style.setStylePic(fileMap.get(style.getStylePic()));
            dto.init(style);
            codeList.add(style.getDesignNo());
            styleDtoList.add(dto);
            dto.setId(packInfoMap.get(style.getId()).getId());
            styleDtoMap.put(dto.getId(), dto);
            allProductSizeList.add(style.getProductSizes()+",");

            //尺码编码数据
            QueryWrapper<BasicsdatumSize> qwSizeCode = new QueryWrapper<>();
            qwSizeCode.eq("company_code",companyCode);
            qwSizeCode.like("model_type",style.getSizeRangeName());
            qwSizeCode.in("hangtags",StringUtils.convertList(style.getProductSizes()));
            Map<String, String> sizeMap = basicsdatumSizeService.list(qwSizeCode)
                    .stream().collect(Collectors.toMap(s -> s.getHangtags(), s -> s.getCode(), (s1, s2) -> s2));
            dto.setSizeMap(sizeMap);
        }


        //sku数据
        QueryWrapper<StyleInfoSku> qwSku = new QueryWrapper<>();
        qwSku.eq("company_code",companyCode);
        qwSku.eq("pack_type","packBigGoods");
        qwSku.in("foreign_id",pIds);
        Map<String, List<StyleInfoSku>> skuMap = styleInfoSkuService.list(qwSku)
                .stream().collect(Collectors.groupingBy(s -> s.getForeignId()));
        skuMap.forEach((k,v)->{
            if (!CollectionUtils.isEmpty(v)){
                if (styleDtoMap.get(k) != null){
                    styleDtoMap.get(k).initSku(v,styleDtoMap.get(k).getCode());
                }
            }
        });

        //skc
        QueryWrapper<StyleInfoColor> qwSkc = new QueryWrapper<>();
        qwSkc.eq("company_code",companyCode);
        qwSkc.eq("pack_type","packBigGoods");
        qwSkc.in("foreign_id",pIds);
        Map<String, List<StyleInfoColor>> skcMap = styleInfoColorService.list(qwSkc)
                .stream().collect(Collectors.groupingBy(s -> s.getForeignId()));
        skcMap.forEach((k,v)->{
            if (!CollectionUtils.isEmpty(v) && styleDtoMap.get(k) != null){
                styleDtoMap.get(k).initSkc(v);
            }
        });

        //吊牌列表
        QueryWrapper<HangTag> qwTag = new QueryWrapper<>();
        qwTag.in("bulk_style_no",codeList);
        Map<String, HangTag> tagMap = hangTagService.list(qwTag)
                .stream().collect(Collectors.toMap(t -> t.getBulkStyleNo(), t -> t, (t1, t2) -> t2));
        if(tagMap.size() > 0){
            for (OpenStyleDto styleDto : styleDtoList) {
                if (tagMap.get(styleDto.getCode()) != null){
                    styleDto.setHangTag(tagMap.get(styleDto.getCode()));
                }
            }
        }

        return styleDtoList;
    }
// 自定义方法区 不替换的区域【other_end】

}
