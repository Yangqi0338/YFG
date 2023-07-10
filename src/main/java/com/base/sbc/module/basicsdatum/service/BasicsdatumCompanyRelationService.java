/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumCompanyRelation;
import com.base.sbc.module.common.service.BaseService;

import java.util.List;

/** 
 * 类描述：基础资料-品类关系表 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumCompanyRelationService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-7-10 9:24:27
 * @version 1.0  
 */
public interface BasicsdatumCompanyRelationService extends BaseService<BasicsdatumCompanyRelation> {


        /**
         * 方法描述-批量新增
         *
         * @param list
         * @return
         */
        Boolean batchAddition(List<BasicsdatumCompanyRelation> list);

        /**
         * 方法描述-先删除在批量新增
         *
         * @param list
         * @return
         */
        Boolean deleteBatchAddition(List<BasicsdatumCompanyRelation> list);

}
