/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.controller;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.dto.EnableFlagSettingDto;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.nodestatus.service.NodeStatusConfigService;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.sample.dto.*;
import com.base.sbc.module.sample.service.PreProductionSampleService;
import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskDetailVo;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskListVo;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskVo;
import com.base.sbc.module.sample.vo.PreProductionSampleVo;
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
import java.security.Principal;

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
public class PreProductionSampleController {

    @Autowired
    private PreProductionSampleService preProductionSampleService;
    @Autowired
    private PreProductionSampleTaskService preProductionSampleTaskService;


    @Autowired
    private NodeStatusService nodeStatusService;
    @Autowired
    private UserUtils userUtils;

    @ApiOperation(value = "分页查询")
    @GetMapping
    public PageInfo<PreProductionSampleVo> pageInfo(PreProductionSampleSearchDto dto) {
        return preProductionSampleService.pageInfo(dto);
    }

    @ApiOperation(value = "生成产前样")
    @GetMapping("/createByPackInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packInfoId", value = "资料包id", required = true, dataType = "String", paramType = "query"),
    })
    public boolean createByPackInfo(@Valid @NotBlank(message = "资料包id不能为空") String packInfoId) {
        return preProductionSampleService.createByPackInfo(packInfoId);
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public boolean save(@Valid @RequestBody PreProductionSampleDto dto) {
        return preProductionSampleService.saveByDto(dto);
    }

    @ApiOperation(value = "任务-列表")
    @GetMapping("/task")
    public PageInfo<PreProductionSampleTaskListVo> taskList(PreProductionSampleTaskSearchDto dto) {
        return preProductionSampleTaskService.taskList(dto);
    }

    @ApiOperation(value = "任务-保存/编辑")
    @PostMapping("/task")
    public PreProductionSampleTaskVo saveTask(@Valid @RequestBody PreProductionSampleTaskDto dto) {
        return preProductionSampleService.saveTaskByDto(dto);
    }

    @ApiOperation(value = "任务-启用/停用")
    @PostMapping("/task/enableSetting")
    public boolean enableSetting(@Valid @RequestBody EnableFlagSettingDto dto) {
        return preProductionSampleTaskService.enableSetting(dto.getId(), dto.getEnableFlag());
    }

    @ApiOperation(value = "任务-删除")
    @DeleteMapping("/task/del")
    public boolean taskDel(@Valid IdsDto idsDto) {
        return preProductionSampleTaskService.removeBatchByIds(StrUtil.split(idsDto.getId(), CharUtil.COMMA));
    }

    @ApiOperation(value = "任务明细", notes = "通过id查询")
    @GetMapping("/getDetail")
    public PreProductionSampleTaskDetailVo getById(@Valid IdDto idDto) {
        return preProductionSampleService.getTaskDetailById(idDto.getId());
    }


    @ApiOperation(value = "任务-分配")
    @PostMapping("/task/assignment")
    public boolean taskAssignment(@Valid @RequestBody PreTaskAssignmentDto dto) {
        return preProductionSampleTaskService.taskAssignment(dto);
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "node", value = "节点", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dataId", value = "打版id", required = false, dataType = "String", paramType = "query"),
    })
    public JSONObject getNodeStatusConfig(Principal user, String node, String status, String dataId) {
        return nodeStatusService.getNodeStatusConfig(NodeStatusConfigService.PRE_PRODUCTION_SAMPLE_TASK, node, status);
    }
}































