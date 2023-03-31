package com.base.sbc.module.categorysize.service;

import com.base.sbc.module.categorysize.dao.CategorySizeMethodDao;
import com.base.sbc.module.categorysize.entity.CategorySizeMethod;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：品类尺寸量法 service类
 * @address com.base.sbc.basedata.service.CategorySizeMethodService
 * @author youkehai
 * @email 717407966@qq.com
 * @date 创建时间：2019-1-17 11:41:23
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class CategorySizeMethodService  extends BaseService<CategorySizeMethod> {
    @Autowired
    private CategorySizeMethodDao categorySizeMethodDao;
    @Override
    protected BaseDao<CategorySizeMethod> getEntityDao() {
        return categorySizeMethodDao;
    }

    /***
     * 批量新增品类尺寸量法,先清除之前的量法数据
     * @param users
     * @param categorySizeMethod
     * @param categoryName
     * @param userCompany
     * @return
     */
    @Transactional(readOnly = false)
    public ApiResult insetAll(GroupUser users, List<CategorySizeMethod> categorySizeMethod, String categoryName,
                              String userCompany) {
        //清除
        QueryCondition qc = new QueryCondition();
        qc.andEqualTo("company_code", userCompany);
        //品类名称
        qc.andEqualTo("category_name", categoryName);
        this.deleteByCondition(qc);
        if(categorySizeMethod!=null && categorySizeMethod.size()>0) {
            int j=this.batchInsert(categorySizeMethod);
            if(j>0) {
                return ApiResult.success("",categorySizeMethod);
            }
        }
        return ApiResult.success("删除成功");
    }
}
