/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.planning.dto.ThemePlanningSaveDTO;
import com.base.sbc.module.planning.dto.ThemePlanningSearchDTO;
import com.base.sbc.module.planning.service.ThemePlanningService;
import com.base.sbc.module.planning.vo.ThemePlanningListVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 类描述：主题企划 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.web.ThemePlanningController
 * @email your email
 * @date 创建时间：2023-8-15 13:58:35
 */
@RestController
@Api(tags = "主题企划")
@RequestMapping(value = BaseController.SAAS_URL + "/themePlanning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ThemePlanningController extends BaseController {
    @Autowired
    private ThemePlanningService themePlanningService;

    @ApiOperation(value = "获取主题企划列表")
    @PostMapping("/getThemePlanningList")
    public PageInfo<ThemePlanningListVO> getThemePlanningList(@Valid @RequestBody ThemePlanningSearchDTO themePlanningSearchDTO) {
        return themePlanningService.getThemePlanningList(themePlanningSearchDTO);
    }

    @ApiOperation(value = "保存")
    @PostMapping("/themePlanningSave")
    public ApiResult themePlanningSave(@Valid @RequestBody ThemePlanningSaveDTO dto) {
        return updateSuccess(themePlanningService.themePlanningSave(dto));
    }


    @ApiOperation(value = "获取详情")
    @GetMapping("/getThemePlanningById")
    public ApiResult getThemePlanningById(@Valid @NotBlank(message = "主题企划id不可为空") String id) {
        return selectSuccess(themePlanningService.getThemePlanningById(id));
    }
}































