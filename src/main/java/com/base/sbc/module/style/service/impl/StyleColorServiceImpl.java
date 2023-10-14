/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import com.base.sbc.config.utils.StyleNoImgUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumColourLibraryMapper;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formType.entity.FieldVal;
import com.base.sbc.module.formType.service.FieldValService;
import com.base.sbc.module.formType.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.mapper.PackInfoMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.mapper.StylePricingMapper;
import com.base.sbc.module.pricing.service.PricingTemplateService;
import com.base.sbc.module.smp.DataUpdateScmService;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.smp.dto.PdmStyleCheckParam;
import com.base.sbc.module.style.dto.*;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.StyleColorVo;
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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
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

    private final BasicsdatumColourLibraryMapper basicsdatumColourLibraryMapper;

    private final UserUtils userUtils;

    private final AttachmentService attachmentService;

    private final DataUpdateScmService dataUpdateScmService;

    private final StylePricingMapper stylePricingMapper;

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
        queryDto.setOrderBy("ts.design_no asc,tsc.create_date desc");
        PageHelper.startPage(queryDto);
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
        queryWrapper.notEmptyLike("ts.prod_category3rd_name",queryDto.getProdCategory2ndName());
        queryWrapper.notEmptyLike("ts.prod_category2nd_name",queryDto.getProdCategory2ndName());
        queryWrapper.notEmptyLike("ts.prod_category1st_name",queryDto.getProdCategory2ndName());


        queryWrapper.between("tsc.create_date", queryDto.getCreateDate());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getCreateName()), "tsc.create_name", queryDto.getCreateName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getNewDate()), "tsc.new_date", queryDto.getNewDate());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getIsTrim()), "tsc.is_luxury", queryDto.getIsTrim());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSubdivideName()), "tsc.subdivide_name", queryDto.getSubdivideName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendMainFabricDate()), "tsc.send_main_fabric_date", queryDto.getSendMainFabricDate());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendBatchingDate1()), "tsc.send_batching_date1", queryDto.getSendBatchingDate1());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendBatchingDate2()), "tsc.send_batching_date2", queryDto.getSendBatchingDate2());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendBatchingDate3()), "tsc.send_batching_date3", queryDto.getSendBatchingDate3());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSendSingleDate()), "tsc.send_single_date", queryDto.getSendSingleDate());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getDesignDetailDate()), "tsc.design_detail_date", queryDto.getDesignDetailDate());
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
        // 数据权限
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.styleColor.getK(), "ts.");
        /*获取配色数据*/
        List<StyleColorVo> sampleStyleColorList = new ArrayList<>();
        if (StringUtils.isNotBlank(queryDto.getColorListFlag())) {
            queryWrapper.eq("tsc.del_flag", "0");
            queryWrapper.orderByDesc("tsc.create_date");
//            查询配色列表
            sampleStyleColorList = baseMapper.colorList(queryWrapper);
        } else {
            queryWrapper.eq("ts.del_flag", "0");
            queryWrapper.orderByDesc("ts.create_date");
//            查询款式配色
            sampleStyleColorList = baseMapper.styleColorList(queryWrapper);
            List<String> stringList = new IdGen().getIds(sampleStyleColorList.size());
            int index = 0;
            for (StyleColorVo styleColorVo : sampleStyleColorList) {
                styleColorVo.setIssuerId(stringList.get(index));
                index++;
            }
        }
        /*查询款式图*/
        attachmentService.setListStylePic(sampleStyleColorList, "stylePic");

        if (user != null) {
            /*查询款式配色图*/
            GroupUser userBy = userUtils.getUserBy(user);
            StyleNoImgUtils.setStyleColorPic(userBy, sampleStyleColorList, "styleColorPic");
        }

        PageInfo<StyleColorVo> pageInfo = new PageInfo<>(sampleStyleColorList);
        return pageInfo;
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
        if (StringUtils.isNotBlank(querySampleStyleColorDto.getStyleNo())) {
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
    public Boolean batchAddSampleStyleColor(List<AddRevampStyleColorDto> list) {
        int index = 0;
        /*查询颜色*/
        List<String> colourLibraryIds = list.stream().map(AddRevampStyleColorDto::getColourLibraryId).distinct().collect(Collectors.toList());
        List<BasicsdatumColourLibrary> libraryList = basicsdatumColourLibraryMapper.selectBatchIds(colourLibraryIds);
        /*颜色数据*/
        Map<String, BasicsdatumColourLibrary> map = libraryList.stream().collect(Collectors.toMap(BasicsdatumColourLibrary::getId, c -> c));
        /*查询款式主数据*/
        List<String> styleMasterDataIds = list.stream().map(AddRevampStyleColorDto::getStyleId).distinct().collect(Collectors.toList());
        List<Style> masterDataList = styleService.listByIds(styleMasterDataIds);
        Map<String, Style> map1 = masterDataList.stream().collect(Collectors.toMap(Style::getId, s -> s));
        /*款式主数据数据*/
        for (AddRevampStyleColorDto addRevampStyleColorDto : list) {
            BasicsdatumColourLibrary basicsdatumColourLibrary = map.get(addRevampStyleColorDto.getColourLibraryId());
            Style style = map1.get(addRevampStyleColorDto.getStyleId());
            addRevampStyleColorDto.setStyleId(style.getId());
            addRevampStyleColorDto.setColorName(basicsdatumColourLibrary.getColourName());
            addRevampStyleColorDto.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());
            addRevampStyleColorDto.setColorCode(basicsdatumColourLibrary.getColourCode());
            addRevampStyleColorDto.setStyleNo(getNextCode(style.getBrand(), style.getYearName(), style.getBandName(), style.getProdCategory(), style.getDesignNo(), style.getDesigner(), ++index));
        }
        List<StyleColor> styleColorList = BeanUtil.copyToList(list, StyleColor.class);
        saveBatch(styleColorList);
        return true;
    }

    /**
     * 大货款号生成规则 品牌 + 年份  + 波段 +品类 +设计款号流水号+颜色流水号
     *
     * @param
     * @param brand
     * @param year
     * @param
     * @param bandName
     * @param category
     * @param designNo
     * @return
     */

    public String getNextCode(String brand, String year, String bandName, String category, String designNo, String designer, int index) {
        if (ccmFeignService.getSwitchByCode("STYLE_EQUAL_DESIGN_NO")) {
            return designNo;
        }
        if (StrUtil.contains(category, StrUtil.COMMA)) {
            category = getCategory(category);
        }
        if (StringUtils.isBlank(bandName)) {
            throw new OtherException("款式波段为空");
        }
     /*   if (StringUtils.isBlank(month)) {
            throw new OtherException("款式月份为空");
        }*/
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
//            月份
//            month = getMonth(bandName);
//            波段
            bandName = getBandName(bandName);

            /*获取款式流水号*/
            /**
             *款式流水号： 款式去掉设计师设计师标识 获取后三位字符串
             */
            String[] designers = designer.split(",");
            if (designers == null || designers.length == 0) {
                throw new OtherException("设计师未缺少编码");
            }
            String designNo1 = designNo.replace(designers[1], "");
            designNo = designNo1.substring(designNo1.length() - 3);
        } catch (Exception e) {
            throw new OtherException("大货编码生成失败");
        }
