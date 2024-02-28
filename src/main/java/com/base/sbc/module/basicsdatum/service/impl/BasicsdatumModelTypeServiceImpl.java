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
import cn.hutool.core.collection.CollectionUtil;
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
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumModelTypeMapper;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumSizeMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumModelTypeVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.Page;
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



/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 基础资料-号型类型分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    public PageInfo getBasicsdatumModelTypeList(QueryDto queryDto) {
        /*分页*/
        BaseQueryWrapper<BasicsdatumModelType> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.notEmptyLike("model_type", queryDto.getModelType());
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getStatus()),"status", queryDto.getStatus());
        queryWrapper.eq(StringUtils.isNotBlank(queryDto.getPdmTypeName()),"pdm_type_name", queryDto.getPdmTypeName());
        if(StringUtils.isNotBlank(queryDto.getCategoryId())){
            if(queryDto.getCategoryId().contains(",")){
                queryWrapper.apply(StringUtils.findInSet(queryDto.getCategoryId(),"category_id"));
            }else {
                queryWrapper.like(StringUtils.isNotBlank(queryDto.getCategoryId()),"category_id", queryDto.getCategoryId());
            }
        }
        queryWrapper.notEmptyLike("code", queryDto.getCode());
        queryWrapper.notEmptyLike("description", queryDto.getDescription());
        queryWrapper.notEmptyLike("dimension_type", queryDto.getDimensionType());
        queryWrapper.between("create_date",queryDto.getCreateDate());
        queryWrapper.notEmptyEq("create_name", queryDto.getCreateName());
        queryWrapper.likeList("brand",StringUtils.convertList(queryDto.getBrand()));
        /*查询基础资料-号型类型数据*/
        Page<BasicsdatumModelTypeVo> objects = PageHelper.startPage(queryDto);
        getBaseMapper().selectList(queryWrapper);
        return objects.toPageInfo();
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
        List<BasicCategoryDot> basicCategoryDotList = ccmFeignService.getTreeByNamelList("品类", "1");
        for (BasicsdatumModelTypeExcelDto basicsdatumModelTypeExcelDto : list) {
            List<BasicsdatumSize> basicsdatumSizeList =new ArrayList<>();
//            获取品类id
            if (StringUtils.isNotBlank(basicsdatumModelTypeExcelDto.getCategoryName())) {
                String[] categoryNames =  basicsdatumModelTypeExcelDto.getCategoryName().replaceAll(" ","").split(",");
                /*依赖品类*/
                List<BasicCategoryDot> list1 = basicCategoryDotList.stream().filter(b ->  Arrays.asList(categoryNames).contains(b.getName())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(list1)){
                 List<String> stringList =  list1.stream().map(BasicCategoryDot::getValue).collect(Collectors.toList());
                 List<String> stringList1 =  list1.stream().map(BasicCategoryDot::getName).collect(Collectors.toList());
                    basicsdatumModelTypeExcelDto.setCategoryId(StringUtils.join(stringList,","));
                    basicsdatumModelTypeExcelDto.setCategoryName(StringUtils.join(stringList1,","));
                }
            }
            if (StringUtils.isNotBlank(basicsdatumModelTypeExcelDto.getSize())) {
//                获取尺码id
               basicsdatumModelTypeExcelDto.setSize(basicsdatumModelTypeExcelDto.getSize().replaceAll(" ",""));
               String[] strings =  basicsdatumModelTypeExcelDto.getSize().split(",");
               QueryWrapper queryWrapper=new QueryWrapper();
               queryWrapper.in("hangtags",strings);
                queryWrapper.eq("model_type_code",basicsdatumModelTypeExcelDto.getCode());
              basicsdatumSizeList =basicsdatumSizeMapper.selectList(queryWrapper);
               if(!CollectionUtils.isEmpty(basicsdatumSizeList)){
                   List<String> stringListId =  basicsdatumSizeList.stream().map(BasicsdatumSize::getId).collect(Collectors.toList());
                   List<String> stringList =  basicsdatumSizeList.stream().map(BasicsdatumSize::getSort).collect(Collectors.toList());
                   basicsdatumModelTypeExcelDto.setSizeIds( StringUtils.join(stringListId,","));
                   basicsdatumModelTypeExcelDto.setSizeCode( StringUtils.join(stringList,","));
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
        BaseQueryWrapper<BasicsdatumModelType> queryWrapper = new BaseQueryWrapper<>();
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
        /*查找基础尺码编码*/
        if(StringUtils.isNotBlank(addRevampBasicsdatumModelTypeDto.getBasicsSize())) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("hangtags", addRevampBasicsdatumModelTypeDto.getBasicsSize());
            List<BasicsdatumSize> basicsdatumSizeList = basicsdatumSizeMapper.selectList(queryWrapper);
            addRevampBasicsdatumModelTypeDto.setBasicsSizeSort(CollectionUtils.isEmpty(basicsdatumSizeList) ? null : basicsdatumSizeList.get(0).getSort());
        }
        // 查找默认尺码
        addRevampBasicsdatumModelTypeDto.setDefaultSizeIds("");
        addRevampBasicsdatumModelTypeDto.setDefaultSizeCode("");
        if(StringUtils.isNotBlank(addRevampBasicsdatumModelTypeDto.getDefaultSize())){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("hangtags", StringUtils.convertList(addRevampBasicsdatumModelTypeDto.getDefaultSize()));
            List<BasicsdatumSize> basicsdatumSizeList = basicsdatumSizeMapper.selectList(queryWrapper);
            if(CollectionUtil.isNotEmpty(basicsdatumSizeList)) {
                StringBuilder sizeIds = new StringBuilder();
                StringBuilder sizeCodes = new StringBuilder();
                StringBuilder sizeRealCodes = new StringBuilder();
                for (BasicsdatumSize item : basicsdatumSizeList) {
                    sizeIds.append(item.getId()).append(",");
                    sizeCodes.append(item.getCode()).append(",");
                    sizeRealCodes.append(item.getRealCode()).append(",");
                }
                addRevampBasicsdatumModelTypeDto.setDefaultSizeIds(sizeIds.deleteCharAt(sizeIds.length() - 1).toString());
                addRevampBasicsdatumModelTypeDto.setDefaultSizeCode(sizeCodes.deleteCharAt(sizeCodes.length() - 1).toString());
                addRevampBasicsdatumModelTypeDto.setDefaultSizeRealCode(sizeRealCodes.deleteCharAt(sizeRealCodes.length() - 1).toString());
            }
        }

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
//                List<BasicsdatumSize> list = basicsdatumSizeService.list(queryWrapper1);
//                for (BasicsdatumSize basicsdatumSize : list) {
//                    basicsdatumSize.setModelType(addRevampBasicsdatumModelTypeDto.getModelType());
//                    basicsdatumSize.setModelTypeCode(addRevampBasicsdatumModelTypeDto.getCode());
//                }
//                basicsdatumSizeService.updateBatchById(list);
            }

        } else {
            /*修改*/
            basicsdatumModelType = baseMapper.selectById(addRevampBasicsdatumModelTypeDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumModelType)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }

