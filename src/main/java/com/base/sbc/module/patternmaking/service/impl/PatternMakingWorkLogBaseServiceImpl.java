/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.patternmaking.dto.PatternMakingWorkLogSaveDto;
import com.base.sbc.module.patternmaking.dto.PatternMakingWorkLogSearchDto;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.entity.PatternMakingWorkLog;
import com.base.sbc.module.patternmaking.mapper.PatternMakingWorkLogMapper;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.service.PatternMakingWorkLogService;
import com.base.sbc.module.patternmaking.vo.PatternMakingWorkLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：打版管理-工作记录 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.service.PatternMakingWorkLogService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-5 19:42:22
 */
@Service
public class PatternMakingWorkLogBaseServiceImpl extends BaseServiceImpl<PatternMakingWorkLogMapper, PatternMakingWorkLog> implements PatternMakingWorkLogService {


// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private PatternMakingService patternMakingService;


    @Override
    public List<PatternMakingWorkLogVo> findList(PatternMakingWorkLogSearchDto dto) {
        QueryWrapper<PatternMakingWorkLog> qw = new QueryWrapper<>();
        qw.eq("pattern_making_id", dto.getPatternMakingId());
        qw.eq("user_type", dto.getUserType());
        List<PatternMakingWorkLog> list = list(qw);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        List<PatternMakingWorkLogVo> voList = BeanUtil.copyToList(list, PatternMakingWorkLogVo.class);
        // 设置人员名称
        PatternMaking patternMaking = patternMakingService.getById(dto.getPatternMakingId());
        if (patternMaking != null) {
            String worker = null;
            if (StrUtil.equals("裁剪工", dto.getUserType())) {
                worker = patternMaking.getCutterName();
            } else if (StrUtil.equals("车缝工", dto.getUserType())) {
                worker = patternMaking.getStitcher();
            }
            for (PatternMakingWorkLogVo vo : voList) {
                vo.setWorker(worker);
            }
        }

        return voList;
    }

    @Override
    public boolean saveLog(PatternMakingWorkLogSaveDto dto) {
        PatternMakingWorkLog patternMakingWorkLog = BeanUtil.copyProperties(dto, PatternMakingWorkLog.class);
        return save(patternMakingWorkLog);
    }

    @Override
    public boolean updateLog(PatternMakingWorkLogSaveDto dto) {
        PatternMakingWorkLog byId = getById(dto.getId());
        if (byId == null) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        BeanUtil.copyProperties(dto, byId);
        return updateById(byId);
    }

// 自定义方法区 不替换的区域【other_end】

}
