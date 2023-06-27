package com.base.sbc.module.categorysize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.categorysize.entity.CategorySizeMethod;
import com.base.sbc.module.categorysize.mapper.CategorySizeMethodMapper;
import com.base.sbc.module.categorysize.service.CategorySizeMethodService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 卞康
 * @data 2023/4/6 15:22
 */
@Service
public class CategorySizeMethodServiceImpl extends BaseServiceImpl<CategorySizeMethodMapper, CategorySizeMethod> implements CategorySizeMethodService {


    @Override
    public Integer updateList(List<CategorySizeMethod> categorySizeMethodList, String categoryName) {
        QueryWrapper<CategorySizeMethod> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("category_name",categoryName);
       return this.addAndUpdateAndDelList(categorySizeMethodList,queryWrapper);
    }
}
