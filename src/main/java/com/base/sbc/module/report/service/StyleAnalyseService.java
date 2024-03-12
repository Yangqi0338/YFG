package com.base.sbc.module.report.service;

import com.base.sbc.module.report.dto.StyleAnalyseQueryDto;
import com.base.sbc.module.report.vo.StyleAnalyseVo;
import com.github.pagehelper.PageInfo;

public interface StyleAnalyseService {

    PageInfo<StyleAnalyseVo> findDesignPage(StyleAnalyseQueryDto dto);

    PageInfo<StyleAnalyseVo> findStylePage(StyleAnalyseQueryDto dto);
}
