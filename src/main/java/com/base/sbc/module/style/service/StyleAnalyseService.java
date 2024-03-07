package com.base.sbc.module.style.service;

import com.base.sbc.module.style.dto.StyleAnalyseQueryDto;
import com.base.sbc.module.style.vo.StyleColorVo;
import com.github.pagehelper.PageInfo;

public interface StyleAnalyseService {

    PageInfo<StyleColorVo> findDesignPage(StyleAnalyseQueryDto dto);
}
