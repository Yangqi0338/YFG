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
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.band.service.BandService;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.entity.Specification;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumColourLibraryMapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.AddRevampSampleStyleColorDto;
import com.base.sbc.module.sample.dto.QuerySampleStyleColorDto;
import com.base.sbc.module.sample.dto.updateTagPriceDto;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.entity.SampleStyleColor;
import com.base.sbc.module.sample.mapper.SampleDesignMapper;
import com.base.sbc.module.sample.mapper.SampleStyleColorMapper;
import com.base.sbc.module.sample.service.SampleStyleColorService;
import com.base.sbc.module.sample.vo.SampleStyleColorVo;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.smp.dto.SmpColorDto;
import com.base.sbc.module.smp.dto.SmpGoodsDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
public class SampleStyleColorServiceImpl extends BaseServiceImpl<SampleStyleColorMapper, SampleStyleColor> implements SampleStyleColorService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private BasicsdatumColourLibraryMapper basicsdatumColourLibraryMapper;

    @Autowired
    private SampleDesignMapper sampleDesignMapper;

    @Autowired
    private BandService bandService;

    @Autowired
    private SmpService smpService;

    @Autowired
    private CcmFeignService ccmFeignService;


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
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getSampleDesignId()), "sample_design_id", queryDto.getSampleDesignId());
        /*查询样衣-款式配色数据*/
        List<SampleStyleColor> sampleStyleColorList = baseMapper.selectList(queryWrapper);
        PageInfo<SampleStyleColor> pageInfo = new PageInfo<>(sampleStyleColorList);
        /*转换vo*/
        List<SampleStyleColorVo> list = BeanUtil.copyToList(sampleStyleColorList, SampleStyleColorVo.class);
        List<String> stringList = list.stream().filter(s -> StringUtils.isNotBlank(s.getBandCode())).map(SampleStyleColorVo::getBandCode).collect(Collectors.toList());
        String codes = String.join(",", stringList);
        Map<String, String> m = bandService.getNamesByCodes(codes);
        for (SampleStyleColorVo sampleStyleColorVo : list) {
            if (StringUtils.isNotBlank(sampleStyleColorVo.getBandCode())) {
                sampleStyleColorVo.setBandCode(m.get(sampleStyleColorVo.getBandCode()));
            }
        }
        PageInfo<SampleStyleColorVo> pageInfo1 = new PageInfo<>();
        pageInfo1.setList(list);
        pageInfo1.setTotal(pageInfo.getTotal());
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        return pageInfo1;
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
    public Boolean updateTagPrice(updateTagPriceDto updateTagPriceDto) {
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
    public Boolean batchAddSampleStyleColor(List<AddRevampSampleStyleColorDto> list) {


        for (AddRevampSampleStyleColorDto addRevampSampleStyleColorDto : list) {
            BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryMapper.selectById(addRevampSampleStyleColorDto.getColourLibraryId());
            SampleDesign sampleDesign = sampleDesignMapper.selectById(addRevampSampleStyleColorDto.getSampleDesignId());
            addRevampSampleStyleColorDto.setColorName(basicsdatumColourLibrary.getColourName());
            addRevampSampleStyleColorDto.setColorSpecification(basicsdatumColourLibrary.getColourSpecification());
            addRevampSampleStyleColorDto.setStyleNo(getNextCode(addRevampSampleStyleColorDto.getSampleDesignId(), sampleDesign.getBrand(),sampleDesign.getYear(),sampleDesign.getMonth(),sampleDesign.getBandCode(),sampleDesign.getCategoryName(),sampleDesign.getDesignNo()));
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
     * @param band
     * @param category
     * @param designNo
     * @return
     */

    public String getNextCode(String sampleDesignId,String brand,String year,String month,String band,String category,String designNo) {
        if (StrUtil.contains(category, StrUtil.COMMA)) {
            category = getCategory(category);
        }
        String yearOn ="";
        try {
//        获取年份
            int initial = Integer.parseInt("2019");
            int year1 = Integer.parseInt(year);
            int ascii = (int) 'A';
            char c = (char) (ascii + (year1 - initial));
            yearOn=String.valueOf(c);
            /*判断月份是否是1到九月*/
            month = month.replace("0", "");
            if (!month.matches("[1-9]")) {
                month = month.equals("10") ? "A" : month.equals("11") ? "B" : month.equals("12") ? "C" : "";
            }
            /*波段*/
            String bandName =  bandService.getNameByCode(band);
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
                int char1 = (int) charArray[0];
                band = String.valueOf(char1-64);
            }else {
                band="";
            }
            /*流水号*/
            designNo = designNo.substring(5, 8);
        } catch (Exception e) {
            throw new OtherException("大货编码生成失败");
        }
//        获取款式下的配色
        int number = baseMapper.getStyleColorNumber(sampleDesignId);
        String styleNo =brand + yearOn + month + band + category +designNo + (number+1);
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
            sampleStyleColor = baseMapper.selectById(addRevampSampleStyleColorDto.getId());
            if (ObjectUtils.isEmpty(sampleStyleColor)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampSampleStyleColorDto, sampleStyleColor);
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
    public Boolean delSampleStyleColor(String id) {
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
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.in("id",StringUtils.convertList(ids));
        List<SampleStyleColor> list =  baseMapper.selectList(queryWrapper);
        /*获取字典值*/
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_ColorChroma,C8_ColorType");
        Map<String, String> mapColorChroma = dictInfoToMap.get("C8_ColorChroma");
        Map<String, String> mapColorType = dictInfoToMap.get("C8_ColorType");
        list.forEach(sampleStyleColor -> {
            /*查询颜色*/
            BasicsdatumColourLibrary basicsdatumColourLibrary = basicsdatumColourLibraryMapper.selectById(sampleStyleColor.getColourLibraryId());
            /*下发颜色*/
            smpService.issueColor(basicsdatumColourLibrary,mapColorChroma,mapColorType,baseController);

           /*查询样衣数据*/

            SampleDesign sampleDesign =  sampleDesignMapper.selectById(sampleStyleColor.getSampleDesignId());
            /*下发商品*/
            smpService.issueGoods(sampleDesign,dictInfoToMap);


        });
        return null;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
