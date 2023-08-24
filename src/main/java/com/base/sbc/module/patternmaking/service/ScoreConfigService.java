/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.patternmaking.dto.ScoreConfigSearchDto;
import com.base.sbc.module.patternmaking.entity.ScoreConfig;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：默认评分配置 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.service.ScoreConfigService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-24 13:37:10
 */
public interface ScoreConfigService extends BaseService<ScoreConfig> {

// 自定义方法区 不替换的区域【other_start】

    PageInfo<ScoreConfig> pageByDto(ScoreConfigSearchDto dto);

    ScoreConfig saveBy(ScoreConfig scoreConfig);

    void checkRepeat(ScoreConfig scoreConfig);

    boolean updateScore(ScoreConfig scoreConfig);

    ScoreConfig findOne(ScoreConfigSearchDto dto);


// 自定义方法区 不替换的区域【other_end】


}
