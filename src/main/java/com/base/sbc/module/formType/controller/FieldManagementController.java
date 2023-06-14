/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.formType.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.formType.dto.QueryFieldManagementDto;
import com.base.sbc.module.formType.dto.SaveUpdateFieldManagementDto;
import com.base.sbc.module.formType.entity.FieldManagement;
import com.base.sbc.module.formType.entity.Option;
import com.base.sbc.module.formType.service.FieldManagementService;
import com.base.sbc.module.formType.service.OptionService;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
* 类描述：字段管理表 Controller类
* @address com.base.sbc.module.formType.web.FieldManagementController
* @author lxl
* @email lxl.fml@gmail.com
* @date 创建时间：2023-4-15 18:33:51
* @version 1.0
*/
@RestController
@Api(tags = "字段管理表")
@RequestMapping(value = BaseController.SAAS_URL + "/fieldManagement", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FieldManagementController {

	@Autowired
	private FieldManagementService fieldManagementService;
	@Autowired
	private OptionService optionService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getFieldManagementList")
	public PageInfo<FieldManagementVo> getFieldManagementList(@Valid QueryFieldManagementDto queryFieldManagementDto) {
		return fieldManagementService.getFieldManagementList(queryFieldManagementDto);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public FieldManagement getById(@PathVariable("id") String id) {
		return fieldManagementService.getById(id);
	}

	@ApiOperation(value = "保存修改字段管理表")
	@PostMapping("/saveUpdateField")
	public ApiResult saveUpdateField(@Valid @RequestBody SaveUpdateFieldManagementDto saveUpdateFieldManagementDto) {
		return fieldManagementService.saveUpdateField(saveUpdateFieldManagementDto);
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("delField/{id}")
	public Boolean removeById(@PathVariable("id") String id) {
		List<String> ids = StringUtils.convertList(id);
		QueryWrapper<Option> queryWrapper=new QueryWrapper<>();
		queryWrapper.in("field_id",ids);
		List<Option> optionList= optionService.list(queryWrapper);
		List<String> optionIds =	optionList.stream().map(Option::getId).collect(Collectors.toList());
		optionService.removeByIds(optionIds);
		return fieldManagementService.removeByIds(ids);
	}

	@ApiOperation(value = "调整顺序")
	@PostMapping("/adjustmentOrder")
	public ApiResult adjustmentOrder(@Valid @RequestBody QueryFieldManagementDto queryFieldManagementDto) {
		return fieldManagementService.adjustmentOrder(queryFieldManagementDto);
	}

}































