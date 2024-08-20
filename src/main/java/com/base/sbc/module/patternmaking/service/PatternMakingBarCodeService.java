/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.patternmaking.entity.PatternMakingBarCode;
import com.base.sbc.module.patternmaking.dto.PatternMakingBarCodeQueryDto;
import com.base.sbc.module.patternmaking.vo.PatternMakingBarCodeVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述： service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.service.PatternMakingBarCodeService
 * @email your email
 * @date 创建时间：2024-7-3 15:56:26
 */
public interface PatternMakingBarCodeService extends BaseService<PatternMakingBarCode> {


    PageInfo<PatternMakingBarCodeVo> findPage(PatternMakingBarCodeQueryDto dto);


    PageInfo<PatternMakingBarCodeVo> findPageLog(PatternMakingBarCodeQueryDto dto);

    List<PatternMakingBarCodeVo> getByBarCode(String barCode);

    Boolean removeByBarCode(String barCode);

    void saveMain(PatternMakingBarCode patternMakingBarCode);

    List<PatternMakingBarCode> listByCode(String headId, Integer pitSite);

    void status(PatternMakingBarCode patternMakingBarCode);

    List<PatternMakingBarCode> listbyHeadId(List<String> ids);

    PageInfo<PatternMakingBarCodeVo> pageAudit(PatternMakingBarCodeQueryDto dto);
}