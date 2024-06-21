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
import com.base.sbc.module.replay.dto.ReplayRatingFabricDTO;
import com.base.sbc.module.replay.dto.ReplayRatingPatternDTO;
import com.base.sbc.module.replay.dto.ReplayRatingSaveDTO;
import com.base.sbc.module.replay.dto.ReplayRatingStyleDTO;
import com.base.sbc.module.replay.service.ReplayRatingService;
import com.base.sbc.module.replay.vo.ReplayRatingQO;
import com.base.sbc.module.replay.vo.ReplayRatingVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
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
public class ReplayRatingController extends BaseController {

    @Autowired
    private ReplayRatingService replayRatingService;

    @ApiOperation(value = "分页查询")
    @GetMapping("queryPageInfo")
    public ApiResult<PageInfo<? extends ReplayRatingVO>> queryPageInfo(@Validated ReplayRatingQO dto) {
        return selectSuccess(replayRatingService.queryPageInfo(dto));
    }

    @ApiOperation(value = "单款复盘明细")
    @GetMapping("/style/{styleColorId}")
    public ApiResult<ReplayRatingStyleDTO> getStyleById(@PathVariable("styleColorId") @NotBlank(message = "id不能为空") String styleColorId) {
        return selectSuccess(replayRatingService.getStyleById(styleColorId));
    }

    @ApiOperation(value = "版型复盘明细")
    @GetMapping("/pattern/{id}")
    public ApiResult<ReplayRatingPatternDTO> getPatternById(@PathVariable("id") @NotBlank(message = "id不能为空") String id) {
        return selectSuccess(replayRatingService.getPatternById(id));
    }


    @ApiOperation(value = "面料复盘明细")
    @GetMapping("/fabric/{id}")
    public ApiResult<ReplayRatingFabricDTO> getFabricById(@PathVariable("id") @NotBlank(message = "id不能为空") String id) {
        return selectSuccess(replayRatingService.getFabricById(id));
    }

    @ApiOperation(value = "保存")
    @PostMapping("save")
    @DuplicationCheck
    public ApiResult<String> save(@RequestBody ReplayRatingSaveDTO replayRatingSaveDTO) {
        return updateSuccess(replayRatingService.doSave(replayRatingSaveDTO));
    }

    @ApiOperation(value = "导出")
    @PostMapping("exportExcel")
    @DuplicationCheck
    public ApiResult<String> exportExcel(@RequestBody ReplayRatingSaveDTO replayRatingSaveDTO) {
        return updateSuccess(replayRatingService.doSave(replayRatingSaveDTO));
    }

    @ApiOperation(value = "大货异常信息")
    @GetMapping("bulkWarnMsg")
    public ApiResult<String> bulkWarnMsg(@RequestBody ReplayRatingSaveDTO replayRatingSaveDTO) {
        return updateSuccess(replayRatingService.doSave(replayRatingSaveDTO));
    }

    @ApiOperation(value = "转入版型库")
    @PostMapping("transferPatternLibrary")
    @DuplicationCheck
    public ApiResult<String> transferPatternLibrary(@RequestBody ReplayRatingSaveDTO replayRatingSaveDTO) {
        return updateSuccess(replayRatingService.doSave(replayRatingSaveDTO));
    }

    @ApiOperation(value = "历史套版详情/应用款")
    @GetMapping("yearListByStyleNo")
    public ApiResult<String> yearListByStyleNo(@RequestBody ReplayRatingSaveDTO replayRatingSaveDTO) {
        return updateSuccess(replayRatingService.doSave(replayRatingSaveDTO));
    }

}