//            List<BasicsdatumSize> list1 = basicsdatumSizeService.list(new QueryWrapper<BasicsdatumSize>().eq("model_type_code", addRevampBasicsdatumModelTypeDto.getCode()));
//            for (BasicsdatumSize basicsdatumSize : list1) {
//                basicsdatumSize.setModelType("");
//                basicsdatumSize.setModelTypeCode("");
//                basicsdatumSizeService.updateBatchById(list1);
//            }

            if (StringUtils.isNotEmpty(addRevampBasicsdatumModelTypeDto.getSizeIds())) {
//                String[] sizeIds = addRevampBasicsdatumModelTypeDto.getSizeIds().split(",");
//
//                List<BasicsdatumSize> list = basicsdatumSizeService.listByIds( Arrays.asList(sizeIds));
//                for (BasicsdatumSize basicsdatumSize : list) {
//                    basicsdatumSize.setModelType(addRevampBasicsdatumModelTypeDto.getModelType());
//                    basicsdatumSize.setModelTypeCode(addRevampBasicsdatumModelTypeDto.getCode());;
//                }
//                basicsdatumSizeService.updateBatchById(list);
            }
            BeanUtils.copyProperties(addRevampBasicsdatumModelTypeDto, basicsdatumModelType);
            basicsdatumModelType.updateInit();
            baseMapper.updateById(basicsdatumModelType);
        }
        return true;
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

    @Override
    public List<BasicsdatumModelType> queryByCode(String companyCode, String code){
        QueryWrapper<BasicsdatumModelType> qw = new QueryWrapper<>();
        qw.eq("company_code", companyCode);
        qw.eq("code", code);
        return list(qw);
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
