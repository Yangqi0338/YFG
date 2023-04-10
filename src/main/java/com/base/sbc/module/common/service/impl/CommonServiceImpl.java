package com.base.sbc.module.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 卞康
 * @data 2023/4/10 9:41
 */
@Service
public  class CommonServiceImpl<T extends BaseEntity> implements CommonService<T> {

    @Resource
    private UserUtils userUtils;
    /**
     * 批量修改新增逻辑删除封装
     *
     * @param queryWrapper 条件构造器
     */
    @Override
    @Transactional
    public Integer updateList(IService<T> iService,List<T> entityList, QueryWrapper<T> queryWrapper) {
        String companyCode = userUtils.getCompanyCode();
        //分类
        // 新增的
        Collection<T> addList = new ArrayList<>();
        // 修改的
        Collection<T> updateList = new ArrayList<>();

        Collection<String> ids=new ArrayList<>();

        for (T entity : entityList) {
            if (StringUtils.isEmpty(entity.getId()) || entity.getId().contains("-")) {
                //说明是新增的
                entity.setId(null);
                addList.add(entity);
            } else {
                //说明是修改的
                updateList.add(entity);
                ids.add(entity.getId());
            }
        }

        //逻辑删除传进来不存在的
        if (ids.size()>0){
            queryWrapper.notIn("id",ids);
        }
        queryWrapper.eq("company_code",companyCode);
        iService.remove(queryWrapper);
        //新增
        iService.saveBatch(addList);
        //修改
        iService.updateBatchById(updateList);

        return entityList.size();
    }

}
