/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.band.service.BandService;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumColourLibraryMapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.mapper.PackInfoMapper;
import com.base.sbc.module.sample.dto.*;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.entity.SampleStyleColor;
import com.base.sbc.module.sample.mapper.SampleDesignMapper;
import com.base.sbc.module.sample.mapper.SampleStyleColorMapper;
import com.base.sbc.module.sample.service.SampleStyleColorService;
import com.base.sbc.module.sample.vo.SampleStyleColorVo;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.smp.dto.PdmStyleCheckParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
public class SampleStyleColorServiceImpl extends BaseServiceImpl<SampleStyleColorMapper, SampleStyleColor> implements SampleStyleColorService {

	private final BaseController baseController;

	private final BasicsdatumColourLibraryMapper basicsdatumColourLibraryMapper;

	private final SampleDesignMapper sampleDesignMapper;

	private final BandService bandService;

    @Lazy
    @Resource
	private SmpService smpService;


	private final PackInfoMapper packInfoMapper;


/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 样衣-款式配色分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    public PageInfo<SampleStyleColorVo> getSampleStyleColorList(QuerySampleStyleColorDto queryDto) {
        /*分页*/
        PageHelper.startPage(queryDto);
        QueryWrapper<SampleStyleColor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ssc.company_code", baseController.getUserCompany());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSampleDesignId()), "ssc.sample_design_id", queryDto.getSampleDesignId());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getColorSpecification()), "ssc.color_specification", queryDto.getColorSpecification());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getStyleNo()), "ssc.style_no", queryDto.getStyleNo());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSubdivide()), "ssc.subdivide", queryDto.getSubdivide());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getIsTrim()), "ssc.is_trim", queryDto.getIsTrim());
        String meetFlag ="";
        if (StrUtil.isNotBlank(queryDto.getMeetFlag())) {
            if (queryDto.getMeetFlag().equals(BaseGlobal.STATUS_NORMAL)) {
                meetFlag = BaseGlobal.STATUS_CLOSE;
            }
        }
        queryWrapper.eq("ssc.del_flag", "0");
        queryWrapper.orderByDesc("ssc.create_date");
        /*查询样衣-款式配色数据*/
        List<SampleStyleColorVo> sampleStyleColorList = baseMapper.getSampleStyleColorList(queryWrapper,meetFlag);
        PageInfo<SampleStyleColorVo> pageInfo = new PageInfo<>(sampleStyleColorList);
        return pageInfo;
    }

    /**
     * 方法描述: 获取款式或配饰
     *
     * @return
     */
    @Override
    public  List<SampleStyleColorVo> getStyleAccessoryBystyleNo(String designNo) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("design_no", designNo);
        SampleDesign sampleDesign = sampleDesignMapper.selectOne(qw);
        if (ObjectUtils.isEmpty(sampleDesign)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        QueryWrapper<SampleStyleColor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq("sample_design_id",sampleDesign.getId());
        List<SampleStyleColor> sampleStyleColorList = baseMapper.selectList(queryWrapper);
        /*转换vo*/
        List<SampleStyleColorVo> list = BeanUtil.copyToList(sampleStyleColorList, SampleStyleColorVo.class);
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
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.set("tag_price",updateTagPriceDto.getTagPrice());
        updateWrapper.eq("id",updateTagPriceDto.getId());
        baseMapper.update(null,updateWrapper);
        return true;
    }

    /**
     * 大货款号查询
     *
     * @param querySampleStyleColorDto
     * @return
     */
    @Override
    public List<SampleStyleColorVo> getByStyleNo(QuerySampleStyleColorDto querySampleStyleColorDto) {
        if(StringUtils.isNotBlank(querySampleStyleColorDto.getStyleNo())){
            throw new OtherException(BaseErrorEnum.ERR_MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION);
        }
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.in("style_no", StringUtils.convertList(querySampleStyleColorDto.getStyleNo()));
        List<SampleStyleColor> list= baseMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(list)){
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        List<SampleStyleColorVo> sampleStyleColorVoList =  BeanUtil.copyToList(list, SampleStyleColorVo.class);
        return sampleStyleColorVoList;
    }

    /**
     * 方法描述: 批量新增款式配色-款式配色
     *
     * @param list@return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean batchAddSampleStyleColor(List<AddRevampSampleStyleColorDto> list) {

         int index =0;
        for (AddRevampSampleStyleColorDto addRevampSampleStyleColorDto : list) {
            BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryMapper.selectById(addRevampSampleStyleColorDto.getColourLibraryId());
            SampleDesign sampleDesign = sampleDesignMapper.selectById(addRevampSampleStyleColorDto.getSampleDesignId());
            addRevampSampleStyleColorDto.setColorName(basicsdatumColourLibrary.getColourName());
            addRevampSampleStyleColorDto.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());
            addRevampSampleStyleColorDto.setColorCode(basicsdatumColourLibrary.getColourCode());
            addRevampSampleStyleColorDto.setStyleNo(getNextCode(addRevampSampleStyleColorDto.getSampleDesignId(), sampleDesign.getBrand(), sampleDesign.getYearName(), sampleDesign.getMonth(), sampleDesign.getBandName(), sampleDesign.getProdCategory(), sampleDesign.getDesignNo(), index++));
        }
        List<SampleStyleColor> sampleStyleColorList = BeanUtil.copyToList(list, SampleStyleColor.class);
        saveBatch(sampleStyleColorList);
        return true;
    }

    /**
     * 大货款号生成规则 品牌 + 年份 +月份 + 波段 +品类 +设计款号流水号+颜色标识
     * @param sampleDesignId
     * @param brand
     * @param year
     * @param month
     * @param bandName
     * @param category
     * @param designNo
     * @return
     */

