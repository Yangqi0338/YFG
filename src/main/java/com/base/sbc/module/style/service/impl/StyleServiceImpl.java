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
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.CustomStylePicUpload;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.constant.RFIDProperties;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.*;
import com.base.sbc.module.band.service.BandService;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumModelTypeMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.formtype.dto.QueryFieldOptionConfigDto;
import com.base.sbc.module.formtype.entity.FieldOptionConfig;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FieldOptionConfigService;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.formtype.vo.FieldOptionConfigVo;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomSize;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackBomSizeService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.entity.*;
import com.base.sbc.module.planning.mapper.PlanningDemandMapper;
import com.base.sbc.module.planning.service.*;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.base.sbc.module.planning.vo.*;
import com.base.sbc.module.purchase.entity.MaterialStock;
import com.base.sbc.module.purchase.service.MaterialStockService;
import com.base.sbc.module.sample.dto.SampleAttachmentDto;
import com.base.sbc.module.sample.vo.MaterialVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.smp.dto.PlmStyleSizeParam;
import com.base.sbc.module.style.dto.*;
import com.base.sbc.module.style.entity.*;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.mapper.StyleMapper;
import com.base.sbc.module.style.service.*;
import com.base.sbc.module.style.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：款式设计 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.service.SampleService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-9 11:16:15
 */
@Service
public class StyleServiceImpl extends BaseServiceImpl<StyleMapper, Style> implements StyleService {

    protected static Logger logger = LoggerFactory.getLogger(StyleServiceImpl.class);

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
    private MinioUtils minioUtils;
    @Autowired
    private StylePicUtils stylePicUtils;

    @Autowired
    private StylePicService stylePicService;
    @Autowired
    private PlanningCategoryItemService planningCategoryItemService;

    @Autowired
    private StyleColorMapper styleColorMapper;

    @Autowired
    CcmFeignService ccmFeignService;
    @Autowired
    AmcFeignService amcFeignService;
    @Autowired
    private BasicsdatumModelTypeService basicsdatumModelTypeService;
    @Autowired
    private FieldManagementService fieldManagementService;
    @Autowired
    private PlanningDimensionalityService planningDimensionalityService;
    @Autowired
    private FieldValService fieldValService;
    @Autowired
    private BandService bandService;
    @Autowired
    private PackBomService packBomService;
    @Autowired
    private DataPermissionsService dataPermissionsService;
    @Autowired
    private BasicsdatumModelTypeMapper basicsdatumModelTypeMapper;
    @Lazy
    @Resource
    private SmpService smpService;

    @Autowired
    private ColorPlanningService colorPlanningService;
    @Autowired
    private ThemePlanningService themePlanningService;
    @Autowired
    private PlanningDemandMapper planningDemandMapper;
    @Autowired
    private PlanningDemandProportionDataService planningDemandProportionDataService;
    @Resource
    private StyleInfoColorService styleInfoColorService;
    @Resource
    private StyleInfoSkuService styleInfoSkuService;
    @Autowired
    private CcmService ccmService;

    @Autowired
    private FieldOptionConfigService fieldOptionConfigService;

    @Autowired
    private PackBomSizeService packBomSizeService;

    @Autowired
    private PlanningChannelService planningChannelService;

    @Autowired
    private CustomStylePicUpload customStylePicUpload;

    @Autowired
    private MaterialStockService materialStockService;

    @Autowired
    private UserUtils userUtils;

    private IdGen idGen = new IdGen();

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public Style saveStyle(StyleSaveDto dto) {
        Style style = null;
        CommonUtils.removeQuerySplit(dto, ",", "patternPartsPic");
        CommonUtils.removeQuery(dto, "stylePic");

        if (CommonUtils.isInitId(dto.getId())) {
            style = saveNewStyle(dto);
        } else {
            style = getById(dto.getId());
            String oldDesignNo = style.getDesignNo();
            this.saveOperaLog("修改", "款式设计", style.getStyleName(), style.getDesignNo(), dto, style);
            resetDesignNo(dto, style);

            /*判断产品季是否有变化*/
            if(!StrUtil.equals(dto.getPlanningSeasonId(),style.getPlanningSeasonId())  ){
                /*产品季有变化并且没有旧产品季*/
                PlanningSeason planningSeason = planningSeasonService.getById(dto.getPlanningSeasonId());
                dto.setYear(planningSeason.getYear());
                dto.setYearName(planningSeason.getYearName());
                dto.setSeason(planningSeason.getSeason());
                dto.setSeasonName(planningSeason.getSeasonName());
                dto.setBrand(planningSeason.getBrand());
                dto.setBrandName(planningSeason.getBrandName());

                if(StrUtil.isBlank(style.getOldPlanningSeasonId())){
                    dto.setOldPlanningSeasonId(style.getPlanningSeasonId());
                }
            }
            BeanUtil.copyProperties(dto, style);
            setMainStylePic(style, dto.getStylePicList());

            this.updateById(style);
            reviseAllDesignNo(oldDesignNo, style.getDesignNo());
            planningCategoryItemService.updateBySampleDesignChange(style);
            //修改生产类型
            baseMapper.changeDevtType(style.getId(), style.getDevtType(), style.getDevtTypeName());

        }

        //是否同步SCM
        boolean isPushScm = false;

        //添加打标逻辑 - markingType 默认为空时和设计阶段，打标逻辑一致，如果维度数据全部填写则全部打标，否则部分打标，全部未填写时为未打标
        if(StrUtil.isEmpty(dto.getMarkingType())){
            // 保存工艺信息
            fieldValService.save(style.getId(), FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY, dto.getTechnologyInfo());
        }else{
            // 保存下单阶段 打标状态
            LambdaUpdateWrapper<StyleColor> styleColorUpdateWrapper = new LambdaUpdateWrapper<>();
            styleColorUpdateWrapper.set(StyleColor::getOrderMarkingStatus,dto.getOrderMarkingStatus());
            styleColorUpdateWrapper.eq(StyleColor::getId,dto.getStyleColorId());
            styleColorMapper.update(null,styleColorUpdateWrapper);

            String plateType = "";
            String GarmentWash = "";
            for (FieldVal fieldVal : dto.getTechnologyInfo()) {
                if("plateType".equals(fieldVal.getFieldName())){
                    isPushScm = true;
                    plateType = fieldVal.getVal();
                }else if("GarmentWash".equals(fieldVal.getFieldName())){
                    isPushScm = true;
                    GarmentWash = fieldVal.getVal();
                }
            }
            //判断是否修改了  水洗字段和自主研发版型  字段 如有有修改则触发同步SCM
            if(isPushScm){
                List<FieldVal> fvList = fieldValService.list(dto.getStyleColorId(), FieldValDataGroupConstant.STYLE_MARKING_ORDER);
                if(CollUtil.isEmpty(fvList)){
                    fvList = fieldValService.list(style.getId(), FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);;
                }
                Map<String, String> oldFvMap = fvList.stream().collect(Collectors.toMap(FieldVal::getFieldName, FieldVal::getVal,(a, b) -> b));
                if(plateType.equals(oldFvMap.getOrDefault("plateType","")) && GarmentWash.equals(oldFvMap.getOrDefault("GarmentWash",""))){
                    isPushScm = false;
                }
            }

            // 保存下单阶段工艺信息
            fieldValService.save(dto.getStyleColorId(), FieldValDataGroupConstant.STYLE_MARKING_ORDER, dto.getTechnologyInfo());
        }

        // 附件信息
        saveFiles(style.getId(), dto.getAttachmentList(), AttachmentTypeConstant.SAMPLE_DESIGN_FILE_ATTACHMENT);

        // 保存审批同意图
        saveFiles(style.getId(), dto.getAttachmentList1(), AttachmentTypeConstant.SAMPLE_DESIGN_FILE_APPROVE_PIC);
        // 保存图片信息
        if (!customStylePicUpload.isOpen()) {
            saveFiles(style.getId(), dto.getStylePicList(), AttachmentTypeConstant.SAMPLE_DESIGN_FILE_STYLE_PIC);
        }


        //保存关联的素材库
        planningCategoryItemMaterialService.saveMaterialList(dto);
//        try {
//            // 是否开启单款多色开关
//            Boolean ifSwitch = ccmFeignService.getSwitchByCode(CcmBaseSettingEnum.STYLE_MANY_COLOR.getKeyCode());
//            if (ifSwitch) {
//                // 保存款式设计详情颜色
//                this.saveBomInfoColorList(dto);
//            }
//        } catch (Exception e) {
//            logger.error(" 是否开启单款多色开关/保存款式设计详情颜色异常报错如下：" , e);
//        }
        if(isPushScm && StrUtil.isNotBlank(dto.getStyleColorId())){
            //判断款式配色 同步状态，如果不能同步则报错

            List<StyleColor> styleColorList = styleColorMapper.getStyleMainAccessories(Collections.singletonList(dto.getStyleColorId()));
            /*查询配色是否下发*/
            if (CollectionUtils.isEmpty(styleColorList)) {
                throw new OtherException("该大货款号已经同步，请在款式配色解锁后保存下发");
            }
            StyleColorService styleColorService = SpringContextHolder.getBean(StyleColorService.class);
            try {
                styleColorService.issueScm(dto.getStyleColorId());
            }catch (Exception e){
                log.error("同步SCM失败",e);
                throw new OtherException("同步SCM失败："+e.getMessage());
            }
        }

        return style;
    }

