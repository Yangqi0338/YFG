package com.base.sbc.module.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.common.base.BaseEntity;

import java.util.List;

/**
 * @author 卞康
 * @data 2023/4/10 9:39
 */

public interface  CommonService extends IService<BaseDataEntity> {
    /**
     * 批量逻辑修改新增删除封装
     */
    Integer updateList (List<? extends BaseEntity> entityList, QueryWrapper<? extends BaseEntity> queryWrapper);

}
