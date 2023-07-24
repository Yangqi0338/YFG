/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.goodscolor.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.goodscolor.entity.GoodsColor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：物料颜色 service类
 * @address com.base.sbc.baseData.service.GoodsColorService
 * @author gcc
 * @email gcc@bestgcc.cn
 * @date 创建时间：2021-4-25 9:05:01
 * @version 1.0
 */
@Service
public interface GoodsColorService extends BaseService<GoodsColor> {


    /**
     * 基础资料-物料颜色导入
     * @param file
     * @return
     */
    Boolean importExcel(MultipartFile file) throws  Exception;

    /**
     * 基础资料-物料颜色导出
     * @param response
     * @return
     */
    void deriveExcel(HttpServletResponse response) throws Exception;

}
