/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.esorderbook.dto.EsOrderBookQueryDto;
import com.base.sbc.module.esorderbook.service.EsOrderBookService;
import com.base.sbc.module.esorderbook.vo.EsOrderBookItemVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类描述：ES订货本 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.esorderbook.web.EsOrderBookController
 * @email your email
 * @date 创建时间：2024-3-28 16:21:15
 */
@RestController
@Api(tags = "ES订货本")
@RequestMapping(value = BaseController.SAAS_URL + "/esOrderBook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class EsOrderBookController {

    @Autowired
    private EsOrderBookService esOrderBookService;

    @ApiOperation(value = "分页查询")
    @GetMapping
    public PageInfo<EsOrderBookItemVo> page(EsOrderBookQueryDto dto) {
        return esOrderBookService.findPage(dto);
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/{id}")
    public Boolean removeById(@PathVariable("id") String id) {
        List<String> ids = StringUtils.convertList(id);
        return esOrderBookService.removeByIds(ids);
    }





}































