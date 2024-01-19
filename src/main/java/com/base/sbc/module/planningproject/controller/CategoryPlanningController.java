package com.base.sbc.module.planningproject.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.planningproject.dto.CategoryPlanningQueryDto;
import com.base.sbc.module.planningproject.service.CategoryPlanningService;
import com.base.sbc.module.planningproject.vo.CategoryPlanningVo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-19 17:22:13
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "品类企划-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/categoryPlanning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class CategoryPlanningController extends BaseController{
    private final CategoryPlanningService categoryPlanningService;
    /**
     * 根据条件查询列表
     */
    @RequestMapping("/queryList")
    public ApiResult queryList(CategoryPlanningQueryDto categoryPlanningQueryDto) {
        List<CategoryPlanningVo> categoryPlanningVos = categoryPlanningService.queryList(categoryPlanningQueryDto);
        return selectSuccess(categoryPlanningVos);
    }
}
