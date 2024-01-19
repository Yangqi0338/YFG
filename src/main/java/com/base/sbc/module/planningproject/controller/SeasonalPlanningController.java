package com.base.sbc.module.planningproject.controller;

import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningSaveDto;
import com.base.sbc.module.planningproject.service.SeasonalPlanningService;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningQueryDto;
import com.base.sbc.module.planningproject.vo.SeasonalPlanningVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-18 17:08:27
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "季节企划-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/seasonalPlanning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class SeasonalPlanningController extends BaseController {
    private final SeasonalPlanningService seasonalPlanningService;

    /**
     * 导入季节企划
     */
    @ApiOperation(value = "导入Excel")
    @PostMapping("/importExcel")
    @DuplicationCheck(type = 1,time = 20)
    public ApiResult importExcel(MultipartFile file,  SeasonalPlanningSaveDto seasonalPlanningSaveDto) throws Exception {
        seasonalPlanningSaveDto.setCompanyCode(this.getUserCompany());
        seasonalPlanningService.importExcel(file,seasonalPlanningSaveDto);
        return insertSuccess("导入成功");
    }

    /**
     * 根据条件查询
     */
    @ApiOperation(value = "根据条件查询")
    @GetMapping("/queryList")
    public ApiResult queryList(SeasonalPlanningQueryDto seasonalPlanningQueryDto){
        List<SeasonalPlanningVo> list = seasonalPlanningService.queryList(seasonalPlanningQueryDto);
        return selectSuccess(list);
    }
}
