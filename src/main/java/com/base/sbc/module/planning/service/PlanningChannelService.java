/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.dto.PlanningChannelDto;
import com.base.sbc.module.planning.dto.PlanningChannelSearchDto;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.vo.PlanningChannelVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * 类描述：企划-渠道 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningChannelService
 * @email your email
 * @date 创建时间：2023-7-21 16:00:35
 */
public interface PlanningChannelService extends BaseService<PlanningChannel> {

// 自定义方法区 不替换的区域【other_start】

    PlanningChannelVo saveByDto(PlanningChannelDto dto);

    void checkedRepeat(PlanningChannelDto dto);

    PageInfo<PlanningChannelVo> channelPageInfo(PlanningChannelSearchDto dto);

    boolean checkHasSub(String id);

    boolean delChannel(String id);

    Map<String, Long> countByPlanningSeason();


// 自定义方法区 不替换的区域【other_end】


}
