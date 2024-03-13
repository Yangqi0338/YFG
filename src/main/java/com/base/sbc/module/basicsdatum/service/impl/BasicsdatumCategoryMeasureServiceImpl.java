/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.entity.*;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumCategoryMeasureMapper;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMeasurementMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumCategoryMeasureService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumCategoryMeasureVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-品类测量组 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumCategoryMeasureService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 19:08:55
 */
@Service
public class BasicsdatumCategoryMeasureServiceImpl extends BaseServiceImpl<BasicsdatumCategoryMeasureMapper, BasicsdatumCategoryMeasure> implements BasicsdatumCategoryMeasureService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private CcmFeignService ccmFeignService;

    @Autowired
    private BasicsdatumMeasurementMapper basicsdatumMeasurementMapper;


/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 基础资料-品类测量组分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    public PageInfo getBasicsdatumCategoryMeasureList(QueryCategoryMeasureDto queryDto) {
        /*分页*/
        BaseQueryWrapper<BasicsdatumCategoryMeasure> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getCode()),"code",queryDto.getCode());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getCategoryCode()),"category_code",queryDto.getCategoryCode());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getName()),"name",queryDto.getName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getRangeDifferenceName()),"range_difference_name",queryDto.getRangeDifferenceName());
        queryWrapper.orderByAsc("category_code");
        /*查询基础资料-品类测量组数据*/
        Page<BasicsdatumCategoryMeasureVo> objects = PageHelper.startPage(queryDto);
        getBaseMapper().selectList(queryWrapper);
        return objects.toPageInfo();
    }


    /**
     * 基础资料-品类测量组导入
     *
     * @param file
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Boolean basicsdatumCategoryMeasureImportExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumCategoryMeasureExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumCategoryMeasureExcelDto.class, params);
        /*品类数据*/
        List<BasicCategoryDot> basicCategoryDotList = ccmFeignService.getTreeByNamelList("品类",  "1");

