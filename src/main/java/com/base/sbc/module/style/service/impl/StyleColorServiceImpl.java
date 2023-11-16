/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.dto.UploadStylePicDto;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.mapper.PackInfoMapper;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import com.base.sbc.module.pack.service.PackPricingService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.mapper.StylePricingMapper;
import com.base.sbc.module.smp.DataUpdateScmService;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.smp.dto.PdmStyleCheckParam;
import com.base.sbc.module.style.dto.*;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.entity.StyleMainAccessories;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleMainAccessoriesService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.StyleColorVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 类描述：样衣-款式配色 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.sample.service.SampleStyleColorService
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 */
@Service
@RequiredArgsConstructor
public class StyleColorServiceImpl<pricingTemplateService> extends BaseServiceImpl<StyleColorMapper, StyleColor> implements StyleColorService {

    private final BaseController baseController;

    private final BasicsdatumColourLibraryService basicsdatumColourLibraryServicel;

    private final UserUtils userUtils;

    private final DataUpdateScmService dataUpdateScmService;

    private final StylePricingMapper stylePricingMapper;

    @Autowired
    private StylePicUtils stylePicUtils;
    @Lazy
    @Resource
    private SmpService smpService;

    private final PackInfoService packInfoService;

    private final PackInfoMapper packInfoMapper;
    @Lazy
    @Resource
    private final StyleService styleService;
    private final PackInfoStatusService packInfoStatusService;
    private final CcmFeignService ccmFeignService;

    private final FieldValService fieldValService;

    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Autowired
    private PackPricingService packPricingService;

    @Autowired
    private PackBomService packBomService;

    @Autowired
    private StyleMainAccessoriesService styleMainAccessoriesService;

    @Autowired
    private HangTagService hangTagService;

