package com.base.sbc.module.report.service;

import com.base.sbc.module.report.dto.HangTagReportQueryDto;
import com.base.sbc.module.report.vo.HangTagReportVo;
import com.github.pagehelper.PageInfo;

public interface ReportService {

    /**
     * 吊牌充绒量报表
     * @return
     */
    PageInfo<HangTagReportVo> getHangTagReortPage(HangTagReportQueryDto dto);

}
