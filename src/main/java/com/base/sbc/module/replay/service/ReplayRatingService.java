/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.replay.dto.ReplayRatingFabricDTO;
import com.base.sbc.module.replay.dto.ReplayRatingPatternDTO;
import com.base.sbc.module.replay.dto.ReplayRatingSaveDTO;
import com.base.sbc.module.replay.dto.ReplayRatingStyleDTO;
import com.base.sbc.module.replay.dto.ReplayRatingTransferDTO;
import com.base.sbc.module.replay.entity.ReplayRating;
import com.base.sbc.module.replay.vo.ReplayRatingBulkWarnVO;
import com.base.sbc.module.replay.vo.ReplayRatingPageVO;
import com.base.sbc.module.replay.vo.ReplayRatingQO;
import com.base.sbc.module.replay.vo.ReplayRatingVO;
import com.base.sbc.module.replay.vo.ReplayRatingYearQO;
import com.base.sbc.module.replay.vo.ReplayRatingYearVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：基础资料-复盘评分 service类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.service.ReplayRatingService
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
public interface ReplayRatingService extends BaseService<ReplayRating> {

// 自定义方法区 不替换的区域【other_start】

    ReplayRatingPageVO<? extends ReplayRatingVO> queryPageInfo(ReplayRatingQO dto);

    String doSave(ReplayRatingSaveDTO replayRatingSaveDTO);

    ReplayRatingStyleDTO getStyleById(String styleColorId);

    ReplayRatingPatternDTO getPatternById(String styleColorId);

    ReplayRatingFabricDTO getFabricById(String styleColorId);

    PageInfo<ReplayRatingYearVO> yearListByStyleNo(ReplayRatingYearQO replayRatingYearQO);

    String transferPatternLibrary(ReplayRatingTransferDTO transferDTO);

    List<ReplayRatingBulkWarnVO> bulkWarnMsg(String bulkStyleNo);

    void exportExcel(ReplayRatingQO qo);

// 自定义方法区 不替换的区域【other_end】


}