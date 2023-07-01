package com.base.sbc.module.band.service.impl;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.band.entity.Band;
import com.base.sbc.module.band.mapper.BandMapper;
import com.base.sbc.module.band.service.BandService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @data 2023/4/7 10:35
 */
@Service
public class BandServiceImpl extends BaseServiceImpl<BandMapper, Band> implements BandService {
    @Override
    public String getNameByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return "";
        }
        QueryWrapper qw = new QueryWrapper();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.eq("code", code);
        qw.last("limit 1");
        return getBaseMapper().getNameByCode(qw);
    }

    @Override
    public Map<String, String> getNamesByCodes(String bandCodes) {
        Map<String, String> result = new HashMap<>(12);
        QueryWrapper<Band> qw = new QueryWrapper();
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.in("code", StrUtil.split(bandCodes, CharUtil.COMMA));
        List<Band> bandList = list(qw);
        for (Band band : bandList) {
            result.put(band.getCode(), band.getBandName());
        }
        return result;
    }
}
