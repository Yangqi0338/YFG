package com.base.sbc.module.fabricsummary.controller;


import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.fabricsummary.service.FabricSummaryService;
import com.base.sbc.module.sample.dto.FabricSummaryStyleMaterialDto;
import com.base.sbc.module.sample.dto.FabricSummaryV2Dto;
import com.base.sbc.module.sample.vo.FabricStyleGroupVo;
import com.base.sbc.module.sample.vo.FabricSummaryGroupVo;
import com.base.sbc.module.sample.vo.FabricSummaryInfoVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api(tags = "面料详单")
@RequestMapping(value = BaseController.SAAS_URL + "/fabricSummary", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FabricSummaryController {

    @Autowired
    FabricSummaryService fabricSummaryService;

    @ApiOperation(value = "/面料汇总组创建/修改")
    @PostMapping("/fabricSummaryGroupSaveOrUpdate")
    public boolean  fabricSummaryGroupSaveOrUpdate(@RequestBody FabricStyleGroupVo fabricStyleGroupVo) {
        return fabricSummaryService.fabricSummaryGroupSaveOrUpdate(fabricStyleGroupVo);
    }

    @ApiOperation(value = "/面料汇总组删除")
    @DeleteMapping("/fabricSummaryGroup")
    public boolean  deleteFabricSummaryGroup(@RequestBody FabricStyleGroupVo fabricStyleGroupVo) {
        return fabricSummaryService.deleteFabricSummaryGroup(fabricStyleGroupVo);
    }

    @ApiOperation(value = "/面料汇总组列表")
    @GetMapping("/fabricSummaryGroup")
    public PageInfo<FabricSummaryGroupVo> fabricSummaryGroup(FabricSummaryStyleMaterialDto dto) {
        return fabricSummaryService.fabricSummaryGroup(dto);
    }

    @GetMapping("/selectFabricSummaryStyle")
    @ApiOperation(value = "选择面料款式列表")
    public PageInfo<FabricSummaryInfoVo> selectFabricSummaryStyle(FabricSummaryV2Dto dto) {
        return fabricSummaryService.selectFabricSummaryStyle(dto);
    }

    @PostMapping("/saveFabricSummary")
    @ApiOperation(value = "添加物料款式")
    public boolean saveFabricSummary(@RequestBody List<FabricSummaryInfoVo> dto) {
        return fabricSummaryService.saveFabricSummary(dto);
    }

    @PostMapping("/fabricSummarySync")
    @ApiOperation(value = "面料信息同步更新")
    public boolean fabricSummarySync(@RequestBody List<FabricSummaryV2Dto> dto) {
        return fabricSummaryService.fabricSummarySync(dto);
    }

}