    @Autowired
    private UploadFileService uploadFileService;
    Pattern pattern = Pattern.compile("[a-z||A-Z]");


/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 样衣-款式配色分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    public PageInfo<StyleColorVo> getSampleStyleColorList(Principal user, QueryStyleColorDto queryDto) {

        /*分页*/
        Page<Object> objects = PageHelper.startPage(queryDto);
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getStyleId()), "tsc.style_id", queryDto.getStyleId());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getIsTrim()), "tsc.is_trim", queryDto.getIsTrim());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getColorSpecification()), "tsc.color_specification", queryDto.getColorSpecification());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getStyleNo()), "tsc.style_no", queryDto.getStyleNo());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getDesignNo()), "ts.design_no", queryDto.getDesignNo());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getColorName()), "tsc.color_name", queryDto.getColorName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getColorCode()), "tsc.color_code", queryDto.getColorCode());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getMeetFlag()), "tsc.meet_flag", queryDto.getMeetFlag());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getProdCategoryName()), "ts.prod_category_name", queryDto.getProdCategoryName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getProdCategory()), "ts.prod_category", queryDto.getProdCategory());

        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSubdivide()), "tsc.subdivide", queryDto.getSubdivide());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getCategoryName()), "ts.prod_category_name", queryDto.getCategoryName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getTaskLevelName()), "ts.task_level_name", queryDto.getTaskLevelName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getDevtTypeName()), "ts.devt_type_name", queryDto.getDevtTypeName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getDesignerId()), "ts.designer_id", queryDto.getDesignerId());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getTechnicianId()), "ts.technician_id", queryDto.getTechnicianId());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getPlanningSeasonId()), "ts.planning_season_id", queryDto.getPlanningSeasonId());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getProdCategory1st()), "ts.prod_category1st", queryDto.getProdCategory1st());
        queryWrapper.in(StringUtils.isNotBlank(queryDto.getStyleStatus()), "ts.status", StringUtils.convertList(queryDto.getStyleStatus()));
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getStyleTypeName()), "ts.style_type_name", queryDto.getStyleTypeName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getHisDesignNo()), "ts.his_design_no", queryDto.getHisDesignNo());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSizeRangeName()), "ts.size_range_name", queryDto.getSizeRangeName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getBandName()), "ts.band_name", queryDto.getBandName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getDesigner()), "ts.designer", queryDto.getDesigner());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getTechnicianName()), "ts.technician_name", queryDto.getTechnicianName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getStatus()), "tsc.status", queryDto.getStatus());
        queryWrapper.notEmptyLike("ts.prod_category3rd_name", queryDto.getProdCategory3ndName());
        queryWrapper.notEmptyLike("ts.prod_category2nd_name", queryDto.getProdCategory2ndName());
        queryWrapper.notEmptyLike("ts.prod_category1st_name", queryDto.getProdCategory1stName());
        queryWrapper.notEmptyLike("ts.season_name", queryDto.getSeasonName());
        queryWrapper.notEmptyLike("ts.year_name", queryDto.getYearName());
        queryWrapper.notEmptyLike("tppst.technologist_name", queryDto.getTechnologistName());

        queryWrapper.between("tsc.create_date", queryDto.getCreateDate());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getCreateName()), "tsc.create_name", queryDto.getCreateName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getNewDate()), "tsc.new_date", queryDto.getNewDate());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getIsLuxury()), "tsc.is_luxury", queryDto.getIsLuxury());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSubdivideName()), "tsc.subdivide_name", queryDto.getSubdivideName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendMainFabricDate()), "tsc.send_main_fabric_date", queryDto.getSendMainFabricDate());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendBatchingDate1()), "tsc.send_batching_date1", queryDto.getSendBatchingDate1());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendBatchingDate2()), "tsc.send_batching_date2", queryDto.getSendBatchingDate2());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendBatchingDate3()), "tsc.send_batching_date3", queryDto.getSendBatchingDate3());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendSingleDate()), "tsc.send_single_date", queryDto.getSendSingleDate());
        if (StringUtils.isNotBlank(queryDto.getDesignDetailDate())) {

            queryWrapper.between("tsc.design_detail_date", queryDto.getDesignDetailDate().split(","));
        }


        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getDesignCorrectDate()), "tsc.design_correct_date", queryDto.getDesignCorrectDate());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getProductSubdivideName()), "tsc.product_subdivide_name", queryDto.getProductSubdivideName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getPrincipalStyle()), "tsc.principal_style", queryDto.getPrincipalStyle());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getPrincipalStyleNo()), "tsc.principal_style_no", queryDto.getPrincipalStyleNo());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getAccessory()), "tsc.accessory", queryDto.getAccessory());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getAccessoryNo()), "tsc.accessory_no", queryDto.getAccessoryNo());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSenderDesignerName()), "tsc.sender_designer_name", queryDto.getSenderDesignerName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getMerchDesignName()), "ts.merch_design_name", queryDto.getMerchDesignName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getBom()), "tsc.bom", queryDto.getBom());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getBomStatus()), "tsc.bom_status", queryDto.getBomStatus());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSalesTypeName()), "tsc.sales_type_name", queryDto.getSalesTypeName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getTagPrice()), "tsc.tag_price", queryDto.getTagPrice());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getStyleFlavourName()), "ts.style_flavour_name", queryDto.getStyleFlavourName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSupplier()), "tsc.supplier", queryDto.getSupplier());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSupplier()), "tsc.supplier", queryDto.getSupplier());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSupplier()), "tsc.supplier", queryDto.getSupplier());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSupplierColor()), "tsc.supplier_color", queryDto.getSupplierColor());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSupplierAbbreviation()), "tsc.supplier_abbreviation", queryDto.getSupplierAbbreviation());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getSupplierNo()), "tsc.supplier_no", queryDto.getSupplierNo());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getProductName()), "tsc.product_name", queryDto.getProductName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getDefectiveName()), "tsc.defective_name", queryDto.getDefectiveName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getProductHangtagConfirm()), "tsp.control_confirm", queryDto.getProductHangtagConfirm());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getControlConfirm()), "tsp.product_hangtag_confirm", queryDto.getControlConfirm());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getControlHangtagConfirm()), "tsp.control_hangtag_confirm", queryDto.getControlHangtagConfirm());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getPatternDesignName()), "ts.pattern_design_name", queryDto.getPatternDesignName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getOrderFlag()), "tsc.order_flag", queryDto.getOrderFlag());
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.styleColor.getK(), "ts.");
        /*获取配色数据*/
        List<StyleColorVo> sampleStyleColorList = new ArrayList<>();
        if (StringUtils.isNotBlank(queryDto.getColorListFlag())) {
            queryWrapper.eq("tsc.del_flag", "0");
            // bug 3325
            objects.setOrderBy("CASE tsc.scm_send_flag " +
                    " WHEN 0 THEN 0 " +
                    " WHEN 3 THEN 1 " +
                    " WHEN 2 THEN 2 " +
                    " WHEN 1 THEN 3 " +
                    " ELSE 99 " +
                    "END " +
                    ",tsc.create_date asc");
            //queryWrapper.orderByAsc("tsc.scm_send_flag asc ");
//            查询配色列表
            sampleStyleColorList = baseMapper.colorList(queryWrapper);
//            查询主款配饰
            sampleStyleColorList.forEach(s -> {
                List<StyleMainAccessories> mainAccessoriesList = styleMainAccessoriesService.styleMainAccessoriesList(s.getId(), null);
                if (CollUtil.isNotEmpty(mainAccessoriesList)) {
                    String styleNos = mainAccessoriesList.stream().map(StyleMainAccessories::getStyleNo).collect(Collectors.joining(","));
                    String colorName = mainAccessoriesList.stream().map(StyleMainAccessories::getColorName).collect(Collectors.joining(","));
                    if (StringUtils.equals(s.getIsTrim(), BaseGlobal.NO)) {
                        s.setAccessory(colorName);
                        s.setAccessoryNo(styleNos);
                    } else {
                        s.setPrincipalStyle(colorName);
                        s.setPrincipalStyleNo(styleNos);
                    }
                }
            });

        } else {
            queryWrapper.eq("ts.del_flag", "0");
            queryWrapper.orderByDesc("ts.id");
//            查询款式配色
            sampleStyleColorList = baseMapper.styleColorList(queryWrapper);
            List<String> stringList = IdGen.getIds(sampleStyleColorList.size());
            int index = 0;
            for (StyleColorVo styleColorVo : sampleStyleColorList) {
                if (stringList != null) {
                    styleColorVo.setIssuerId(stringList.get(index));
                }
                index++;
            }
        }
        /*查询款式图*/
        stylePicUtils.setStylePic(sampleStyleColorList, "stylePic");

        if (user != null) {
            /*查询款式配色图*/
            GroupUser userBy = userUtils.getUserBy(user);
            stylePicUtils.setStyleColorPic2(sampleStyleColorList, "styleColorPic");
        }

        return new PageInfo<>(sampleStyleColorList);
    }

    /**
     * 方法描述: 获取款式或配饰
     *
     * @return
     */
    @Override
    public List<StyleColorVo> getStyleAccessoryBystyleNo(String designNo) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("design_no", designNo);
        Style style = styleService.getOne(qw);
        if (ObjectUtils.isEmpty(style)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        QueryWrapper<StyleColor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq("style_id", style.getId());
        List<StyleColor> styleColorList = baseMapper.selectList(queryWrapper);
        /*转换vo*/
        List<StyleColorVo> list = BeanUtil.copyToList(styleColorList, StyleColorVo.class);
        return list;
    }

    /**
     * 修改吊牌价-款式配色
     *
     * @param updateTagPriceDto
     * @return
     */
    @Override
    public Boolean updateTagPrice(UpdateTagPriceDto updateTagPriceDto) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.set("tag_price", updateTagPriceDto.getTagPrice());
        updateWrapper.eq("id", updateTagPriceDto.getId());
        baseMapper.update(null, updateWrapper);
        return true;
    }

    /**
     * 大货款号查询
     *
     * @param querySampleStyleColorDto
     * @return
     */
    @Override
    public List<StyleColorVo> getByStyleNo(QueryStyleColorDto querySampleStyleColorDto) {
        if (StringUtils.isBlank(querySampleStyleColorDto.getStyleNo())) {
            throw new OtherException(BaseErrorEnum.ERR_MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("style_no", StringUtils.convertList(querySampleStyleColorDto.getStyleNo()));
        List<StyleColor> list = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        List<StyleColorVo> styleColorVoList = BeanUtil.copyToList(list, StyleColorVo.class);
        return styleColorVoList;
    }

    /**
     * 方法描述: 批量新增款式配色-款式配色
     *
     * @param list@return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean batchAddSampleStyleColor(Principal user,List<AddRevampStyleColorDto> list) throws Exception {
        int index = 0;
        list = CollUtil.distinct(list, AddRevampStyleColorDto::getColorCode, true);
        /*查询颜色*/
        if(CollUtil.isEmpty(list)){
            throw new OtherException("未选中数据");
        }
        List<String> colorCodeList = list.stream().map(AddRevampStyleColorDto::getColorCode).distinct().collect(Collectors.toList());
        List<BasicsdatumColourLibrary> libraryList = basicsdatumColourLibraryServicel.listByField("colour_code",colorCodeList);
        /*颜色数据*/
        Map<String, BasicsdatumColourLibrary> map = libraryList.stream().collect(Collectors.toMap(BasicsdatumColourLibrary::getColourCode, c -> c));
        /*查询款式主数据*/
        Style style = styleService.getById(list.get(0).getStyleId());
        /*款式主数据数据*/
        for (AddRevampStyleColorDto addRevampStyleColorDto : list) {
            BasicsdatumColourLibrary basicsdatumColourLibrary = map.get(addRevampStyleColorDto.getColorCode());
            addRevampStyleColorDto.setStyleId(style.getId());
            addRevampStyleColorDto.setAccessory("");
            addRevampStyleColorDto.setAccessoryNo("");
            addRevampStyleColorDto.setPrincipalStyle("");
            addRevampStyleColorDto.setPrincipalStyleNo("");
            addRevampStyleColorDto.setColourLibraryId(basicsdatumColourLibrary.getId());
            addRevampStyleColorDto.setColorName(basicsdatumColourLibrary.getColourName());
            addRevampStyleColorDto.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());
            addRevampStyleColorDto.setColorCode(basicsdatumColourLibrary.getColourCode());
            addRevampStyleColorDto.setStyleNo(getNextCode(style.getBrand(), style.getYearName(),style.getSeason(), StringUtils.isNotEmpty(addRevampStyleColorDto.getBandName()) ? addRevampStyleColorDto.getBandName() : style.getBandName(), style.getProdCategory(), StringUtils.isNotBlank(style.getOldDesignNo())?style.getOldDesignNo():style.getDesignNo() ,style.getId(),addRevampStyleColorDto.getIsLuxury(),  ++index));
        }
        List<StyleColor> styleColorList = BeanUtil.copyToList(list, StyleColor.class);
        saveBatch(styleColorList);
        addMainAccessories(style, list, styleColorList);
        for (StyleColor styleColor : styleColorList) {
            /*获取到款式里面款式图给到配色中*/
            if(StringUtils.isNotBlank(style.getStylePic())){
                UploadStylePicDto uploadStylePicDto = new UploadStylePicDto();
                uploadStylePicDto.setStyleColorId(styleColor.getId());
                MultipartFile multipartFile = uploadFileService.downloadImage(style.getStylePic(), styleColor.getStyleNo() + ".jpg");
                uploadStylePicDto.setFile(multipartFile);
                uploadFileService.uploadStyleImage(uploadStylePicDto,user);
            }
        }

        return true;
    }

    /**
     * 新增配色异步操作维度信息和主款配色数据
     *
     * @param style
     * @param list
     * @param styleColorList
     */
