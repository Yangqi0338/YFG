package com.base.sbc.module.planningproject.controller;

import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.planningproject.dto.CategoryPlanningDetailDTO;
import com.base.sbc.module.planningproject.dto.CategoryPlanningDetailsQueryDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import com.base.sbc.module.planningproject.service.CategoryPlanningDetailsService;
import com.base.sbc.module.planningproject.vo.CategoryPlanningDetailVO;
import com.base.sbc.module.planningproject.vo.CategoryPlanningDetailsVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2024-01-22 14:01:47
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "品类企划明细-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/categoryPlanningDetails", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class CategoryPlanningDetailsController extends BaseController {
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
     * 获取所有的品类
     */
    @GetMapping("/getProdCategoryNameList")
    public ApiResult getProdCategoryNameList(CategoryPlanningDetailsQueryDto dto) {
        BaseQueryWrapper<CategoryPlanningDetails> baseQueryWrapper = new BaseQueryWrapper<>();
        baseQueryWrapper.eq("category_planning_id", dto.getCategoryPlanningId());
        baseQueryWrapper.groupBy("prod_category_name");
        baseQueryWrapper.select("prod_category_code", "prod_category_name");
        baseQueryWrapper.isNotNullStr("prod_category_name");
        List<CategoryPlanningDetails> list = categoryPlanningDetailsService.list(baseQueryWrapper);
        List<Map<String, String>> maps = new ArrayList<>();
        for (CategoryPlanningDetails categoryPlanningDetails : list) {
            Map<String, String> map = new HashMap<>();
            map.put("code", categoryPlanningDetails.getProdCategoryCode());
            map.put("name", categoryPlanningDetails.getProdCategoryName());
            maps.add(map);
        }
        return selectSuccess(maps);
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
    public ApiResult updateDetail(@RequestBody CategoryPlanningDetailsVo categoryPlanningDetailsVo) {
        categoryPlanningDetailsService.updateDetail(categoryPlanningDetailsVo);
        return updateSuccess(categoryPlanningDetailsVo);
    }

    /**
     * 暂存,不生成数据
     */
    @RequestMapping("/saveDetail")
    public ApiResult saveDetail(@RequestBody CategoryPlanningDetailsVo categoryPlanningDetailsVo) {
        if ("1".equals(categoryPlanningDetailsVo.getIsGenerate())) {
            throw new RuntimeException("已经生成数据,无法保存");
        }
        categoryPlanningDetailsService.updateById(categoryPlanningDetailsVo);
        return updateSuccess(categoryPlanningDetailsVo);
    }

    // ==================== >企划看板 2.0

    /**
     * 根据id查询明细详情
     *
     * @param categoryPlanningDetailDTO 查询条件
     * @return 品类企划数据
     */
    @PostMapping("/getDetail")
    @ApiOperation(value = "企划看板 2.0 根据 id 查询明细详情")
    public ApiResult<CategoryPlanningDetailVO> getDetail(@RequestBody CategoryPlanningDetailDTO categoryPlanningDetailDTO) {
        CategoryPlanningDetailVO categoryPlanningDetailVO = categoryPlanningDetailsService.getDetail(categoryPlanningDetailDTO);
        return selectSuccess(categoryPlanningDetailVO);
    }

    /**
     * 品类企划暂存
     *
     * @param categoryPlanningDetailsList 要暂存的数据
     */
    @PostMapping("/staging")
    @ApiOperation(value = "企划看板 2.0 品类企划暂存")
    public ApiResult<String> staging(@RequestBody List<CategoryPlanningDetails> categoryPlanningDetailsList) {
        categoryPlanningDetailsService.staging(categoryPlanningDetailsList);
        return ApiResult.success("暂存成功！");
    }

    /**
     * 品类企划保存
     *
     * @param categoryPlanningDetailsList 要保存的数据
     */
    @PostMapping("/preservation")
    @ApiOperation(value = "企划看板 2.0 品类企划保存")
    public ApiResult<String> preservation(@RequestBody List<CategoryPlanningDetails> categoryPlanningDetailsList) {
        categoryPlanningDetailsService.preservation(categoryPlanningDetailsList);
        return ApiResult.success("保存成功！");
    }

    /**
     * 根据品类（多选逗号分隔）和品类企划 ID 获取维度系数
     *
     * @param categoryPlanningDetailDTO 要保存的数据
     */
    @PostMapping("/getDimensionality")
    @ApiOperation(value = "企划看板 2.0 根据品类和品类企划 ID 获取维度系数")
    public ApiResult<List<CategoryPlanningDetails>> getDimensionality(@RequestBody CategoryPlanningDetailDTO categoryPlanningDetailDTO) {
        List<CategoryPlanningDetails> list = categoryPlanningDetailsService.getDimensionality(categoryPlanningDetailDTO);
        return selectSuccess(list);
    }

    /**
     * 撤回品类企划 只能撤回未提交的数据 撤回的粒度是到品类级别
     *
     * @param categoryPlanningDetailDTO 要撤回的数据
     */
    @PostMapping("/revocation")
    @ApiOperation(value = "企划看板 2.0 撤回品类企划 只能撤回未提交的数据 撤回的粒度是到品类级别")
    public ApiResult<String> revocation(@RequestBody CategoryPlanningDetailDTO categoryPlanningDetailDTO) {
        categoryPlanningDetailsService.revocation(categoryPlanningDetailDTO);
        return ApiResult.success();
    }

    // <==================== 企划看板 2.0
}
