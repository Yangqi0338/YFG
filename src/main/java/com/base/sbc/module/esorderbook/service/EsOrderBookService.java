/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.esorderbook.dto.EsOrderBookQueryDto;
import com.base.sbc.module.esorderbook.dto.EsOrderBookSaveDto;
import com.base.sbc.module.esorderbook.entity.EsOrderBook;
import com.base.sbc.module.esorderbook.vo.EsOrderBookItemVo;
import com.base.sbc.module.esorderbook.vo.EsOrderBookVo;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * 类描述：ES订货本 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.esorderbook.service.EsOrderBookService
 * @email your email
 * @date 创建时间：2024-3-28 16:21:15
 */
public interface EsOrderBookService extends BaseService<EsOrderBook> {


    PageInfo<EsOrderBookItemVo> findPage(EsOrderBookQueryDto dto);


    EsOrderBook saveMain(EsOrderBookVo dto);

    void lock(List<EsOrderBookItemVo> list);

    void unLock(List<EsOrderBookItemVo> list);

    void exportExcel(HttpServletResponse response, EsOrderBookQueryDto dto) throws IOException;

    void del(List<EsOrderBookItemVo> list);

    void delItem(List<EsOrderBookItemVo> list);

    void delHead(String id);

    void updateHeadName(EsOrderBookItemVo vo);

    ApiResult uploadStyleColorPics(Principal user, MultipartFile file, EsOrderBookItemVo vo);

    void saveItemList(EsOrderBookSaveDto dto);

    @Transactional
    void updateItemList(List<EsOrderBookItemVo> itemList);
}