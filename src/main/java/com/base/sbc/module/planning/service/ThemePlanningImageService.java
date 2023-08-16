/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.entity.ThemePlanningImage;

import java.util.List;

/**
 * 类描述：主题企划图片 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.ThemePlanningImageService
 * @email your email
 * @date 创建时间：2023-8-15 13:58:40
 */
public interface ThemePlanningImageService extends BaseService<ThemePlanningImage> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 通过主题企划id获取
     *
     * @param themePlanningId
     * @return
     */
    String getByThemePlanningId(String themePlanningId);

    /**
     * 保存
     *
     * @param images
     * @param themePlanningId
     */
    void save(String images, String themePlanningId);


// 自定义方法区 不替换的区域【other_end】


}

