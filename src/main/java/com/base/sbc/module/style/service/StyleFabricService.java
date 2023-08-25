/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.style.dto.StyleFabricSaveDTO;
import com.base.sbc.module.style.entity.StyleFabric;
import com.base.sbc.module.style.vo.StyleFabricVO;

import java.util.List;

/**
 * 类描述：款式面料 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.service.StyleFabricService
 * @email your email
 * @date 创建时间：2023-8-24 10:17:48
 */
public interface StyleFabricService extends BaseService<StyleFabric> {
    // 自定义方法区 不替换的区域【other_start】

    /**
     * 保存
     *
     * @param dtos
     */
    void save(List<StyleFabricSaveDTO> dtos);

    /**
     * 修改
     *
     * @param dto
     */
    void update(StyleFabricSaveDTO dto);


    /**
     * 获取列表
     *
     * @param styleId
     * @return
     */
    List<StyleFabricVO> getByStyleId(String styleId);


// 自定义方法区 不替换的区域【other_end】


}
