/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.patternmaking.dto.WorkLogSaveDto;
import com.base.sbc.module.patternmaking.dto.WorkLogSearchDto;
import com.base.sbc.module.patternmaking.entity.WorkLog;
import com.base.sbc.module.patternmaking.mapper.WorkLogMapper;
import com.base.sbc.module.patternmaking.service.WorkLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 类描述：工作小账 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.service.WorkLogService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-10 19:29:31
 */
@Service
public class WorkLogServiceImpl extends BaseServiceImpl<WorkLogMapper, WorkLog> implements WorkLogService {


    // 自定义方法区 不替换的区域【other_start】
    @Resource
    RedisUtils redisUtils;


    @Override
    public PageInfo<WorkLog> pageInfo(WorkLogSearchDto dto) {
        BaseQueryWrapper<WorkLog> qw = new BaseQueryWrapper<>();
        qw.notEmptyIn("worker_id", dto.getWorkerId());
        qw.notEmptyIn("log_type", dto.getLogType());
        qw.notEmptyIn("num_type", dto.getNumType());
        qw.andLike(dto.getSearch(), "worker", "reference_no", "work_description");
        if (dto.getWorkDate() != null) {
            String tempDate = DateUtil.format(dto.getWorkDate(), DatePattern.NORM_DATE_PATTERN);
            qw.between("work_date",
                    DateUtil.parse(tempDate + " 00:00:00"),
                    DateUtil.parse(tempDate + " 23:59:59"));
        }
        Page<WorkLog> objects = PageHelper.startPage(dto);
        list(qw);
        return objects.toPageInfo();
    }

    @Override
    public WorkLog saveByDto(WorkLogSaveDto workLog) {
        WorkLog bean = BeanUtil.copyProperties(workLog, WorkLog.class);
        String redisKey = redis_key + getCompanyCode();
        long incr = redisUtils.incr(redisKey, 1);
        String code = StrUtil.padPre(String.valueOf(incr), 8, "0");
        bean.setCode(code);
        save(bean);
        return bean;
    }

// 自定义方法区 不替换的区域【other_end】

}
