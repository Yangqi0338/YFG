package com.base.sbc.module.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/4/13 11:49:17
 * 自定义增强
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 批量提交修改，逻辑删除新增修改
     *
     * @param entityList   实体列表
     * @param queryWrapper 构造器
     * @return 传入实体列表的总长度
     */
    Integer addAndUpdateAndDelList(List<T> entityList, QueryWrapper<T> queryWrapper);


    /**
     * 根据id物理删除数据（慎用）
     * @param id 主键id
     * @return 操作结果
     */
    Boolean physicalDeleteById(String id);
}