//        操作存在编码数据
        list = list.stream().filter(s -> StringUtils.isNotBlank(s.getCode())).collect(Collectors.toList());
        for (BasicsdatumCategoryMeasureExcelDto basicsdatumCategoryMeasureExcelDto : list) {
            if(!StringUtils.isEmpty(basicsdatumCategoryMeasureExcelDto.getMeasurement())){
                basicsdatumCategoryMeasureExcelDto.setMeasurement(basicsdatumCategoryMeasureExcelDto.getMeasurement().replaceAll(" ",""));
                String[] measurement =  basicsdatumCategoryMeasureExcelDto.getMeasurement().split(",");
                QueryWrapper queryWrapper=new QueryWrapper();
                queryWrapper.in("measurement",measurement);
                List<BasicsdatumMeasurement> basicsdatumSizeList =basicsdatumMeasurementMapper.selectList(queryWrapper);
                if(!CollectionUtils.isEmpty(basicsdatumSizeList)){
                    List<String> stringList =  basicsdatumSizeList.stream().filter(s -> StringUtils.isNotBlank(s.getCode())).map(BasicsdatumMeasurement::getCode ).collect(Collectors.toList());
                    basicsdatumCategoryMeasureExcelDto.setMeasurementCode( StringUtils.join(stringList,","));
                }
            }

            if(StringUtils.isNotBlank(basicsdatumCategoryMeasureExcelDto.getCategoryName())){
              basicsdatumCategoryMeasureExcelDto.setCategoryName( basicsdatumCategoryMeasureExcelDto.getCategoryName().replaceAll(" ",""));
              String[] strings =  basicsdatumCategoryMeasureExcelDto.getCategoryName().split(",");
              List<BasicCategoryDot> basicCategoryDotList1 = basicCategoryDotList.stream().filter(b ->  Arrays.asList(strings).contains(b.getName())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(basicCategoryDotList1)){
                   List<String> stringList =  basicCategoryDotList1.stream().map(BasicCategoryDot::getValue).collect(Collectors.toList());
                    List<String> stringList1 =  basicCategoryDotList1.stream().map(BasicCategoryDot::getName).collect(Collectors.toList());
                    basicsdatumCategoryMeasureExcelDto.setCategoryCode(StringUtils.join(stringList,","));
                    basicsdatumCategoryMeasureExcelDto.setCategoryName(StringUtils.join(stringList1,","));
                }
            }
        }
        /*按编码操作*/
        List<BasicsdatumCategoryMeasure> basicsdatumColourLibraryList = BeanUtil.copyToList(list, BasicsdatumCategoryMeasure.class);
        for (BasicsdatumCategoryMeasure basicsdatumCategoryMeasure : basicsdatumColourLibraryList) {
            QueryWrapper<BasicsdatumCategoryMeasure> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("code", basicsdatumCategoryMeasure.getCode());
            this.saveOrUpdate(basicsdatumCategoryMeasure, queryWrapper1);
        }
        return true;
    }

    /**
     * 基础资料-品类测量组导出
     *
     * @param
     * @return
     */
    @Override
    public void basicsdatumCategoryMeasureDeriveExcel(HttpServletResponse response,QueryCategoryMeasureDto queryDto) throws Exception {
        BaseQueryWrapper<BasicsdatumCategoryMeasure> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getCode()),"code",queryDto.getCode());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getCategoryCode()),"category_code",queryDto.getCategoryCode());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getName()),"name",queryDto.getName());
        queryWrapper.like(StringUtils.isNotBlank(queryDto.getRangeDifferenceName()),"range_difference_name",queryDto.getRangeDifferenceName());
        List<BasicsdatumCategoryMeasureExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), BasicsdatumCategoryMeasureExcelDto.class);
        ExcelUtils.exportExcel(list, BasicsdatumCategoryMeasureExcelDto.class, "基础资料-品类测量组.xlsx", new ExportParams(), response);
    }


    /**
     * 方法描述：新增修改基础资料-品类测量组
     *
     * @param addRevampBasicsdatumCategoryMeasureDto 基础资料-品类测量组Dto类
     * @return boolean
     */
    @Override
    public BasicsdatumCategoryMeasure addRevampBasicsdatumCategoryMeasure(AddRevampBasicsdatumCategoryMeasureDto addRevampBasicsdatumCategoryMeasureDto) {
        BasicsdatumCategoryMeasure basicsdatumCategoryMeasure = new BasicsdatumCategoryMeasure();

        QueryWrapper<BasicsdatumCategoryMeasure> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", addRevampBasicsdatumCategoryMeasureDto.getCode());
        /*查询数据是否存在*/
        List<BasicsdatumCategoryMeasure> list = baseMapper.selectList(queryWrapper);
        if (StringUtils.isEmpty(addRevampBasicsdatumCategoryMeasureDto.getId())) {
            if (!CollectionUtils.isEmpty(list)) {
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }

            /*新增*/
            BeanUtils.copyProperties(addRevampBasicsdatumCategoryMeasureDto, basicsdatumCategoryMeasure);
            basicsdatumCategoryMeasure.setCompanyCode(baseController.getUserCompany());
            basicsdatumCategoryMeasure.insertInit();
            baseMapper.insert(basicsdatumCategoryMeasure);
        } else {
            /*修改*/
            basicsdatumCategoryMeasure = baseMapper.selectById(addRevampBasicsdatumCategoryMeasureDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumCategoryMeasure)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            if (!addRevampBasicsdatumCategoryMeasureDto.getCode().equals(basicsdatumCategoryMeasure.getCode()) && !CollectionUtils.isEmpty(list)) {
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            BeanUtils.copyProperties(addRevampBasicsdatumCategoryMeasureDto, basicsdatumCategoryMeasure);
            basicsdatumCategoryMeasure.updateInit();
            baseMapper.updateById(basicsdatumCategoryMeasure);

        }
        return basicsdatumCategoryMeasure;
    }


    /**
     * 方法描述：删除基础资料-品类测量组
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delBasicsdatumCategoryMeasure(String id) {
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
    public Boolean startStopBasicsdatumCategoryMeasure(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumCategoryMeasure> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}

