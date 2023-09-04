/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.planning.dto.ColorPlanningSaveDTO;
import com.base.sbc.module.planning.dto.ColorPlanningSearchDTO;
import com.base.sbc.module.planning.entity.ColorPlanning;
import com.base.sbc.module.planning.service.ColorPlanningService;
import com.base.sbc.module.planning.vo.ColorPlanningListVO;
import com.base.sbc.module.planning.vo.ColorPlanningVO;
import com.base.sbc.module.purchase.entity.MaterialWarehouse;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：颜色企划 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.web.ColorPlanningController
 * @email your email
 * @date 创建时间：2023-8-15 13:58:50
 */
@RestController
@Api(tags = "颜色企划")
@RequestMapping(value = BaseController.SAAS_URL + "/colorPlanning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ColorPlanningController extends BaseController {

    @Autowired
    private ColorPlanningService colorPlanningService;

    @ApiOperation(value = "获取颜色企划列表")
    @PostMapping("/getColorPlanningList")
    public PageInfo<ColorPlanningListVO> getColorPlanningList(@Valid @RequestBody ColorPlanningSearchDTO colorPlanningSearchDTO) {
        return colorPlanningService.getColorPlanningList(colorPlanningSearchDTO);
    }

    @ApiOperation(value = "保存")
    @PostMapping("/colorPlanningSave")
    public ApiResult colorPlanningSave(@Valid @RequestBody ColorPlanningSaveDTO colorPlanningSaveDTO) {
        return updateSuccess(colorPlanningService.colorPlanningSave(colorPlanningSaveDTO));
    }


    @ApiOperation(value = "获取详情")
    @GetMapping("/getDetailById")
    public ApiResult getDetailById(@Valid @NotBlank(message = "颜色企划id不可为空") String id) {
        return selectSuccess(colorPlanningService.getDetailById(id));
    }

    @ApiOperation(value = "通过产品季获取")
    @GetMapping("/getListByPlanningSeasonId")
    public ApiResult getListByPlanningSeasonId(String planningSeasonId) {
        return selectSuccess(colorPlanningService.getListByPlanningSeasonId(planningSeasonId));
    }

    @ApiOperation(value = "删除-通过id查询")
    @DeleteMapping
    public ApiResult delete(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("id") String id) {
        if(StringUtils.isBlank(id)){
            return deleteAttributeNotRequirements("id");
        }
        ColorPlanningVO colorPlanningVO = colorPlanningService.getDetailById(id);
        if(CollectionUtil.isNotEmpty(colorPlanningVO.getColorPlanningItems())){
            return ApiResult.error("该颜色企划还存在明细数据无法删除，请处理！", 500);
        }

        boolean result = colorPlanningService.removeById(id);
        if(result) {
            return ApiResult.success("删除成功！");
        }
        return deleteNotFound();
    }
}































