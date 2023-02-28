package com.base.sbc.basedata.dao;

import com.base.sbc.basedata.entity.CategorySizeMethod;
import com.base.sbc.config.common.annotation.MyBatisDao;
import com.base.sbc.config.common.base.BaseDao;

/**
 * 类描述：品类尺寸量法 dao类
 * @address com.base.sbc.basedata.dao.CategorySizeMethodDao
 * @author youkehai
 * @email  717407966@qq.com
 * @date 创建时间：2019-1-17 11:41:23
 * @version 1.0
 */
@MyBatisDao
public class CategorySizeMethodDao extends BaseDao<CategorySizeMethod> {

    @Override
    protected String getMapperNamespace() {
        return "CategorySizeMethodDao";
    }

}