//        获取款式下的配色
        String styleNo = brand + yearOn + bandName + category + designNo;
        String number = baseMapper.getStyleColorNumber(styleNo);
        String maxMark ="0";
        if(StringUtils.isNotBlank(number)){
            /*获取最大流水号*/
             maxMark = number.replaceAll(styleNo,"");
        }
        /*拼接流水号*/
        styleNo = styleNo + (Long.valueOf(maxMark) + index);
        /*查询编码是否重复*/
        int i = baseMapper.isStyleNoExist(styleNo);
        if (i != 0) {
            throw new OtherException("编码重复");
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
        Pattern pattern = Pattern.compile("[a-z||A-Z]");
        Matcher matcher = pattern.matcher(bandName);
        String Letter = "";
        String month = "";
        // 打印匹配到的字母
        while (matcher.find()) {
            Letter += matcher.group();
        }
        if (!StringUtils.isEmpty(Letter)) {
            month = getMonth(bandName, Letter);
            Letter = Letter.toUpperCase();
            char[] charArray = Letter.toCharArray();
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
            month = month.equals("10") ? "A" : month.equals("11") ? "B" : month.equals("12") ? "C" : "";
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
            QueryWrapper<StyleColor> queryWrapper = new QueryWrapper<>();
            /*新增*/
            BeanUtils.copyProperties(addRevampStyleColorDto, styleColor);
            styleColor.setCompanyCode(baseController.getUserCompany());
            styleColor.insertInit();
            this.save(styleColor, "款式配色");
        } else {
            /*修改*/
            if (StringUtils.isBlank(addRevampStyleColorDto.getColourLibraryId())) {
                throw new OtherException("颜色不能为空");
            }
            BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryMapper.selectById(addRevampStyleColorDto.getColourLibraryId());
            if(ObjectUtils.isEmpty(basicsdatumColourLibrary)){
                throw new OtherException("无颜色");
            }
            styleColor = baseMapper.selectById(addRevampStyleColorDto.getId());
            StyleColor old = new StyleColor();
            BeanUtil.copyProperties(styleColor, old);
            if (!addRevampStyleColorDto.getBandCode().equals(styleColor.getBandCode())) {
                /*新大货款号 ：换标波段生成的字符*/
                /**
                 * 先生成波段之前的字符串替换为空，在拼接
                 */
                Style style = styleService.getById(styleColor.getStyleId());
                //获取年份
                String year = getYearOn(style.getYearName());
                /*品牌*/
                String brand = style.getBrand();
                /*大货款的前及位*/
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
                baseMapper.reviseAllStyleNo(styleColor.getStyleNo(), addRevampStyleColorDto.getStyleNo());
            }
            /*判断波段及细分是否改动 改动则需要同步大货款号*/
            if (StringUtils.isNotBlank(addRevampStyleColorDto.getSubdivide())) {
                /**
                 * 修改所有引用的大货款号
                 * 查看之前有没有细分
                 */
                String styleNo ="";
                if (StrUtil.isNotBlank(styleColor.getSubdivide())) {
                    styleNo = styleColor.getStyleNo().replace(styleColor.getSubdivide(), "");
                } else {
                    styleNo = styleColor.getStyleNo();
                }
                baseMapper.reviseAllStyleNo(styleColor.getStyleNo(), styleNo + addRevampStyleColorDto.getSubdivide());
                /*新大货款号=大货款号+细分*/
                addRevampStyleColorDto.setStyleNo(styleNo + addRevampStyleColorDto.getSubdivide());
            }
            if (ObjectUtils.isEmpty(styleColor)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            addRevampStyleColorDto.setStyleColorPic(styleColor.getStyleColorPic());
            BeanUtils.copyProperties(addRevampStyleColorDto, styleColor);
            /*赋值颜色*/
            styleColor.setColourLibraryId(basicsdatumColourLibrary.getId());
            styleColor.setColorCode(basicsdatumColourLibrary.getColourCode());
            styleColor.setColorName(basicsdatumColourLibrary.getColourName());
            styleColor.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());

            this.updateById(styleColor);
            StyleColor styleColor1 = this.getById(styleColor.getId());

            this.saveOperaLog("修改", "款式配色", styleColor.getColorName(), styleColor.getStyleNo(), styleColor1, old);

            /*重新下发配色*/
            dataUpdateScmService.updateStyleColorSendById(styleColor.getId());
        }
        return true;
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

     * @return boolean
     */
    @Override
    public Boolean delStyleColor(RemoveDto removeDto) {
        /*配色数据和BOM关联的不能删除*/
        List<StyleColor> styleColors = baseMapper.selectBatchIds(StringUtils.convertList(removeDto.getIds()));
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
        BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryMapper.selectById(updateColorDto.getColourLibraryId());
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
     * @param ids
     * @return
     */
    @Override
    public ApiResult issueScm(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new OtherException("ids为空");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("id", StringUtils.convertList(ids));
        queryWrapper.ne("scm_send_flag", BaseGlobal.YES);
        List<StyleColor> styleColorList = baseMapper.selectList(queryWrapper);
        /*查询配色是否下发*/
        if (CollectionUtils.isEmpty(styleColorList)) {
            throw new OtherException("存在已下发数据");
        }
        List<String> stringList = styleColorList.stream().filter(s -> StringUtils.isNotBlank(s.getBom())).map(StyleColor::getId).collect(Collectors.toList());
        /*禁止下发未关联bom数据*/
        if (CollectionUtils.isEmpty(stringList)) {
            throw new OtherException("无数据关联bom");
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
            /*核价成本*/
            BigDecimal packBomCost = packBomService.calculateCosts(packCommonSearchDto);
            /*物料成本为0时查询核价信息的总成本*/
            if (packBomCost.compareTo(BigDecimal.ZERO) == 0) {
                /*核价信息总成本*/
                Map<String, BigDecimal> otherStatistics = packPricingService.calculateCosts(packCommonSearchDto);
                /*将value中的值转成LIst汇总成本*/
                List<BigDecimal> valuesList = new ArrayList<>(otherStatistics.values());
                BigDecimal totalCost = valuesList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                if (totalCost.compareTo(BigDecimal.ZERO) == 0) {
                    throw new OtherException(styleColor.getStyleNo() + "的开发成本为0,请联系设计工艺员添加材料到BOM");
                }
            }
        }
        int i = smpService.goods(StringUtils.convertListToString(stringList).split(","));
        if (StringUtils.convertList(ids).size() == i) {
            return ApiResult.success("下发：" + StringUtils.convertList(ids).size() + "条，成功：" + i + "条");
        } else {
            return ApiResult.error("下发：" + StringUtils.convertList(ids).size() + "条，成功：" + i + "条,失败：" + (StringUtils.convertList(ids).size() - i) + "条", 200);
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
        return styleColorList.stream().map(StyleColor::getColourLibraryId).collect(Collectors.toList());
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
        StyleColor styleColor1 =new StyleColor();
        styleColor1.setStyleNo(styleNo);
        if (ObjectUtils.isEmpty(sampleStyleColor)) {
            throw new OtherException("id错误");
        }
        /*修改大货款号*/
        if (StringUtils.isNotBlank(sampleStyleColor.getStyleNo()) && StringUtils.isNotBlank(updateStyleNoBandDto.getStyleNo()) && !sampleStyleColor.getStyleNo().equals(updateStyleNoBandDto.getStyleNo())) {

            if (!updateStyleNoBandDto.getStyleNo().substring(0, 5).equals(sampleStyleColor.getStyleNo().substring(0, 5))) {
                throw new OtherException("无法修改大货款号前五位");
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

        StyleColor styleColor =new StyleColor();
        styleColor.setStyleNo(updateStyleNoBandDto.getStyleNo());

        this.saveOperaLog("修改大货款号", "款式配色", sampleStyleColor.getColorName(), sampleStyleColor.getStyleNo(), styleColor, styleColor1 );
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
                throw new OtherException("暂不支持修改");
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
         * 大货款号加上次品编号，改为次品 ，同时复制一个bom名称为次品编号
         */
        if (StringUtils.isBlank(publicStyleColorDto.getDefectiveName())) {
            throw new OtherException("次品必填");
        }
        if (StringUtils.isBlank(publicStyleColorDto.getDefectiveNo())) {
            throw new OtherException("次品必填");
        }
        if (StringUtils.isBlank(publicStyleColorDto.getColourLibraryId())) {
            throw new OtherException("颜色必填");
        }
        /*配色信息*/
        StyleColor styleColor = baseMapper.selectById(publicStyleColorDto.getId());
        if (ObjectUtils.isEmpty(styleColor) || StringUtils.isBlank(styleColor.getBom())) {
            throw new OtherException("id错误无数据或该配色无bom");
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
        PackInfo packInfo = packInfoService.getOne(queryWrapper1);

        /*复制配色数据*/
        StyleColor copyStyleColor = new StyleColor();
        BeanUtils.copyProperties(styleColor, copyStyleColor, "id");

        //设置bom编码
        QueryWrapper codeQw = new QueryWrapper();
        codeQw.eq("foreign_id", styleColor.getStyleId());
        long count = packInfoMapper.countByQw(codeQw);
        /*复制配色*/

        BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryMapper.selectById(publicStyleColorDto.getColourLibraryId());
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
        copyStyleColor.setId(null);
        baseMapper.insert(copyStyleColor);

        /*新建一个资料包*/
        PackInfo copyPackInfo = new PackInfo();
        BeanUtils.copyProperties(packInfo, copyPackInfo, "id");
        copyPackInfo.setStyleNo(copyStyleColor.getStyleNo());
        copyPackInfo.setStyleColorId(copyStyleColor.getId());
        copyPackInfo.setCode(styleColor.getDesignNo() + StrUtil.DASHED + (count + 1));
        copyPackInfo.setName(publicStyleColorDto.getDefectiveNo());
        copyPackInfo.setColor(basicsdatumColourLibrary.getColourName());
        copyPackInfo.setColorCode(basicsdatumColourLibrary.getColourCode());
        packInfoService.save(copyPackInfo);


        PackInfoStatus packInfoStatus = packInfoStatusService.get(packInfo.getId(), PackUtils.PACK_TYPE_DESIGN);

        /*复制资料包里面的数据*/
        packInfoService.copyPack(packInfo.getId(), packInfoStatus.getPackType(), copyPackInfo.getId(), packInfoStatus.getPackType(), BasicNumber.ZERO.getNumber());
        /*复制状态*/
        packInfoStatusService.copy(packInfo.getId(), packInfoStatus.getPackType(), copyPackInfo.getId(), packInfoStatus.getPackType());
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
        updateWrapper.set("order_flag", publicStyleColorDto.getOrderFlag());
        updateWrapper.in("id", StringUtils.convertList(publicStyleColorDto.getId()));
        baseMapper.update(null, updateWrapper);
        return true;
    }

    @Override
    public void updateTagPrice(String id, BigDecimal tagPrice) {
        StyleColor styleColor = new StyleColor();
        styleColor.setId(id);
        styleColor.setTagPrice(tagPrice);
        styleColor.updateInit();
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
            p.setName(p.getDesignNo()+ p.getStyleName()+" BOM");
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
    /** 自定义方法区 不替换的区域【other_end】 **/

}
