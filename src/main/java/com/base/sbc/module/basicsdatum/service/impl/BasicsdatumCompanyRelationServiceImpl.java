/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumCompanyRelation;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumCompanyRelationMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumCompanyRelationService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：基础资料-品类关系表 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumCompanyRelationService
 * @email XX.com
 * @date 创建时间：2023-7-10 9:24:27
 */
@Service
public class BasicsdatumCompanyRelationServiceImpl extends BaseServiceImpl<BasicsdatumCompanyRelationMapper, BasicsdatumCompanyRelation> implements BasicsdatumCompanyRelationService {

    /**
     * 方法描述-批量新增
     *
     * @param list
     * @return
     */
    @Override
    public Boolean batchAddition(List<BasicsdatumCompanyRelation> list) {
        return saveBatch(list);
    }

    /**
     * 方法描述-先删除在批量新增
     *
     * @param list
     * @return
     */
    @Override
    public Boolean deleteBatchAddition(List<BasicsdatumCompanyRelation> list) {
        if(!CollectionUtils.isEmpty(list)){
            /*删除之前的数据*/
            baseMapper.deleteRelation(list.get(0).getDataId(),list.get(0).getType());
            //再次新增
            return saveBatch(list);
        }
         return true;
    }
}
