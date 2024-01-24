package com.base.sbc.module.planningproject.controller;

import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.planningproject.dto.CategoryPlanningDetailsQueryDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import com.base.sbc.module.planningproject.service.CategoryPlanningDetailsService;
import com.base.sbc.module.planningproject.vo.CategoryPlanningDetailsVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 卞康
 * @date 2024-01-22 14:01:47
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "品类企划明细-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/categoryPlanningDetails", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class CategoryPlanningDetailsController extends BaseController{
    private final CategoryPlanningDetailsService categoryPlanningDetailsService;

    /**
     * 根据条件查询分页列表
     */
    @RequestMapping("/queryPage")
    public ApiResult queryPage(CategoryPlanningDetailsQueryDto dto) {
        PageInfo<CategoryPlanningDetailsVo> pageInfo = categoryPlanningDetailsService.queryPage(dto);
        return selectSuccess(pageInfo);
    }

    /**
     * 根据id查询明细详情
     */
    @RequestMapping("/getDetailById")
    public ApiResult getDetailById(String id) {
        CategoryPlanningDetailsVo categoryPlanningDetailsVo = categoryPlanningDetailsService.getDetailById(id);
        return selectSuccess(categoryPlanningDetailsVo);
    }

    /**
     * 编辑详情后保存
     */
    @RequestMapping("/updateDetail")
    @DuplicationCheck
    public ApiResult updateDetail(CategoryPlanningDetailsVo categoryPlanningDetailsVo) {
        categoryPlanningDetailsService.updateDetail(categoryPlanningDetailsVo);
        return updateSuccess(categoryPlanningDetailsVo);
    }
}
