package com.base.sbc.module.common.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @author 卞康
 * @data 2023/4/10 9:39
 */

public interface CommonService<T>{
    /**
     * 批量提交修改，逻辑删除新增修改
     * @param entityList 实体列表
     * @param queryWrapper 构造器
     * @return 传入实体列表的总长度
     */
    Integer addAndUpdateAndDelList (IService<T> iService,List<T> entityList, QueryWrapper<T> queryWrapper);

}