    /**
     * 保存款式设计详情颜色
     *
     * @param packInfoDto 资料包DTO
     * @return 款式设计详情颜色列表
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public List<StyleInfoColorVo> saveBomInfoColorList(PackInfoDto packInfoDto) {
        if (null == packInfoDto || null == packInfoDto.getStyleInfoColorDtoList()) {
            throw new OtherException("款式设计详情-颜色数据为空！！！");
        }

        List<StyleInfoColor> styleInfoColors = BeanUtil.copyToList(packInfoDto.getStyleInfoColorDtoList(), StyleInfoColor.class);
        List<StyleInfoColor> colorCodeList =
                styleInfoColorService.list(new QueryWrapper<StyleInfoColor>().in("color_code",
                                styleInfoColors.stream().map(StyleInfoColor::getColorCode).collect(Collectors.toList()))
                        .eq("foreign_id", packInfoDto.getId()).eq("pack_type", packInfoDto.getPackType()));
        if (CollectionUtil.isNotEmpty(colorCodeList)) {
            String colorNames = colorCodeList.stream().map(StyleInfoColor::getColorName).collect(Collectors.joining(BaseGlobal.D));
            throw new OtherException(colorNames + "已添加颜色，请勿重新添加");
        }

        // 初始化数据
        styleInfoColors.forEach(styleInfoColor -> {
            styleInfoColor.insertInit();
        });
        // 保存款式设计详情颜色
        boolean flg = styleInfoColorService.saveBatch(styleInfoColors);
        if (!flg) {
            throw new OtherException("新增款式设计详情颜色失败，请联系管理员");
        }
        // 添加颜色为大货资料包类型 才会生成SKU信息
        if (null != packInfoDto.getPackType() && PackUtils.PACK_TYPE_BIG_GOODS.equals(packInfoDto.getPackType())) {
            // 保存款式设计SKU
            List<StyleInfoSku> styleInfoSkuList = new ArrayList<>();
            // 拼接款式SKU数据： 颜色多个尺码
            styleInfoColors.forEach(styleInfoColor -> {
                // 取款式尺码编码
                List<String> sizeCodeList = com.base.sbc.config.utils.StringUtils.convertList(packInfoDto.getSizeCodes());
                // 取除款式尺码
                List<String> productSizeList = com.base.sbc.config.utils.StringUtils.convertList(packInfoDto.getProductSizes());
                // 拼接款式SKU ：颜色code + 尺码code
                for (int i = 0; i < sizeCodeList.size(); i++) {
                    // 尺码code
                    String sizeCode = sizeCodeList.get(i);
                    // 尺码名称
                    String sizeName = null != productSizeList.get(i) ? productSizeList.get(i) : "";
                    // SKU ：颜色code + - + 尺码code
                    String skuCode = packInfoDto.getDesignNo() + styleInfoColor.getColorCode() + sizeCode;
                    StyleInfoSku styleInfoSku = new StyleInfoSku();
                    styleInfoSku.setForeignId(styleInfoColor.getForeignId());
                    styleInfoSku.setPackType(styleInfoColor.getPackType());
                    styleInfoSku.setSkuCode(skuCode);
                    styleInfoSku.setColorCode(styleInfoColor.getColorCode());
                    styleInfoSku.setColorName(styleInfoColor.getColorName());
                    styleInfoSku.setSizeCode(sizeCode);
                    styleInfoSku.setSizeName(sizeName);
                    styleInfoSku.setCostPrice(styleInfoColor.getSkcCostPrice());
                    styleInfoSku.setTagPrice(styleInfoColor.getTagPrice());
                    styleInfoSku.insertInit();
                    styleInfoSkuList.add(styleInfoSku);
                }
            });
            // 保存款式SKU数据
            boolean skuFlg = styleInfoSkuService.saveBatch(styleInfoSkuList);
            if (!skuFlg) {
                throw new OtherException("新增款式SKU失败，请联系管理员");
            }
        }
        // 修改颜色codes、颜色名称
        Style style = new Style();
        style.setId(packInfoDto.getForeignId());
        style.setColorCodes(packInfoDto.getColorCodes());
        style.setProductColors(packInfoDto.getProductColors());
        baseMapper.updateById(style);
        return BeanUtil.copyToList(styleInfoColors, StyleInfoColorVo.class);
    }

    @Override
    public void updateProductCost(String id, BigDecimal productCost) {
        Style style = new Style();
        style.setId(id);
        style.setProductCost(productCost);
        style.updateInit();
        super.updateById(style);
    }

    private void resetDesignNo(StyleSaveDto dto, Style db) {
        boolean initId = CommonUtils.isInitId(dto.getId());
        if (StrUtil.isBlank(dto.getDesignNo()) && !initId) {
            throw new OtherException("设计款号不能为空");
        }
        /*判断款号是否有改动*/
        if (StrUtil.equals(dto.getOldDesignNo(), dto.getDesignNo())) {
            String newDesignNo = PlanningUtils.getNewDesignNo(dto.getDesignNo(), db.getDesigner(), dto.getDesigner());
            dto.setDesignNo(newDesignNo);
        } else {
            String prefix = PlanningUtils.getDesignNoPrefix(db.getDesignNo(), db.getDesigner());
            if (StrUtil.startWith(dto.getDesignNo(), prefix)) {
                String newDesignNo = PlanningUtils.getNewDesignNo(dto.getDesignNo(), db.getDesigner(), dto.getDesigner());
                dto.setDesignNo(newDesignNo);
            } else {
                // QueryWrapper qw = new QueryWrapper();
                //qw.eq("design_no", dto.getDesignNo());
                //qw.ne(!initId, "id", dto.getId());
                // long count = count(qw);
                //if (count > 0) {
                //throw new OtherException("设计款号重复");
                // }
            }

        }


    }


    public void setMainStylePic(Style style, List<SampleAttachmentDto> stylePicList) {
        if (CollUtil.isNotEmpty(stylePicList)) {
            style.setStylePic(stylePicList.get(0).getFileId());
        } else {
            style.setStylePic("");
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

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public Style saveNewStyle(StyleSaveDto dto) {

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

        if (ObjectUtil.isEmpty(planningSeason)) {
            throw new OtherException("产品季为空");
        }


        // 新增坑位信息
        PlanningCategoryItem categoryItem = new PlanningCategoryItem();
        BeanUtil.copyProperties(dto, categoryItem, "id");
        categoryItem.setPlanningSeasonId(planningSeason.getId());
        if (CollUtil.isNotEmpty(dto.getStylePicList())) {
            String urlById = uploadFileService.getUrlById(dto.getStylePicList().get(0).getFileId());
            categoryItem.setStylePic(urlById);
        }
        categoryItem.setStatus("2");
        categoryItem.setDesigner(userInfo.getAliasUserName() + StrUtil.COMMA + userInfo.getUserCode());
        categoryItem.setDesignerId(userInfo.getUserId());
        categoryItem.setMaterialCount(new BigDecimal(String.valueOf(CollUtil.size(dto.getMaterialList()))));
        categoryItem.setHisDesignNo(dto.getHisDesignNo());
        // 设置款号
        String designNo = planningCategoryItemService.getNextCode(dto);
        if (StrUtil.isBlank(designNo)) {
            throw new OtherException("款号生成失败");
        }
        designNo = designNo + userInfo.getUserCode();
        categoryItem.setDesignNo(designNo);
        categoryItem.setOldDesignNo(designNo);
        planningCategoryItemService.save(categoryItem);

        // 新增款式设计
        Style style = BeanUtil.copyProperties(dto, Style.class);
        PlanningUtils.toSampleDesign(style, planningSeason, categoryItem);
        setMainStylePic(style, dto.getStylePicList());
        save(style);
        dto.setPlanningCategoryItemId(categoryItem.getId());
        dto.setPlanningSeasonId(planningSeason.getId());
        return style;
    }


    @Override
    public PageInfo queryPageInfo(Principal user, StylePageDto dto) {
        String companyCode = getCompanyCode();
        GroupUser userBy = userUtils.getUserBy(user);
        String userId = getUserId();
        BaseQueryWrapper<Style> qw = new BaseQueryWrapper<>();
        qw.and(StrUtil.isNotEmpty(dto.getSearch()), i -> i.like("design_no", dto.getSearch()).or().like("his_design_no", dto.getSearch()));
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getDesignerId()), "designer_id", dto.getDesignerId());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "season", dto.getSeason());
        qw.in(StrUtil.isNotBlank(dto.getStatus()), "status", StrUtil.split(dto.getStatus(), CharUtil.COMMA));
        qw.in(StrUtil.isNotBlank(dto.getKitting()), "kitting", StrUtil.split(dto.getKitting(), CharUtil.COMMA));
        qw.eq(StrUtil.isNotBlank(dto.getProdCategory3rd()), "prod_category3rd", dto.getProdCategory3rd());
        qw.eq(StrUtil.isNotBlank(dto.getProdCategory()), "prod_category", dto.getProdCategory());
        qw.eq(StrUtil.isNotBlank(dto.getProdCategory1st()), "prod_category1st", dto.getProdCategory1st());
        qw.like(StrUtil.isNotBlank(dto.getDesignNo()), "design_no", dto.getDesignNo());
        qw.in(StrUtil.isNotBlank(dto.getBandCode()), "band_code", StrUtil.split(dto.getBandCode(), StrUtil.COMMA));
        qw.in(StrUtil.isNotBlank(dto.getDesignerIds()), "designer_id", com.base.sbc.config.utils.StringUtils.convertList(dto.getDesignerIds()));
        qw.eq(StrUtil.isNotBlank(dto.getDevtType()), "devt_type", dto.getDevtType());
        qw.eq(StrUtil.isNotBlank(dto.getPlanningSeasonId()), "planning_season_id", dto.getPlanningSeasonId());
        if (StrUtil.isNotBlank(dto.getEnableStatus()) && !StrUtil.equals(dto.getEnableStatus(), BaseGlobal.STOCK_STATUS_CHECKED)) {
            qw.eq("enable_status", dto.getEnableStatus());
        } else {
            qw.eq("enable_status", BaseGlobal.NO);
        }
        qw.eq("del_flag", BaseGlobal.NO);
        if (!StringUtils.isEmpty(dto.getIsTrim())) {
            if (dto.getIsTrim().equals(BaseGlobal.STATUS_NORMAL)) {
                /*查询配饰*/
                qw.like("category_name", "配饰");
            } else {
                /*查询主款*/
                qw.notLike("category_name", "配饰");
            }
        }
        qw.eq(BaseConstant.COMPANY_CODE, companyCode);
        if(StrUtil.isNotBlank(dto.getOrderStatus())){
            if("1".equals(dto.getOrderStatus())){
                qw.exists("SELECT 1 FROM t_style_color WHERE design_no = s.design_no AND `scm_send_flag` = '1' AND `status` = '0' AND order_flag = '1'");
            }else {
                qw.notExists("SELECT 1 FROM t_style_color WHERE design_no = s.design_no AND `scm_send_flag` = '1'  AND `status` = '0' AND order_flag = '1'");
            }
        }

        //1我下发的
        if (StrUtil.equals(dto.getUserType(), StylePageDto.USER_TYPE_1)) {
            qw.eq("sender", userId);
        }
        //2我创建的
        else if (StrUtil.equals(dto.getUserType(), StylePageDto.USER_TYPE_2)) {
            qw.isNullStr("sender");
            qw.eq("create_id", userId);
        }
        //3我负责的
        else if (StrUtil.equals(dto.getUserType(), StylePageDto.USER_TYPE_3)) {
            qw.and(aqw -> aqw.lambda()
                    //设计师
                    .eq(Style::getDesignerId, userId)
                    //设计工艺员id
                    .or().eq(Style::getTechnicianId, userId)
                    //材料专员id
                    .or().eq(Style::getFabDevelopeId, userId)
                    //跟款设计师Id
                    .or().eq(Style::getMerchDesignId, userId)
                    //审版设计师id
                    .or().eq(Style::getReviewedDesignId, userId)
                    //revisedDesignId
                    .or().eq(Style::getRevisedDesignId, userId)
            );

        }
        //数据权限
        dataPermissionsService.getDataPermissionsForQw(qw, dto.getBusinessType());
        //排序
        //未下发 按下发时间排序
        if (StrUtil.equals(BasicNumber.ZERO.getNumber(), dto.getStatus())) {
            dto.setOrderBy("planning_finish_date is null ,planning_finish_date asc , create_date asc ");
        }
        //已经开款 按审核时间(开款时间)
        else if (StrUtil.equals(BasicNumber.ONE.getNumber(), dto.getStatus())) {
            dto.setOrderBy("check_end_time desc  ");
        }
        //已下发打版  按下发打版时间
        else if (StrUtil.equals(BasicNumber.TWO.getNumber(), dto.getStatus())) {
            dto.setOrderBy("send_pattern_making_date desc  ");
        } else {
            qw.orderByDesc("create_date");
        }

        Page<StylePageVo> objects = PageHelper.startPage(dto);
        getBaseMapper().selectByQw(qw);
        List<StylePageVo> result = objects.getResult();
        // 设置图片
        stylePicUtils.setStylePic(result, "stylePic");

        //判断是否下单标识，一个设计款号存在多个大货款号， 只要有一个是启用且下单 的 标识下单
        if(CollUtil.isNotEmpty(result)){
            LambdaQueryWrapper<StyleColor> styleColorQuery = new LambdaQueryWrapper<>();
            styleColorQuery.in(StyleColor::getDesignNo, result.stream().map(Style::getDesignNo).distinct().collect(Collectors.toList()));
            styleColorQuery.eq(StyleColor::getScmSendFlag, "1");
            styleColorQuery.eq(StyleColor::getStatus, "0");
            styleColorQuery.eq(StyleColor::getOrderFlag, "1");
            List<StyleColor> styleColors = styleColorMapper.selectList(styleColorQuery);
            List<String> orderStyleColorList = styleColors.stream().map(StyleColor::getDesignNo).distinct().collect(Collectors.toList());
            result.forEach(o->{
                if(orderStyleColorList.contains(o.getDesignNo())){
                    o.setOrderFlag("1");
                }
            });
        }

        amcFeignService.addUserAvatarToList(result, "designerId", "aliasUserAvatar");
        return objects.toPageInfo();
    }


    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean startApproval(String id) {
        Style style = getById(id);
        if (style == null) {
            throw new OtherException("样衣数据不存在,请先保存");
        }
        if (StrUtil.equals(style.getConfirmStatus(), BaseGlobal.STOCK_STATUS_WAIT_CHECK)) {
            throw new OtherException("当前数据已在审批中");
        }
        style.setConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
        style.setCheckStartTime(new Date());
        updateById(style);
        Map<String, Object> variables = BeanUtil.beanToMap(style);
        //查询附件
        // List<AttachmentVo> attachmentVoList1 = attachmentService.findByforeignId(id, AttachmentTypeConstant.SAMPLE_DESIGN_FILE_APPROVE_PIC);
        // String url = "";
        // if (CollUtil.isNotEmpty(attachmentVoList1)) {
        //     url = attachmentVoList1.get(0).getId();
        // }
        return flowableService.start(FlowableService.SAMPLE_DESIGN_PDN + "[" + style.getDesignNo() + "]", FlowableService.SAMPLE_DESIGN_PDN, id, "/pdm/api/saas/style/approval", "/pdm/api/saas/style/approval", "/pdm/api/saas/style/approval", "/sampleClothesDesign/sampleClothingInfo?sampleDesignId=" + id, variables);
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean approval(AnswerDto dto) {
        Style style = getById(dto.getBusinessKey());
        logger.info("————————————————款式设计回调方法————————————————", JSON.toJSONString(style));
        logger.info("————————————————回调类型————————————————", dto.getApprovalType());
        if (style != null) {
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                //设置样衣未开款状态为 已开款
                if (style.getStatus().equals(BaseGlobal.STOCK_STATUS_DRAFT)) {
                    style.setStatus("1");
                    style.setCheckEndTime(new Date());
                    style.setConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);
                }
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                style.setStatus("0");
                style.setConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
            } else {
                style.setConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);
            }
            updateById(style);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean sendMaking(SendSampleMakingDto dto) {
        Style style = checkedStyleExists(dto.getId());
        style.setStatus("2");
        style.setSendPatternMakingDate(new Date());
        style.setKitting(dto.getKitting());
        updateById(style);
        return true;
    }

    @Override
    public Style checkedStyleExists(String id) {
        Style style = getById(id);
        if (style == null) {
            throw new OtherException("样衣数据不存在");
        }
        return style;
    }

    @Override
    public StyleVo getDetail(String id) {
        Style style = getById(id);
        if (style == null) {
            return null;
        }
        StyleVo sampleVo = BeanUtil.copyProperties(style, StyleVo.class);
        // mini 加密

        sampleVo.setCustomStylePicUploadOpen(customStylePicUpload.isOpen());
        //查询附件
        List<AttachmentVo> attachmentVoList = attachmentService.findByforeignId(id, AttachmentTypeConstant.SAMPLE_DESIGN_FILE_ATTACHMENT);
        sampleVo.setAttachmentList(attachmentVoList);
        //查询附件
        List<AttachmentVo> attachmentVoList1 = attachmentService.findByforeignId(id, AttachmentTypeConstant.SAMPLE_DESIGN_FILE_APPROVE_PIC);
        sampleVo.setAttachmentList1(attachmentVoList1);
        // 关联的素材库
        QueryWrapper<PlanningCategoryItemMaterial> mqw = new QueryWrapper<>();
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
        if (customStylePicUpload.isOpen()) {
            List<StylePicVo> stylePicVos = stylePicService.listByStyleId(style.getId());
            sampleVo.setStylePicList(stylePicVos);
        } else {
            List<AttachmentVo> stylePicList = attachmentService.findByforeignId(id, AttachmentTypeConstant.SAMPLE_DESIGN_FILE_STYLE_PIC);
            sampleVo.setStylePicList(BeanUtil.copyToList(stylePicList, StylePicVo.class));
        }

        sampleVo.setSeatStylePic(planningCategoryItemService.getStylePicUrlById(sampleVo.getPlanningCategoryItemId()));
        minioUtils.setObjectUrlToObject(sampleVo, "seatStylePic", "patternPartsPic");
//        //维度标签
//        sampleVo.setDimensionLabels(queryDimensionLabelsBySdId(id));

        //如果有设置系列，则同步更新坑位信息表
        if (com.base.sbc.config.utils.StringUtils.isNotBlank(style.getSeriesId()) || com.base.sbc.config.utils.StringUtils.isNotBlank(style.getSeries())) {
            UpdateWrapper<PlanningCategoryItem> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", style.getPlanningCategoryItemId());
            wrapper.set("series_id", style.getSeriesId());
            wrapper.set("series", style.getSeries());
            planningCategoryItemService.update(null, wrapper);
        }
        // 查询 款式设计详情颜色列表
        List<StyleInfoColor> styleInfoColorList = styleInfoColorService.list(new QueryWrapper<StyleInfoColor>().eq("foreign_id", id));
        if (CollectionUtil.isNotEmpty(styleInfoColorList)) {
            sampleVo.setStyleInfoColorVoList(BeanUtil.copyToList(styleInfoColorList, StyleInfoColorVo.class));
        }

        // RFID标识
        if (StrUtil.isBlank(sampleVo.getRfidFlag())) {
            sampleVo.setRfidFlag(YesOrNoEnum.NO.getValueStr());
            if (RFIDProperties.filterList.stream().anyMatch(filter-> filter.check(sampleVo.getYear(), sampleVo.getBrandName(), sampleVo.getProdCategory()))) {
                sampleVo.setRfidFlag(YesOrNoEnum.YES.getValueStr());
            }
        }

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
        List<FieldManagementVo> result = new ArrayList<>(16);
        List<String> stringList2 = new ArrayList<>();
        //1 查询企划需求管理
        DimensionLabelsSearchDto pdqw = new DimensionLabelsSearchDto();
        BeanUtil.copyProperties(dto, pdqw);
        // 查询样衣的
        DimensionalityListVo listVo = planningDimensionalityService.getDimensionalityList(pdqw);
        List<PlanningDimensionality> pdList = listVo.getPlanningDimensionalities();
        List<FieldVal> fvList = fieldValService.list(dto.getForeignId(), dto.getDataGroup());
        //款式打标-下单阶段逻辑，如果第一次查看下单阶段数据，则查询为空，复制一份设计阶段数据作为下单阶段数据
        if(StrUtil.isNotBlank(dto.getShowConfig()) && "styleMarkingOrder".equals(dto.getShowConfig())){
            List<FieldVal> fvList1 = fieldValService.list(dto.getStyleColorId(), FieldValDataGroupConstant.STYLE_MARKING_ORDER);
            if(CollUtil.isNotEmpty(fvList1)){
                fvList = fvList1;
            }
        }
        if (CollUtil.isNotEmpty(pdList)) {
            List<String> fmIds = pdList.stream().map(PlanningDimensionality::getFieldId).collect(Collectors.toList());
            List<FieldManagementVo> fieldManagementListByIds = fieldManagementService.getFieldManagementListByIds(fmIds,null,null,null);
            if (!CollectionUtils.isEmpty(fieldManagementListByIds)) {
                /*用于查询字段配置数据*/
                stringList2 = fieldManagementListByIds.stream().map(FieldManagementVo::getId).collect(Collectors.toList());
                Map<String, Integer> sortMap = pdList.stream().collect(Collectors.toMap(PlanningDimensionality::getFieldId, PlanningDimensionality::getSort, (a, b) -> b));
                CollUtil.sort(fieldManagementListByIds, (a, b) -> {
                    int n1 = MapUtil.getInt(sortMap, a.getId(), 0);
                    int n2 = MapUtil.getInt(sortMap, b.getId(), 0);
                    return NumberUtil.compare(n1, n2);
                });
                QueryFieldOptionConfigDto queryFieldOptionConfigDto = new QueryFieldOptionConfigDto();
                if (BaseGlobal.YES.equals(listVo.getCategoryFlag())) {
                    queryFieldOptionConfigDto.setProdCategory2nd(dto.getProdCategory2nd());
                } else {
                    queryFieldOptionConfigDto.setCategoryCode(dto.getProdCategory());
                }
                /*查询每个字段下的配置选项*/
                queryFieldOptionConfigDto.setBrand(dto.getBrand());
                queryFieldOptionConfigDto.setSeason(dto.getSeason());
                queryFieldOptionConfigDto.setFieldManagementIdList(stringList2);
                Map<String, List<FieldOptionConfig>> listMap = fieldOptionConfigService.getFieldConfig(queryFieldOptionConfigDto);
                /*赋值*/
                fieldManagementListByIds.forEach(i -> {
                    List<FieldOptionConfig> configList = listMap.get(i.getId());
                    if (CollUtil.isNotEmpty(configList)) {
                        i.setConfigVoList(BeanUtil.copyToList(configList, FieldOptionConfigVo.class));
                    }
                });
            }
            // [3].查询字段值
            if (CollUtil.isNotEmpty(fieldManagementListByIds) && StrUtil.isNotBlank(dto.getForeignId())) {
                fieldManagementService.conversion(fieldManagementListByIds, fvList);
                result.addAll(fieldManagementListByIds);
            }
        }
        return result;
    }

    @Override
    public List<FieldManagementVo> queryDimensionLabelsByStyle(DimensionLabelsSearchDto dto) {
        dto.setDataGroup(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
        //修改时
        if (StrUtil.isNotBlank(dto.getForeignId()) && !CommonUtils.isInitId(dto.getForeignId())) {
            Style style = getById(dto.getForeignId());
            if (style != null) {
                BeanUtil.copyProperties(style, dto);
                return queryDimensionLabels(dto);
            }
        }
        //新增时
        else if (StrUtil.isAllNotBlank(dto.getPlanningSeasonId(), dto.getChannel(), dto.getProdCategory())) {
            return queryDimensionLabels(dto);
        }
        return null;
    }

    /**
     * 查询维度标签
     *
     * @param dto
     * @return
     */
    @Override
    public Map<String,List<FieldManagementVo>> queryCoefficient(DimensionLabelsSearchDto dto) {
        dto.setCoefficientFlag(BaseGlobal.YES);
        Map<String, List<FieldManagementVo>> result = new HashMap<>();
        List<String> stringList2 = new ArrayList<>();
        //1 查询企划需求管理
        DimensionLabelsSearchDto pdqw = new DimensionLabelsSearchDto();
        BeanUtil.copyProperties(dto, pdqw);
        // 获取围度系数
        DimensionalityListVo listVo = planningDimensionalityService.getDimensionalityList(pdqw);
        List<PlanningDimensionality> pdList = listVo.getPlanningDimensionalities();

        //region 为满足业务需求重新排序
        logicSort(pdList);
        //endregion

        List<FieldVal> fvList = fieldValService.list(dto.getForeignId(), dto.getDataGroup());
        //款式打标-下单阶段逻辑，如果第一次查看下单阶段数据，则查询为空，复制一份设计阶段数据作为下单阶段数据
        if(StrUtil.isNotBlank(dto.getShowConfig()) && "styleMarkingOrder".equals(dto.getShowConfig()) && StrUtil.isNotBlank(dto.getStyleColorId())){
            List<FieldVal> fvList1 = fieldValService.list(dto.getStyleColorId(), FieldValDataGroupConstant.STYLE_MARKING_ORDER);
            if(CollUtil.isNotEmpty(fvList1)){
                fvList = fvList1;
            }
        }
        if (CollUtil.isNotEmpty(pdList)) {
            // showConfig 为空时，表示所有场景都展示，否则只有入参showConfig = 数据中showConfig时才展示
            // 展示数据根据显示配置传参进行过滤
            if (StrUtil.isNotEmpty(dto.getShowConfig())) {
                if ("styleMarkingOrder".equals(dto.getShowConfig())) {
                    pdList = pdList.stream().filter(o -> BaseGlobal.YES.equals(o.getResearchShowFlag())).collect(Collectors.toList());
                }
            }else{
                pdList = pdList.stream().filter(o -> BaseGlobal.YES.equals(o.getDesignShowFlag())).collect(Collectors.toList());
            }
            if (CollUtil.isEmpty(pdList)) {
                return result;
            }
            List<String> fmIds = pdList.stream().map(PlanningDimensionality::getFieldId).collect(Collectors.toList());
            List<FieldManagementVo> fieldManagementListByIds = fieldManagementService.getFieldManagementListByIds(fmIds,dto.getPlanningSeasonId(),dto.getProdCategory(),dto.getChannel());
            if (!CollectionUtils.isEmpty(fieldManagementListByIds)) {
                /*用于查询字段配置数据*/
                stringList2 = fieldManagementListByIds.stream().map(FieldManagementVo::getId).collect(Collectors.toList());
                /*Map<String, Integer> sortMap = pdList.stream().collect(Collectors.toMap(PlanningDimensionality::getFieldId, PlanningDimensionality::getSort, (a, b) -> b));
                CollUtil.sort(fieldManagementListByIds, (a, b) -> {
                    int n1 = MapUtil.getInt(sortMap, a.getId(), 0);
                    int n2 = MapUtil.getInt(sortMap, b.getId(), 0);
                    return NumberUtil.compare(n1, n2);
                });*/
                QueryFieldOptionConfigDto queryFieldOptionConfigDto = new QueryFieldOptionConfigDto();
                if (BaseGlobal.YES.equals(listVo.getCategoryFlag())) {
                    queryFieldOptionConfigDto.setProdCategory2nd(dto.getProdCategory2nd());
                } else {
                    queryFieldOptionConfigDto.setCategoryCode(dto.getProdCategory());
                }
               //*查询每个字段下的配置选项*//*
                queryFieldOptionConfigDto.setBrand(dto.getBrand());
                queryFieldOptionConfigDto.setSeason(dto.getSeason());
                queryFieldOptionConfigDto.setFieldManagementIdList(stringList2);
                Map<String, List<FieldOptionConfig>> listMap = fieldOptionConfigService.getFieldConfig(queryFieldOptionConfigDto);

       /*         Map<String, Map<String, String>> dictInfoToMap = new HashMap<>();
                *//*查询字段的字典和结构管理*//*
                *//*查询字典*//*
                List<FieldManagementVo> managementVoList = fieldManagementListByIds.stream().filter(f -> StrUtil.equals(f.getIsOption(), BaseGlobal.YES)).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(managementVoList)) {
                    *//*获取查询的字典*//*
                    String collect = managementVoList.stream().map(FieldManagementVo::getOptionDictKey).distinct().collect(Collectors.joining(","));
                    if (StrUtil.isNotBlank(collect)) {
                        dictInfoToMap = ccmFeignService.getDictInfoToMap(collect);
                    }
                }*/

                //*赋值*//*
                for (FieldManagementVo i : fieldManagementListByIds) {

                    List<FieldOptionConfig> configList = listMap.get(i.getId());
                    if (CollUtil.isNotEmpty(configList)) {
                        i.setConfigVoList(BeanUtil.copyToList(configList, FieldOptionConfigVo.class));
                    } else {
    /*                   当是字典时的数据
                        if (StrUtil.equals(i.getIsOption(), BaseGlobal.YES)) {
                            if (CollUtil.isNotEmpty(dictInfoToMap)) {
                                Map<String, String> map = dictInfoToMap.get(i.getOptionDictKey());
                                if(CollUtil.isNotEmpty(map)){
//                                    赋值选的数据
                                    List<FieldOptionConfigVo> list = new ArrayList<>();
                                    for (Object key : map.keySet()) {
                                        FieldOptionConfigVo fieldOptionConfigVo = new FieldOptionConfigVo();
                                        fieldOptionConfigVo.setOptionCode(key.toString());
                                        fieldOptionConfigVo.setOptionName(map.get(key));
                                        list.add(fieldOptionConfigVo);
                                    }
                                    i.setConfigVoList(list);
                                }
                            }
                        }*/
                    }
                }
            }
            // [3].查询字段值
            if (CollUtil.isNotEmpty(fieldManagementListByIds) && StrUtil.isNotBlank(dto.getForeignId())) {
                fieldManagementService.conversion(fieldManagementListByIds, fvList);
                result =  fieldManagementListByIds.stream().collect(Collectors.groupingBy(p -> p.getGroupName(), LinkedHashMap::new, Collectors.toList()));
            }
        }
        return result;

    }

    /**
     * 对维度洗漱重新排序
     * groupsort为空时，按创建时间倒序排序
     * groupsort 有值时，排在groupsort为空后面
     * @param dimensionalityList
     */
    private static void logicSort(List<PlanningDimensionality> dimensionalityList) {
        Map<String,Integer> sortMap = new HashMap<>();
        for (PlanningDimensionality planningDimensionalityVo : dimensionalityList) {
            if (StrUtil.isNotEmpty(planningDimensionalityVo.getFieldId()) && planningDimensionalityVo.getGroupSort() != null) {
                sortMap.put(planningDimensionalityVo.getFieldId(),planningDimensionalityVo.getGroupSort());
            }
        }
        for (PlanningDimensionality planningDimensionalityVo : dimensionalityList) {
            if (StrUtil.isNotEmpty(planningDimensionalityVo.getFieldId()) && planningDimensionalityVo.getGroupSort() == null) {
                Integer sort = sortMap.get(planningDimensionalityVo.getFieldId());
                if (sort != null) {
                    planningDimensionalityVo.setGroupSort(sortMap.get(planningDimensionalityVo.getFieldId()));
                }
            }
        }

        //重新排序
        dimensionalityList.sort((o1, o2) -> {
            if (o1.getGroupSort() == null) {
                return 1;
            }
            if (o2.getGroupSort() == null) {
                return 1;
            }
            if (o2.getGroupSort() == null && o1.getGroupSort() == null) {
                if(o1.getCreateDate().getTime() < o2.getCreateDate().getTime()){
                    return 1;
                }
                if(o1.getCreateDate().getTime() > o2.getCreateDate().getTime()){
                    return -1;
                }
            }
            if(o1.getGroupSort()>o2.getGroupSort()){
                return 1;
            }
            if(o1.getGroupSort()<o2.getGroupSort()){
                return -1;
            }
            return 0;
        });
    }
    /**
     * 查询围度系数
     *
     * @param dto @return
     */
    @Override
    public Map<String,List<FieldManagementVo>> queryCoefficientByStyle(DimensionLabelsSearchDto dto) {
        dto.setDataGroup(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
        //修改时
        if (StrUtil.isNotBlank(dto.getForeignId()) && !CommonUtils.isInitId(dto.getForeignId())) {
            Style style = getById(dto.getForeignId());
            if (style != null) {
                BeanUtil.copyProperties(style, dto);
                return queryCoefficient(dto);
            }
        }
        //新增时
        else if (StrUtil.isAllNotBlank(dto.getPlanningSeasonId(), dto.getChannel(), dto.getProdCategory())) {
            return queryCoefficient(dto);
        }
        return null;
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
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.ChartBar.getK());
        amcFeignService.teamAuth(qw, "planning_season_id", getUserId());
        List<ChartBarVo> chartBarVos = getBaseMapper().getBandChart(qw);
        return getChartList(chartBarVos);
    }

    @Override
    public List getCategoryChart(String category) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.in(StrUtil.isNotBlank(category), "prod_category", StrUtil.split(category, CharUtil.COMMA));
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.ProductBar.getK());
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
        QueryWrapper<Style> qhxfxqzsQw = new QueryWrapper<>();
        qhxfxqzsQw.isNotNull("sender");
        getDesignDataOverviewCommonQw(qhxfxqzsQw, timeRange);
        long qhxfxqzs = this.count(qhxfxqzsQw);
        result.put("企划下发需求总数", qhxfxqzs);
        // 设计需求总数(统计从坑位下发的数据 + 新建的数据)
        QueryWrapper<Style> sjxqzsQw = new QueryWrapper<>();
        sjxqzsQw.isNull("sender");
        getDesignDataOverviewCommonQw(sjxqzsQw, timeRange);
        long sjxqzs = this.count(sjxqzsQw);
        result.put("设计需求总数", sjxqzs);
        //未开款 状态为0
        QueryWrapper<Style> wkkQw = new QueryWrapper<>();
        wkkQw.eq("status", BasicNumber.ZERO.getNumber());
        getDesignDataOverviewCommonQw(wkkQw, timeRange);
        long wkks = this.count(wkkQw);
        result.put("未开款", wkks);

        //已开款数 状态为1
        QueryWrapper<Style> ykkQw = new QueryWrapper<>();
        ykkQw.eq("status", BasicNumber.ONE.getNumber());
        getDesignDataOverviewCommonQw(ykkQw, timeRange);
        long ykk = this.count(ykkQw);
        result.put("已开款", ykk);

        //已下发数 状态为2
        QueryWrapper<Style> yxfsQw = new QueryWrapper<>();
        yxfsQw.eq("status", BasicNumber.TWO.getNumber());
        getDesignDataOverviewCommonQw(yxfsQw, timeRange);
        long yxfs = this.count(yxfsQw);
        result.put("已下发打版", yxfs);
        return result;
    }


    @Override
    public StyleSummaryVo categoryBandSummary(Principal user, PlanningBoardSearchDto dto) {
        GroupUser userBy = userUtils.getUserBy(user);
        StyleSummaryVo vo = new StyleSummaryVo();
        //查询波段统计
        QueryWrapper brandTotalQw = new QueryWrapper();
        brandTotalQw.select("sd.band_name as name,count(1) as total");
        brandTotalQw.groupBy("sd.band_name");
        dataPermissionsService.getDataPermissionsForQw(brandTotalQw, DataPermissionsBusinessTypeEnum.StyleBoard.getK(), "sd.");
        stylePlanningCommonQw(brandTotalQw, dto);
        List<DimensionTotalVo> bandTotal = getBaseMapper().dimensionTotal(brandTotalQw);
        vo.setXList(PlanningUtils.removeEmptyAndSort(bandTotal));

        //查询品类统计
        QueryWrapper categoryQw = new QueryWrapper();
        categoryQw.select("prod_category_name as name,count(1) as total");
        categoryQw.groupBy("prod_category_name");
        dataPermissionsService.getDataPermissionsForQw(categoryQw, DataPermissionsBusinessTypeEnum.StyleBoard.getK(), "sd.");
        stylePlanningCommonQw(categoryQw, dto);
        List<DimensionTotalVo> categoryTotal = getBaseMapper().dimensionTotal(categoryQw);
        vo.setYList(PlanningUtils.removeEmptyAndSort(categoryTotal));
        //查询明细
        QueryWrapper detailQw = new QueryWrapper();
        dataPermissionsService.getDataPermissionsForQw(categoryQw, DataPermissionsBusinessTypeEnum.StyleBoard.getK(), "sd.");
        stylePlanningCommonQw(detailQw, dto);
        List<PlanningSummaryDetailVo> detailVoList = getBaseMapper().categoryBandSummary(detailQw);
        if (CollUtil.isNotEmpty(detailVoList)) {
            amcFeignService.setUserAvatarToList(detailVoList);
            stylePicUtils.setStylePic(detailVoList, "stylePic");
            Map<String, List<PlanningSummaryDetailVo>> seatData = detailVoList.stream().collect(Collectors.groupingBy(k -> k.getBandName() + StrUtil.DASHED + k.getProdCategoryName()));
            vo.setXyData(seatData);
        }
        return vo;
    }

    @Override
    public List<StyleBoardCategorySummaryVo> categorySummary(PlanningBoardSearchDto dto) {
        QueryWrapper<Style> qw = new QueryWrapper<>();
        qw.eq(StrUtil.isNotEmpty(dto.getPlanningSeasonId()), "sd.planning_season_id", dto.getPlanningSeasonId());
        qw.and(StrUtil.isNotEmpty(dto.getSearch()), i -> i.like("sd.design_no", dto.getSearch()).or().like("sd.style_no", dto.getSearch()));
        qw.in(StrUtil.isNotEmpty(dto.getBandCode()), "sd.band_code", StrUtil.split(dto.getBandCode(), CharUtil.COMMA));
        qw.in(StrUtil.isNotEmpty(dto.getMonth()), "sd.month", StrUtil.split(dto.getMonth(), CharUtil.COMMA));
        qw.in(StrUtil.isNotEmpty(dto.getProdCategory()), "sd.prod_category", StrUtil.split(dto.getProdCategory(), CharUtil.COMMA));
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.StyleBoard.getK(), "sd.");
        List<StyleBoardCategorySummaryVo> styleBoardCategorySummaryVos = getBaseMapper().categorySummary(qw);
        // 统计大类数量
        if (CollUtil.isNotEmpty(styleBoardCategorySummaryVos)) {
            Map<String, Long> category1stTotal = styleBoardCategorySummaryVos.stream().collect(Collectors.groupingBy(StyleBoardCategorySummaryVo::getProdCategory1st)).entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> {
                return v.getValue().stream().map(StyleBoardCategorySummaryVo::getSkc).reduce((a, b) -> a + b).orElse(0L);
            }));
            //反写品类名称
            Set<String> categoryIds = new HashSet<>(16);
            for (StyleBoardCategorySummaryVo vo : styleBoardCategorySummaryVos) {
                categoryIds.add(vo.getProdCategory());
                categoryIds.add(vo.getProdCategory1st());
                categoryIds.add(vo.getProdCategory2nd());
            }
            Map<String, String> categoryNames = ccmFeignService.findStructureTreeNameByCodes(CollUtil.join(categoryIds, StrUtil.COMMA), "品类");
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
        dataPermissionsService.getDataPermissionsForQw(prsQw, DataPermissionsBusinessTypeEnum.StyleBoard.getK(), "sd.");
        Long planRequirementSkc = getBaseMapper().colorCount(prsQw);
        vo.setPlanRequirementSkc(planRequirementSkc);
        //设计需求数
        QueryWrapper<CategoryStylePlanningVo> drsQw = new QueryWrapper<>();
        dataPermissionsService.getDataPermissionsForQw(drsQw, DataPermissionsBusinessTypeEnum.StyleBoard.getK(), "sd.");
        stylePlanningCommonQw(drsQw, dto);
        Long designRequirementSkc = getBaseMapper().colorCount(drsQw);
        vo.setDesignRequirementSkc(designRequirementSkc);
        vo.setBandCode(dto.getBandCode());
        vo.setBandName(bandService.getNameByCode(dto.getBandCode()));

        return vo;
    }

    /**
     * 获取产品季品类树
     *
     * @param vo
     */
    @Override
    public List<ProductCategoryTreeVo> getProductCategoryTree(ProductCategoryTreeVo vo) {
        //第一级产品季
        if (vo.getLevel() == null) {
            QueryWrapper<PlanningSeason> qc = new QueryWrapper<>();
            qc.eq("company_code", getCompanyCode());
            qc.eq("del_flag", BasicNumber.ZERO.getNumber());
            qc.select("id", "name", "season");
            qc.orderByDesc("name");
            dataPermissionsService.getDataPermissionsForQw(qc, vo.getBusinessType(), "", new String[]{"brand"}, true);
            /*查询到的产品季*/
            List<PlanningSeason> planningSeasonList = planningSeasonService.list(qc);
            if (CollUtil.isNotEmpty(planningSeasonList)) {
                List<ProductCategoryTreeVo> result = planningSeasonList.stream().map(ps -> {
                    ProductCategoryTreeVo tree = BeanUtil.copyProperties(vo, ProductCategoryTreeVo.class);
                    tree.setChildren(true);
                    tree.setLevel(0);
                    tree.setSeason(ps.getSeason());
                    tree.setPlanningSeasonId(ps.getId());
                    tree.setName(ps.getName());
                    return tree;
                }).collect(Collectors.toList());
                return result;
            }
        }
        //第二级 大类
        else if (vo.getLevel() == 0) {
            BaseQueryWrapper qw = new BaseQueryWrapper();
            getProductCategoryTreeQw(vo, qw);
            qw.select("prod_category1st_name,prod_category1st");
            qw.groupBy("prod_category1st_name,prod_category1st");
            qw.notNull("prod_category1st_name");
            dataPermissionsService.getDataPermissionsForQw(qw, vo.getBusinessType());
            List result = null;
            if (StrUtil.equals(vo.getDataForm(), "seat")) {
                result = planningCategoryItemService.listMaps(qw);
            } else {
                result = listMaps(qw);
            }
            List<ProdCategoryVo> list = BeanUtil.copyToList(result, ProdCategoryVo.class);
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().map(item -> {
                    ProductCategoryTreeVo tree = BeanUtil.copyProperties(vo, ProductCategoryTreeVo.class);
                    tree.setProdCategory1stName(item.getProdCategory1stName());
                    tree.setProdCategory1st(item.getProdCategory1st());
                    tree.setLevel(1);
                    tree.setChildren(true);
                    return tree;
                }).collect(Collectors.toList());
            }
        }
        //第3级 品类
        else if (vo.getLevel() == 1) {
            BaseQueryWrapper qw = new BaseQueryWrapper<>();
            getProductCategoryTreeQw(vo, qw);
            qw.select("prod_category_name,prod_category");
            qw.groupBy("prod_category_name,prod_category");
            qw.notNull("prod_category_name");
            List result = null;
            dataPermissionsService.getDataPermissionsForQw(qw, vo.getBusinessType());
            if (StrUtil.equals(vo.getDataForm(), "seat")) {
                result = planningCategoryItemService.listMaps(qw);
            } else {
                result = listMaps(qw);
            }
            List<ProdCategoryVo> list = BeanUtil.copyToList(result, ProdCategoryVo.class);
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().map(item -> {
                    ProductCategoryTreeVo tree = BeanUtil.copyProperties(vo, ProductCategoryTreeVo.class);
                    tree.setProdCategoryName(item.getProdCategoryName());
                    tree.setProdCategory(item.getProdCategory());
                    tree.setLevel(2);
                    tree.setChildren(false);
                    return tree;
                }).collect(Collectors.toList());
            }
        }
        return null;
    }

    /**
     * 获取产品季全品类
     *
     * @param vo
     * @return
     */
    @Override
    public List getProductAllCategory(ProductCategoryTreeVo vo) {
        QueryWrapper<PlanningSeason> qc = new QueryWrapper<>();
        qc.eq("company_code", getCompanyCode());
        qc.eq("del_flag", BasicNumber.ZERO.getNumber());
        qc.select("id", "name", "season", "brand", "brand_name");
        qc.orderByDesc("name");
        dataPermissionsService.getDataPermissionsForQw(qc, vo.getBusinessType(), "", new String[]{"brand"}, true);
        /*查询到的产品季*/
        List<PlanningSeason> planningSeasonList = planningSeasonService.list(qc);
        Snowflake idGen = IdUtil.getSnowflake();
        /*查品类*/
        List<BasicStructureTreeVo> basicStructureTreeVoList = new ArrayList<>();
        /*重新赋值新ids*/
        if (vo.getLevel() == null) {

            if (CollUtil.isNotEmpty(planningSeasonList)) {
                List<ProductCategoryTreeVo> result = planningSeasonList.stream().map(ps -> {
                    ProductCategoryTreeVo tree = BeanUtil.copyProperties(vo, ProductCategoryTreeVo.class);
                    tree.setIds(idGen.nextIdStr());
                    tree.setChildren(true);
                    tree.setLevel(0);
                    tree.setSeason(ps.getSeason());
                    tree.setBrand(ps.getBrand());
                    tree.setBrandName(ps.getBrandName());
                    tree.setPlanningSeasonId(ps.getId());
                    tree.setName(ps.getName());
                    return tree;
                }).collect(Collectors.toList());
                return result;
            }
        } else if (vo.getLevel() == 0) {
            /*获取产品季渠道*/
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("planning_season_id", vo.getPlanningSeasonId());
            List<PlanningChannel> channelList = planningChannelService.list(queryWrapper);
            if (!CollUtil.isNotEmpty(channelList)) {
                return null;
            }
            if (CollUtil.isNotEmpty(planningSeasonList)) {
                List<ProductCategoryTreeVo> result = channelList.stream().map(ps -> {
                    ProductCategoryTreeVo tree = BeanUtil.copyProperties(vo, ProductCategoryTreeVo.class);
                    tree.setId(ps.getId());
                    tree.setPlanningChannelId(ps.getId());
                    tree.setName(ps.getChannelName() + "-" + ps.getSexName() + "-" + ps.getCreateName());
                    tree.setIds(idGen.nextIdStr());
                    tree.setChannel(ps.getChannel());
                    tree.setChannelName(ps.getChannelName());
                    tree.setLevel(1);
                    tree.setPlanningSeasonId(vo.getPlanningSeasonId());

                    return tree;
                }).collect(Collectors.toList());
                return result;
            }
        } else if (vo.getLevel() == 1) {
            basicStructureTreeVoList = ccmFeignService.basicStructureTreeByCode("品类", "", "0");
            if (CollUtil.isNotEmpty(planningSeasonList)) {
                List<ProductCategoryTreeVo> result = basicStructureTreeVoList.stream().map(ps -> {
                    ProductCategoryTreeVo tree = BeanUtil.copyProperties(vo, ProductCategoryTreeVo.class);
                    tree.setId(ps.getId());
                    tree.setIds(idGen.nextIdStr());
                    tree.setProdCategory1st(ps.getValue());
                    tree.setProdCategory1stName(ps.getName());
                    tree.setLevel(2);
                    tree.setPlanningSeasonId(vo.getPlanningSeasonId());
                    tree.setName(ps.getName());
                    return tree;
                }).collect(Collectors.toList());
                return result;
            }
        } else if (vo.getLevel() == 2) {
            basicStructureTreeVoList = ccmFeignService.queryBasicStructureNextLevelList("品类", vo.getProdCategory1st(), 0);
            if (CollUtil.isNotEmpty(planningSeasonList)) {
                List<ProductCategoryTreeVo> result = basicStructureTreeVoList.stream().map(ps -> {
                    ProductCategoryTreeVo tree = BeanUtil.copyProperties(vo, ProductCategoryTreeVo.class);
                    tree.setIds(idGen.nextIdStr());
                    tree.setId(ps.getId());
                    tree.setLevel(3);
                    tree.setChildren(true);
                    tree.setProdCategory(ps.getValue());
                    tree.setProdCategoryName(ps.getName());
                    tree.setPlanningSeasonId(vo.getPlanningSeasonId());
                    tree.setName(ps.getName());
                    return tree;
                }).collect(Collectors.toList());
                return result;
            }
        } else if (vo.getLevel() == 3) {
            basicStructureTreeVoList = ccmFeignService.queryBasicStructureNextLevelList("品类", vo.getProdCategory(), 1);
            if (CollUtil.isNotEmpty(planningSeasonList)) {
                List<ProductCategoryTreeVo> result = basicStructureTreeVoList.stream().map(ps -> {
                    ProductCategoryTreeVo tree = BeanUtil.copyProperties(vo, ProductCategoryTreeVo.class);
                    tree.setIds(idGen.nextIdStr());
                    tree.setLevel(4);
                    tree.setChildren(false);
                    tree.setProdCategory2nd(ps.getValue());
                    tree.setProdCategory2ndName(ps.getName());
                    tree.setName(ps.getName());
                    return tree;
                }).collect(Collectors.toList());
                return result;
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateBySeatChange(PlanningCategoryItem item) {
        Style style = BeanUtil.copyProperties(item, Style.class, "id", "stylePic", "status");
        CommonUtils.resetCreateUpdate(style);
        UpdateWrapper<Style> uw = new UpdateWrapper<>();
        uw.eq("del_flag", BaseGlobal.NO);
        uw.eq("planning_category_item_id", item.getId());
        update(style, uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateByChannelChange(PlanningChannel channel) {
        Style item = new Style();
        item.setChannel(channel.getChannel());
        item.setChannelName(channel.getChannelName());
        item.setSex(channel.getSex());
        item.setSexName(channel.getSexName());
        UpdateWrapper<Style> uw = new UpdateWrapper<>();

        uw.eq("planning_channel_id", channel.getId());
        update(item, uw);
    }

    @Override
    public PageInfo<PackBomVo> bomList(StyleBomSearchDto dto) {
        PackBomPageSearchDto bomDto = BeanUtil.copyProperties(dto, PackBomPageSearchDto.class);
        bomDto.setForeignId(dto.getStyleId());
        bomDto.setPackType(PackUtils.PACK_TYPE_STYLE);
        PageInfo<PackBomVo> pageInfo = packBomService.pageInfo(bomDto);
        if(CollUtil.isNotEmpty(pageInfo.getList())){
            /* 获取库存 */
            if("1".equals(dto.getIsStock())){
                List<PackBomVo> list = pageInfo.getList();
                List<String> materialCodeList = list.stream().map(PackBomVo::getMaterialCode).collect(Collectors.toList());
                Map<String, MaterialStock> materialStockMap = new HashMap<>();

                BaseQueryWrapper msQw= new BaseQueryWrapper();
                msQw.in("material_code", materialCodeList);
                List<MaterialStock> materialStockList = materialStockService.list(msQw);
                for(MaterialStock materialStock : materialStockList){
                    MaterialStock oldMaterialStock = materialStockMap.get(materialStock.getMaterialCode());
                    if(oldMaterialStock == null){
                        materialStockMap.put(materialStock.getMaterialCode(), materialStock);
                    }else{
                        oldMaterialStock.setStockQuantity(BigDecimalUtil.add(oldMaterialStock.getStockQuantity(), materialStock.getStockQuantity()));
                        materialStockMap.put(materialStock.getMaterialCode(), oldMaterialStock);
                    }
                }

                for(PackBomVo packBomVo : list){
                    MaterialStock materialStock = materialStockMap.get(packBomVo.getMaterialCode());
                    if(materialStock != null){
                        packBomVo.setStockQuantity(materialStock.getStockQuantity());
                    }
                }
            }
        }
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean delBom(String id) {
        boolean b = packBomService.removeByIds(StrUtil.split(id, CharUtil.COMMA));
        return b;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean saveBom(StyleBomSaveDto dto) {
        if (StrUtil.isBlank(dto.getStyleId())) {
            throw new OtherException("款式设计id为空");
        }
        if (CollUtil.isEmpty(dto.getBomList())) {
            throw new OtherException("物料数据为空");
        }
        //覆盖
        if (StrUtil.equals(dto.getOverlayFlg(), BaseGlobal.YES)) {
            packBomService.del(dto.getStyleId(), PackUtils.PACK_TYPE_STYLE);
        }
        Style style = baseMapper.selectById(dto.getStyleId());
        List<PackBomDto> bomList = dto.getBomList();
        for (PackBomDto packBomDto : bomList) {
            packBomDto.setId(null);
            packBomDto.setForeignId(dto.getStyleId());
            packBomDto.setPackType(PackUtils.PACK_TYPE_STYLE);
            packBomDto.setUnusableFlag(BaseGlobal.NO);
        }
        List<PackBom> packBoms = BeanUtil.copyToList(bomList, PackBom.class);
        packBomService.saveBatch(packBoms);
        /*保存尺码*/
        List<String> productSizes = StrUtil.split(style.getProductSizes(), ',');
        List<String> sizeIds = StrUtil.split(style.getSizeIds(), ',');
        List<PackBomSize> packBomSizeList = new ArrayList<>();
        for (PackBom packBom : packBoms) {
            if (CollUtil.isNotEmpty(productSizes)) {
                for (int i = 0; i < productSizes.size(); i++) {
                    PackBomSize packBomSize = new PackBomSize();
                    packBomSize.setSize(CollUtil.get(productSizes, i));
                    packBomSize.setSizeId(CollUtil.get(sizeIds, i));
                    packBomSize.setBomId(packBom.getId());
                    packBomSize.setForeignId(dto.getStyleId());
                    packBomSize.setWidthCode(packBom.getTranslateCode());
                    packBomSize.setWidth(packBom.getTranslate());
                    packBomSizeList.add(packBomSize);
                }
            }
        }
        if (CollUtil.isNotEmpty(packBomSizeList)) {
            packBomSizeService.saveBatch(packBomSizeList);
        }
        return true;
    }

    @Override
    public StyleVo getDetail(String id, String historyStyleId) {
        StyleVo detail = getDetail(id);
        if (detail==null){
            return null;
        }
        detail.setColorPlanningCount(colorPlanningService.getColorPlanningCount(detail.getPlanningSeasonId()));
        detail.setThemePlanningCount(themePlanningService.getThemePlanningCount(detail.getPlanningSeasonId()));
        List<BasicsdatumModelType> basicsdatumModelTypeList = basicsdatumModelTypeService.queryByCode(detail.getCompanyCode(), detail.getSizeRange());
        if (CollectionUtil.isNotEmpty(basicsdatumModelTypeList)) {
            BasicsdatumModelType modelType = basicsdatumModelTypeList.get(0);
            detail.setSizeRangeSizes(modelType.getSize());
            detail.setSizeRangeSizeIds(modelType.getSizeIds());
            detail.setSizeRangeSizeCodes(modelType.getSizeCode());
            detail.setSizeRangeSizeRealCodes(modelType.getSizeRealCode());
        }
        if (StrUtil.isBlank(historyStyleId)) {
            return detail;
        }
        Style hisStyle = getById(historyStyleId);
        if (hisStyle == null) {
            return detail;
        }
        //大类不一样跳过
        if (!StrUtil.equals(detail.getProdCategory1st(), hisStyle.getProdCategory1st())) {
            return detail;
        }
        //品类不一样跳过
        if (!StrUtil.equals(detail.getProdCategory(), hisStyle.getProdCategory())) {
            return detail;
        }
        //中类
        detail.setProdCategory2nd(hisStyle.getProdCategory2nd());
        detail.setProdCategory2ndName(hisStyle.getProdCategory2ndName());
        // 小类
        detail.setProdCategory3rd(hisStyle.getProdCategory3rd());
        detail.setProdCategory3rdName(hisStyle.getProdCategory3rdName());
        //月份
        detail.setMonth(hisStyle.getMonth());
        detail.setMonthName(hisStyle.getMonthName());
        //波段
        detail.setBandCode(hisStyle.getBandCode());
        detail.setBandName(hisStyle.getBandName());
        //生产模式
        detail.setDevtType(hisStyle.getDevtType());
        detail.setDevtTypeName(hisStyle.getDevtTypeName());
        //主题
        detail.setSubject(hisStyle.getSubject());
        //版型
        detail.setPlateType(hisStyle.getPlateType());
        detail.setPlateTypeName(hisStyle.getPlateTypeName());
        //开发分类
        detail.setDevClass(hisStyle.getDevClass());
        detail.setDevClassName(hisStyle.getDevClassName());
        //号型
        detail.setSizeRange(hisStyle.getSizeRange());
        detail.setSizeRangeName(hisStyle.getSizeRangeName());
        //Default颜色
        detail.setDefaultColor(hisStyle.getDefaultColor());
        detail.setDefaultColorCode(hisStyle.getDefaultColorCode());
        //Default尺码
        detail.setDefaultSize(hisStyle.getDefaultSize());
        detail.setProductSizes(hisStyle.getProductSizes());
        detail.setSizeIds(hisStyle.getSizeIds());
        detail.setSizeCodes(hisStyle.getSizeCodes());
        //目标成本
        detail.setProductCost(hisStyle.getProductCost());
        //主材料
        detail.setMainMaterials(hisStyle.getMainMaterials());
        //研发材料
        detail.setRdMat(hisStyle.getRdMat());

        //打板难度
        detail.setPatDiff(hisStyle.getPatDiff());
        detail.setPatDiffName(hisStyle.getPatDiffName());
        //廓形
        detail.setSilhouette(hisStyle.getSilhouette());
        detail.setSilhouetteName(hisStyle.getSilhouetteName());
        //款式风格
        detail.setStyleFlavour(hisStyle.getStyleFlavour());
        detail.setStyleFlavourName(hisStyle.getStyleFlavourName());
        //款式定位
        detail.setPositioning(hisStyle.getPositioning());
        detail.setPositioningName(hisStyle.getPositioningName());

        /*款式来源*/
        detail.setStyleOrigin(hisStyle.getStyleOrigin());
        detail.setStyleOriginName(hisStyle.getStyleOriginName());

//        款式名称
        detail.setStyleName(hisStyle.getStyleName());
//        尺码
        detail.setSizeIds(hisStyle.getSizeIds());
        /*号型类型*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code", hisStyle.getSizeRange());
        BasicsdatumModelType basicsdatumModelType = basicsdatumModelTypeService.getOne(queryWrapper);
        detail.setSizeRangeSizes(basicsdatumModelType.getSize());
        detail.setSizeRangeSizeIds(basicsdatumModelType.getSizeIds());
        detail.setSizeRangeSizeCodes(basicsdatumModelType.getSizeCode());
        /*模板部件*/
        detail.setPatternParts(hisStyle.getPatternParts());
        detail.setPatternPartsPic(hisStyle.getPatternPartsPic());

       /*任务信息*/
/*        detail.setDesigner(hisStyle.getDesigner());
        detail.setDesignerId(hisStyle.getDesignerId());*/
/*        detail.setTechnicianId(hisStyle.getTechnicianId());
        detail.setTechnicianName(hisStyle.getTechnicianName());
        detail.setFabDevelopeId(hisStyle.getFabDevelopeId());
        detail.setFabDevelopeName(hisStyle.getFabDevelopeName());
        detail.setMerchDesignId(hisStyle.getMerchDesignId());
        detail.setMerchDesignName(hisStyle.getMerchDesignName());
        detail.setReviewedDesignId(hisStyle.getReviewedDesignId());
        detail.setReviewedDesignName(hisStyle.getReviewedDesignName());
        detail.setRevisedDesignId(hisStyle.getRevisedDesignId());
        detail.setRevisedDesignName(hisStyle.getRevisedDesignName());
        detail.setTaskLevel(hisStyle.getTaskLevel());
        detail.setTaskLevelName(hisStyle.getTaskLevelName());
        detail.setPatternDesignName(hisStyle.getPatternDesignName());
        detail.setPatternDesignId(hisStyle.getPatternDesignId());*/
        /*物料信息*/
        StyleBomSearchDto styleBomSearchDto = new StyleBomSearchDto();
        styleBomSearchDto.setStyleId(hisStyle.getId());
        List<PackBomVo> packBomVoList = bomList(styleBomSearchDto).getList();
        packBomVoList.forEach(p -> {
            p.setId(IdUtil.randomUUID());
            p.setForeignId(detail.getId());
        });
        detail.setPackBomVoList(packBomVoList);

        // mini 加密
        minioUtils.setObjectUrlToObject(detail, "seatStylePic", "patternPartsPic");
        return detail;
    }

    /**
     * 方法描述 验证款式号型类型是否可修改
     *
     * @param publicStyleColorDto
     * @return
     */
    @Override
    public Boolean checkColorSize(PublicStyleColorDto publicStyleColorDto) {
        /*查询款式下已下发的的配色*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("style_id", publicStyleColorDto.getId());
        queryWrapper.in("scm_send_flag", com.base.sbc.config.utils.StringUtils.convertList("1,3"));
        List<StyleColor> list = styleColorMapper.selectList(queryWrapper);
        Boolean b = true;
        if (!CollectionUtils.isEmpty(list)) {
            /*查询号型类型*/
            queryWrapper.clear();
            queryWrapper.eq("code", publicStyleColorDto.getSizeRange());
            BasicsdatumModelType basicsdatumModelType = basicsdatumModelTypeMapper.selectOne(queryWrapper);
            if (ObjectUtil.isEmpty(basicsdatumModelType)) {
                throw new OtherException("号型类型查询失败");
            }
            /*大货款号*/
            List<String> stringList = list.stream().map(StyleColor::getStyleNo).collect(Collectors.toList());

            for (String s : stringList) {
                PlmStyleSizeParam plmStyleSizeParam = new PlmStyleSizeParam();
                plmStyleSizeParam.setSizeCategory(publicStyleColorDto.getSizeRange());
                plmStyleSizeParam.setStyleNo(s);
                plmStyleSizeParam.setSizeNum(basicsdatumModelType.getSizeCode().split(",").length);
                b = smpService.checkStyleSize(plmStyleSizeParam);
            }
        }
        if (!b) {
            throw new OtherException("SCM系统存在投产单或备料单,不允许修改尺码组!");
        }
        return true;
    }

    @Override
    public PlanningDemandStatisticsResultVo planningDemandStatistics(String id) {
        Style style = getById(id);
        if (style == null) {
            return null;
        }
        //查询需求占比
        QueryWrapper<PlanningDemand> qw = new QueryWrapper<>();
        qw.eq("d.category_id", style.getProdCategory())
                .eq("d.company_code", getCompanyCode())
                .eq("d.planning_season_id", style.getPlanningSeasonId())
        ;
        List<PlanningDemandStatisticsVo> demands = planningDemandMapper.queryDemandStatistics(qw);

        if (CollUtil.isEmpty(demands)) {
            return null;
        }
        Map<String, List<PlanningDemandStatisticsVo>> resultMap = new LinkedHashMap<>();
        List<String> demandIds = new ArrayList<>();
        for (PlanningDemandStatisticsVo demand : demands) {
            demandIds.add(demand.getDemandId());
            resultMap.put(demand.getDemandId(), CollUtil.newArrayList(demand));
        }
        //查询款式的维度标签
        QueryWrapper<Style> styleQw = new QueryWrapper<>();
        styleQw.lambda().eq(Style::getProdCategory, style.getProdCategory())
                .eq(Style::getPlanningSeasonId, style.getPlanningSeasonId())
                .eq(Style::getCompanyCode, style.getCompanyCode());
        List<Object> styleIds = listObjs(styleQw);

        List<FieldVal> fv = fieldValService.list(new QueryWrapper<FieldVal>().lambda().in(FieldVal::getForeignId, styleIds).eq(FieldVal::getDataGroup, FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY));
        List<String> fvItem = new ArrayList<>(16);

        if (CollUtil.isNotEmpty(fv)) {
            for (FieldVal fieldVal : fv) {
                if (StrUtil.contains(fieldVal.getVal(), StrUtil.COMMA)) {
                    List<String> split = StrUtil.split(fieldVal.getVal(), StrUtil.COMMA);
                    for (String s : split) {
                        fvItem.add(fieldVal.getFieldName() + StrUtil.DASHED + s);
                    }
                } else {
                    fvItem.add(fieldVal.getFieldName() + StrUtil.DASHED + fieldVal.getVal());
                }
            }
        }
        int maxLength = 0;
        //查询企划维度数量配置
        Map<String, List<String>> fvItemMap = fvItem.stream().collect(Collectors.groupingBy(item -> item));
        QueryWrapper<PlanningDemandProportionData> pdpdQw = new QueryWrapper<>();
        pdpdQw.lambda().in(PlanningDemandProportionData::getDemandId, demandIds);
        List<PlanningDemandProportionData> pdpdList = planningDemandProportionDataService.list(pdpdQw);
        Map<String, List<PlanningDemandProportionData>> pdpdMap = Optional.ofNullable(pdpdList).orElse(CollUtil.newArrayList()).stream().collect(Collectors.groupingBy(PlanningDemandProportionData::getDemandId));
        for (Map.Entry<String, List<PlanningDemandStatisticsVo>> r : resultMap.entrySet()) {
            List<PlanningDemandProportionData> itemList = pdpdMap.get(r.getKey());
            List<PlanningDemandStatisticsVo> value = r.getValue();
            PlanningDemandStatisticsVo first = value.get(0);
            if (CollUtil.isEmpty(itemList)) {
                continue;
            }
            maxLength = Math.max(maxLength, itemList.size());
            for (PlanningDemandProportionData item : itemList) {
                PlanningDemandStatisticsVo vo = new PlanningDemandStatisticsVo();
                vo.setDemandId(first.getDemandId());
                vo.setFieldId(first.getFieldId());
                vo.setName(item.getClassifyName());
                vo.setCode(item.getClassify());
                vo.setTotal(String.valueOf(item.getNum()));
                vo.setQuantity(Optional.ofNullable(fvItemMap.get(first.getCode() + StrUtil.DASHED + item.getClassify())).map(il -> String.valueOf(CollUtil.size(il))).orElse("0"));

                value.add(vo);
            }
        }

        Collection<List<PlanningDemandStatisticsVo>> values = resultMap.values();
        PlanningDemandStatisticsResultVo resultVo = new PlanningDemandStatisticsResultVo();
        resultVo.setList(values);
        resultVo.setMaxLength(maxLength + 1);
        return resultVo;
    }

    private void getProductCategoryTreeQw(ProductCategoryTreeVo vo, QueryWrapper<?> qw) {
        qw.eq(StrUtil.isNotBlank(vo.getPlanningSeasonId()), "planning_season_id", vo.getPlanningSeasonId());
        qw.eq(StrUtil.isNotBlank(vo.getProdCategory1stName()), "prod_category1st_name", vo.getProdCategory1stName());
        qw.eq(StrUtil.isNotBlank(vo.getProdCategoryName()), "prod_category_name", vo.getProdCategoryName());
    }

    private void stylePlanningCommonQw(QueryWrapper<?> qw, PlanningBoardSearchDto dto) {
        qw.eq("sd." + COMPANY_CODE, getCompanyCode());
        qw.and(StrUtil.isNotEmpty(dto.getSearch()), i -> i.like("sd.design_no", dto.getSearch()).or().like("sd.style_no", dto.getSearch()));
        qw.eq(StrUtil.isNotEmpty(dto.getPlanningSeasonId()), "sd.planning_season_id", dto.getPlanningSeasonId());
        qw.in(StrUtil.isNotEmpty(dto.getBandCode()), "sd.band_code", StrUtil.split(dto.getBandCode(), CharUtil.COMMA));
        qw.in(StrUtil.isNotEmpty(dto.getMonth()), "sd.month", StrUtil.split(dto.getMonth(), CharUtil.COMMA));
        qw.in(StrUtil.isNotEmpty(dto.getProdCategory()), "sd.prod_category", StrUtil.split(dto.getProdCategory(), CharUtil.COMMA));
    }

    private void getDesignDataOverviewCommonQw(QueryWrapper<Style> qw, List timeRange) {
        qw.ne("del_flag", BaseGlobal.YES);
        qw.eq(COMPANY_CODE, getCompanyCode());
        amcFeignService.teamAuth(qw, "planning_season_id", getUserId());
        qw.between(CollUtil.isNotEmpty(timeRange), "create_date", CollUtil.getFirst(timeRange), CollUtil.getLast(timeRange));
    }

    private List<List> getChartList(List<ChartBarVo> chartBarVos) {
        List first = CollUtil.newArrayList("product", "总数", "未开款数", "已开款数", "已下发打版");
        List<List> result = new ArrayList<>(16);
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
        if (categoryIdx == 1 && StrUtil.isBlank(designDocTreeVo.getProdCategory1st())) {
            return result;
        }
        QueryWrapper<Style> qw = new QueryWrapper<>();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.eq(DEL_FLAG, BaseGlobal.DEL_FLAG_NORMAL);
        qw.eq("year", designDocTreeVo.getYear());
        qw.eq("season", designDocTreeVo.getSeason());
        qw.eq("band_code", designDocTreeVo.getBandCode());
        qw.eq(StrUtil.isNotBlank(designDocTreeVo.getProdCategory()), "prod_category", designDocTreeVo.getProdCategory());
        qw.eq(StrUtil.isNotBlank(designDocTreeVo.getProdCategory1st()), "prod_category1st", designDocTreeVo.getProdCategory1st());
        qw.select("DISTINCT prod_category,prod_category_name,prod_category1st,prod_category1st_name");
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.style_info.getK());
        List<Style> list = list(qw);
        if (CollUtil.isNotEmpty(list)) {
            Set<String> categoryIdsSet = new HashSet<>(16);
            for (Style style : list) {
                String code = categoryIdx == 1 ? style.getProdCategory() : style.getProdCategory1st();
                String name = categoryIdx == 1 ? style.getProdCategoryName() : style.getProdCategory1stName();

                if (categoryIdsSet.contains(code)) {
                    continue;
                }
                categoryIdsSet.add(code);
                DesignDocTreeVo vo = new DesignDocTreeVo();
                BeanUtil.copyProperties(designDocTreeVo, vo);
                vo.setLevel(2 + categoryIdx);
                vo.setChildren(categoryIdx == 0);
                if (categoryIdx == 1) {
                    vo.setProdCategory(code);
                } else {
                    vo.setProdCategory1st(code);
                }
                vo.setLabel(name);
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
        BaseQueryWrapper<Style> qw = new BaseQueryWrapper<>();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.eq(DEL_FLAG, BaseGlobal.DEL_FLAG_NORMAL);
        qw.eq("year", designDocTreeVo.getYear());
        qw.eq("season", designDocTreeVo.getSeason());
        qw.notNull("band_code");
        qw.select("DISTINCT band_code,band_name");
        qw.orderByAsc("band_code");
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.style_info.getK());
        List<Style> list = list(qw);
        if (CollUtil.isNotEmpty(list)) {
            for (Style style : list) {
                DesignDocTreeVo vo = new DesignDocTreeVo();
                BeanUtil.copyProperties(designDocTreeVo, vo);
                vo.setBandCode(style.getBandCode());
                vo.setLevel(1);
                vo.setLabel(style.getBandName());
                vo.setChildren(true);
                result.add(vo);
            }
        }
        return result;
    }

    private List<DesignDocTreeVo> getAllYearSeason() {
        QueryWrapper<Style> qw = new QueryWrapper<>();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.eq(DEL_FLAG, BaseGlobal.DEL_FLAG_NORMAL);
        qw.select("DISTINCT year,season");
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.style_info.getK());
        List<Style> list = list(qw);
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

            for (Style style : list) {
                DesignDocTreeVo vo = new DesignDocTreeVo();
                BeanUtil.copyProperties(style, vo);
                vo.setLevel(0);
                vo.setLabel(MapUtil.getStr(c8Year, vo.getYear(), vo.getYear()) + MapUtil.getStr(c8Quarter, vo.getSeason(), vo.getSeason()));
                vo.setChildren(true);
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    public String genDesignNo(Style style) {
        GetMaxCodeRedis getNextCode = new GetMaxCodeRedis(ccmService);
        Map<String, String> params = new HashMap<>(12);
        params.put("seriesId", style.getSeriesId());
        params.put("designChannelId", style.getDesignChannelId());
        params.put("year", style.getYear());
        params.put("season", style.getSeason());
        params.put("prodCategory3rd", style.getProdCategory3rd());
        return getNextCode.genCodeExists("STYLE_DESIGN_NO", params);
    }

    @Override
    public String getMaxDesignNo(GetMaxCodeRedis data, String userCompany) {
        List<String> regexps = new ArrayList<>(12);
        List<String> textFormats = new ArrayList<>(12);
        data.getValueMap().forEach((key, val) -> {
            if (BaseConstant.FLOWING.equals(key)) {
                textFormats.add("(" + val + ")");
            } else {
                textFormats.add(String.valueOf(val));
            }
            regexps.add(String.valueOf(val));
        });
        String regexp = "^" + CollUtil.join(regexps, "");
        QueryWrapper qc = new QueryWrapper();
        qc.eq(COMPANY_CODE, userCompany);
        qc.apply(" design_no REGEXP '" + regexp + "'");
        qc.eq("del_flag", BaseGlobal.DEL_FLAG_NORMAL);
        String maxCode = baseMapper.selectMaxDesignNo(qc);
        if (StrUtil.isBlank(maxCode)) {
            return null;
        }
        // 替换,保留流水号
        String formatStr = CollUtil.join(textFormats, "");
        try {
            String flowing = ReUtil.get(formatStr, maxCode, 1);
            if (StrUtil.isNotBlank(flowing)) {
                return flowing;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public void saveDesignNo(Style style) {
        boolean flag = this.update(style, new UpdateWrapper<Style>().eq("id", style.getId()));
        if (flag) {
            Style hasStyle = getById(style.getId());
            PlanningCategoryItem planningCategoryItem = new PlanningCategoryItem();
            planningCategoryItem.setDesignNo(style.getDesignNo());
            planningCategoryItemService.update(planningCategoryItem, new UpdateWrapper<PlanningCategoryItem>().eq("id", hasStyle.getPlanningCategoryItemId()));
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean reviseAllDesignNo(String oldDesignNo, String newDesignNo) {
        if ((StrUtil.hasBlank(oldDesignNo, newDesignNo)) && StrUtil.equals(oldDesignNo, newDesignNo)) {
            return true;
        }
        return baseMapper.reviseAllDesignNo(oldDesignNo, newDesignNo);
    }

    @Override
    public void reviseAllDesignNo(Map<String, String> designNoUpdate) {
        if (CollUtil.isNotEmpty(designNoUpdate)) {
            for (Map.Entry<String, String> e : designNoUpdate.entrySet()) {
                reviseAllDesignNo(e.getKey(), e.getValue());
            }
        }
    }

    /**
     * 款式的停用启用
     *
     * @param startStopDto
     * @return
     */
    @Override
    public Boolean startStopStyle(StartStopDto startStopDto) {
        /*查看配色是否停用 全部停用款式才可以停用*/
        if(StrUtil.equals(startStopDto.getStatus(),BaseGlobal.STATUS_CLOSE)){
            QueryWrapper queryWrapper =new QueryWrapper();
            queryWrapper.eq("style_id",startStopDto.getIds());
            queryWrapper.eq("status",BaseGlobal.STATUS_NORMAL);
            List<StyleColor> list = styleColorMapper.selectList(queryWrapper);
            if(CollUtil.isNotEmpty(list)){
                throw new OtherException("配色存在未停用数据");
            }
        }
        UpdateWrapper<Style> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", com.base.sbc.config.utils.StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("enable_status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setMainPic(StyleSaveDto dto) {
        UpdateWrapper<Style> uw = new UpdateWrapper<>();
        uw.lambda().eq(Style::getId, dto.getId())
                .set(Style::getStylePic, dto.getStylePic());
        update(uw);
        UpdateWrapper<StylePic> picUw1 = new UpdateWrapper<>();
        picUw1.lambda().eq(StylePic::getStyleId, dto.getId()).set(StylePic::getMainPicFlag, BaseGlobal.NO);
        stylePicService.update(picUw1);
        UpdateWrapper<StylePic> picUw2 = new UpdateWrapper<>();
        picUw2.lambda()
                .eq(StylePic::getStyleId, dto.getId())
                .eq(StylePic::getFileName, dto.getStylePic())
                .set(StylePic::getMainPicFlag, BaseGlobal.YES);
        stylePicService.update(picUw2);

        return true;
    }

    @Override
    public String selectMaxOldDesignNo(QueryWrapper qc) {
        return baseMapper.selectMaxOldDesignNo(qc);
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean startMarkingApproval(String id, String showFOB, String styleColorId) {
        Style style = getById(id);
        if (style == null) {
            throw new OtherException("样衣数据不存在,请先保存");
        }
        if (StrUtil.isBlank(style.getDesignAuditStatus())) {
            style.setDesignAuditStatus(BaseGlobal.STOCK_STATUS_DRAFT);
        }
        if (StrUtil.equals(style.getDesignAuditStatus(), BaseGlobal.STOCK_STATUS_WAIT_CHECK)) {
            throw new OtherException("当前数据已在审批中");
        }
        style.setDesignAuditStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
        updateById(style);
        Map<String, Object> variables = BeanUtil.beanToMap(style);
        return flowableService.start(FlowableService.DESIGN_MARKING + "[" + style.getDesignNo() + "]", FlowableService.DESIGN_MARKING, id,
                "/pdm/api/saas/style/approvalMarking",
                "/pdm/api/saas/style/approvalMarking",
                "/pdm/api/saas/style/approvalMarking",
                "/styleAnalysis/styleMarkingDetails?sampleDesignId=" + id + "&isEdit=0&panelValue=design&showFOB="
                        + showFOB + "&id=" + id + "&styleColorId=" + styleColorId, variables);
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean approvalMarking(AnswerDto dto) {
        Style style = getById(dto.getBusinessKey());
        logger.info("————————————————款式打标-设计阶段审批 回调方法————————————————", JSON.toJSONString(style));
        logger.info("————————————————回调类型————————————————", dto.getApprovalType());
        if (style != null) {
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                style.setDesignAuditStatus(BaseGlobal.STOCK_STATUS_CHECKED);
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                style.setDesignAuditStatus(BaseGlobal.STOCK_STATUS_REJECT);
            } else {
                style.setDesignAuditStatus(BaseGlobal.STOCK_STATUS_DRAFT);
            }
            updateById(style);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean startMarkingOrderApproval(String id, String showFOB, String styleColorId) {
        /*Style style = getById(id);
        if (style == null) {
            throw new OtherException("样衣数据不存在,请先保存");
        }
        if (StrUtil.isBlank(style.getOrderAuditStatus())) {
            style.setOrderAuditStatus(BaseGlobal.STOCK_STATUS_DRAFT);
        }
        if (StrUtil.equals(style.getOrderAuditStatus(), BaseGlobal.STOCK_STATUS_WAIT_CHECK)) {
            throw new OtherException("当前数据已在审批中");
        }
        style.setOrderAuditStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
        updateById(style);
        Map<String, Object> variables = BeanUtil.beanToMap(style);
        return flowableService.start(FlowableService.ORDER_MARKING + "[" + style.getDesignNo() + "]", FlowableService.ORDER_MARKING, id,
                "/pdm/api/saas/style/approvalMarkingOrder",
                "/pdm/api/saas/style/approvalMarkingOrder",
                "/pdm/api/saas/style/approvalMarkingOrder",
                "/styleAnalysis/styleMarkingDetails?sampleDesignId=" + id + "&isEdit=0&panelValue=order&showFOB="
                        + showFOB + "&id=" + id + "&styleColorId=" + styleColorId, variables);*/
        return true;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean approvalMarkingOrder(AnswerDto dto) {
        /*Style style = getById(dto.getBusinessKey());
        logger.info("————————————————款式打标-下单阶段审批 回调方法————————————————", JSON.toJSONString(style));
        logger.info("————————————————回调类型————————————————", dto.getApprovalType());
        if (style != null) {
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                style.setOrderAuditStatus(BaseGlobal.STOCK_STATUS_CHECKED);
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                style.setOrderAuditStatus(BaseGlobal.STOCK_STATUS_REJECT);
            } else {
                style.setOrderAuditStatus(BaseGlobal.STOCK_STATUS_DRAFT);
            }
            updateById(style);
        }*/
        return true;
    }

    /**
     * 保存打板中的维度系数数据
     * @param fieldValList
     * @param styleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveCoefficient(List<FieldVal> fieldValList, String styleId) {
        // 保存系数数据
        fieldValService.save(styleId,FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY,fieldValList);
        /*查询这个款式下的配色*/
        QueryWrapper<StyleColor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("style_id",styleId);
        List<StyleColor> styleColorList = styleColorMapper.selectList(queryWrapper);
        /*获取版师修改的字段*/
        Map<String,FieldVal> map = fieldValList.stream().collect(Collectors.toMap(k -> k.getFieldName(), v -> v, (a, b) -> b));

        if(CollUtil.isNotEmpty(styleColorList)){
            /*数据放到配色*/
            for (StyleColor styleColor : styleColorList) {
                List<FieldVal> list = fieldValService.list(styleColor.getId(), FieldValDataGroupConstant.STYLE_MARKING_ORDER);
                /*下单阶段存在数据时修改字段*/
                if (CollUtil.isNotEmpty(list)) {
                    list.forEach(fieldVal -> {
                        /*查看是否修改字段*/
                        FieldVal fieldVal1 = map.get(fieldVal.getFieldName());
                        if(ObjectUtil.isNotEmpty(fieldVal1)){
                            fieldVal.setVal(fieldVal1.getVal());
                            fieldVal.setValName(fieldVal1.getValName());
                        }
                    });
                    // 保存系数数据
                    fieldValService.saveOrUpdateBatch(list);
                }
            }
        }
        return true;
    }

}

