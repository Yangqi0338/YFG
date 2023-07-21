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
import cn.hutool.core.util.StrUtil;
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
import com.base.sbc.module.basicsdatum.entity.BasicsdatumCompanyRelation;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumRangeDifference;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumModelTypeMapper;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumSizeMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumCompanyRelationService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumModelTypeVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-号型类型 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 9:31:14
 */
@Service
@RequiredArgsConstructor
public class BasicsdatumModelTypeServiceImpl extends BaseServiceImpl<BasicsdatumModelTypeMapper, BasicsdatumModelType> implements BasicsdatumModelTypeService {


    private final BaseController baseController;
    private final CcmFeignService ccmFeignService;

    private final BasicsdatumSizeMapper basicsdatumSizeMapper;
    private final BasicsdatumSizeService basicsdatumSizeService;

    private final BasicsdatumCompanyRelationService basicsdatumCompanyRelationService;


/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 基础资料-号型类型分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    public PageInfo<BasicsdatumModelTypeVo> getBasicsdatumModelTypeList(QueryDto queryDto) {
        /*分页*/
        if (queryDto.getPageNum() != 0 && queryDto.getPageSize() != 0) {
            PageHelper.startPage(queryDto);
        }

        BaseQueryWrapper<BasicsdatumModelType> queryWrapper = new BaseQueryWrapper<>();

        queryWrapper.notEmptyLike("mt.model_type", queryDto.getModelType());
        queryWrapper.eq("mt.company_code", baseController.getUserCompany());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getStatus()),"mt.status", queryDto.getStatus());
        queryWrapper.in(StringUtils.isNotBlank(queryDto.getCategoryId()),"cr.category_ids", StringUtils.convertList(queryDto.getCategoryId()) );
        queryWrapper.notEmptyLike("mt.code", queryDto.getCode());
        queryWrapper.notEmptyLike("mt.description", queryDto.getDescription());
        queryWrapper.notEmptyLike("mt.dimension_type", queryDto.getDimensionType());
        queryWrapper.between("mt.create_date",queryDto.getCreateDate());
        queryWrapper.eq("mt.del_flag", "0");
        /*查询基础资料-号型类型数据*/
        List<BasicsdatumModelTypeVo> basicsdatumModelTypeList = baseMapper.getBasicsdatumModelTypeList(queryWrapper,StringUtils.convertList(queryDto.getCategoryId()));
        PageInfo<BasicsdatumModelTypeVo> pageInfo = new PageInfo<>(basicsdatumModelTypeList);
        return pageInfo;
    }


    /**
     * 基础资料-号型类型导入
     *
     * @param file
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean basicsdatumModelTypeImportExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumModelTypeExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumModelTypeExcelDto.class, params);
        list = list.stream().filter(s -> StringUtils.isNotBlank(s.getCode())).collect(Collectors.toList());
        for (BasicsdatumModelTypeExcelDto basicsdatumModelTypeExcelDto : list) {
            List<BasicsdatumSize> basicsdatumSizeList =new ArrayList<>();
//            获取品类id
            if (StringUtils.isNotBlank(basicsdatumModelTypeExcelDto.getCategory())) {

                basicsdatumModelTypeExcelDto.setCategory(basicsdatumModelTypeExcelDto.getCategory().replaceAll(" ",""));
                List<BasicCategoryDot> basicCategoryDotList = ccmFeignService.getCategorySByNameAndLevel("品类", basicsdatumModelTypeExcelDto.getCategory(), "1");
                List<BasicsdatumCompanyRelation> basicsdatumCompanyRelationList = new ArrayList<>();
                basicCategoryDotList.forEach(b ->{
                    BasicsdatumCompanyRelation basicsdatumCompanyRelation =new BasicsdatumCompanyRelation();
                    basicsdatumCompanyRelation.setCategoryId(b.getId());
                    basicsdatumCompanyRelation.setCategoryName(b.getName());
                    basicsdatumCompanyRelation.setCompanyCode(baseController.getUserCompany());
                    basicsdatumCompanyRelation.setType("difference");
                    basicsdatumCompanyRelationList.add(basicsdatumCompanyRelation);
                });
                basicsdatumModelTypeExcelDto.setBasicsdatumCompanyRelation(basicsdatumCompanyRelationList);
            }
            if (StringUtils.isNotBlank(basicsdatumModelTypeExcelDto.getSize())) {
//                获取尺码id
               basicsdatumModelTypeExcelDto.setSize(basicsdatumModelTypeExcelDto.getSize().replaceAll(" ",""));
               String[] strings =  basicsdatumModelTypeExcelDto.getSize().split(",");
               QueryWrapper queryWrapper=new QueryWrapper();
               queryWrapper.in("hangtags",strings);
              basicsdatumSizeList =basicsdatumSizeMapper.selectList(queryWrapper);
               if(!CollectionUtils.isEmpty(basicsdatumSizeList)){
                  List<String> stringList =  basicsdatumSizeList.stream().map(BasicsdatumSize::getSort).collect(Collectors.toList());
                  basicsdatumModelTypeExcelDto.setSizeIds( StringUtils.join(stringList,","));
               }else {
                   basicsdatumModelTypeExcelDto.setSize("");
               }
            }
            if(StringUtils.isNotBlank(basicsdatumModelTypeExcelDto.getBasicsSize())) {
                if (!CollectionUtils.isEmpty(basicsdatumSizeList)) {
                    List<BasicsdatumSize> basicsdatumSizeList1 = basicsdatumSizeList.stream().filter(s -> basicsdatumModelTypeExcelDto.getBasicsSize().equals(s.getHangtags())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(basicsdatumSizeList1)) {
                        basicsdatumModelTypeExcelDto.setBasicsSizeSort(basicsdatumSizeList1.get(0).getSort());
                    } else {
                        basicsdatumModelTypeExcelDto.setBasicsSize("");
                    }
                }
            }
        }
        List<BasicsdatumModelType> basicsdatumModelTypeList = BeanUtil.copyToList(list, BasicsdatumModelType.class);
        for (BasicsdatumModelType basicsdatumModelType : basicsdatumModelTypeList) {
            QueryWrapper<BasicsdatumModelType> queryWrapper =new BaseQueryWrapper<>();
            queryWrapper.eq("code",basicsdatumModelType.getCode());
            this.saveOrUpdate(basicsdatumModelType,queryWrapper);
            /*添加品类关系*/
            BasicsdatumModelType modelType =   baseMapper.selectOne(queryWrapper) ;
            if(!ObjectUtils.isEmpty(modelType)){
                basicsdatumCompanyRelationService.deleteBatchAddition(assignmentCompany(basicsdatumModelType.getBasicsdatumCompanyRelation(), modelType.getId()));
            }
        }
        return true;
    }

    /**
     * 基础资料-号型类型导出
     *
     * @param
     * @return
     */
    @Override
    public void basicsdatumModelTypeDeriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumModelType> queryWrapper = new QueryWrapper<>();
        List<BasicsdatumModelTypeExcelDto> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), BasicsdatumModelTypeExcelDto.class);
        ExcelUtils.exportExcel(list, BasicsdatumModelTypeExcelDto.class, "基础资料-号型类型.xlsx", new ExportParams(), response);
    }


    /**
     * 方法描述：新增修改基础资料-号型类型
     *
     * @param addRevampBasicsdatumModelTypeDto 基础资料-号型类型Dto类
     * @return boolean
     */
    @Override
    public Boolean addRevampBasicsdatumModelType(AddRevampBasicsdatumModelTypeDto addRevampBasicsdatumModelTypeDto) {
        BasicsdatumModelType basicsdatumModelType = new BasicsdatumModelType();
        if (StringUtils.isEmpty(addRevampBasicsdatumModelTypeDto.getId())) {
            QueryWrapper<BasicsdatumModelType> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code", addRevampBasicsdatumModelTypeDto.getCode());
            queryWrapper.eq("company_code", baseController.getUserCompany());
            if (!CollectionUtils.isEmpty(baseMapper.selectList(queryWrapper))) {
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            /*新增*/
            BeanUtils.copyProperties(addRevampBasicsdatumModelTypeDto, basicsdatumModelType);
            baseMapper.insert(basicsdatumModelType);
            if (StringUtils.isNotEmpty(addRevampBasicsdatumModelTypeDto.getSizeIds())) {
                String[] sizeIds = addRevampBasicsdatumModelTypeDto.getSizeIds().split(",");
                QueryWrapper<BasicsdatumSize> queryWrapper1 = new BaseQueryWrapper<>();
                queryWrapper1.in("id", Arrays.asList(sizeIds));
                List<BasicsdatumSize> list = basicsdatumSizeService.list(queryWrapper1);
                for (BasicsdatumSize basicsdatumSize : list) {
                    basicsdatumSize.setModelType(addRevampBasicsdatumModelTypeDto.getModelType());
                    basicsdatumSize.setModelTypeCode(addRevampBasicsdatumModelTypeDto.getCode());
                }
                basicsdatumSizeService.updateBatchById(list);
            }

            /*新增品类*/
            if (!CollectionUtils.isEmpty(addRevampBasicsdatumModelTypeDto.getList())) {
                basicsdatumCompanyRelationService.batchAddition(assignmentCompany(addRevampBasicsdatumModelTypeDto.getList(), basicsdatumModelType.getId()));
            }
        } else {
            /*修改*/
            basicsdatumModelType = baseMapper.selectById(addRevampBasicsdatumModelTypeDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumModelType)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }

            List<BasicsdatumSize> list1 = basicsdatumSizeService.list(new QueryWrapper<BasicsdatumSize>().eq("model_type_code", addRevampBasicsdatumModelTypeDto.getCode()));
            for (BasicsdatumSize basicsdatumSize : list1) {
                basicsdatumSize.setModelType("");
                basicsdatumSize.setModelTypeCode("");
                basicsdatumSizeService.updateBatchById(list1);
            }

            if (StringUtils.isNotEmpty(addRevampBasicsdatumModelTypeDto.getSizeIds())) {
                String[] sizeIds = addRevampBasicsdatumModelTypeDto.getSizeIds().split(",");

                List<BasicsdatumSize> list = basicsdatumSizeService.listByIds( Arrays.asList(sizeIds));
                for (BasicsdatumSize basicsdatumSize : list) {
                    basicsdatumSize.setModelType(addRevampBasicsdatumModelTypeDto.getModelType());
                    basicsdatumSize.setModelTypeCode(addRevampBasicsdatumModelTypeDto.getCode());;
                }
                basicsdatumSizeService.updateBatchById(list);
            }
            BeanUtils.copyProperties(addRevampBasicsdatumModelTypeDto, basicsdatumModelType);
            basicsdatumModelType.updateInit();
            baseMapper.updateById(basicsdatumModelType);
            /*新增品类*/
            if (!CollectionUtils.isEmpty(addRevampBasicsdatumModelTypeDto.getList())) {
                basicsdatumCompanyRelationService.deleteBatchAddition(assignmentCompany(addRevampBasicsdatumModelTypeDto.getList(), basicsdatumModelType.getId()));
            }
        }
        return true;
    }

    /*赋值*/
    public List<BasicsdatumCompanyRelation> assignmentCompany(List<BasicsdatumCompanyRelation> list, String id) {
        if(!CollectionUtils.isEmpty(list)){
            for (BasicsdatumCompanyRelation b : list) {
                b.setCompanyCode(baseController.getUserCompany());
                b.setDataId(id);
                b.setType("modelType");
            }
            return list;
        }
        return null;
    }

    /**
     * 方法描述：删除基础资料-号型类型
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delBasicsdatumModelType(String id) {
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
    public Boolean startStopBasicsdatumModelType(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumModelType> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public String getNameById(String id) {
        if (StrUtil.isBlank(id)) {
            return null;
        }
        return getBaseMapper().getNameById(id);
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
