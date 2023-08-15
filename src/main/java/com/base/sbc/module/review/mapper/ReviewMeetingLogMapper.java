/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.review.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.review.entity.ReviewMeetingLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：评审会-会议记录 dao类
 * @address com.base.sbc.module.review.dao.ReviewMeetingLogDao
 * @author tzy  
 * @email  974849633@qq.com
 * @date 创建时间：2023-8-14 17:06:39 
 * @version 1.0  
 */
@Mapper
public interface ReviewMeetingLogMapper extends BaseMapper<ReviewMeetingLog> {
    List<ReviewMeetingLog> selectMeetingLogRelation(@Param(Constants.WRAPPER) BaseQueryWrapper qw);
}