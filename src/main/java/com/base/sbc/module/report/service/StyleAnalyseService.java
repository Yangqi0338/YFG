package com.base.sbc.module.report.service;

import com.base.sbc.module.report.dto.StyleAnalyseQueryDto;
import com.base.sbc.module.report.vo.StyleAnalyseVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface StyleAnalyseService {

    PageInfo<StyleAnalyseVo> findDesignPage(StyleAnalyseQueryDto dto);

    PageInfo<StyleAnalyseVo> findStylePage(StyleAnalyseQueryDto dto);

    void findDesignPageExport(HttpServletResponse response, StyleAnalyseQueryDto dto) throws IOException;

    void findStylePageExport(HttpServletResponse response, StyleAnalyseQueryDto dto) throws IOException;
}
