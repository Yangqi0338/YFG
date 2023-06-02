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
import com.base.sbc.module.process.dto.AddRevampProcessNodeActionDto;
import com.base.sbc.module.process.entity.ProcessNodeAction;
import com.base.sbc.module.process.service.ProcessNodeActionService;
import com.base.sbc.module.process.vo.ProcessNodeActionVo;
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
* 类描述：流程配置-节点动作 Controller类
* @address com.base.sbc.module.process.web.ProcessNodeActionController
* @author mengfanjiang
* @email lxl.fml@gmail.com
* @date 创建时间：2023-6-2 20:15:14
* @version 1.0
*/
@RestController
@Api(tags = "流程配置-节点动作")
@RequestMapping(value = BaseController.SAAS_URL + "/processNodeAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ProcessNodeActionController{

	@Autowired
	private ProcessNodeActionService processNodeActionService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getProcessNodeActionList")
	public PageInfo<ProcessNodeActionVo> getProcessNodeActionList(QueryDto queryDto) {
		return  processNodeActionService.getProcessNodeActionList(queryDto);
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopProcessNodeAction")
	public Boolean startStopProcessNodeAction(@Valid @RequestBody StartStopDto startStopDto) {
	return processNodeActionService.startStopProcessNodeAction(startStopDto);
	}


	@ApiOperation(value = "新增修改流程配置-节点动作")
	@PostMapping("/addRevampProcessNodeAction")
	public Boolean addRevampProcessNodeAction(@Valid @RequestBody AddRevampProcessNodeActionDto addRevampProcessNodeActionDto) {
	return processNodeActionService.addRevampProcessNodeAction(addRevampProcessNodeActionDto);
	}

	@ApiOperation(value = "删除流程配置-节点动作")
	@DeleteMapping("/delProcessNodeAction")
	public Boolean delProcessNodeAction(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return processNodeActionService.delProcessNodeAction(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public ProcessNodeAction getById(@PathVariable("id") String id) {
		return processNodeActionService.getById(id);
	}


}































