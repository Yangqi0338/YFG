/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.controller;

import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.replay.dto.ReplayRatingDTO;
import com.base.sbc.module.replay.dto.ReplayRatingQO;
import com.base.sbc.module.replay.entity.ReplayRating;
import com.base.sbc.module.replay.service.ReplayRatingService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：基础资料-复盘评分 Controller类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.web.ReplayRatingController
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@RestController
@Api(tags = "基础资料-复盘评分")
@RequestMapping(value = BaseController.SAAS_URL + "/replayRating")
@Validated
public class ReplayRatingController {

    @Autowired
    private ReplayRatingService replayRatingService;

    @ApiOperation(value = "分页查询")
    @GetMapping("queryPageInfo")
    public PageInfo<ReplayRatingDTO> queryPageInfo(ReplayRatingQO dto) {
        return replayRatingService.findPage(dto);
    }

    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/{id}")
    public ReplayRating getById(@PathVariable("id") String id) {
        return replayRatingService.getById(id);
    }

    @ApiOperation(value = "保存")
    @PostMapping("save")
    @DuplicationCheck
    public ApiResult<String> save(@RequestBody ReplayRating replayRating) {
        replayRatingService.save(replayRating);
        return replayRating;
    }

}































