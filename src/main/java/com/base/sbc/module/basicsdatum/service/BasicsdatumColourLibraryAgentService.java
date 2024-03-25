/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.basicsdatum.dto.QueryBasicsdatumColourLibraryAgentDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumColourLibraryAgentVo;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibraryAgent;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：基础资料-颜色库 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryAgentService
 * @email your email
 * @date 创建时间：2024-2-28 16:13:32
 */
public interface BasicsdatumColourLibraryAgentService extends BaseService<BasicsdatumColourLibraryAgent> {

    PageInfo<BasicsdatumColourLibraryAgentVo> findPage(QueryBasicsdatumColourLibraryAgentDto dto);

    ApiResult importExcel(MultipartFile file) throws Exception;

    void exportExcel(HttpServletResponse response, QueryBasicsdatumColourLibraryAgentDto dto) throws IOException;

    void statusUpdate(StartStopDto startStopDto);

    void del(String id);

    void saveOrUpdateMian(BasicsdatumColourLibraryAgent basicsdatumColourLibraryAgent);
}