    public String getNextCode(String sampleDesignId,String brand,String year,String month,String bandName,String category,String designNo,int index) {
        if (StrUtil.contains(category, StrUtil.COMMA)) {
            category = getCategory(category);
        }
        if (StringUtils.isBlank(bandName)){
            throw new OtherException("款式波段为空");
        }
        if (StringUtils.isBlank(month)){
            throw new OtherException("款式月份为空");
        }
        if (StringUtils.isBlank(brand)){
            throw new OtherException("款式品牌为空");
        }
        if (StringUtils.isBlank(year)){
            throw new OtherException("款式年份为空");
        }

        String yearOn ="";
        try {
//        获取年份
            int initial = Integer.parseInt("2019");
            int year1 = Integer.parseInt(year);
            int ascii = 'A';
            /*超过2044年重新开始*/
            if(year1 <= 2044){
                char c = (char) (ascii + (year1 - initial));
                yearOn =String.valueOf(c);
            }else {
                char c1 = (char) (ascii + (year1 - initial) -((year1 - initial)/26)*26 );
                yearOn =String.valueOf(c1) + (year1 - initial)/26;
            }
            /*判断月份是否是1到九月*/
            month = month.replace("0", "");
            if (!month.matches("[1-9]")) {
                month = month.equals("10") ? "A" : month.equals("11") ? "B" : month.equals("12") ? "C" : "";
            }

            // 使用正则表达式匹配字母
            Pattern pattern = Pattern.compile("[a-z||A-Z]");
            Matcher matcher = pattern.matcher(bandName);
            String Letter="";
            // 打印匹配到的字母
            while (matcher.find()) {
                Letter+=matcher.group();
            }

            if(!StringUtils.isEmpty(Letter)){
                Letter = Letter.toUpperCase();
                char[] charArray = Letter.toCharArray();
                int char1 = charArray[0];
                bandName = String.valueOf(char1-64);
            }else {
                bandName="";
            }
            /*流水号*/
            designNo = designNo.substring(5, 8);
        } catch (Exception e) {
            throw new OtherException("大货编码生成失败");
        }
//        获取款式下的配色
        int number = baseMapper.getStyleColorNumber(sampleDesignId);
        String styleNo =brand + yearOn + month + bandName + category +designNo + (number+1+index);
        /*查询编码是否重复*/
       int i = baseMapper.isStyleNoExist(styleNo);
       if(i!=0){
           throw new OtherException("编码重复");
       }
        return styleNo;
    }
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
     * @param addRevampSampleStyleColorDto 样衣-款式配色Dto类
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean addRevampSampleStyleColor(AddRevampSampleStyleColorDto addRevampSampleStyleColorDto) {
        SampleStyleColor sampleStyleColor = new SampleStyleColor();
        if (StringUtils.isEmpty(addRevampSampleStyleColorDto.getId())) {
            QueryWrapper<SampleStyleColor> queryWrapper = new QueryWrapper<>();
            /*新增*/
            BeanUtils.copyProperties(addRevampSampleStyleColorDto, sampleStyleColor);
            sampleStyleColor.setCompanyCode(baseController.getUserCompany());
            sampleStyleColor.insertInit();
            baseMapper.insert(sampleStyleColor);
        } else {
            /*修改*/
            if(StringUtils.isBlank(addRevampSampleStyleColorDto.getColourLibraryId())){
                throw  new OtherException("颜色不能为空");
            }
            BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryMapper.selectById(addRevampSampleStyleColorDto.getColourLibraryId());
            sampleStyleColor = baseMapper.selectById(addRevampSampleStyleColorDto.getId());
            if (ObjectUtils.isEmpty(sampleStyleColor)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampSampleStyleColorDto, sampleStyleColor);
            /*赋值颜色*/
            sampleStyleColor.setColourLibraryId(basicsdatumColourLibrary.getId());
            sampleStyleColor.setColorCode(basicsdatumColourLibrary.getColourCode());
            sampleStyleColor.setColorName(basicsdatumColourLibrary.getColourName());
            sampleStyleColor.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());
            sampleStyleColor.updateInit();
            baseMapper.updateById(sampleStyleColor);
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
    public Boolean delSampleStyleColor(String id,String sampleDesignId) {
  /*      List<String> ids = StringUtils.convertList(id);
        *//*批量删除*//*
        baseMapper.deleteBatchIds(ids);*/
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.set("del_flag","1");
        updateWrapper.eq("sample_design_id",sampleDesignId);
        updateWrapper.in("colour_library_id",StringUtils.convertList(id));
        baseMapper.update(null,updateWrapper);
        return true;
    }

    /**
     * 方法描述：删除样衣-款式配色
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delStyleColor(String id) {
        List<String> ids = StringUtils.convertList(id);
        /*批量删除*/
        baseMapper.deleteBatchIds(ids);
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
        UpdateWrapper<SampleStyleColor> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 方法描述 下发scm
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean issueScm(String ids) {
        if(StringUtils.isBlank(ids)){
            throw new OtherException("ids为空");
        }
        return smpService.goods(ids.split(","))>0;
    }

    /**
     * 方法描述 获取款式下的颜色
     *
     * @param sampleDesignId
     */
    @Override
    public List<String> getStyleColorId(String sampleDesignId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sample_design_id",sampleDesignId);
        List<SampleStyleColor> sampleStyleColorList = baseMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(sampleStyleColorList)){
            return new ArrayList<>();
        }
        return sampleStyleColorList.stream().map(SampleStyleColor::getColourLibraryId).collect(Collectors.toList());
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
        SampleStyleColor  sampleStyleColor=  baseMapper.selectById(relevanceBomDto.getStyleColorId());
        if(ObjectUtils.isEmpty(sampleStyleColor)){
            throw new OtherException("无配色信息");
        }
        if (StringUtils.isNotBlank(sampleStyleColor.getBom())) {
            throw new OtherException("该配色已关联bom");
        }
        /*查询bom信息*/
        PackInfo packInfo = packInfoMapper.selectById(relevanceBomDto.getPackInfoId());
        if(ObjectUtils.isEmpty(packInfo)){
            throw new OtherException("无bom信息");
        }
        if (StringUtils.isNotBlank(packInfo.getStyleNo())) {
            throw new OtherException("该bom以关联bom");
        }
        /*关联bom*/
        sampleStyleColor.setBom(packInfo.getCode());
        /*bom关联配色*/
        packInfo.setStyleNo(sampleStyleColor.getStyleNo());
        packInfo.setColorCode(sampleStyleColor.getColorCode());
        packInfo.setColor(sampleStyleColor.getColorName());
        packInfo.setSampleStyleColorId(sampleStyleColor.getId());
        baseMapper.updateById(sampleStyleColor);
        packInfoMapper.updateById(packInfo);
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
        SampleStyleColor sampleStyleColor =   baseMapper.selectById(updateStyleNoBandDto.getId());
        if(ObjectUtils.isEmpty(sampleStyleColor)){
            throw new OtherException("id错误");
        }
        /*修改大货款号*/
        if(StringUtils.isNotBlank(sampleStyleColor.getStyleNo()) && StringUtils.isNotBlank(updateStyleNoBandDto.getStyleNo()) &&   !sampleStyleColor.getStyleNo().equals(updateStyleNoBandDto.getStyleNo()) ){

            if(!updateStyleNoBandDto.getStyleNo().substring(0,5).equals(sampleStyleColor.getStyleNo().substring(0,5))){
                throw new OtherException("无法修改大货款号前五位");
            }
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("style_no",updateStyleNoBandDto.getStyleNo());
            SampleStyleColor styleColor = baseMapper.selectOne(queryWrapper);
            if(!ObjectUtils.isEmpty(styleColor)){
                throw new OtherException("大货款号已存在");
            }
            /**
             * 修改下放大货款号
             */
            baseMapper.reviseAllStyleNo(sampleStyleColor.getStyleNo(),updateStyleNoBandDto.getStyleNo());
            sampleStyleColor.setHisStyleNo(sampleStyleColor.getStyleNo());
            sampleStyleColor.setStyleNo(updateStyleNoBandDto.getStyleNo());
        }
        /*修改波段*/
        if(StringUtils.isNotBlank(sampleStyleColor.getBandCode()) && StringUtils.isNotBlank(updateStyleNoBandDto.getBandCode())){
            if(!sampleStyleColor.getBandCode().equals(updateStyleNoBandDto.getBandCode())){
                sampleStyleColor.setBandCode(updateStyleNoBandDto.getBandCode());
                sampleStyleColor.setBandName(updateStyleNoBandDto.getBandName());
            }
        }
        baseMapper.updateById(sampleStyleColor);
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
        SampleStyleColor sampleStyleColor = baseMapper.selectById(id);
        if(ObjectUtils.isEmpty(sampleStyleColor)){
            throw new OtherException(BaseErrorEnum.ERR_SELECT_ATTRIBUTE_NOT_REQUIREMENTS);
        }
        PdmStyleCheckParam pdmStyleCheckParam = new PdmStyleCheckParam();
        pdmStyleCheckParam.setStyleNo(sampleStyleColor.getStyleNo());
        pdmStyleCheckParam.setCode(sampleStyleColor.getColorCode());
        try {
           Boolean b = smpService.checkColorSize(pdmStyleCheckParam);
           if(!b){
               throw new OtherException("暂不支持修改");
           }
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        }
        return true;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
