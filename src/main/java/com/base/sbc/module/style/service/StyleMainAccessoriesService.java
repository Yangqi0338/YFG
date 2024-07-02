/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.style.entity.StyleMainAccessories;

import java.util.List;

/** 
 * 类描述：款式-款式主款配饰表 service类
 * @address com.base.sbc.module.style.service.StyleMainAccessoriesService
 * @author mengfanjiang
 * @email xx@qq.com
 * @date 创建时间：2023-10-17 11:06:10
 * @version 1.0  
 */
public interface StyleMainAccessoriesService extends BaseService<StyleMainAccessories>{

// 自定义方法区 不替换的区域【other_start】

    /**
     * 获取配饰下的主款配饰
     * @param styleColorId
     * @param isTrim
     * @return
     */
  List<StyleMainAccessories> styleMainAccessoriesList(String styleColorId,String isTrim);


    List<StyleMainAccessories> styleMainAccessoriesListBatch(List<String> styleColorIds, String isTrim);

    /**
     * 清除主款或配饰
     * @param styleColorId
     * @param isTrim
     * @return
     */
    Boolean delMainAccessories(String styleColorId,String isTrim);

// 自定义方法区 不替换的区域【other_end】

	
}
