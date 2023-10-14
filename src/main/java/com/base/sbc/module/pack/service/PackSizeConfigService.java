/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.pack.dto.PackSizeConfigDto;
import com.base.sbc.module.pack.dto.PackSizeConfigSearchDto;
import com.base.sbc.module.pack.entity.PackSizeConfig;
import com.base.sbc.module.pack.vo.PackSizeConfigVo;
import com.base.sbc.module.style.entity.Style;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：资料包-尺寸表配置 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackSizeConfigService
 * @email your email
 * @date 创建时间：2023-9-1 14:07:14
 */
public interface PackSizeConfigService extends PackBaseService<PackSizeConfig> {

// 自定义方法区 不替换的区域【other_start】

    PackSizeConfigVo getConfig(String foreignId, String packType);

    PackSizeConfigVo saveConfig(PackSizeConfigDto dto);

    PackSizeConfig createByStyle(String foreignId, String packType, Style style);

    PageInfo<PackSizeConfigVo> pageInfo(PackSizeConfigSearchDto dto);

    /**
     * 获取测量部位的当差设置
     * @param dto
     * @return
     */
    String  gatPartDifference(PackSizeConfigDto dto);

// 自定义方法区 不替换的区域【other_end】


}

