/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.process.controller;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.process.entity.ProcessNodeStatusCondition;
import com.base.sbc.module.process.service.ProcessNodeStatusConditionService;
import com.base.sbc.module.process.dto.AddRevampProcessNodeStatusConditionDto;
import com.base.sbc.module.process.vo.ProcessNodeStatusConditionVo;
import org.hibernate.validator.constraints.NotBlank;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.List;

/**
* 类描述：流程配置-节点状态条件 Controller类
* @address com.base.sbc.module.process.web.ProcessNodeStatusConditionController
* @author mengfanjiang
* @email lxl.fml@gmail.com
* @date 创建时间：2023-6-5 17:10:23
* @version 1.0
*/
@RestController
@Api(tags = "流程配置-节点状态条件")
@RequestMapping(value = BaseController.SAAS_URL + "/processNodeStatusCondition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ProcessNodeStatusConditionController{

	@Autowired
	private ProcessNodeStatusConditionService processNodeStatusConditionService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getProcessNodeStatusConditionList")
	public PageInfo<ProcessNodeStatusConditionVo> getProcessNodeStatusConditionList(QueryDto queryDto) {
		return  processNodeStatusConditionService.getProcessNodeStatusConditionList(queryDto);
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopProcessNodeStatusCondition")
	public Boolean startStopProcessNodeStatusCondition(@Valid @RequestBody StartStopDto startStopDto) {
	return processNodeStatusConditionService.startStopProcessNodeStatusCondition(startStopDto);
	}


	@ApiOperation(value = "新增修改流程配置-节点状态条件")
	@PostMapping("/addRevampProcessNodeStatusCondition")
	public Boolean addRevampProcessNodeStatusCondition(@Valid @RequestBody AddRevampProcessNodeStatusConditionDto addRevampProcessNodeStatusConditionDto) {
	return processNodeStatusConditionService.addRevampProcessNodeStatusCondition(addRevampProcessNodeStatusConditionDto);
	}

	@ApiOperation(value = "删除流程配置-节点状态条件")
	@DeleteMapping("/delProcessNodeStatusCondition")
	public Boolean delProcessNodeStatusCondition(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return processNodeStatusConditionService.delProcessNodeStatusCondition(id);
	}

	@ApiOperation(value = "查询明细节点状态条件")
	@GetMapping("/getNodeStatusConditionDetail")
	public ProcessNodeStatusConditionVo getNodeStatusConditionDetail(AddRevampProcessNodeStatusConditionDto addRevampProcessNodeStatusConditionDto) {
		return processNodeStatusConditionService.getNodeStatusConditionDetail(addRevampProcessNodeStatusConditionDto);
	}


	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public ProcessNodeStatusCondition getById(@PathVariable("id") String id) {
		return processNodeStatusConditionService.getById(id);
	}


}































