/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.process.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.process.dto.AddRevampProcessActionDto;
import com.base.sbc.module.process.dto.QueryActionDto;
import com.base.sbc.module.process.entity.ProcessAction;
import com.base.sbc.module.process.service.ProcessActionService;
import com.base.sbc.module.process.vo.ProcessActionVo;
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
* 类描述：流程配置-动作定义 Controller类
* @address com.base.sbc.module.process.web.ProcessActionController
* @author mengfanjiang
* @email lxl.fml@gmail.com
* @date 创建时间：2023-6-5 11:03:08
* @version 1.0
*/
@RestController
@Api(tags = "流程配置-动作定义")
@RequestMapping(value = BaseController.SAAS_URL + "/processAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ProcessActionController{

	@Autowired
	private ProcessActionService processActionService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getProcessActionList")
	public PageInfo<ProcessActionVo> getProcessActionList(QueryActionDto queryActionDto) {
		return  processActionService.getProcessActionList(queryActionDto);
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopProcessAction")
	public Boolean startStopProcessAction(@Valid @RequestBody StartStopDto startStopDto) {
	return processActionService.startStopProcessAction(startStopDto);
	}


	@ApiOperation(value = "新增修改流程配置-动作定义")
	@PostMapping("/addRevampProcessAction")
	public Boolean addRevampProcessAction(@Valid @RequestBody AddRevampProcessActionDto addRevampProcessActionDto) {
	return processActionService.addRevampProcessAction(addRevampProcessActionDto);
	}

	@ApiOperation(value = "删除流程配置-动作定义")
	@DeleteMapping("/delProcessAction")
	public Boolean delProcessAction(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return processActionService.delProcessAction(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public ProcessAction getById(@PathVariable("id") String id) {
		return processActionService.getById(id);
	}


}































