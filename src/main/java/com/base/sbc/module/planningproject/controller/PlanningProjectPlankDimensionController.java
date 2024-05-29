package com.base.sbc.module.planningproject.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlankDimension;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankDimensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 卞康
 * @date 2024-03-11 10:00:08
 * @mail 247967116@qq.com
 * 企划看板坑位维度信息
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/planningProjectPlankDimension", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PlanningProjectPlankDimensionController extends BaseController{
    private final PlanningProjectPlankDimensionService planningProjectPlankDimensionService;

    /**
     * 保存
     */
    @PostMapping(value = "/saveOrUpdate")
    public ApiResult<PlanningProjectPlankDimension> saveOrUpdate(@RequestBody PlanningProjectPlankDimension planningProjectPlankDimension) {
        planningProjectPlankDimensionService.saveOrUpdate(planningProjectPlankDimension, "企划看板坑位维度信息");
        return insertSuccess(planningProjectPlankDimension);
    }
}
