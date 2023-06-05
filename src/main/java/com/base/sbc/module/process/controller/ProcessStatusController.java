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
import com.base.sbc.module.process.dto.AddRevampProcessStatusDto;
import com.base.sbc.module.process.entity.ProcessStatus;
import com.base.sbc.module.process.service.ProcessStatusService;
import com.base.sbc.module.process.vo.ProcessStatusVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
* 类描述：流程配置-状态定义 Controller类
* @address com.base.sbc.module.process.web.ProcessStatusController
* @author mengfanjiang
* @email lxl.fml@gmail.com
* @date 创建时间：2023-6-2 20:15:16
* @version 1.0
*/
@RestController
@Api(tags = "流程配置-状态定义")
@RequestMapping(value = BaseController.SAAS_URL + "/processStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ProcessStatusController{

	@Autowired
	private ProcessStatusService processStatusService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getProcessStatusList")
	public PageInfo<ProcessStatusVo> getProcessStatusList(QueryDto queryDto) {
		return  processStatusService.getProcessStatusList(queryDto);
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopProcessStatus")
	public Boolean startStopProcessStatus(@Valid @RequestBody StartStopDto startStopDto) {
	return processStatusService.startStopProcessStatus(startStopDto);
	}


	@ApiOperation(value = "新增修改流程配置-状态定义")
	@PostMapping("/addRevampProcessStatus")
	public Boolean addRevampProcessStatus(@Valid @RequestBody AddRevampProcessStatusDto addRevampProcessStatusDto) {
	return processStatusService.addRevampProcessStatus(addRevampProcessStatusDto);
	}

	@ApiOperation(value = "删除流程配置-状态定义")
	@DeleteMapping("/delProcessStatus")
	public Boolean delProcessStatus(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return processStatusService.delProcessStatus(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public ProcessStatus getById(@PathVariable("id") String id) {
		return processStatusService.getById(id);
	}


}































