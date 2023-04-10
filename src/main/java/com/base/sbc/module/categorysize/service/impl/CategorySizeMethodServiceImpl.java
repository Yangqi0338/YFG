package com.base.sbc.module.categorysize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.categorysize.entity.CategorySizeMethod;
import com.base.sbc.module.categorysize.mapper.CategorySizeMethodMapper;
import com.base.sbc.module.categorysize.service.CategorySizeMethodService;
import com.base.sbc.module.common.service.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @data 2023/4/6 15:22
 */
@Service
public class CategorySizeMethodServiceImpl extends ServiceImpl<CategorySizeMethodMapper, CategorySizeMethod> implements CategorySizeMethodService {
    @Resource
    private UserUtils userUtils;

    @Resource
    private CommonService<CategorySizeMethod> commonService;


    @Override
    @Transactional
    public Integer updateList(List<CategorySizeMethod> categorySizeMethodList, String categoryName) {
        QueryWrapper<CategorySizeMethod> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("category_name",categoryName);
       return commonService.updateList(this,categorySizeMethodList,queryWrapper);
        //String companyCode = userUtils.getCompanyCode();
        ////分类
        //// 新增的
        //List<CategorySizeMethod> addCategorySizeMethods = new ArrayList<>();
        //// 修改的
        //List<CategorySizeMethod> updateCategorySizeMethods = new ArrayList<>();
        //
        //List<String> ids=new ArrayList<>();
        //for (CategorySizeMethod categorySizeMethod : categorySizeMethodList) {
        //    if (StringUtils.isEmpty(categorySizeMethod.getId()) || categorySizeMethod.getId().contains("-")) {
        //        //说明是新增的
        //        categorySizeMethod.setId(null);
        //        addCategorySizeMethods.add(categorySizeMethod);
        //    } else {
        //        //说明是修改的
        //        updateCategorySizeMethods.add(categorySizeMethod);
        //        ids.add(categorySizeMethod.getId());
        //    }
        //
        //}
        //
        ////逻辑删除传进来不存在的
        //QueryWrapper<CategorySizeMethod> queryWrapper=new QueryWrapper<>();
        //queryWrapper.eq("company_code",companyCode).eq("category_name",categoryName);
        //if (ids.size()>0){
        //    queryWrapper.notIn("id",ids);
        //}
        //this.remove(queryWrapper);
        ////新增
        //this.saveBatch(addCategorySizeMethods);
        ////修改
        //this.updateBatchById(updateCategorySizeMethods);
        //
        //return categorySizeMethodList.size();
    }
}
