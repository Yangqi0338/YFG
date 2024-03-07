package com.base.sbc.module.style.service.impl;

import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.module.style.dto.StyleAnalyseQueryDto;
import com.base.sbc.module.style.service.StyleAnalyseService;
import com.base.sbc.module.style.vo.StyleColorVo;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StyleAnalyseServiceImpl implements StyleAnalyseService {



    @Override
    public PageInfo<StyleColorVo> findDesignPage(StyleAnalyseQueryDto dto) {

        BaseQueryWrapper qw = new BaseQueryWrapper<>();

        boolean isColumnHeard = QueryGenerator.initQueryWrapper(qw, dto);




        if(isColumnHeard){

        }

        return null;
    }

}
