/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.process.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.process.dto.AddRevampProcessNodeStatusDto;
import com.base.sbc.module.process.dto.QueryNodeStatusDto;
import com.base.sbc.module.process.entity.ProcessNodeStatus;
import com.base.sbc.module.process.service.ProcessNodeStatusService;
import com.base.sbc.module.process.vo.ProcessNodeStatusVo;
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
* 类描述：流程配置-节点状态 Controller类
* @address com.base.sbc.module.process.web.ProcessNodeStatusController
* @author mengfanjiang
* @email lxl.fml@gmail.com
* @date 创建时间：2023-6-2 20:15:15
* @version 1.0
*/
@RestController
@Api(tags = "流程配置-节点状态")
@RequestMapping(value = BaseController.SAAS_URL + "/processNodeStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ProcessNodeStatusController{

	@Autowired
	private ProcessNodeStatusService processNodeStatusService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getProcessNodeStatusList")
	public PageInfo<ProcessNodeStatusVo> getProcessNodeStatusList(QueryNodeStatusDto queryNodeStatusDto) {
		return  processNodeStatusService.getProcessNodeStatusList(queryNodeStatusDto);
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopProcessNodeStatus")
	public Boolean startStopProcessNodeStatus(@Valid @RequestBody StartStopDto startStopDto) {
	return processNodeStatusService.startStopProcessNodeStatus(startStopDto);
	}


	@ApiOperation(value = "新增修改流程配置-节点状态")
	@PostMapping("/addRevampProcessNodeStatus")
	public Boolean addRevampProcessNodeStatus(@Valid @RequestBody AddRevampProcessNodeStatusDto addRevampProcessNodeStatusDto) {
	return processNodeStatusService.addRevampProcessNodeStatus(addRevampProcessNodeStatusDto);
	}

	@ApiOperation(value = "批量新增修改流程配置-节点状态")
	@PostMapping("/batchAddRevamp")
	public List<ProcessNodeStatusVo>  batchAddRevamp(@Valid @RequestBody List<AddRevampProcessNodeStatusDto> addRevampProcessNodeStatusDto) {
		return processNodeStatusService.batchAddRevamp(addRevampProcessNodeStatusDto);
	}


	@ApiOperation(value = "删除流程配置-节点状态")
	@DeleteMapping("/delProcessNodeStatus")
	public Boolean delProcessNodeStatus(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return processNodeStatusService.delProcessNodeStatus(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public ProcessNodeStatus getById(@PathVariable("id") String id) {
		return processNodeStatusService.getById(id);
	}


}































