/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumPressingPackagingDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumPressingPackaging;
import com.base.sbc.module.basicsdatum.service.BasicsdatumPressingPackagingService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumPressingPackagingVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
* 类描述：基础资料-整烫包装 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumPressingPackagingController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-20 19:08:55
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-整烫包装")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumPressingPackaging", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumPressingPackagingController{

	@Autowired
	private BasicsdatumPressingPackagingService basicsdatumPressingPackagingService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getBasicsdatumPressingPackagingList")
	public PageInfo<BasicsdatumPressingPackagingVo> getBasicsdatumPressingPackagingList(QueryDto queryDto) {
		return  basicsdatumPressingPackagingService.getBasicsdatumPressingPackagingList(queryDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/basicsdatumPressingPackagingImportExcel")
	public Boolean basicsdatumPressingPackagingImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
	return basicsdatumPressingPackagingService.basicsdatumPressingPackagingImportExcel(file);
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/basicsdatumPressingPackagingDeriveExcel")
	public void basicsdatumPressingPackagingDeriveExcel(HttpServletResponse response) throws Exception {
       basicsdatumPressingPackagingService.basicsdatumPressingPackagingDeriveExcel(response);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopBasicsdatumPressingPackaging")
	public Boolean startStopBasicsdatumPressingPackaging(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumPressingPackagingService.startStopBasicsdatumPressingPackaging(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-整烫包装")
	@PostMapping("/addRevampBasicsdatumPressingPackaging")
	public Boolean addRevampBasicsdatumPressingPackaging(@Valid @RequestBody AddRevampBasicsdatumPressingPackagingDto addRevampBasicsdatumPressingPackagingDto) {
	return basicsdatumPressingPackagingService.addRevampBasicsdatumPressingPackaging(addRevampBasicsdatumPressingPackagingDto);
	}

	@ApiOperation(value = "删除基础资料-整烫包装")
	@DeleteMapping("/delBasicsdatumPressingPackaging")
	public Boolean delBasicsdatumPressingPackaging(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumPressingPackagingService.delBasicsdatumPressingPackaging(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumPressingPackaging getById(@PathVariable("id") String id) {
		return basicsdatumPressingPackagingService.getById(id);
	}


}































