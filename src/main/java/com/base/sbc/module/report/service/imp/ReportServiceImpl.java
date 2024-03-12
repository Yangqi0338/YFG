package com.base.sbc.module.report.service.imp;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.report.dto.HangTagReportQueryDto;
import com.base.sbc.module.report.mapper.ReportMapper;
import com.base.sbc.module.report.service.ReportService;
import com.base.sbc.module.report.vo.HangTagReportVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;
    @Resource
    private StylePicUtils stylePicUtils;

    @Override
    public PageInfo<HangTagReportVo> getHangTagReortPage(HangTagReportQueryDto dto) {
        BaseQueryWrapper<HangTagReportQueryDto> qw = new BaseQueryWrapper<>();
        qw.eq("t.del_flag", "0");
        qw.eq("t1.del_flag", "0");
        qw.eq("ts.del_flag", "0");
        qw.eq("t2.del_flag", "0");
        qw.eq("tbs.del_flag", "0");
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        PageHelper.startPage(dto);
        List<HangTagReportVo> list = reportMapper.getHangTagReortList(qw);

        if (CollUtil.isEmpty(list)) {
            return new PageInfo<>(list);
        }
        if (isColumnHeard) {
            return new PageInfo<>(list);
        }
        /*查询款式图*/
        //stylePicUtils.setStylePic(list, "stylePic");
        stylePicUtils.setStyleColorPic2(list, "styleColorPic");

        return new PageInfo<>(list);
    }
}
