/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;
import com.base.sbc.module.planning.dao.PlanningSeasonDao;
import com.base.sbc.module.planning.entity.PlanningSeason;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类描述：企划-产品季 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningSeasonService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-27 17:42:08
 */
@Service
@Transactional(readOnly = true)
public class PlanningSeasonService extends BaseService<PlanningSeason> {

    @Autowired
    private PlanningSeasonDao planningSeasonDao;

    @Override
    protected BaseDao<PlanningSeason> getEntityDao() {
        return planningSeasonDao;
    }


    @Transactional(readOnly = false)
    public boolean del(String companyCode,String id){
        //删除产品季信息
        int flg= deleteByConditionDelFlag(new QueryCondition(companyCode).andEqualTo("id",id));
        return flg>0;
    }

}
