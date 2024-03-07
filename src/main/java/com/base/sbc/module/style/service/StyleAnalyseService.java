package com.base.sbc.module.style.service;

import com.base.sbc.module.style.dto.StyleAnalyseQueryDto;
import com.base.sbc.module.style.vo.StyleAnalyseVo;
import com.github.pagehelper.PageInfo;

public interface StyleAnalyseService {

    PageInfo<StyleAnalyseVo> findDesignPage(StyleAnalyseQueryDto dto);

    PageInfo<StyleAnalyseVo> findStylePage(StyleAnalyseQueryDto dto);
}
