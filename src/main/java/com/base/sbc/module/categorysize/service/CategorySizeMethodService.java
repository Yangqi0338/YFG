package com.base.sbc.module.categorysize.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.module.categorysize.entity.CategorySizeMethod;

import java.util.List;

/**
 * @author 卞康
 * @data 2023/4/6 15:22
 */

public interface CategorySizeMethodService extends IService<CategorySizeMethod> {
    Integer updateList(List<CategorySizeMethod> categorySizeMethodList, String categoryName);
}
