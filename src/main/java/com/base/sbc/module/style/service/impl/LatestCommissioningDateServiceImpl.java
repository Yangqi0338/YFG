/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.style.dto.LatestCommissioningDateQueryDto;
import com.base.sbc.module.style.entity.LatestCommissioningDate;
import com.base.sbc.module.style.mapper.LatestCommissioningDateMapper;
import com.base.sbc.module.style.service.LatestCommissioningDateService;
import com.base.sbc.module.style.vo.LatestCommissioningDateExcel;
import com.base.sbc.module.style.vo.LatestCommissioningDateVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：下单最晚投产日期管理 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.service.LatestCommissioningDateService
 * @email your email
 * @date 创建时间：2024-3-11 19:05:30
 */
@Service
public class LatestCommissioningDateServiceImpl extends BaseServiceImpl<LatestCommissioningDateMapper, LatestCommissioningDate> implements LatestCommissioningDateService {

// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private CcmFeignService ccmFeignService;

    @Override
    public PageInfo<LatestCommissioningDateVo> findPage(LatestCommissioningDateQueryDto dto) {
        Page<Object> objects = PageHelper.startPage(dto);
        BaseQueryWrapper<LatestCommissioningDate> qw = new BaseQueryWrapper<>();
        List<LatestCommissioningDate> list = list(qw);
        return new PageInfo<>(BeanUtil.copyToList(list, LatestCommissioningDateVo.class));
    }

    @Override
    @Transactional
    public ApiResult importExcel(List<LatestCommissioningDateExcel> list) {


        //品牌
        List<BasicBaseDict> dictInfoToList = ccmFeignService.getDictInfoToList("C8_Brand");
        Map<String, List<BasicBaseDict>> brandNameMap = dictInfoToList.stream().collect(Collectors.groupingBy(BasicBaseDict::getName));
        //年份
        List<BasicBaseDict> dictInfoToList1 = ccmFeignService.getDictInfoToList("C8_Year");
        Map<String, List<BasicBaseDict>> yearNameMap = dictInfoToList1.stream().collect(Collectors.groupingBy(BasicBaseDict::getValue));
        //波段
        List<BasicBaseDict> dictInfoToList2 = ccmFeignService.getDictInfoToList("C8_Band");
        Map<String, List<BasicBaseDict>> bandNameMap = dictInfoToList2.stream().collect(Collectors.groupingBy(BasicBaseDict::getName));

        for (LatestCommissioningDateExcel excelDto : list) {
            //品牌
            if(brandNameMap.containsKey(excelDto.getBrandName())){
                String name = brandNameMap.get(excelDto.getBrandName()).get(0).getValue();
                excelDto.setBrand(name);
            }else{
                throw new OtherException("品牌不存在："+excelDto.getBrandName());
            }
            //年份
            if(yearNameMap.containsKey(excelDto.getYearName())){
                String name = yearNameMap.get(excelDto.getYearName()).get(0).getName();
                excelDto.setYear(name);
            }else{
                throw new OtherException("年份不存在："+excelDto.getYearName());
            }
            //波段
            if(bandNameMap.containsKey(excelDto.getBandName())){
                String name = bandNameMap.get(excelDto.getBandName()).get(0).getValue();
                excelDto.setBandCode(name);
            }else{
                throw new OtherException("波段不存在："+excelDto.getBandName());
            }
        }
        List<LatestCommissioningDate> list1 = BeanUtil.copyToList(list, LatestCommissioningDate.class);
        for (LatestCommissioningDate latestCommissioningDate : list1) {
            LambdaUpdateWrapper<LatestCommissioningDate> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(LatestCommissioningDate::getBrand,latestCommissioningDate.getBrand());
            updateWrapper.eq(LatestCommissioningDate::getYear,latestCommissioningDate.getYear());
            updateWrapper.eq(LatestCommissioningDate::getBandCode,latestCommissioningDate.getBandCode());
            saveOrUpdate(latestCommissioningDate,updateWrapper);
        }

        return ApiResult.success("导入成功");
    }

    @Override
    public void exportExcel(HttpServletResponse response, LatestCommissioningDateQueryDto dto) throws IOException {
        List<LatestCommissioningDateVo> list = findPage(dto).getList();
        List<LatestCommissioningDateExcel> latestCommissioningDateExcels = BeanUtil.copyToList(list, LatestCommissioningDateExcel.class);
        ExcelUtils.exportExcel(latestCommissioningDateExcels, LatestCommissioningDateExcel.class, "下单最晚投产日期.xlsx", new ExportParams("下单最晚投产日期", "下单最晚投产日期", ExcelType.HSSF), response);
    }

    @Override
    public ApiResult updateMain(LatestCommissioningDate vo) {
        LambdaUpdateWrapper<LatestCommissioningDate> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(LatestCommissioningDate::getLatestCommissioningDate, vo.getLatestCommissioningDate());
        updateWrapper.set(LatestCommissioningDate::getUpdateId, getUserId());
        updateWrapper.set(LatestCommissioningDate::getUpdateName, getUserName());
        updateWrapper.set(LatestCommissioningDate::getUpdateDate, new Date());
        updateWrapper.eq(LatestCommissioningDate::getId, vo.getId());
        update(updateWrapper);
        return ApiResult.success("修改成功");
    }

// 自定义方法区 不替换的区域【other_end】

}
