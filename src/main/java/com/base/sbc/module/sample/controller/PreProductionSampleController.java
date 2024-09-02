/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.dto.EnableFlagSettingDto;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.nodestatus.service.NodeStatusConfigService;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.patternmaking.dto.SamplePicUploadDto;
import com.base.sbc.module.patternmaking.dto.ScoreDto;
import com.base.sbc.module.patternmaking.dto.TechRemarksDto;
import com.base.sbc.module.sample.dto.PreProductionSampleTaskDto;
import com.base.sbc.module.sample.dto.PreProductionSampleTaskSearchDto;
import com.base.sbc.module.sample.dto.PreTaskAssignmentDto;
import com.base.sbc.module.sample.dto.PreTaskAssignmentSampleBarCodeDto;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskDetailVo;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskVo;
import com.base.sbc.module.smp.SmpService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * 类描述：产前样 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.sample.web.PreProductionSampleController
 * @email your email
 * @date 创建时间：2023-7-18 11:04:06
 */
@RestController
@Api(tags = "产前样")
@RequestMapping(value = BaseController.SAAS_URL + "/preProductionSample", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
@RequiredArgsConstructor
public class PreProductionSampleController extends BaseController{

    @Autowired
    private PreProductionSampleTaskService preProductionSampleTaskService;


    @Autowired
    private NodeStatusService nodeStatusService;
    @Autowired
    private UserUtils userUtils;
    private final SmpService smpService;

    @ApiOperation(value = "任务-列表")
    @GetMapping("/task")
    public PageInfo<PreProductionSampleTaskVo> taskList(PreProductionSampleTaskSearchDto dto) {
        return preProductionSampleTaskService.taskList(dto);
    }

    @ApiOperation(value = "车缝工所有")
    @GetMapping("/stitcher")
    public List<String> stitcherList(PreProductionSampleTaskSearchDto dto) {
        return preProductionSampleTaskService.stitcherList(dto);
    }

    @ApiOperation(value = "任务-列表导出")
    @GetMapping("/taskderiveExcel")
    public void taskderiveExcel(HttpServletResponse response, PreProductionSampleTaskSearchDto dto) throws IOException {
         preProductionSampleTaskService.taskderiveExcel(response,dto);
    }

    @ApiOperation(value = "生成产前样任务")
    @PostMapping("/task/createByPackInfo")
    public boolean createByPackInfo(@RequestBody PackInfoListVo vo) {
        return preProductionSampleTaskService.createByPackInfo(vo);
    }

    @ApiOperation(value = "修改")
    @PostMapping("/task")
    public boolean updateByDto(@Valid @RequestBody PreProductionSampleTaskDto dto) {
        return preProductionSampleTaskService.updateByDto(dto);
    }


    @ApiOperation(value = "任务-启用/停用")
    @PostMapping("/task/enableSetting")
    public boolean enableSetting(@Valid @RequestBody EnableFlagSettingDto dto) {
        return preProductionSampleTaskService.enableSetting(dto.getId(), dto.getEnableFlag(),dto.getCode());
    }

    @ApiOperation(value = "任务-删除")
    @DeleteMapping("/task/del")
    public boolean taskDel(RemoveDto removeDto) {
        return preProductionSampleTaskService.removeByIds(removeDto);
    }

    @ApiOperation(value = "任务明细", notes = "通过id查询")
    @GetMapping("/task/getDetail")
    public PreProductionSampleTaskDetailVo getById(@Valid IdDto idDto) {
        return preProductionSampleTaskService.getTaskDetailById(idDto.getId());
    }


    @ApiOperation(value = "任务-分配")
    @PostMapping("/task/assignment")
    public boolean taskAssignment(@Valid @RequestBody PreTaskAssignmentDto dto) {
        return preProductionSampleTaskService.taskAssignment(dto);
    }

    @ApiOperation(value = "设置样衣条码", notes = "")
    @PostMapping("/setSampleBarCode")
    public ApiResult setSampleBarCode(@Validated @RequestBody PreTaskAssignmentSampleBarCodeDto dto) {
        QueryWrapper<PreProductionSampleTask> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.eq("sample_bar_code",dto.getSampleBarCode());
        queryWrapper.ne("id",dto.getId());
        queryWrapper.eq("del_flag", BaseGlobal.NO);

        PreProductionSampleTask update = new PreProductionSampleTask();
        update.setSampleBarCode(dto.getSampleBarCode());
        UpdateWrapper<PreProductionSampleTask> uw = new UpdateWrapper<>();
        uw.lambda().eq(PreProductionSampleTask::getId, dto.getId());

        PreProductionSampleTask old = preProductionSampleTaskService.getById(dto.getId());

        preProductionSampleTaskService.update(update, uw);
        PreProductionSampleTask newTask = preProductionSampleTaskService.getById(dto.getId());
        preProductionSampleTaskService.saveOperaLog("设置样衣条码", "产前样看板", null,newTask.getCode(),newTask,old);
        //下发产前样
        smpService.antenatalSample( new String[]{dto.getId()});
        return  updateSuccess("绑定成功");

    }


    @ApiOperation(value = "前往下一个节点", notes = "")
    @GetMapping("/task/next")
    public boolean next(Principal user, @Validated IdDto idDto) {
        return preProductionSampleTaskService.nextOrPrev(user, idDto.getId(), NodeStatusConfigService.NEXT);
    }

    @ApiOperation(value = "前往上一个节点", notes = "")
    @GetMapping("/task/prev")
    public boolean prev(Principal user, @Validated IdDto idDto) {
        return preProductionSampleTaskService.nextOrPrev(user, idDto.getId(), NodeStatusConfigService.PREV);
    }

    @ApiOperation(value = "获取节点状态配置", notes = "")
    @GetMapping("/task/getNodeStatusConfig")
    @ApiImplicitParams({@ApiImplicitParam(name = "node", value = "节点", required = false, dataType = "String", paramType = "query"), @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "String", paramType = "query"), @ApiImplicitParam(name = "dataId", value = "打版id", required = false, dataType = "String", paramType = "query"),})
    public JSONObject getNodeStatusConfig(Principal user, String node, String status, String dataId) {
        return nodeStatusService.getNodeStatusConfig(NodeStatusConfigService.PRE_PRODUCTION_SAMPLE_TASK, node, status);
    }

    @ApiOperation(value = "样衣工编辑", notes = "")
    @PostMapping("/task/sampleMakingEdit")
    public boolean sampleMakingEdit(@Validated @RequestBody PreProductionSampleTaskDto dto) {
        return preProductionSampleTaskService.sampleMakingEdit(dto);
    }

    @ApiOperation(value = "样衣制作评分", notes = "")
    @PostMapping("/task/sampleMakingScore")
    public boolean sampleMakingScore(@Validated @RequestBody ScoreDto dto) {
        PreProductionSampleTaskDto taskDto = new PreProductionSampleTaskDto();
        taskDto.setId(dto.getId());
        taskDto.setSampleMakingScore(dto.getScore());
        return preProductionSampleTaskService.sampleMakingScore(taskDto);
    }

    @ApiOperation(value = "样衣工的质量打分", notes = "")
    @PostMapping("/task/sampleQualityScore")
    public boolean sampleQualityScore(@Validated @RequestBody ScoreDto dto) {
        return preProductionSampleTaskService.sampleQualityScore(dto.getId(), dto.getScore());
    }

    @ApiOperation(value = "后技术备注说明", notes = "")
    @PostMapping("/task/techRemarks")
    public boolean techRemarks(Principal user, @RequestBody TechRemarksDto dto) {
        return preProductionSampleTaskService.techRemarks(user, dto.getId(), dto.getRemark());
    }

    @ApiOperation(value = "样衣图上传", notes = "")
    @PostMapping("/samplePicUpload")
    public boolean samplePicUpload(@Validated @RequestBody SamplePicUploadDto dto) {
        return preProductionSampleTaskService.samplePicUpload(dto);
    }

    @ApiOperation(value = "保存工艺确认时间")
    @PostMapping("/saveTechReceiveDate")
    public ApiResult saveTechReceiveDate( @Valid @RequestBody PreProductionSampleTaskDto task) {
        preProductionSampleTaskService.saveTechReceiveDate(task);
        return ApiResult.success();
    }

}































