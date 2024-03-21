package com.base.sbc.module.report.service;

import com.base.sbc.module.report.dto.*;
import com.base.sbc.module.report.vo.*;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 技术中心看板
 */
public interface TechnologyCenterBoardService {

    /**
     * 吊牌充绒量报表
     * @return
     */
    PageInfo<HangTagReportVo> getHangTagReortPage(HangTagReportQueryDto dto);



}
