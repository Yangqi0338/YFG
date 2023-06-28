/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.dto.FieldDisplaySaveDto;
import com.base.sbc.module.planning.entity.FieldDisplayConfig;
import com.base.sbc.module.planning.vo.FieldDisplayVo;

import java.util.List;

/**
 * 类描述：字段显示隐藏配置 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.FieldDisplayConfigService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-28 14:22:42
 */
public interface FieldDisplayConfigService extends BaseService<FieldDisplayConfig> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 款式看板
     */
    String styleBoard = "styleBoard";

    /**
     * 企划看板
     */
    String planningBoard = "planningBoard";

    /**
     * 获取配置 key=default为默认配置,key=user为用户配置
     *
     * @param type
     * @return
     */
    List<FieldDisplayVo> getConfig(String type);

    /**
     * 保存配置
     *
     * @param dto
     * @return
     */
    boolean saveConfig(FieldDisplaySaveDto dto);

// 自定义方法区 不替换的区域【other_end】


}
