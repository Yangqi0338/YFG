/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.controller;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.patternmaking.dto.*;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.PatternDesignVo;
import com.base.sbc.module.patternmaking.vo.PatternMakingDetailVo;
import com.base.sbc.module.patternmaking.vo.PatternMakingListVo;
import com.base.sbc.module.patternmaking.vo.PatternMakingTaskListVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：打版管理 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.web.PatternMakingController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 13:33:05
 */
@RestController
@Api(tags = "打版管理")
@RequestMapping(value = BaseController.SAAS_URL + "/patternMaking", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PatternMakingController {

    @Autowired
    private PatternMakingService patternMakingService;



    @ApiOperation(value = "技术中心看板-任务列表")
    @GetMapping("/technologyCenterTaskList")
    public PageInfo technologyCenterTaskList(TechnologyCenterTaskSearchDto dto){
        return patternMakingService.technologyCenterTaskList(dto);
    }

    @ApiOperation(value = "通过样衣设计id查询")
    @GetMapping("/findBySampleDesignId")
    public List<PatternMakingListVo> findBySampleDesignId(@NotBlank(message = "(sampleDesignId)样衣设计id不能为空") String sampleDesignId) {
        List<PatternMakingListVo> list = patternMakingService.findBySampleDesignId(sampleDesignId);
        return list;
    }

    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/{id}")
    public PatternMakingDetailVo getById(@PathVariable("id") String id) {
        return patternMakingService.getDetailById(id);
    }

    @ApiOperation(value = "保存附件-纸样文件")
    @PostMapping("/saveAttachment")
    public boolean saveAttachment(@Valid @RequestBody SaveAttachmentDto dto) {
        return patternMakingService.saveAttachment(dto);
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/{id}")
    public Boolean removeById(@PathVariable("id") String id) {
        List<String> ids = StringUtils.convertList(id);
        return patternMakingService.removeByIds(ids);
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public PatternMaking save(@Valid @RequestBody PatternMakingDto dto) {
        PatternMaking patternMaking = patternMakingService.savePatternMaking(dto);
        return patternMaking;
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public PatternMaking update(@RequestBody PatternMakingDto dto) {
        PatternMaking patternMaking = BeanUtil.copyProperties(dto, PatternMaking.class);
        patternMakingService.updateById(patternMaking);
        return patternMaking;
    }


    @ApiOperation(value = "样衣设计下发")
    @PostMapping("/sampleDesignSend")
    public boolean sampleDesignSend(@Valid @RequestBody SampleDesignSendDto dto) {
        return patternMakingService.sampleDesignSend(dto);
    }

    @ApiOperation(value = "版房主管下发")
    @PostMapping("/prmSend")
    public Integer prmSend(@Valid @RequestBody List<SetPatternDesignDto> dtos) {
        return patternMakingService.prmSendBatch(dtos);
    }


    @ApiOperation(value = "指定版师")
    @PostMapping("/setPatternDesign")
    public boolean setPatternDesign(@Valid @RequestBody SetPatternDesignDto dto) {
        return patternMakingService.setPatternDesign(dto);
    }

    @ApiOperation(value = "指定版师批量")
    @PostMapping("/setPatternDesignBatch")
    public boolean setPatternDesignBatch(@Valid @RequestBody List<SetPatternDesignDto> dto) {
        return patternMakingService.setPatternDesignBatch(dto);
    }

    @ApiOperation(value = "获取版师列表")
    @GetMapping("/getPatternDesignList")
    @ApiImplicitParams({@ApiImplicitParam(name = "planningSeasonId", value = "产品季节id", required = true, paramType = "query")})
    public List<PatternDesignVo> getPatternDesignList(@Valid @NotBlank(message = "产品季节id不能为空") String planningSeasonId) {
        return patternMakingService.getPatternDesignList(planningSeasonId);
    }

    @ApiOperation(value = "中断样衣")
    @GetMapping("/breakOffSample")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "编号", required = true, paramType = "query")})
    public boolean breakOffSample(@Valid @NotBlank(message = "id不能为空") String id) {
        return patternMakingService.breakOffSample(id);
    }

    @ApiOperation(value = "中断打版")
    @GetMapping("/breakOffPattern")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "编号", required = true, paramType = "query")})
    public boolean breakOffPattern(@Valid @NotBlank(message = "id不能为空") String id) {
        return patternMakingService.breakOffPattern(id);
    }

    @ApiOperation(value = "节点状态改变")
    @PostMapping("/nodeStatusChange")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "编号", required = true, paramType = "query")})
    public boolean nodeStatusChange(@RequestBody NodeStatusChangeDto dto) {
        return patternMakingService.nodeStatusChange(dto);
    }


    @ApiOperation(value = "打版管理任务-列表", notes = "")
    @GetMapping("/patternMakingTaskList")
    public List<PatternMakingTaskListVo> patternMakingTaskList(PatternMakingTaskSearchDto dto) {
        return patternMakingService.patternMakingTaskList(dto);
    }

    @ApiOperation(value = "设置排序", notes = "")
    @PostMapping("/setSort")
    public Integer setSort(@Valid @RequestBody List<SetSortDto> dtoList) {
        return patternMakingService.setSort(dtoList);
    }

    @ApiOperation(value = "挂起", notes = "")
    @PostMapping("/suspend")
    public boolean suspend(@Valid @RequestBody SuspendDto dto) {
        return patternMakingService.suspend(dto);
    }

    @ApiOperation(value = "取消挂起", notes = "")
    @GetMapping("/cancelSuspend")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "编号", required = true, paramType = "query")})
    public boolean cancelSuspend(@Valid @NotBlank(message = "id不能为空") String id) {
        return patternMakingService.cancelSuspend(id);
    }

    @ApiOperation(value = "工艺员设置齐套", notes = "")
    @PostMapping("/technicianKitting")
    public boolean technicianKitting(@Valid @RequestBody SetKittingDto dto) {
        return patternMakingService.setKitting("technician_", dto);
    }

    @ApiOperation(value = "样衣组长设置齐套", notes = "")
    @PostMapping("/sglKitting")
    public boolean sglKitting(@Valid @RequestBody SetKittingDto dto) {
        return patternMakingService.setKitting("sgl_", dto);
    }
}































