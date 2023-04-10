package com.base.sbc.module.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @data 2023/4/10 9:41
 */
@Service
public class CommonServiceImpl extends ServiceImpl<BaseMapper<BaseDataEntity>, BaseDataEntity> implements CommonService {

    @Resource
    private UserUtils userUtils;
    /**
     * 批量修改新增逻辑删除封装
     *
     * @param queryWrapper 条件构造器
     */
    @Override
    @Transactional
    public Integer updateList(List<? extends BaseEntity> entityList, QueryWrapper<? extends BaseEntity> queryWrapper) {
        String companyCode = userUtils.getCompanyCode();
        //分类
        // 新增的
        List<BaseDataEntity> addList = new ArrayList<>();
        // 修改的
        List<BaseDataEntity> updateList = new ArrayList<>();

        List<String> ids=new ArrayList<>();

        for (BaseEntity entity : entityList) {
            if (StringUtils.isEmpty(entity.getId()) || entity.getId().contains("-")) {
                //说明是新增的
                entity.setId(null);
                addList.add((BaseDataEntity) entity);
            } else {
                //说明是修改的
                updateList.add((BaseDataEntity) entity);
                ids.add(entity.getId());
            }
        }

        //逻辑删除传进来不存在的
        if (ids.size()>0){
            queryWrapper.notIn("id",ids);
        }
        queryWrapper.eq("company_code",companyCode);
        this.remove((Wrapper<BaseDataEntity>) queryWrapper);
        //新增
        this.saveBatch( addList);
        //修改
        this.updateBatchById( updateList);

        return entityList.size();
    }
}