//    @Async
    public void addMainAccessories(Style style, List<AddRevampStyleColorDto> list, List<StyleColor> styleColorList) {
        /*保存维度信息*/
        /*查询款式中的维度信息*/
        DimensionLabelsSearchDto dto = new DimensionLabelsSearchDto();
        BeanUtil.copyProperties(style, dto);
        dto.setForeignId(style.getId());
        dto.setDataGroup(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
        List<FieldManagementVo> fieldManagementVoList = styleService.queryDimensionLabels(dto);
        List<FieldVal> fieldValList = BeanUtil.copyToList(fieldManagementVoList, FieldVal.class);
        /*主款配饰数据*/
        List<StyleMainAccessoriesSaveDto> saveDtoList = list.get(0).getSaveDtoList();
        List<StyleMainAccessoriesSaveDto> styleMainAccessoriesSaveDtoList = new ArrayList<>();
        /*获取新增的维度信息*/
        for (StyleColor styleColor : styleColorList) {
            // 保存工艺信息
            fieldValService.save(styleColor.getId(), FieldValDataGroupConstant.STYLE_COLOR, fieldValList);
            /*获取全部的主款或配饰*/
            if (CollUtil.isNotEmpty(saveDtoList)) {
                saveDtoList.forEach(s -> s.setStyleColorId(styleColor.getId()));
                styleMainAccessoriesSaveDtoList.addAll(saveDtoList);
            }
        }
        /*保存主款配饰信息*/
        if (CollUtil.isNotEmpty(styleMainAccessoriesSaveDtoList)) {
            List<StyleMainAccessories> mainAccessoriesList = BeanUtil.copyToList(styleMainAccessoriesSaveDtoList, StyleMainAccessories.class);
            /*主款配饰相互绑定*/
            //获取配色信息
            List<String> styleNolist = mainAccessoriesList.stream().map(StyleMainAccessories::getStyleNo).collect(Collectors.toList());
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("style_no", styleNolist);
            /*需要反向绑定的配色*/
            List<StyleColor> colorList = baseMapper.selectList(queryWrapper);

            for (StyleColor styleColor : colorList) {
                for (StyleColor color : styleColorList) {
                    StyleMainAccessories styleMainAccessories = new StyleMainAccessories();
                    styleMainAccessories.setColorCode(color.getColorCode());
                    styleMainAccessories.setColorName(color.getColorName());
                    styleMainAccessories.setStyleColorId(styleColor.getId());
                    styleMainAccessories.setIsTrim(styleColor.getIsTrim());
                    styleMainAccessories.setStyleNo(color.getStyleNo());
                    mainAccessoriesList.add(styleMainAccessories);
                }
            }
            /*新增*/
            styleMainAccessoriesService.saveBatch(mainAccessoriesList);
        }
    }

    /**
     * 大货款号生成规则 品牌 + 年份  + 波段 +品类 +设计款号流水号+颜色流水号
     * @param brand
     * @param year
     * @param season
     * @param bandName
     * @param category
     * @param designNo
     * @param styleId
     * @param index
     * @return
     */

    public String getNextCode(String brand, String year,String season,String bandName, String category, String designNo,String styleId,String isLuxury, int index) {
        if (ccmFeignService.getSwitchByCode("STYLE_EQUAL_DESIGN_NO")) {
            return designNo;
        }
        if (StrUtil.contains(category, StrUtil.COMMA)) {
            category = getCategory(category);
        }
        if (StringUtils.isBlank(bandName)) {
            throw new OtherException("款式波段为空");
        }
        if (StringUtils.isBlank(brand)) {
            throw new OtherException("款式品牌为空");
        }
        if (StringUtils.isBlank(year)) {
            throw new OtherException("款式年份为空");
        }

        String yearOn = "";
        try {
//        获取年份
            yearOn = getYearOn(year);
//            波段
            bandName = getBandName(bandName);
            /*获取款号流水号*/
            /**
             * 款号流水号：拼接品牌 年份 季节 品类 成为设计款号前几位 去掉设计编码
             */
            String years = year.substring(year.length() - 2);
            /*拼接的设计款号（用于获取流水号）*/
            String joint = brand + years + season + category;
            designNo = designNo.replaceAll(joint, "");
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(designNo);
            designNo = m.replaceAll("").trim();
        } catch (Exception e) {
            throw new OtherException(e.getMessage()+"大货编码生成失败");
        }
//        获取款式下的配色
        String styleNoFront = brand + yearOn + bandName + category + designNo;
        String styleNo = "";
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("style_id",styleId);
        Long aLong = baseMapper.selectCount(queryWrapper);

        /*轻奢款选是，大货款号最后一位自带Q*/
        isLuxury = StrUtil.equals(isLuxury,BaseGlobal.YES)?"Q":"";
        /*先使用款式下的流水号 如果重复
         * 不加流水号查询大货款号的最大流水号
         * */
        styleNo =   styleNoFront + (aLong+ index)+isLuxury;
        int i = baseMapper.isStyleNoExist(styleNo);
        if (i != 0) {
            String maxMark = "0";
            String number = baseMapper.getStyleColorNumber(styleNoFront, styleNoFront.length() + 1);
            if (StringUtils.isNotBlank(number)) {
                /*获取最大流水号*/
                maxMark = number;
            }
            /*拼接流水号*/
            styleNo = styleNoFront + (Long.parseLong(maxMark) + index) +isLuxury;
            /*查询编码是否重复*/
            i = baseMapper.isStyleNoExist(styleNo);
            if (i != 0) {
                throw new OtherException("编码重复");
            }
        }
        return styleNo;
    }


    /**
     * 波段：使用波段中的字母生产 1A：1 2B：2
     *
     * @param bandName
     * @return
     */
    public String getBandName(String bandName) {
        // 使用正则表达式匹配字母
        Matcher matcher = pattern.matcher(bandName);
        StringBuilder Letter = new StringBuilder();
        String month = "";
        // 打印匹配到的字母
        while (matcher.find()) {
            Letter.append(matcher.group());
        }
        if (!StringUtils.isEmpty(Letter.toString())) {
            month = getMonth(bandName, Letter.toString());
            Letter = new StringBuilder(Letter.toString().toUpperCase());
            char[] charArray = Letter.toString().toCharArray();
            int char1 = charArray[0];
            bandName = String.valueOf(char1 - 64);
        } else {
            bandName = "";
        }

        return month + bandName;
    }

    /**
     * 年份 初始值从2019开始为A依次往后推 超过26年份为A1
     *
     * @param year
     * @return
     */
    public String getYearOn(String year) {
        if (StrUtil.equals(year, "2099")) {
            return "99";
        }
        String yearOn = "";
        int initial = Integer.parseInt("2019");
        int year1 = Integer.parseInt(year);
        int ascii = 'A';
        /*超过2044年重新开始*/
        if (year1 <= 2044) {
            char c = (char) (ascii + Math.abs(year1 - initial));
            yearOn = String.valueOf(c);
        } else {
            char c1 = (char) (ascii + (year1 - initial) - ((year1 - initial) / 26) * 26);
            yearOn = String.valueOf(c1) + (year1 - initial) / 26;
        }
        return yearOn;
    }

    /**
     * 查询波段中的月份
     *
     * @param bandName
     * @param s
     * @return
     */
    public String getMonth(String bandName, String s) {
        String month = bandName.replace(s, "");
        if (!month.matches("[1-9]")) {
            month = "10".equals(month) ? "A" : "11".equals(month) ? "B" : "12".equals(month) ? "C" : "";
        }
        return month;
    }

    /**
     * 获取品类生产的字符
     *
     * @param categoryName
     * @return
     */
    private String getCategory(String categoryName) {
        if (StrUtil.isBlank(categoryName)) {
            throw new OtherException("未选择品类,不能生成设计款号");
        }
        String categoryCode = null;
        try {
            categoryCode = categoryName.split(StrUtil.SLASH)[1].split(StrUtil.COMMA)[1];
        } catch (Exception e) {
            throw new OtherException("品类编码获取失败");
        }
        return categoryCode;
    }

    /**
     * 方法描述：新增修改样衣-款式配色
     *
     * @param addRevampStyleColorDto 样衣-款式配色Dto类
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean addRevampSampleStyleColor(AddRevampStyleColorDto addRevampStyleColorDto) {
        StyleColor styleColor = new StyleColor();
        if (StringUtils.isEmpty(addRevampStyleColorDto.getId())) {
            /*新增*/
            BeanUtils.copyProperties(addRevampStyleColorDto, styleColor);
            styleColor.setCompanyCode(baseController.getUserCompany());
            styleColor.insertInit();
            this.save(styleColor, "款式配色");
        } else {
            /*修改*/
            if (StringUtils.isBlank(addRevampStyleColorDto.getColorCode())) {
                throw new OtherException("颜色不能为空");
            }
            BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryServicel.getByOne("colour_code",addRevampStyleColorDto.getColorCode());
            if (ObjectUtils.isEmpty(basicsdatumColourLibrary)) {
                throw new OtherException("无颜色");
            }
            styleColor = baseMapper.selectById(addRevampStyleColorDto.getId());
            StyleColor old = new StyleColor();
            BeanUtil.copyProperties(styleColor, old);

            /*修改*/
           boolean b = !StringUtils.equals(styleColor.getScmSendFlag(), BaseGlobal.IN_READY)&&StrUtil.isBlank(styleColor.getHisStyleNo());

            /*判断轻奢款是否修改*/
            if( !StrUtil.equals( styleColor.getIsLuxury(),addRevampStyleColorDto.getIsLuxury())&& b) {
                /*修改了*/
                if (StrUtil.equals(addRevampStyleColorDto.getIsLuxury(), BaseGlobal.NO)) {
                    /*判断最后以为是不是Q*/
                    String s = addRevampStyleColorDto.getStyleNo().substring(addRevampStyleColorDto.getStyleNo().length() - 1);
                    if (StrUtil.equals(s, "Q")) {
                        /*去掉Q*/
                        addRevampStyleColorDto.setStyleNo(addRevampStyleColorDto.getStyleNo().substring(0,addRevampStyleColorDto.getStyleNo().length()-1));
                    }
                } else {
                    addRevampStyleColorDto.setStyleNo(addRevampStyleColorDto.getStyleNo() + "Q");
                }
                /**
                 * 修改所有引用的大货款号
                 */
                updateStyleColor(styleColor.getBom(),styleColor.getStyleNo(), addRevampStyleColorDto.getStyleNo());
            }
            /*判断是否修改波段
             * 当配色未下发时可以修改会影响大货款号
             * 当配色下发后可以修改波段不会影响大货款号
             * 如果手动修改过大货款号 再去修改波段则不会变化
             */
            if (!StringUtils.equals(addRevampStyleColorDto.getBandCode(), styleColor.getBandCode()) && b ) {
                /*新大货款号 ：换标波段生成的字符*/
                /**
                 * 先生成波段之前的字符串替换为空，在拼接
                 */
                Style style = styleService.getById(styleColor.getStyleId());
                //获取年份
                String year = getYearOn(style.getYearName());
                /*品牌*/
                String brand = style.getBrand();
                /*大货款的前及位 品牌加年份*/
                String newStyle = brand + year;
                /*后几位大货款号*/
                String s = styleColor.getStyleNo().replaceAll(newStyle + getBandName(styleColor.getBandName()), "");
                /*拼接新大货款号*/
                String styleNo = newStyle + getBandName(addRevampStyleColorDto.getBandName()) + s;
                int i = baseMapper.isStyleNoExist(styleNo);
                if (i > 0) {
                    throw new OtherException("波段修改失败，大货款重复");
                }
                addRevampStyleColorDto.setStyleNo(styleNo);
                /**
                 * 修改所有引用的大货款号
                 */
                updateStyleColor(styleColor.getBom(),styleColor.getStyleNo(), addRevampStyleColorDto.getStyleNo());
            }
            /*判断波段及细分是否改动 改动则需要同步大货款号*/
            if (StringUtils.isNotBlank(addRevampStyleColorDto.getSubdivide())&& b) {
                /**
                 * 修改所有引用的大货款号
                 * 查看之前有没有细分
                 */
                String styleNo = "";
                if (StrUtil.isNotBlank(styleColor.getSubdivide())) {
                    styleNo = addRevampStyleColorDto.getStyleNo().replace(styleColor.getSubdivide(), "");
                } else {
                    styleNo = addRevampStyleColorDto.getStyleNo();
                }
                /*新大货款号=大货款号+细分*/
                addRevampStyleColorDto.setStyleNo(styleNo + addRevampStyleColorDto.getSubdivide());
                updateStyleColor(styleColor.getBom(),styleColor.getStyleNo(), addRevampStyleColorDto.getStyleNo());
            }
            if (ObjectUtils.isEmpty(styleColor)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            /*主款配饰*/
            List<StyleMainAccessoriesSaveDto> saveDtoList = addRevampStyleColorDto.getSaveDtoList();
            if (StrUtil.equals(addRevampStyleColorDto.getSaveDtoFlag(), BaseGlobal.STATUS_CLOSE)) {
                //删除
                QueryWrapper<StyleMainAccessories> styleColorIdQw = new QueryWrapper<StyleMainAccessories>();
                styleColorIdQw.eq("style_color_id", styleColor.getId()).or().eq("style_no", styleColor.getStyleNo());
                styleMainAccessoriesService.remove(styleColorIdQw);
                if (CollUtil.isNotEmpty(saveDtoList)) {
                    /*查询需要保存的数据*/
                    List<StyleMainAccessories> accessoriesList = BeanUtil.copyToList(saveDtoList, StyleMainAccessories.class);
                    for (StyleMainAccessories styleMainAccessories : accessoriesList) {
                        styleMainAccessories.setStyleColorId(styleColor.getId());
                        styleMainAccessories.setIsTrim(styleMainAccessories.getIsTrim());
                    }
                    /*修改主款配饰*/
                    List<String> styleNoList = accessoriesList.stream().map(StyleMainAccessories::getStyleNo).collect(Collectors.toList());
                    List<StyleColor> colorList = listByField("style_no", styleNoList);
                    /*需要反向绑定的配色*/

                    /*新增主款配饰*/
                    if (CollUtil.isNotEmpty(colorList)) {
                        for (StyleColor color : colorList) {
                            StyleMainAccessories styleMainAccessories = new StyleMainAccessories();
                            styleMainAccessories.setIsTrim(color.getIsTrim());
                            styleMainAccessories.setStyleColorId(color.getId());
                            styleMainAccessories.setColorCode(styleColor.getColorCode());
                            styleMainAccessories.setColorName(styleColor.getColorName());
                            styleMainAccessories.setStyleNo(styleColor.getStyleNo());
                            accessoriesList.add(styleMainAccessories);
                        }
                    }
                    styleMainAccessoriesService.saveBatch(accessoriesList);
                }

            }
            addRevampStyleColorDto.setStyleColorPic(styleColor.getStyleColorPic());
            BeanUtils.copyProperties(addRevampStyleColorDto, styleColor);
            /*赋值颜色*/
            styleColor.setColourLibraryId(basicsdatumColourLibrary.getId());
            styleColor.setColorCode(basicsdatumColourLibrary.getColourCode());
            styleColor.setColorName(basicsdatumColourLibrary.getColourName());
            styleColor.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());
            styleColor.setAccessory("");
            styleColor.setAccessoryNo("");
            styleColor.setPrincipalStyle("");
            styleColor.setPrincipalStyleNo("");
            this.updateById(styleColor);
            StyleColor styleColor1 = this.getById(styleColor.getId());

            this.saveOperaLog("修改", "款式配色", styleColor.getColorName(), styleColor.getStyleNo(), styleColor1, old);

            if (StrUtil.isNotBlank(styleColor1.getBom())) {
                /*重新下发配色*/
                dataUpdateScmService.updateStyleColorSendById(styleColor.getId());
            }
        }
        return true;
    }

    /**
     * 同步大货
     * @param styleNo
     * @param newStyleNo
     * @return
     */
    public void updateStyleColor(String bom,String styleNo, String newStyleNo) {
        baseMapper.reviseAllStyleNo(styleNo, newStyleNo);
        if (StringUtils.isNotBlank(bom)) {
            packInfoService.updateBomName(bom,newStyleNo);
        }
    }

    /**
     * 方法描述：删除样衣-款式配色
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delSampleStyleColor(String id, String styleId) {
        /*      List<String> ids = StringUtils.convertList(id);
         *//*批量删除*//*
        baseMapper.deleteBatchIds(ids);*/
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.set("del_flag", "1");
        updateWrapper.eq("style_id", styleId);
        updateWrapper.in("colour_library_id", StringUtils.convertList(id));
        baseMapper.update(null, updateWrapper);
        return true;
    }

    /**
     * 方法描述：删除样衣-款式配色
     *
     * @return boolean
     */
    @Override
    public Boolean delStyleColor(RemoveDto removeDto) {
        /*配色数据和BOM关联的不能删除*/
        List<StyleColor> styleColors = baseMapper.selectBatchIds(StringUtils.convertList(removeDto.getIds()));

    /*    styleColors = styleColors.stream().filter(s -> StringUtils.isBlank(s.getAccessoryNo()) || StringUtils.isBlank(s.getPrincipalStyleNo())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(styleColors)){
            throw new OtherException("该配色存在主款或配饰");
        }*/
        styleColors = styleColors.stream().filter(s -> StringUtils.isNotBlank(s.getBom())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(styleColors)) {
            throw new OtherException("存在BOM关联数据无法删除");
        }
        /*批量删除*/
        this.removeByIds(removeDto);
        return true;
    }


    /**
     * 方法描述：启用停止
     *
     * @param startStopDto 启用停止Dto类
     * @return boolean
     */
    @Override
    public Boolean startStopSampleStyleColor(StartStopDto startStopDto) {
        UpdateWrapper<StyleColor> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        this.startStopLog(startStopDto);
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 方法描述: 修改颜色
     *
     * @param updateColorDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean updateColor(UpdateColorDto updateColorDto) {

        StyleColor styleColor = baseMapper.selectById(updateColorDto.getId());
        BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryServicel.getByOne("colour_code",updateColorDto.getColorCode());
        if (ObjectUtils.isEmpty(basicsdatumColourLibrary)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        /*赋值颜色*/
        styleColor.setColourLibraryId(basicsdatumColourLibrary.getId());
        styleColor.setColorCode(basicsdatumColourLibrary.getColourCode());
        styleColor.setColorName(basicsdatumColourLibrary.getColourName());
        styleColor.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());
        baseMapper.updateById(styleColor);
        /*重新下发配色*/
        dataUpdateScmService.updateStyleColorSendById(styleColor.getId());
        return true;
    }

    /**
     * 方法描述 下发scm
     * 已下发数据不能再次下发
     * 没关联bom的配色不能下发
     * 物料成本为0的不能下发
     * 核价里面总成本为0的不能下发
     * 吊牌价为0不能下发
     *
     * @param ids
     * @return
     */
    @Override
    public ApiResult issueScm(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new OtherException("ids为空");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
/*        queryWrapper.in("id", StringUtils.convertList(ids));
        queryWrapper.ne("scm_send_flag", BaseGlobal.YES);*/
        List<StyleColor> styleColorList = baseMapper.getStyleMainAccessories(StringUtils.convertList(ids));
        /*查询配色是否下发*/
        if (CollectionUtils.isEmpty(styleColorList)) {
            throw new OtherException("存在已下发数据");
        }
        List<String> stringList = styleColorList.stream().filter(s -> StringUtils.isNotBlank(s.getBom())).map(StyleColor::getId).collect(Collectors.toList());

        /*禁止下发未关联bom数据*/
        if (CollectionUtils.isEmpty(stringList) || styleColorList.size() != stringList.size()) {
            String styleNo =  styleColorList.stream().filter(s -> StringUtils.isBlank(s.getBom())).map(StyleColor::getStyleNo).collect(Collectors.joining(","));
            throw new OtherException(styleNo+"无关联BOM，主款或配饰未关联BOM");
        }
        /*查询BOM*/
        queryWrapper.clear();
        queryWrapper.in("code", styleColorList.stream().map(StyleColor::getBom).collect(Collectors.toList()));
        List<PackInfo> packInfoList = packInfoService.list(queryWrapper);
        Map<String, List<PackInfo>> listMap = packInfoList.stream().collect(Collectors.groupingBy(PackInfo::getCode));
        /*查询配色关联的bom总成本价不等为0
         * 先查询物料的总成本 没有则去查询核价里面的总成本*/
        for (StyleColor styleColor : styleColorList) {
            PackInfo packInfo = listMap.get(styleColor.getBom()).get(0);
            PackCommonSearchDto packCommonSearchDto = new PackCommonSearchDto();
            packCommonSearchDto.setForeignId(packInfo.getId());
            packCommonSearchDto.setPackType(PackUtils.PACK_TYPE_DESIGN);
//            核价成本
            BigDecimal packBomCost = packBomService.calculateCosts(packCommonSearchDto);
//            物料成本为0时查询核价信息的总成本
            if (packBomCost.compareTo(BigDecimal.ZERO) == 0) {
//                核价信息总成本
                BigDecimal bigDecimal = packPricingService.countTotalPrice(packInfo.getId(), "1");
                if (bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
                    throw new OtherException(styleColor.getStyleNo() + "的开发成本为0,请联系设计工艺员添加材料到BOM");
                }
            }
        }
        int i = smpService.goods(StringUtils.convertListToString(stringList).split(","));
        if (stringList.size() == i) {
            return ApiResult.success("下发：" + stringList.size() + "条，成功：" + i + "条");
        } else {
            return ApiResult.error("下发：" + stringList.size() + "条，成功：" + i + "条,失败：" + (stringList.size() - i) + "条", 200);
        }
    }

    /**
     * 方法描述 获取款式下的颜色
     *
     * @param styleId
     */
    @Override
    public List<String> getStyleColorId(String styleId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("style_id", styleId);
        queryWrapper.eq("is_defective", BaseGlobal.NO);
        List<StyleColor> styleColorList = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(styleColorList)) {
            return new ArrayList<>();
        }
        return styleColorList.stream().map(StyleColor::getColorCode).collect(Collectors.toList());
    }

    /**
     * 方法描述 关联bom
     *
     * @param relevanceBomDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean relevanceBom(RelevanceBomDto relevanceBomDto) {
        /*查询配色信息*/
        StyleColor styleColor = baseMapper.selectById(relevanceBomDto.getStyleColorId());
        if (ObjectUtils.isEmpty(styleColor)) {
            throw new OtherException("无配色信息");
        }
        if (StringUtils.isNotBlank(styleColor.getBom())) {
            throw new OtherException("该配色已关联bom");
        }
        /*查询bom信息*/
        PackInfo packInfo = packInfoService.getById(relevanceBomDto.getPackInfoId());
        if (ObjectUtils.isEmpty(packInfo)) {
            throw new OtherException("无bom信息");
        }
        if (StringUtils.isNotBlank(packInfo.getStyleNo())) {
            throw new OtherException("该bom以关联bom");
        }
        /*关联bom*/
        styleColor.setBom(packInfo.getCode());
        /*bom关联配色*/
        packInfo.setStyleNo(styleColor.getStyleNo());
        packInfo.setColorCode(styleColor.getColorCode());
        packInfo.setColor(styleColor.getColorName());
        packInfo.setStyleColorId(styleColor.getId());
        packInfo.setName(styleColor.getStyleNo());
        baseMapper.updateById(styleColor);
        packInfoService.updateById(packInfo);
        return true;
    }

    /**
     * 方法描述 修改大货款号,波段
     *
     * @param updateStyleNoBandDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean updateStyleNoBand(UpdateStyleNoBandDto updateStyleNoBandDto) {
        StyleColor sampleStyleColor = baseMapper.selectById(updateStyleNoBandDto.getId());
        String styleNo = sampleStyleColor.getStyleNo();
        StyleColor styleColor1 = new StyleColor();
        styleColor1.setStyleNo(styleNo);
        if (ObjectUtils.isEmpty(sampleStyleColor)) {
            throw new OtherException("id错误");
        }
        /*修改大货款号*/
        if (StringUtils.isNotBlank(sampleStyleColor.getStyleNo()) && StringUtils.isNotBlank(updateStyleNoBandDto.getStyleNo()) && !sampleStyleColor.getStyleNo().equals(updateStyleNoBandDto.getStyleNo())) {

            if (!updateStyleNoBandDto.getStyleNo().substring(0, 1).equals(sampleStyleColor.getStyleNo().substring(0, 1))) {
                throw new OtherException("无法修改大货款号前1位");
            }
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("style_no", updateStyleNoBandDto.getStyleNo());
            StyleColor styleColor = baseMapper.selectOne(queryWrapper);
            if (!ObjectUtils.isEmpty(styleColor)) {
                throw new OtherException("大货款号已存在");
            }
            /**
             * 修改下放大货款号
             */
            baseMapper.reviseAllStyleNo(sampleStyleColor.getStyleNo(), updateStyleNoBandDto.getStyleNo());

            /*修改关联的BOM名称*/
            if (StringUtils.isNotBlank(sampleStyleColor.getBom())) {
                packInfoService.updateBomName(sampleStyleColor.getBom(),updateStyleNoBandDto.getStyleNo());
            }
            /*只会记录最开始的大货款号*/
            if (StringUtils.isBlank(sampleStyleColor.getHisStyleNo())) {
                sampleStyleColor.setHisStyleNo(sampleStyleColor.getStyleNo());
            }
            sampleStyleColor.setStyleNo(updateStyleNoBandDto.getStyleNo());
        }
        /*修改波段*/
    /*    if(StringUtils.isNotBlank(sampleStyleColor.getBandCode()) && StringUtils.isNotBlank(updateStyleNoBandDto.getBandCode())){
            if(!sampleStyleColor.getBandCode().equals(updateStyleNoBandDto.getBandCode())){
                sampleStyleColor.setBandCode(updateStyleNoBandDto.getBandCode());
                sampleStyleColor.setBandName(updateStyleNoBandDto.getBandName());
            }
        }*/
        baseMapper.updateById(sampleStyleColor);
        StyleColor styleColor = new StyleColor();
        styleColor.setStyleNo(updateStyleNoBandDto.getStyleNo());
        this.saveOperaLog("修改大货款号", "款式配色", sampleStyleColor.getColorName(), sampleStyleColor.getStyleNo(), styleColor, styleColor1);
        /*重新下发配色*/
        dataUpdateScmService.updateStyleColorSendById(sampleStyleColor.getId());
        return true;
    }

    /**
     * 方法描述 验证配色是否可修改
     *
     * @param id
     * @return
     */
    @Override
    public Boolean verification(String id) {
        StyleColor styleColor = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(styleColor)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_ATTRIBUTE_NOT_REQUIREMENTS);
        }
        PdmStyleCheckParam pdmStyleCheckParam = new PdmStyleCheckParam();
        pdmStyleCheckParam.setStyleNo(styleColor.getStyleNo());
        pdmStyleCheckParam.setCode(styleColor.getColorCode());
        try {
            Boolean b = smpService.checkColorSize(pdmStyleCheckParam);
            if (!b) {
                throw new OtherException("SCM系统存在投产单或备料单,不允许修改颜色!");
            }
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        }
        return true;
    }

    /**
     * 方法描述 配色解锁
     *
     * @param publicStyleColorDto
     * @return
     */
    @Override
    public Boolean unlockStyleColor(PublicStyleColorDto publicStyleColorDto) {
        if (StringUtils.isBlank(publicStyleColorDto.getScmSendFlag())) {
            throw new OtherException("SCM下发状态为空");
        }
        StyleColor styleColor = baseMapper.selectById(publicStyleColorDto.getId());
        if (ObjectUtils.isEmpty(styleColor)) {
            throw new OtherException("id错误无数据");
        }
        styleColor.setScmSendFlag(publicStyleColorDto.getScmSendFlag());
        baseMapper.updateById(styleColor);
        return true;
    }

    /**
     * 方法描述 新增次品款
     *
     * @param publicStyleColorDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean addDefective(PublicStyleColorDto publicStyleColorDto) {
        /**
         *复制一个配色次品款
         * 校验 计控吊牌确定已确定
         * 大货款号加上次品编号，改为次品 ，同时复制一个bom名称为大货款号加次品编号
         * 复制一个未审核的吊牌
         * 复制出的BOM是样品阶段，里面的物料也修改未样品阶段未下发
         */
        if (StringUtils.isBlank(publicStyleColorDto.getDefectiveName())) {
            throw new OtherException("次品必填");
        }
        if (StringUtils.isBlank(publicStyleColorDto.getDefectiveNo())) {
            throw new OtherException("次品必填");
        }
        if (StringUtils.isBlank(publicStyleColorDto.getColorCode())) {
            throw new OtherException("颜色必填");
        }
        /*配色信息*/
        StyleColor styleColor = baseMapper.selectById(publicStyleColorDto.getId());
        if (ObjectUtils.isEmpty(styleColor) || StringUtils.isBlank(styleColor.getBom())) {
            throw new OtherException("id错误无数据或该配色无bom");
        }
        if (StringUtils.isNotEmpty(styleColor.getDefectiveName())) {
            throw new OtherException("报次款不能生成报次款");
        }
        /*查询次品的大货款号是否重复*/
        int i = baseMapper.isStyleNoExist(styleColor.getStyleNo() + publicStyleColorDto.getDefectiveNo());
        if (i != 0) {
            throw new OtherException("该次品已生成款");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("tsc.style_no", styleColor.getStyleNo());
        List<StyleColorVo> styleColorVoList = baseMapper.colorList(queryWrapper);
        if (CollectionUtils.isEmpty(styleColorVoList)) {
            throw new OtherException("大货款号查无数据");
        }
        StyleColorVo styleColorVo = styleColorVoList.get(0);
        /*判断吊牌价是否确定*/
        if (StringUtils.isBlank(styleColorVo.getProductHangtagConfirm()) || styleColorVo.getProductHangtagConfirm().equals(BaseGlobal.NO)) {
            throw new OtherException("计控吊牌确定未确定");
        }
        /*查询配色关联的资料包*/
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("style_color_id", styleColor.getId());
        queryWrapper1.eq("style_no", styleColor.getStyleNo());
        PackInfo packInfo = packInfoService.getOne(queryWrapper1);

        /*复制配色数据*/
        StyleColor copyStyleColor = new StyleColor();
        BeanUtils.copyProperties(styleColor, copyStyleColor, "id");

        //设置bom编码
        QueryWrapper codeQw = new QueryWrapper();
        codeQw.eq("foreign_id", styleColor.getStyleId());
        long count = packInfoMapper.countByQw(codeQw);
        /*复制配色*/

        BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryServicel.getByOne("colour_code",publicStyleColorDto.getColorCode());
        copyStyleColor.setColorName(basicsdatumColourLibrary.getColourName());
        copyStyleColor.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());
        copyStyleColor.setColorCode(basicsdatumColourLibrary.getColourCode());
        copyStyleColor.setColourLibraryId(basicsdatumColourLibrary.getId());
        copyStyleColor.setStyleNo(styleColor.getStyleNo() + publicStyleColorDto.getDefectiveNo());
        copyStyleColor.setDefectiveName(publicStyleColorDto.getDefectiveName());
        copyStyleColor.setDefectiveNo(publicStyleColorDto.getDefectiveNo());
        copyStyleColor.setIsDefective(BaseGlobal.YES);
        copyStyleColor.setBom(styleColor.getDesignNo() + StrUtil.DASHED + (count + 1));
        copyStyleColor.setWareCode(null);
        copyStyleColor.setScmSendFlag(BaseGlobal.NO);
        copyStyleColor.setBomStatus(BaseGlobal.STATUS_NORMAL);
        copyStyleColor.setPrincipalStyleNo("");
        copyStyleColor.setPrincipalStyle("");
        copyStyleColor.setAccessoryNo("");
        copyStyleColor.setAccessory("");
        copyStyleColor.setId(null);
        baseMapper.insert(copyStyleColor);

        /*吊牌复制*/
        hangTagService.copyPack(styleColor.getStyleNo(), copyStyleColor.getStyleNo());
        /*新建一个资料包*/
        PackInfo copyPackInfo = new PackInfo();
        BeanUtils.copyProperties(packInfo, copyPackInfo, "id");
        copyPackInfo.setStyleNo(copyStyleColor.getStyleNo());
        copyPackInfo.setStyleColorId(copyStyleColor.getId());
        copyPackInfo.setCode(styleColor.getDesignNo() + StrUtil.DASHED + (count + 1));
        copyPackInfo.setName(styleColor.getStyleNo() + publicStyleColorDto.getDefectiveNo());
        copyPackInfo.setColor(basicsdatumColourLibrary.getColourName());
        copyPackInfo.setColorCode(basicsdatumColourLibrary.getColourCode());
        packInfoService.save(copyPackInfo);

        PackInfoStatus packInfoStatus = packInfoStatusService.get(packInfo.getId(), PackUtils.PACK_TYPE_DESIGN);

        /*复制资料包里面的数据*/
        packInfoService.copyPack(packInfo.getId(), packInfoStatus.getPackType(), copyPackInfo.getId(), packInfoStatus.getPackType(), BaseGlobal.YES, BasicNumber.ZERO.getNumber());
        /*复制状态*/
        packInfoStatusService.copy(packInfo.getId(), packInfoStatus.getPackType(), copyPackInfo.getId(), packInfoStatus.getPackType(), BaseGlobal.YES);
        /*查询BOM状态，BOM阶段修改未为样品 BOM里面物料也修改为样品*/
        /*复制出来的BOM*/
        PackInfoStatus copyPackInfoStatus = packInfoStatusService.get(copyPackInfo.getId(), packInfoStatus.getPackType());
        copyPackInfoStatus.setBomStatus(BaseGlobal.STATUS_NORMAL);
        packInfoStatusService.updateById(copyPackInfoStatus);

        /*        *//*查询物料清单*//*
        List<PackBomVo> packBomVoList = packBomVersionService.getEnableVersionBomList(copyPackInfo.getId(), packInfoStatus.getPackType());
        packBomVoList.forEach(p -> p.setStageFlag(PackUtils.PACK_TYPE_DESIGN));
        *//*修改阶段*//*
        List<PackBom> packBomList = BeanUtil.copyToList(packBomVoList, PackBom.class);
        packBomService.saveOrUpdateBatch(packBomList);*/
        /*复制出款式定价确定数据*/
        StylePricing stylePricing = new StylePricing();
        stylePricing.setControlConfirm(styleColorVo.getControlConfirm());
        stylePricing.setProductHangtagConfirm(styleColorVo.getProductHangtagConfirm());
        stylePricing.setControlHangtagConfirm(styleColorVo.getControlHangtagConfirm());
        stylePricing.setPackId(copyPackInfo.getId());
        stylePricing.setCompanyCode(baseController.getUserCompany());
        stylePricingMapper.insert(stylePricing);
        return true;
    }

    /**
     * 方法描述 更新下单标记
     *
     * @param publicStyleColorDto
     * @return
     */
    @Override
    public Boolean updateOrderFlag(PublicStyleColorDto publicStyleColorDto) {
        if (StringUtils.isBlank(publicStyleColorDto.getOrderFlag())) {
            throw new OtherException("下单标记为空");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        List<String> ids = StringUtils.convertList(publicStyleColorDto.getId());
        /*下单时需要校验维度信息必填*/
        if (StringUtils.equals(publicStyleColorDto.getOrderFlag(), BaseGlobal.STATUS_CLOSE)) {
            updateWrapper.set("order_date", new Date());
            /*获取款式数据*/
            List<StyleColor> styleColorList = baseMapper.selectBatchIds(ids);
            Map<String, StyleColor> styleColorMap = styleColorList.stream().collect(Collectors.toMap(k -> k.getId(), v -> v, (a, b) -> b));
            /*获取款式id*/
            List<String> styleIdList = styleColorList.stream().map(StyleColor::getStyleId).collect(Collectors.toList());
            /*款式列表可能下单不同款下面的配色*/
            List<Style> styleList = styleService.listByIds(styleIdList);
            Map<String, Style> styleMap = styleList.stream().collect(Collectors.toMap(k -> k.getId(), v -> v, (a, b) -> b));
            /*获取维度必填的维度信息*/
            for (String id : ids) {
                StyleColor styleColor = styleColorMap.get(id);
                DimensionLabelsSearchDto dto = new DimensionLabelsSearchDto();
                BeanUtil.copyProperties(styleMap.get(styleColor.getStyleId()), dto);
                dto.setId(styleColor.getStyleId());
                dto.setForeignId(id);
                dto.setDataGroup(FieldValDataGroupConstant.STYLE_COLOR);
                List<FieldManagementVo> managementVoList = styleService.queryDimensionLabels(dto);
                /*过滤必填字段*/
                managementVoList = managementVoList.stream().filter(f -> StringUtils.equals(f.getIsMustFill(), BaseGlobal.STATUS_CLOSE) && StringUtils.isBlank(f.getVal())).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(managementVoList)) {
                    String fieldName = managementVoList.stream().map(FieldManagementVo::getFieldName).collect(Collectors.joining(","));
                    throw new OtherException("大货款号：" + styleColor.getStyleNo() + "中的" + fieldName + "未填写");
                }
            }
        } else {
            updateWrapper.set("order_date", null);
        }

        updateWrapper.set("order_flag", publicStyleColorDto.getOrderFlag());
        updateWrapper.in("id", ids);
        baseMapper.update(null, updateWrapper);
        return true;
    }

    @Override
    public void updateTagPrice(String id, BigDecimal tagPrice) {
        if(StringUtils.isNotBlank(id)){
            StyleColor styleColor = new StyleColor();
            styleColor.setId(id);
            styleColor.setTagPrice(tagPrice);
            styleColor.updateInit();
            super.updateById(styleColor);
            /*重新下发配色*/
            dataUpdateScmService.updateStyleColorSendById(id);
        }
    }

    @Override
    public void updateProductStyle(String id, String productStyle, String productStyleName) {
        StyleColor styleColor = new StyleColor();
        styleColor.setId(id);
        styleColor.setProductStyle(productStyle);
        styleColor.setProductStyleName(productStyleName);
        super.updateById(styleColor);
    }

    /**
     * 取消关联Bom
     *
     * @param publicStyleColorDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean disassociateBom(PublicStyleColorDto publicStyleColorDto) {
        /*查询配色数据*/
        List<StyleColor> styleColorList = baseMapper.selectBatchIds(StringUtils.convertList(publicStyleColorDto.getId()));
        /*校验是否关联配色*/
        List<String> stringList = styleColorList.stream().filter(s -> StringUtils.isNotBlank(s.getBom())).map(StyleColor::getBom).collect(Collectors.toList());
        if (styleColorList.size() != stringList.size()) {
            throw new OtherException("未关联款号");
        }
        /*查询BOM*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("code", stringList);
        List<PackInfo> packInfoList = packInfoService.list(queryWrapper);
        /*取消关联*/
        styleColorList.forEach(s -> {
            /*验证是否下发*/
            if (!s.getScmSendFlag().equals(BaseGlobal.NO)) {
                throw new OtherException("数据存在已下发");
            }
            s.setBom("");
        });
        /*取消关联*/
        packInfoList.forEach(p -> {
            p.setStyleNo("");
            p.setColorCode("");
            p.setColor("");
            p.setStyleColorId("");
            p.setName(p.getDesignNo() + p.getStyleName() + " BOM");
        });
        updateBatchById(styleColorList);
        packInfoService.updateBatchById(packInfoList);
        return true;
    }

    /**
     * 查询款式配色设计维度数据
     *
     * @param id 配色id
     * @return
     */
    @Override
    public List<FieldManagementVo> getStyleColorDynamicDataById(String id) {
        if (StringUtils.isBlank(id)) {
            throw new OtherException("配色id不能为空");
        }
        StyleColor styleColor = baseMapper.selectById(id);
        Style style = styleService.getById(styleColor.getStyleId());
        DimensionLabelsSearchDto dto = new DimensionLabelsSearchDto();
        BeanUtil.copyProperties(style, dto);
        dto.setId(styleColor.getStyleId());
        dto.setForeignId(id);
        dto.setDataGroup(FieldValDataGroupConstant.STYLE_COLOR);
        return styleService.queryDimensionLabels(dto);
    }

    /**
     * 保存配色维度数据
     *
     * @param technologyInfo
     * @return
     */
    @Override
    public Boolean saveStyleColorDynamicData(List<FieldVal> technologyInfo) {
        // 保存工艺信息
        fieldValService.save(technologyInfo.get(0).getForeignId(), FieldValDataGroupConstant.STYLE_COLOR, technologyInfo);
        /*修改后下发已下发的数据*/
        dataUpdateScmService.updateStyleColorSendById(technologyInfo.get(0).getForeignId());
        return true;
    }

    /**
     * 查询款式配色主款配饰数据
     *
     * @param dto
     * @return
     */
    @Override
    public PageInfo<StyleColorVo> getStyleMainAccessoriesList(Principal user, QueryStyleColorDto dto) {
        if (StringUtils.isEmpty(dto.getId()) || StringUtils.isEmpty(dto.getIsTrim())) {
            throw new OtherException("配色id不能为空");
        }
        List<StyleMainAccessories> mainAccessoriesList = styleMainAccessoriesService.styleMainAccessoriesList(dto.getId(), null);
        List<StyleColorVo> styleColorVoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(mainAccessoriesList)) {
            List<String> styleNoList = mainAccessoriesList.stream().map(StyleMainAccessories::getStyleNo).collect(Collectors.toList());
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("style_no", styleNoList);
            queryWrapper.eq(StringUtils.isNotBlank(dto.getStyleNo()), "style_no", dto.getStyleNo());
            queryWrapper.eq(StringUtils.isNotBlank(dto.getColorName()), "color_name", dto.getColorName());
            PageHelper.startPage(dto);
            styleColorVoList = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), StyleColorVo.class);
            /*查询款式配色图*/
            stylePicUtils.setStyleColorPic2(styleColorVoList, "styleColorPic");
        }
        PageInfo<StyleColorVo> pageInfo = new PageInfo<>(styleColorVoList);
        return pageInfo;
    }



    /**
     * 复制配色
     *
     * @param idDto
     * @return
     */
    @Override
    public Boolean copyStyleColor(IdDto idDto) {

        StyleColor styleColor = baseMapper.selectById(idDto.getId());
        styleColor.insertInit();
        styleColor.setId(null);
        styleColor.setScmSendFlag(null);
        styleColor.setBom(null);
//        查询款式
        Style style = styleService.getById(styleColor.getStyleId());
        styleColor.setStyleNo(getNextCode(style.getBrand(), style.getYearName(),style.getSeason(), StringUtils.isNotEmpty(styleColor.getBandName()) ? styleColor.getBandName() : style.getBandName(), style.getProdCategory(), StringUtils.isNotBlank(style.getOldDesignNo())?style.getOldDesignNo():style.getDesignNo() ,style.getId(),styleColor.getIsLuxury(),  1));
        styleColor.setHisStyleNo(null);
        styleColor.setWareCode(null);
        styleColor.setHistoricalData(BaseGlobal.NO);
        baseMapper.insert(styleColor);
        return true;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
